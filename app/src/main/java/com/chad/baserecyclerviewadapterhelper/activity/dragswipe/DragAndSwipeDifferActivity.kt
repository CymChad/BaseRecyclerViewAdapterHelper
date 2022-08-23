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
import com.chad.baserecyclerviewadapterhelper.R
import com.chad.baserecyclerviewadapterhelper.activity.dragswipe.adapter.DiffDragAndSwipeAdapter
import com.chad.baserecyclerviewadapterhelper.base.BaseActivity
import com.chad.baserecyclerviewadapterhelper.data.DataServer
import com.chad.baserecyclerviewadapterhelper.utils.VibratorUtils.vibrate
import com.chad.library.adapter.base.dragswipe.listener.DragAndSwipeDataCallback
import com.chad.library.adapter.base.dragswipe.QuickDragAndSwipe
import com.chad.library.adapter.base.dragswipe.listener.OnItemDragListener
import com.chad.library.adapter.base.dragswipe.listener.OnItemSwipeListener
import com.chad.library.adapter.base.viewholder.QuickViewHolder

/**
 * Created by limuyang
 * Date: 2019/7/14O
 */
class DragAndSwipeDifferActivity : BaseActivity() {
    private lateinit var mRecyclerView: RecyclerView

    private var mAdapter: DiffDragAndSwipeAdapter =
        DiffDragAndSwipeAdapter()

    var quickDragAndSwipe = QuickDragAndSwipe()
        .setDragMoveFlags(ItemTouchHelper.UP or ItemTouchHelper.DOWN)
        .setSwipeMoveFlags(ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_universal_recycler)
        setBackBtn()
        setTitle("Diff Drag Swipe Use")
        findView()
        initRv()

        initDrag()
    }

    private fun findView() {
        mRecyclerView = findViewById(R.id.rv)
        mRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun initRv() {
        mRecyclerView.adapter = mAdapter

        mAdapter.submitList(DataServer.getDiffUtilDemoEntities())
    }


    private fun initDrag() {
        // 拖拽监听
        val listener: OnItemDragListener = object :
            OnItemDragListener {
            override fun onItemDragStart(viewHolder: RecyclerView.ViewHolder?, pos: Int) {
                vibrate(applicationContext)
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
            override fun onItemSwipeStart(viewHolder: RecyclerView.ViewHolder, pos: Int) {
                Log.d(TAG, "onItemSwipeStart")
            }

            override fun onItemSwipeEnd(viewHolder: RecyclerView.ViewHolder, pos: Int) {
                Log.d(TAG, "onItemSwipeEnd")
            }

            override fun onItemSwiped(viewHolder: RecyclerView.ViewHolder, pos: Int) {
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



        quickDragAndSwipe.attachToRecyclerView(mRecyclerView)
            .setDataCallback(object : DragAndSwipeDataCallback {
                override fun dataSwap(fromPosition: Int, toPosition: Int) {
                    mAdapter.swap(fromPosition, toPosition)
                }

                override fun dataRemoveAt(position: Int) {
                    mAdapter.removeAt(position)
                }
            })
            .setItemDragListener(listener)
            .setItemSwipeListener(swipeListener)
    }
}