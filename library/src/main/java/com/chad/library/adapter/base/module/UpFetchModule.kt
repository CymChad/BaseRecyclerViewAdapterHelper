package com.chad.library.adapter.base.module

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnUpFetchListener
import com.chad.library.adapter.base.listener.UpFetchListenerImp

/**
 * @author: limuyang
 * @date: 2019-11-29
 * @Description: 向上加载
 */

/**
 * 需要【向上加载更多】功能的，[BaseQuickAdapter]继承此接口
 */
interface UpFetchModule

open class BaseUpFetchModule(private val baseQuickAdapter: BaseQuickAdapter<*, *>) : UpFetchListenerImp {

    private var mUpFetchListener: OnUpFetchListener? = null

    var isUpFetchEnable = false
    var isUpFetching = false
    /**
     * start up fetch position, default is 1.
     */
    var startUpFetchPosition = 1

    internal fun autoUpFetch(position: Int) {
        if (!isUpFetchEnable || isUpFetching) {
            return
        }
        if (position <= startUpFetchPosition) {
            mUpFetchListener?.onUpFetch()
        }
    }

    override fun setOnUpFetchListener(listener: OnUpFetchListener?) {
        this.mUpFetchListener = listener
    }
}
