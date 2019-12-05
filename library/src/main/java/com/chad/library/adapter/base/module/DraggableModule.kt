package com.chad.library.adapter.base.module

import android.graphics.Canvas
import android.view.MotionEvent
import android.view.View
import android.view.View.OnLongClickListener
import android.view.View.OnTouchListener
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.R
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.dragswipe.DragAndSwipeCallback
import com.chad.library.adapter.base.listener.DraggableListenerImp
import com.chad.library.adapter.base.listener.OnItemDragListener
import com.chad.library.adapter.base.listener.OnItemSwipeListener
import java.util.*

/**
 * @author: limuyang
 * @date: 2019-12-05
 * @Description:
 */

/**
 * 需要【拖拽】功能的，[BaseQuickAdapter]继承此接口
 */
interface DraggableModule

open class BaseDraggableModule(private val baseQuickAdapter: BaseQuickAdapter<*, *>) : DraggableListenerImp {

    var isDragEnabled = false
    var isSwipeEnabled = false
    var toggleViewId = NO_TOGGLE_VIEW
    lateinit var itemTouchHelper: ItemTouchHelper
    lateinit var itemTouchHelperCallback: DragAndSwipeCallback

    protected var mOnToggleViewTouchListener: OnTouchListener? = null
    protected var mOnToggleViewLongClickListener: OnLongClickListener? = null
    protected var mOnItemDragListener: OnItemDragListener? = null
    protected var mOnItemSwipeListener: OnItemSwipeListener? = null

    init {
        initItemTouch()
    }

    private fun initItemTouch() {
        itemTouchHelperCallback = DragAndSwipeCallback(this)
        itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
    }

    internal fun initView(holder: BaseViewHolder) {
        if (isDragEnabled) {
            if (hasToggleView()) {
                val toggleView = holder.itemView.findViewById<View>(toggleViewId)
                if (toggleView != null) {
                    toggleView.setTag(R.id.BaseQuickAdapter_viewholder_support, holder)
                    if (isDragOnLongPressEnabled) {
                        toggleView.setOnLongClickListener(mOnToggleViewLongClickListener)
                    } else {
                        toggleView.setOnTouchListener(mOnToggleViewTouchListener)
                    }
                }
            }
        }
    }


    fun attachToRecyclerView(recyclerView: RecyclerView) {
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    /**
     * Is there a toggle view which will trigger drag event.
     */
    open fun hasToggleView(): Boolean {
        return toggleViewId != NO_TOGGLE_VIEW
    }

    /**
     * Set the drag event should be trigger on long press.
     * Work when the toggleViewId has been set.
     *
     */
    open var isDragOnLongPressEnabled = true
        set(value) {
            field = value
            if (value) {
                mOnToggleViewTouchListener = null
                mOnToggleViewLongClickListener = OnLongClickListener { v ->
                    if (isDragEnabled) {
                        itemTouchHelper.startDrag(v.getTag(R.id.BaseQuickAdapter_viewholder_support) as RecyclerView.ViewHolder)
                    }
                    true
                }
            } else {
                mOnToggleViewTouchListener = OnTouchListener { v, event ->
                    if (event.action == MotionEvent.ACTION_DOWN && !isDragOnLongPressEnabled) {
                        if (isDragEnabled) {
                            itemTouchHelper.startDrag(v.getTag(R.id.BaseQuickAdapter_viewholder_support) as RecyclerView.ViewHolder)
                        }
                        true
                    } else {
                        false
                    }
                }
                mOnToggleViewLongClickListener = null
            }
        }


    protected fun getViewHolderPosition(viewHolder: RecyclerView.ViewHolder): Int {
        return viewHolder.adapterPosition - baseQuickAdapter.getHeaderLayoutCount()
    }

    /************************* Drag *************************/

    open fun onItemDragStart(viewHolder: RecyclerView.ViewHolder?) {
        mOnItemDragListener?.onItemDragStart(viewHolder, getViewHolderPosition(viewHolder!!))
    }

    open fun onItemDragMoving(source: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder) {
        val from = getViewHolderPosition(source)
        val to = getViewHolderPosition(target)
        if (inRange(from) && inRange(to)) {
            if (from < to) {
                for (i in from until to) {
                    Collections.swap(baseQuickAdapter.data, i, i + 1)
                }
            } else {
                for (i in from downTo to + 1) {
                    Collections.swap(baseQuickAdapter.data, i, i - 1)
                }
            }
            baseQuickAdapter.notifyItemMoved(source.adapterPosition, target.adapterPosition)
        }
        mOnItemDragListener?.onItemDragMoving(source, from, target, to)
    }

    open fun onItemDragEnd(viewHolder: RecyclerView.ViewHolder?) {
        mOnItemDragListener?.onItemDragEnd(viewHolder, getViewHolderPosition(viewHolder!!))
    }

    /************************* Swipe *************************/

    open fun onItemSwipeStart(viewHolder: RecyclerView.ViewHolder?): Unit {
        if (isSwipeEnabled) {
            mOnItemSwipeListener?.onItemSwipeStart(viewHolder, getViewHolderPosition(viewHolder!!))
        }
    }

    open fun onItemSwipeClear(viewHolder: RecyclerView.ViewHolder?) {
        if (isSwipeEnabled) {
            mOnItemSwipeListener?.clearView(viewHolder, getViewHolderPosition(viewHolder!!))
        }
    }

    open fun onItemSwiped(viewHolder: RecyclerView.ViewHolder) {
        val pos = getViewHolderPosition(viewHolder)
        if (inRange(pos)) {
            baseQuickAdapter.data.removeAt(pos)
            baseQuickAdapter.notifyItemRemoved(viewHolder.adapterPosition)
            if (isSwipeEnabled) {
                mOnItemSwipeListener?.onItemSwiped(viewHolder, pos)
            }
        }
    }

    open fun onItemSwiping(canvas: Canvas?, viewHolder: RecyclerView.ViewHolder?, dX: Float, dY: Float, isCurrentlyActive: Boolean) {
        if (isSwipeEnabled) {
            mOnItemSwipeListener?.onItemSwipeMoving(canvas, viewHolder, dX, dY, isCurrentlyActive)
        }
    }

    private fun inRange(position: Int): Boolean {
        return position >= 0 && position < baseQuickAdapter.data.size
    }

    /**
     * 设置监听
     * @param onItemDragListener OnItemDragListener?
     */
    override fun setOnItemDragListener(onItemDragListener: OnItemDragListener?) {
        this.mOnItemDragListener = onItemDragListener
    }

    override fun setOnItemSwipeListener(onItemSwipeListener: OnItemSwipeListener?) {
        this.mOnItemSwipeListener = onItemSwipeListener
    }

    companion object {
        private const val NO_TOGGLE_VIEW = 0
    }


}