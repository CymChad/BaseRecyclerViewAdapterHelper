package com.chad.library.adapter.base

import android.view.ViewGroup
import com.chad.library.adapter.base.delegate.BaseMultiTypeDelegate

/**
 * 多类型布局，通过代理类的方式，返回布局 id 和 item 类型；
 * 适用于:
 * 1、实体类不方便扩展，此Adapter的数据类型可以是任意类型，只需要在[BaseMultiTypeDelegate.getItemType]中返回对应类型
 * 2、item 类型较少
 * 如果类型较多，为了方便隔离各类型的业务逻辑，推荐使用[BaseProviderMultiAdapter]
 *
 * @param T
 * @param VH : BaseViewHolder
 * @property mMultiTypeDelegate BaseMultiTypeDelegate<T>?
 * @constructor
 */
abstract class BaseDelegateMultiAdapter<T, VH : BaseViewHolder>(data: MutableList<T>? = null) :
        BaseQuickAdapter<T, VH>(0, data) {

    private var mMultiTypeDelegate: BaseMultiTypeDelegate<T>? = null

    /**
     * 通过此方法设置代理
     * @param multiTypeDelegate BaseMultiTypeDelegate<T>
     */
    fun setMultiTypeDelegate(multiTypeDelegate: BaseMultiTypeDelegate<T>) {
        this.mMultiTypeDelegate = multiTypeDelegate
    }

    fun getMultiTypeDelegate(): BaseMultiTypeDelegate<T>? = mMultiTypeDelegate

    override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int): VH {
        val delegate = getMultiTypeDelegate()
        checkNotNull(delegate) { "please use setMultiTypeDelegate first!" }
        val layoutId = delegate.getLayoutId(viewType)
        return createBaseViewHolder(parent, layoutId)
    }

    override fun getDefItemViewType(position: Int): Int {
        val delegate = getMultiTypeDelegate()
        checkNotNull(delegate) { "please use setMultiTypeDelegate first!" }
        return delegate.getItemType(data, position)
    }
}