package com.chad.library.adapter.base.provider

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import com.chad.library.adapter.base.BaseProviderMultiAdapter
import com.chad.library.adapter.base.util.getItemView
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import java.lang.ref.WeakReference

/**
 * [BaseProviderMultiAdapter] 的Provider基类
 * @param T 数据类型
 */
abstract class BaseItemProvider<T> {

    lateinit var context: Context

    private var weakAdapter: WeakReference<BaseProviderMultiAdapter<T>>? = null
    private val clickViewIds by lazy(LazyThreadSafetyMode.NONE) { ArrayList<Int>() }
    private val longClickViewIds by lazy(LazyThreadSafetyMode.NONE) { ArrayList<Int>() }

    internal fun setAdapter(adapter: BaseProviderMultiAdapter<T>) {
        weakAdapter = WeakReference(adapter)
    }

    open fun getAdapter(): BaseProviderMultiAdapter<T>? {
        return weakAdapter?.get()
    }

    abstract val itemViewType: Int

    abstract val layoutId: Int
        @LayoutRes
        get

    abstract fun convert(helper: BaseViewHolder, item: T)

    open fun convert(helper: BaseViewHolder, item: T, payloads: List<Any>) {}

    /**
     * （可选重写）创建 ViewHolder。
     * 默认实现返回[BaseViewHolder]，可重写返回自定义 ViewHolder
     *
     * @param parent
     */
    open fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return BaseViewHolder(parent.getItemView(layoutId))
    }

    /**
     * （可选重写）ViewHolder创建完毕以后的回掉方法。
     * @param viewHolder VH
     */
    open fun onViewHolderCreated(viewHolder: BaseViewHolder, viewType: Int) {}

    /**
     * Called when a view created by this [BaseItemProvider] has been attached to a window.
     * 当此[BaseItemProvider]出现在屏幕上的时候，会调用此方法
     *
     * This can be used as a reasonable signal that the view is about to be seen
     * by the user. If the [BaseItemProvider] previously freed any resources in
     * [onViewDetachedFromWindow][.onViewDetachedFromWindow]
     * those resources should be restored here.
     *
     * @param holder Holder of the view being attached
     */
    open fun onViewAttachedToWindow(holder: BaseViewHolder) {}

    /**
     * Called when a view created by this [BaseItemProvider] has been detached from its
     * window.
     * 当此[BaseItemProvider]从屏幕上移除的时候，会调用此方法
     *
     * Becoming detached from the window is not necessarily a permanent condition;
     * the consumer of an Adapter's views may choose to cache views offscreen while they
     * are not visible, attaching and detaching them as appropriate.
     *
     * @param holder Holder of the view being detached
     */
    open fun onViewDetachedFromWindow(holder: BaseViewHolder) {}

    /**
     * item 若想实现条目点击事件则重写该方法
     * @param helper VH
     * @param data T
     * @param position Int
     */
    open fun onClick(helper: BaseViewHolder, view: View, data: T, position: Int) {}

    /**
     * item 若想实现条目长按事件则重写该方法
     * @param helper VH
     * @param data T
     * @param position Int
     * @return Boolean
     */
    open fun onLongClick(helper: BaseViewHolder, view: View, data: T, position: Int): Boolean {
        return false
    }

    open fun onChildClick(helper: BaseViewHolder, view: View, data: T, position: Int) {}

    open fun onChildLongClick(helper: BaseViewHolder, view: View, data: T, position: Int): Boolean {
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