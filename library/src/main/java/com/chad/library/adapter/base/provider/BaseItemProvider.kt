package com.chad.library.adapter.base.provider

import android.content.Context
import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import com.chad.library.adapter.base.BaseProviderMultiAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import java.lang.ref.WeakReference

abstract class BaseItemProvider<T, VH : BaseViewHolder> {

    lateinit var context: Context
    private var weakAdapter: WeakReference<BaseProviderMultiAdapter<T, VH>>? = null

    private val clickViewIds: ArrayList<Int> by lazy { ArrayList<Int>() }
    private val longClickViewIds: ArrayList<Int> by lazy { ArrayList<Int>() }

    internal fun setAdapter(adapter: BaseProviderMultiAdapter<T, VH>) {
        weakAdapter = WeakReference(adapter)
    }

    open fun getAdapter(): BaseProviderMultiAdapter<T, VH>? {
        return weakAdapter?.get()
    }

    abstract val itemViewType: Int

    abstract val layoutId: Int
        @LayoutRes
        get

    abstract fun convert(helper: VH, data: T?)

    open fun convert(helper: VH, data: T?, payloads: List<Any>) {}

    /**
     * item 若想实现条目点击事件则重写该方法
     * @param helper VH
     * @param data T
     * @param position Int
     */
    open fun onClick(helper: VH, view: View, data: T, position: Int) {}

    /**
     * item 若想实现条目长按事件则重写该方法
     * @param helper VH
     * @param data T
     * @param position Int
     * @return Boolean
     */
    open fun onLongClick(helper: VH, view: View, data: T, position: Int): Boolean {
        return false
    }

    open fun onChildClick(helper: VH, view: View, data: T, position: Int) {}

    open fun onChildLongClick(helper: VH, view: View, data: T, position: Int): Boolean {
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