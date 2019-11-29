package com.chad.library.adapter.base.module

/**
 * @author: limuyang
 * @date: 2019-11-29
 * @Description: 向上加载
 */

/**
 * 需要【向上加载更多】功能的，继承此接口
 */
interface UpFetchModule

typealias UpFetchListener = () -> Unit

open class BaseUpFetchModule {

    private var mUpFetchListener: UpFetchListener? = null

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
            mUpFetchListener?.invoke()
        }
    }

    fun setUpFetchListener(listener: UpFetchListener) {
        this.mUpFetchListener = listener
    }
}