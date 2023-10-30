package com.chad.baserecyclerviewadapterhelper.activity.dragswipe

import android.animation.ValueAnimator
import android.graphics.Canvas
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.baserecyclerviewadapterhelper.activity.dragswipe.adapter.DiffDragAndSwipeAdapter
import com.chad.baserecyclerviewadapterhelper.base.BaseViewBindingActivity
import com.chad.baserecyclerviewadapterhelper.data.DataServer
import com.chad.baserecyclerviewadapterhelper.databinding.ActivityUniversalRecyclerBinding
import com.chad.baserecyclerviewadapterhelper.utils.vibrate
import com.chad.library.adapter.base.dragswipe.QuickDragAndSwipe
import com.chad.library.adapter.base.dragswipe.listener.DragAndSwipeDataCallback
import com.chad.library.adapter.base.dragswipe.listener.OnItemDragListener
import com.chad.library.adapter.base.dragswipe.listener.OnItemSwipeListener
import com.chad.library.adapter.base.viewholder.QuickViewHolder

/**
 * Created by limuyang
 * Date: 2019/7/14O
 *
 * DiffAdapter DragAndSwipe
 */
class DragAndSwipeDifferActivity : BaseViewBindingActivity<ActivityUniversalRecyclerBinding>() {

    private var mAdapter: DiffDragAndSwipeAdapter = DiffDragAndSwipeAdapter()

    private var quickDragAndSwipe = QuickDragAndSwipe()
        .setDragMoveFlags(ItemTouchHelper.UP or ItemTouchHelper.DOWN)
        .setSwipeMoveFlags(ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)

    override fun initBinding(): ActivityUniversalRecyclerBinding =
        ActivityUniversalRecyclerBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding.titleBar.title = "Diff Drag Swipe Use"
        viewBinding.titleBar.setOnBackListener { finish() }

        viewBinding.rv.layoutManager = LinearLayoutManager(this)
        initRv()

        initDrag()
    }

    private fun initRv() {
        viewBinding.rv.adapter = mAdapter

        mAdapter.submitList(DataServer.diffUtilDemoEntities)
    }


    private fun initDrag() {
        // 拖拽监听
        val listener: OnItemDragListener = object :
            OnItemDragListener {
            override fun onItemDragStart(viewHolder: RecyclerView.ViewHolder?, pos: Int) {
                vibrate()
                Log.d(TAG, "drag start")
                val holder = viewHolder as QuickViewHolder
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
                    TAG, "move from: " + source.bindingAdapterPosition + " to: " + target.bindingAdapterPosition
                )
            }

            override fun onItemDragEnd(viewHolder: RecyclerView.ViewHolder, pos: Int) {
                Log.d(TAG, "drag end")
                val holder = viewHolder as QuickViewHolder
                // 结束时，item背景色变化，demo这里使用了一个动画渐变，使得自然
                val startColor = Color.rgb(245, 245, 245)
                val endColor = Color.WHITE
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
        }

        val swipeListener: OnItemSwipeListener = object :
            OnItemSwipeListener {
            override fun onItemSwipeStart(viewHolder: RecyclerView.ViewHolder?, bindingAdapterPosition: Int) {
                Log.d(TAG, "onItemSwipeStart")
            }

            override fun onItemSwipeEnd(viewHolder: RecyclerView.ViewHolder, bindingAdapterPosition: Int) {
                Log.d(TAG, "onItemSwipeEnd")
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



        quickDragAndSwipe.attachToRecyclerView(viewBinding.rv)
            .setDataCallback(object : DragAndSwipeDataCallback {
                override fun dataMove(fromPosition: Int, toPosition: Int) {
                    mAdapter.swap(fromPosition, toPosition)
                }

                override fun dataRemoveAt(position: Int) {
                    mAdapter.removeAt(position)
                }
            })
            .setItemDragListener(listener)
            .setItemSwipeListener(swipeListener)
    }

    companion object {
        private const val TAG = "Diff Drag Swipe Use"
    }
}