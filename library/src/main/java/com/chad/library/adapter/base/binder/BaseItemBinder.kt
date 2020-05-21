package com.chad.library.adapter.base.binder

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import com.chad.library.adapter.base.BaseBinderAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 * Binder 的基类
 */
abstract class BaseItemBinder<T, VH : BaseViewHolder> {

    private val clickViewIds by lazy(LazyThreadSafetyMode.NONE) { ArrayList<Int>() }
    private val longClickViewIds by lazy(LazyThreadSafetyMode.NONE) { ArrayList<Int>() }

    internal var _adapter: BaseBinderAdapter? = null
    internal var _context: Context? = null

    val adapter: BaseBinderAdapter
        get() {
            checkNotNull(_adapter) {
                """This $this has not been attached to BaseBinderAdapter yet.
                    You should not call the method before addItemBinder()."""
            }
            return _adapter!!
        }

    val context: Context
        get() {
            checkNotNull(_context) {
                """This $this has not been attached to BaseBinderAdapter yet.
                    You should not call the method before onCreateViewHolder()."""
            }
            return _context!!
        }

    val data: MutableList<Any> get() = adapter.data

    abstract fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH

    /**
     * 在此处对设置item数据
     * @param holder VH
     * @param data T
     */
    abstract fun convert(holder: VH, data: T)

    /**
     * 使用局部刷新时候，会调用此方法
     * @param holder VH
     * @param data T
     * @param payloads List<Any>
     */
    open fun convert(holder: VH, data: T, payloads: List<Any>) {}

    open fun onFailedToRecycleView(holder: VH): Boolean {
        return false
    }

    /**
     * Called when a view created by this [BaseItemBinder] has been attached to a window.
     * 当此[BaseItemBinder]出现在屏幕上的时候，会调用此方法
     *
     * This can be used as a reasonable signal that the view is about to be seen
     * by the user. If the [BaseItemBinder] previously freed any resources in
     * [onViewDetachedFromWindow][.onViewDetachedFromWindow]
     * those resources should be restored here.
     *
     * @param holder Holder of the view being attached
     */
    open fun onViewAttachedToWindow(holder: VH) {}

    /**
     * Called when a view created by this [BaseItemBinder] has been detached from its
     * window.
     * 当此[BaseItemBinder]从屏幕上移除的时候，会调用此方法
     *
     * Becoming detached from the window is not necessarily a permanent condition;
     * the consumer of an Adapter's views may choose to cache views offscreen while they
     * are not visible, attaching and detaching them as appropriate.
     *
     * @param holder Holder of the view being detached
     */
    open fun onViewDetachedFromWindow(holder: VH) {}

    /**
     * item 若想实现条目点击事件则重写该方法
     * @param holder VH
     * @param data T
     * @param position Int
     */
    open fun onClick(holder: VH, view: View, data: T, position: Int) {}

    /**
     * item 若想实现条目长按事件则重写该方法
     * @param holder VH
     * @param data T
     * @param position Int
     * @return Boolean
     */
    open fun onLongClick(holder: VH, view: View, data: T, position: Int): Boolean {
        return false
    }

    /**
     * item 子控件的点击事件
     * @param holder VH
     * @param view View
     * @param data T
     * @param position Int
     */
    open fun onChildClick(holder: VH, view: View, data: T, position: Int) {}

    /**
     * item 子控件的长按事件
     * @param holder VH
     * @param view View
     * @param data T
     * @param position Int
     * @return Boolean
     */
    open fun onChildLongClick(holder: VH, view: View, data: T, position: Int): Boolean {
        return false
    }

    fun addChildClickViewIds(@IdRes vararg ids: Int) {
        ids.forEach {
            this.clickViewIds.add(it)
        }
    }

    fun getChildClickViewIds() = this.clickViewIds

    fun addChildLongClickViewIds(@IdRes vararg ids: Int) {
        ids.forEach {
            this.longClickViewIds.add(it)
        }
    }

    fun getChildLongClickViewIds() = this.longClickViewIds
}