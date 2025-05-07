package com.chad.baserecyclerviewadapterhelper.activity.dragswipe

import android.animation.ValueAnimator
import android.graphics.Canvas
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.chad.baserecyclerviewadapterhelper.activity.dragswipe.adapter.DragAndSwipeAdapter
import com.chad.baserecyclerviewadapterhelper.base.BaseViewBindingActivity
import com.chad.baserecyclerviewadapterhelper.databinding.ActivityUniversalRecyclerBinding
import com.chad.baserecyclerviewadapterhelper.utils.Tips
import com.chad.baserecyclerviewadapterhelper.utils.vibrate
import com.chad.library.adapter4.dragswipe.QuickDragAndSwipe
import com.chad.library.adapter4.dragswipe.listener.OnItemDragListener
import com.chad.library.adapter4.dragswipe.listener.OnItemSwipeListener
import com.chad.library.adapter4.viewholder.QuickViewHolder

/**
 * 默认实现拖动与侧滑效果
 * Drag and Drag effects are implemented by default
 */
class DefaultDragAndSwipeActivity : BaseViewBindingActivity<ActivityUniversalRecyclerBinding>() {

    private val mAdapter: DragAndSwipeAdapter = DragAndSwipeAdapter()

    private val quickDragAndSwipe = QuickDragAndSwipe()
        .setDragMoveFlags(ItemTouchHelper.UP or ItemTouchHelper.DOWN or
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)
        .setSwipeMoveFlags(ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)

    override fun initBinding(): ActivityUniversalRecyclerBinding =
        ActivityUniversalRecyclerBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding.titleBar.title = "Default Drag And Swipe"
        viewBinding.titleBar.setOnBackListener { finish() }

        viewBinding.rv.layoutManager = GridLayoutManager(this,3)
        viewBinding.rv.adapter = mAdapter

        val mData = generateData(50)
        mAdapter.submitList(mData)

        // 拖拽监听
        val listener: OnItemDragListener = object : OnItemDragListener {
            override fun onItemDragStart(viewHolder: RecyclerView.ViewHolder?, pos: Int) {
                vibrate()
                Log.d(TAG, "drag start")
                val holder = viewHolder as QuickViewHolder? ?: return
                // 开始时，item背景色变化，demo这里使用了一个动画渐变，使得自然
                val startColor = Color.WHITE
                val endColor = Color.rgb(245, 245, 245)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    val v = ValueAnimator.ofArgb(startColor, endColor)
                    v.addUpdateListener { animation: ValueAnimator ->
                        holder.itemView.setBackgroundColor(
                            animation.animatedValue as Int
                        )
                    }
                    v.duration = 300
                    v.start()
                }
            }

            override fun onItemDragMoving(
                source: RecyclerView.ViewHolder,
                from: Int,
                target: RecyclerView.ViewHolder,
                to: Int
            ) {
                Log.d(
                    TAG,
                    "move from: " + source.bindingAdapterPosition + " to: " + target.bindingAdapterPosition
                )
            }

            override fun onItemDragEnd(viewHolder: RecyclerView.ViewHolder, pos: Int) {
                Log.d(TAG, "drag end")
                val holder = viewHolder as QuickViewHolder
                // 结束时，item背景色变化，demo这里使用了一个动画渐变，使得自然
                val startColor = Color.rgb(245, 245, 245)
                val endColor = Color.WHITE
                // 动画
                val v = ValueAnimator.ofArgb(startColor, endColor)
                v.addUpdateListener { animation: ValueAnimator ->
                    holder.itemView.setBackgroundColor(
                        animation.animatedValue as Int
                    )
                }
                v.duration = 300
                v.start()

                mAdapter.items.forEach {
                    Log.d(
                        TAG,
                        "-------->> w 顺序 ${it} "
                    )
                }

            }
        }
        val swipeListener: OnItemSwipeListener = object : OnItemSwipeListener {
            override fun onItemSwipeStart(viewHolder: RecyclerView.ViewHolder?, bindingAdapterPosition: Int) {
                Log.d(TAG, "onItemSwipeStart")
            }

            override fun onItemSwipeEnd(viewHolder: RecyclerView.ViewHolder, bindingAdapterPosition: Int) {
                Log.d(TAG, "onItemSwipeEnd: " + bindingAdapterPosition)
            }

            override fun onItemSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int, bindingAdapterPosition: Int) {
                Log.d(TAG, "onItemSwiped")
            }

            override fun onItemSwipeMoving(
                canvas: Canvas,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                isCurrentlyActive: Boolean
            ) {
                Log.d(TAG, "onItemSwipeMoving")
            }
        }

        // 滑动事件
        quickDragAndSwipe.attachToRecyclerView(viewBinding.rv)
            .setDataCallback(mAdapter)
            .setItemDragListener(listener)
            .setItemSwipeListener(swipeListener)

        // 点击事件
        mAdapter.setOnItemClickListener { adapter, view, position ->
            Tips.show("点击了：$position")
        }
    }

    private fun generateData(size: Int): List<String> {
        val data = ArrayList<String>(size)
        for (i in 0 until size) {
            data.add("item $i")
        }
        return data
    }

    companion object {
        private const val TAG = "Default Drag And Swipe"
    }
}