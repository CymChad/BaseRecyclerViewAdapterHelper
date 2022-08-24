package com.chad.baserecyclerviewadapterhelper.activity.loadmore

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.baserecyclerviewadapterhelper.R
import com.chad.baserecyclerviewadapterhelper.activity.headerfooter.adapter.HeaderAdapter
import com.chad.baserecyclerviewadapterhelper.activity.loadmore.adapter.RecyclerViewAdapter
import com.chad.baserecyclerviewadapterhelper.base.BaseViewBindingActivity
import com.chad.baserecyclerviewadapterhelper.data.DataServer
import com.chad.baserecyclerviewadapterhelper.databinding.ActivityLoadMoreBinding
import com.chad.baserecyclerviewadapterhelper.entity.Status
import com.chad.baserecyclerviewadapterhelper.utils.Tips
import com.chad.library.adapter.base.QuickAdapterHelper
import com.chad.library.adapter.base.loadState.LoadState
import com.chad.library.adapter.base.loadState.trailing.TrailingLoadStateAdapter.OnTrailingListener

/**
 * 自动加载更多
 */
class AutoLoadMoreRefreshUseActivity : BaseViewBindingActivity<ActivityLoadMoreBinding>() {
    internal class PageInfo {
        var page = 0
        fun nextPage() {
            page++
        }

        fun reset() {
            page = 0
        }

        val isFirstPage: Boolean
            get() = page == 0
    }

    private val pageInfo = PageInfo()

    private val mAdapter: RecyclerViewAdapter = RecyclerViewAdapter()
    private lateinit var helper: QuickAdapterHelper

    override fun initBinding(): ActivityLoadMoreBinding =
        ActivityLoadMoreBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding.titleBar.title = "Auto LoadMore Use"
        viewBinding.titleBar.setOnBackListener { finish() }


        viewBinding.rvList.layoutManager = LinearLayoutManager(this)
        initAdapter()
        addHeadView()
        initRefreshLayout()
    }

    override fun onStart() {
        super.onStart()
        // 进入页面，先显示加载状态布局
        mAdapter.setEmptyViewLayout(this, R.layout.loading_view)
        viewBinding.refreshLayout.isRefreshing = true
        refresh()
    }

    private fun initAdapter() {
        // 使用默认的"加载更多"的样式
        helper = QuickAdapterHelper.Builder(mAdapter)
            .setTrailingLoadStateAdapter(object : OnTrailingListener {
                override fun onLoad() {
                    request()
                }

                override fun onFailRetry() {
                    request()
                }

                override fun isAllowLoading(): Boolean {
                    return !viewBinding.refreshLayout.isRefreshing
                }
            }).build()

        // 设置预加载，请调用以下方法
        // helper.trailingLoadStateAdapter?.preloadSize = 1
        viewBinding.rvList.adapter = helper.adapter
    }

    private fun addHeadView() {
        val headerAdapter = HeaderAdapter()
        headerAdapter.setOnItemClickListener { _, _, _ ->
            addHeadView()
        }
        helper.addBeforeAdapter(0, headerAdapter)
    }

    private fun initRefreshLayout() {
        viewBinding.refreshLayout.setColorSchemeColors(Color.rgb(47, 223, 189))
        viewBinding.refreshLayout.setOnRefreshListener { refresh() }
    }

    /**
     * 刷新
     */
    private fun refresh() {
        // 下拉刷新，需要重置页数
        pageInfo.reset()
        helper.trailingLoadState = LoadState.None
        request()
    }

    /**
     * 请求数据
     */
    private fun request() {

        Request(pageInfo.page, object : RequestCallBack {
            override fun success(data: List<Status?>) {
                viewBinding.refreshLayout.isRefreshing = false
                if (pageInfo.isFirstPage) {
                    // 如果是加载的第一页数据，用 submitList()
                    // If it is the first page of data loaded, use submitList().
                    mAdapter.submitList(data)
                } else {
                    //不是第一页，则用add
                    mAdapter.addAll(data)
                }

                // 如果在数据不满足一屏时，暂停加载更多，请调用下面方法
                // helper.trailingLoadStateAdapter?.checkDisableLoadMoreIfNotFullPage()

                if (pageInfo.page >= PAGE_SIZE) {
                    /*
                    Set the status to not loaded, and there is no paging data.
                    设置状态为未加载，并且没有分页数据了
                     */
                    helper.trailingLoadState = LoadState.NotLoading(true)
                    Tips.show("no more data")
                } else {
                    /*
                    Set the state to not loaded, and there is also paginated data
                    设置状态为未加载，并且还有分页数据
                     */
                    helper.trailingLoadState = LoadState.NotLoading(false)
                }

                // page加一
                pageInfo.nextPage()
            }

            override fun fail(e: Exception) {
                Tips.show(resources.getString(R.string.network_err))
                viewBinding.refreshLayout.isRefreshing = false
                helper.trailingLoadState = LoadState.Error(e)
            }
        }).start()
    }

    /**
     * 模拟加载数据的类，不用特别关注
     */
    internal class Request(private val mPage: Int, private val mCallBack: RequestCallBack) : Thread() {
        private val mHandler: Handler = Handler(Looper.getMainLooper())

        override fun run() {
            try {
                sleep(800) // 模拟网络延迟
            } catch (ignored: InterruptedException) {
            }
            if (mPage == 2 && mFirstError) {
                mFirstError = false
                mHandler.post { mCallBack.fail(RuntimeException("load fail")) }
            } else {
                var size = PAGE_SIZE
                if (mPage == 1) {
                    if (mFirstPageNoMore) {
                        size = 1
                    }
                    mFirstPageNoMore = !mFirstPageNoMore
                    if (!mFirstError) {
                        mFirstError = true
                    }
                } else if (mPage == 4) {
                    size = 1
                }
                val dataSize = size
                mHandler.post { mCallBack.success(DataServer.getSampleData(dataSize)) }
            }
        }

        companion object {
            private var mFirstPageNoMore = false
            private var mFirstError = true
        }
    }

    internal interface RequestCallBack {
        /**
         * 模拟加载成功
         *
         * @param data 数据
         */
        fun success(data: List<Status?>)

        /**
         * 模拟加载失败
         *
         * @param e 错误信息
         */
        fun fail(e: Exception)
    }

    companion object {
        private const val PAGE_SIZE = 5
    }
}