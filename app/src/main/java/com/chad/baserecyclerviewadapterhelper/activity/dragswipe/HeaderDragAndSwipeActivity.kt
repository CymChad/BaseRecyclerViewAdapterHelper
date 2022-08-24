package com.chad.baserecyclerviewadapterhelper.activity.dragswipe

import android.animation.ValueAnimator
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.baserecyclerviewadapterhelper.activity.dragswipe.adapter.HeaderDragAndSwipeAdapter
import com.chad.baserecyclerviewadapterhelper.activity.home.adapter.HomeTopHeaderAdapter
import com.chad.baserecyclerviewadapterhelper.base.BaseViewBindingActivity
import com.chad.baserecyclerviewadapterhelper.databinding.ActivityUniversalRecyclerBinding
import com.chad.baserecyclerviewadapterhelper.utils.VibratorUtils.vibrate
import com.chad.library.adapter.base.QuickAdapterHelper
import com.chad.library.adapter.base.dragswipe.setItemDragListener
import com.chad.library.adapter.base.dragswipe.setItemSwipeListener
import com.chad.library.adapter.base.loadState.LoadState.NotLoading
import com.chad.library.adapter.base.loadState.trailing.TrailingLoadStateAdapter
import com.chad.library.adapter.base.viewholder.QuickViewHolder
import kotlinx.coroutines.*

/**
 * 带头部局以及带加载下一页的，拖拽demo
 */
class HeaderDragAndSwipeActivity : BaseViewBindingActivity<ActivityUniversalRecyclerBinding>() {


    private val pageInfo = PageInfo()

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

    var headerDragAndSwipe = HeaderDragAndSwipe()
        .setDragMoveFlags(ItemTouchHelper.UP or ItemTouchHelper.DOWN)
        .setSwipeMoveFlags(ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)

    private val mAdapter: HeaderDragAndSwipeAdapter = HeaderDragAndSwipeAdapter()
    private lateinit var helper: QuickAdapterHelper

    override fun initBinding(): ActivityUniversalRecyclerBinding =
        ActivityUniversalRecyclerBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding.titleBar.title = "Head Drag And Swipe"
        viewBinding.titleBar.setOnBackListener { finish() }

        viewBinding.rv.layoutManager = LinearLayoutManager(this)


        helper = QuickAdapterHelper.Builder(mAdapter)
            .setTrailingLoadStateAdapter(
                object : TrailingLoadStateAdapter.OnTrailingListener {
                    override fun onLoad() {
                        loadMore()
                    }

                    override fun onFailRetry() {
                    }

                    override fun isAllowLoading(): Boolean {
                        return true
                    }
                })
            .build()
            .addBeforeAdapter(HomeTopHeaderAdapter())

        headerDragAndSwipe.attachToRecyclerView(viewBinding.rv)
            .setDataCallback(mAdapter)
            .setItemDragListener(
                onItemDragStart = { viewHolder, pos ->
                    Log.d(TAG, "drag start")
                    vibrate(applicationContext)
                    val holder = viewHolder as QuickViewHolder
                    // 开始时，item背景色变化，demo这里使用了一个动画渐变，使得自然
                    val startColor = Color.WHITE
                    val endColor = Color.rgb(245, 245, 245)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        ValueAnimator.ofArgb(startColor, endColor).apply {
                            addUpdateListener { animation: ValueAnimator ->
                                holder.itemView.setBackgroundColor(
                                    animation.animatedValue as Int
                                )
                            }
                            duration = 300
                            start()
                        }
                    }
                },
                onItemDragMoving = { source, from, target, to ->
                    Log.d(TAG, "move from: $from  to:  $to")
                },
                onItemDragEnd = { viewHolder, pos ->
                    Log.d(TAG, "drag end")
                    val holder = viewHolder as QuickViewHolder
                    // 结束时，item背景色变化，demo这里使用了一个动画渐变，使得自然
                    val startColor = Color.rgb(245, 245, 245)
                    val endColor = Color.WHITE
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        ValueAnimator.ofArgb(startColor, endColor).apply {
                            addUpdateListener { animation: ValueAnimator ->
                                holder.itemView.setBackgroundColor(
                                    animation.animatedValue as Int
                                )
                            }
                            duration = 300
                            start()
                        }
                    }
                }

            )
            .setItemSwipeListener(
                onItemSwipeStart = { viewHolder, pos ->
                    Log.d(TAG, "onItemSwipeStart")
                },
                onItemSwipeMoving = { canvas, viewHolder, dX, dY, isCurrentlyActive ->
                    Log.d(TAG, "onItemSwipeMoving")
                },
                onItemSwiped = { viewHolder, pos ->
                    Log.d(TAG, "onItemSwiped")
                },
                onItemSwipeEnd = { viewHolder, pos ->
                    Log.d(TAG, "onItemSwipeEnd")
                }
            )
        viewBinding.rv.adapter = helper.adapter
        loadMore()
    }


    private fun loadMore() {
        Request(pageInfo.page, object : RequestCallBack {
            override fun success(data: List<String>) {
                if (pageInfo.page == 0) {
                    mAdapter.submitList(data)
                } else {
                    mAdapter.addAll(data)
                }

                helper.trailingLoadState = NotLoading(false)
                // page加一
                pageInfo.nextPage()
            }

            override fun fail(e: Exception?) {

            }

            override fun end() {
                helper.trailingLoadState = NotLoading(true)
            }
        }).loadMore()
    }

    /**
     * 模拟加载数据的类，不用特别关注
     */
    internal class Request(
        private val mPage: Int,
        private val mCallBack: RequestCallBack
    ) {

        fun loadMore() {
            GlobalScope.launch(Dispatchers.IO) {
                if (mPage != 0) {
                    delay(1500)
                }
                withContext(Dispatchers.Main) {
                    val size = PAGE_SIZE
                    if (mPage == 3) {
                        mCallBack.end()
                    } else {
                        val starIndex = mPage.times(size)
                        mCallBack.success(generateData(starIndex, size))
                    }
                }
            }
        }

        private fun generateData(starIndex: Int, size: Int): List<String> {
            val data = java.util.ArrayList<String>(size)
            val endIndex = starIndex.plus(size)
            for (i in starIndex until endIndex) {
                data.add("item $i")
            }
            return data
        }

    }

    internal interface RequestCallBack {
        /**
         * 模拟加载成功
         *
         * @param data 数据
         */
        fun success(data: List<String>)

        /**
         * 模拟加载失败
         *
         * @param e 错误信息
         */
        fun fail(e: Exception?)

        /**
         * 模拟加载结束
         */
        fun end()
    }

    companion object {
        private const val PAGE_SIZE = 20
        private const val TAG = "Default Drag And Swipe"
    }
}