package com.chad.library.adapter.base.delegate

import android.util.SparseIntArray
import androidx.annotation.LayoutRes

/**
 * help you to achieve multi type easily
 *
 *
 * Created by tysheng
 * Date: 2017/4/6 08:41.
 * Email: tyshengsx@gmail.com
 *
 * more information: https://github.com/CymChad/BaseRecyclerViewAdapterHelper/issues/968
 */

abstract class BaseMultiTypeDelegate<T>(private var layouts: SparseIntArray = SparseIntArray()) {
    private var autoMode: Boolean = false
    private var selfMode: Boolean = false

    /**
     * get the item type from specific entity.
     *
     * @param data entity
     * @param position
     * @return item type
     */
    abstract fun getItemType(data: List<T>, position: Int): Int

    fun getLayoutId(viewType: Int): Int {
        val layoutResId = layouts.get(viewType)
        require(layoutResId != 0) { "ViewType: $viewType found layoutResId，please use registerItemType() first!" }
        return layoutResId
    }

    private fun registerItemType(type: Int, @LayoutRes layoutResId: Int) {
        this.layouts.put(type, layoutResId)
    }

    /**
     * auto increase type vale, start from 0.
     *
     * @param layoutResIds layout id arrays
     * @return MultiTypeDelegate
     */
    fun addItemTypeAutoIncrease(@LayoutRes vararg layoutResIds: Int): BaseMultiTypeDelegate<T> {
        autoMode = true
        checkMode(selfMode)
        for (i in layoutResIds.indices) {
            registerItemType(i, layoutResIds[i])
        }
        return this
    }

    /**
     * set your own type one by one.
     *
     * @param type        type value
     * @param layoutResId layout id
     * @return MultiTypeDelegate
     */
    fun addItemType(type: Int, @LayoutRes layoutResId: Int): BaseMultiTypeDelegate<T> {
        selfMode = true
        checkMode(autoMode)
        registerItemType(type, layoutResId)
        return this
    }

    private fun checkMode(mode: Boolean) {
        require(!mode) { "Don't mess two register mode" }
    }

}
