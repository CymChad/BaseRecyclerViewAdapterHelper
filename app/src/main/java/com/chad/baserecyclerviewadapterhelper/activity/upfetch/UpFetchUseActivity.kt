package com.chad.baserecyclerviewadapterhelper.activity.upfetch

import android.os.Bundle
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.baserecyclerviewadapterhelper.activity.upfetch.adapter.UpFetchAdapter
import com.chad.baserecyclerviewadapterhelper.base.BaseViewBindingActivity
import com.chad.baserecyclerviewadapterhelper.databinding.ActivityUniversalRecyclerBinding
import com.chad.baserecyclerviewadapterhelper.entity.Movie
import com.chad.library.adapter4.QuickAdapterHelper
import com.chad.library.adapter4.loadState.LoadState
import com.chad.library.adapter4.loadState.LoadState.NotLoading
import com.chad.library.adapter4.loadState.leading.LeadingLoadStateAdapter.OnLeadingListener
import java.util.*

/**
 * @author limuyang
 * 2019-12-06
 */
class UpFetchUseActivity : BaseViewBindingActivity<ActivityUniversalRecyclerBinding>() {

    private val mAdapter = UpFetchAdapter()
    private lateinit var helper: QuickAdapterHelper

    override fun initBinding(): ActivityUniversalRecyclerBinding =
        ActivityUniversalRecyclerBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ViewCompat.setOnApplyWindowInsetsListener(viewBinding.titleBar) { view, insets ->
            val bar = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updatePadding(top = bar.top)
            insets
        }

        viewBinding.titleBar.title = "UpFetch Use"
        viewBinding.titleBar.setOnBackListener { finish() }


        viewBinding.rv.layoutManager = LinearLayoutManager(this)

        helper = QuickAdapterHelper.Builder(mAdapter)
            .setLeadingLoadStateAdapter(object : OnLeadingListener {
                override fun onLoad() {
                    requestUoFetch()
                }

                override fun isAllowLoading(): Boolean {
                    return true
                }
            })
            .setLeadPreloadSize(0) // 预加载（默认值为0）
            .build()
        viewBinding.rv.adapter = helper.adapter
    }

    override fun onStart() {
        super.onStart()
        requestUoFetch()
    }

    private var count = 0
    private fun requestUoFetch() {
        if (count == 0) {
            count++
            // 首次进入页面，设置数据
            mAdapter.submitList(genData())
            scrollToBottom()
            helper.leadingLoadState = NotLoading(false)
            return
        }
        count++

        /**
         * When starting to request data from the network, set the status to loading.
         * 当开始网络请求数据的时候，设置状态为加载中
         */
        helper.leadingLoadState = LoadState.Loading

        /*
         * get data from internet.
         * 从网络获取数据
         */
        viewBinding.rv.postDelayed({
            mAdapter.addAll(0, genData())
            if (count > 5) {
                /*
                 * Set the status to not loaded, and there is no paging data.
                 * 设置状态为未加载，并且没有分页数据了
                 */
                helper.leadingLoadState = NotLoading(true)
            } else {
                /**
                 * Set the state to not loaded, and there is also paginated data
                 * 设置状态为未加载，并且还有分页数据
                 */
                helper.leadingLoadState = NotLoading(false)
            }
        }, 600)
    }

    /**
     * 滚动到底部（不带动画）
     */
    private fun scrollToBottom() {
        val ll = viewBinding.rv.layoutManager as LinearLayoutManager
        ll.scrollToPositionWithOffset(bottomDataPosition, 0)
    }

    private val bottomDataPosition: Int
        get() = mAdapter.items.size - 1

    private fun genData(): List<Movie> {
        val list = ArrayList<Movie>()
        val random = Random()
        for (i in 0..9) {
            val name = "Chad"
            val price = random.nextInt(10) + 10
            val len = random.nextInt(80) + 60
            val movie =
                Movie(name, len, price, "He was one of Australia's most distinguished artistes")
            list.add(movie)
        }
        return list
    }
}