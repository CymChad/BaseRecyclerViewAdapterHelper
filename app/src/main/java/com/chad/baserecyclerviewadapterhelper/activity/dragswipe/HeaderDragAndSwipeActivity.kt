package com.chad.baserecyclerviewadapterhelper.activity.dragswipe

import android.animation.ValueAnimator
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.baserecyclerviewadapterhelper.R
import com.chad.baserecyclerviewadapterhelper.adapter.HeaderDragAndSwipeAdapter
import com.chad.baserecyclerviewadapterhelper.adapter.HomeTopHeaderAdapter
import com.chad.baserecyclerviewadapterhelper.base.BaseActivity
import com.chad.baserecyclerviewadapterhelper.utils.VibratorUtils.vibrate
import com.chad.library.adapter.base.QuickAdapterHelper
import com.chad.library.adapter.base.dragswipe.setItemDragListener
import com.chad.library.adapter.base.dragswipe.setItemSwipeListener
import com.chad.library.adapter.base.loadState.LoadState.NotLoading
import com.chad.library.adapter.base.loadState.trailing.TrailingLoadStateAdapter
import com.chad.library.adapter.base.viewholder.QuickViewHolder
import kotlinx.android.synthetic.main.activity_header_drag_and_swipe.*
import kotlinx.coroutines.*

/**
 * 带头部局以及带加载下一页的，拖拽demo
 */
class HeaderDragAndSwipeActivity : BaseActivity() {
    companion object {
        private val PAGE_SIZE = 20
    }

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
    private var mAdapter: HeaderDragAndSwipeAdapter? = null
    private var helper: QuickAdapterHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_header_drag_and_swipe)
        setBackBtn()
        setTitle("Head Drag And Swipe")
        mRVDragAndSwipe.layoutManager = LinearLayoutManager(this);
        mAdapter = HeaderDragAndSwipeAdapter().apply {
            helper = QuickAdapterHelper.Builder(this)
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
                .build().addHeader(HomeTopHeaderAdapter())
            headerDragAndSwipe.attachToRecyclerView(mRVDragAndSwipe)
                .setAdapterImpl(this)
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
                        Log.d(TAG,  "onItemSwiped")
                    },
                    onItemSwipeEnd = { viewHolder, pos ->
                        Log.d(TAG, "onItemSwipeEnd")
                    }
                )
        }
        mRVDragAndSwipe.adapter = helper?.adapter
        loadMore()
    }


    private fun loadMore() {
        Request(pageInfo.page, object : RequestCallBack {
            override fun success(data: List<String>) {
                if (pageInfo.page == 0) {
                    mAdapter?.submitList(data)
                } else {
                    mAdapter?.addAll(data)
                }
                helper?.trailingLoadStateAdapter?.checkDisableLoadMoreIfNotFullPage()
                helper?.trailingLoadState = NotLoading(false)
                // page加一
                pageInfo.nextPage()
            }

            override fun fail(e: Exception?) {

            }

            override fun end() {
                helper?.trailingLoadState = NotLoading(true)
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
                    var size = PAGE_SIZE
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
}