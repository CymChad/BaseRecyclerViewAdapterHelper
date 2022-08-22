package com.chad.library.adapter.base.dragswipe

import android.graphics.Canvas
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.listener.OnItemDragListener
import com.chad.library.adapter.base.listener.OnItemSwipeListener

/**
 * 使用拖拽方式的拓展函数
 */
inline fun DefaultDragAndSwipe.setItemDragListener(
    crossinline onItemDragStart: ((viewHolder: RecyclerView.ViewHolder?, pos: Int) -> Unit) = { viewHolder, pos -> },
    crossinline onItemDragMoving: ((
        source: RecyclerView.ViewHolder?,
        from: Int,
        target: RecyclerView.ViewHolder?,
        to: Int
    ) -> Unit) = { source, from, target, to -> },
    crossinline onItemDragEnd: ((viewHolder: RecyclerView.ViewHolder?, pos: Int) -> Unit) = { viewHolder, pos -> },

    ) = apply {
    val listener = object : OnItemDragListener {
        override fun onItemDragStart(viewHolder: RecyclerView.ViewHolder?, pos: Int) {
            onItemDragStart.invoke(viewHolder, pos)
        }

        override fun onItemDragMoving(
            source: RecyclerView.ViewHolder?,
            from: Int,
            target: RecyclerView.ViewHolder?,
            to: Int
        ) {
            onItemDragMoving.invoke(source, from, target, to)
        }

        override fun onItemDragEnd(viewHolder: RecyclerView.ViewHolder?, pos: Int) {
            onItemDragEnd.invoke(viewHolder, pos)
        }
    }
    this.setItemDragListener(listener)
}

/**
 * 滑动删除的拓展函数
 */
inline fun DefaultDragAndSwipe.setItemSwipeListener(
    crossinline onItemSwipeStart: ((viewHolder: RecyclerView.ViewHolder?, pos: Int) -> Unit) = { viewHolder, pos -> },
    crossinline onItemSwipeMoving: ((
        canvas: Canvas?,
        viewHolder: RecyclerView.ViewHolder?,
        dX: Float,
        dY: Float,
        isCurrentlyActive: Boolean
    ) -> Unit) = { canvas, viewHolder, dX, dY, isCurrentlyActive -> },
    crossinline onItemSwiped: ((viewHolder: RecyclerView.ViewHolder?, pos: Int) -> Unit) = { viewHolder, pos -> },
    crossinline onItemSwipeEnd: ((viewHolder: RecyclerView.ViewHolder?, pos: Int) -> Unit) = { viewHolder, pos -> }
) = apply {
    val listener = object : OnItemSwipeListener {
        override fun onItemSwipeStart(viewHolder: RecyclerView.ViewHolder?, pos: Int) {
            onItemSwipeStart.invoke(viewHolder, pos)
        }

        override fun onItemSwipeMoving(
            canvas: Canvas?,
            viewHolder: RecyclerView.ViewHolder?,
            dX: Float,
            dY: Float,
            isCurrentlyActive: Boolean
        ) {
            onItemSwipeMoving.invoke(canvas, viewHolder, dX, dY, isCurrentlyActive)
        }

        override fun onItemSwiped(viewHolder: RecyclerView.ViewHolder?, pos: Int) {
            onItemSwiped.invoke(viewHolder, pos)
        }

        override fun onItemSwipeEnd(viewHolder: RecyclerView.ViewHolder?, pos: Int) {
            onItemSwipeEnd.invoke(viewHolder, pos)
        }
    }
    this.setItemSwipeListener(listener)
}
