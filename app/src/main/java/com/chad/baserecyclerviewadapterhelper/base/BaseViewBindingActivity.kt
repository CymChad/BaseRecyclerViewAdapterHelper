package com.chad.baserecyclerviewadapterhelper.base

import android.view.View
import androidx.viewbinding.ViewBinding

abstract class BaseViewBindingActivity<V : ViewBinding> : BaseActivity() {

    private var _viewBinding: V? = null

    protected val viewBinding: V
        get() {
            return _viewBinding ?: throw IllegalStateException(
                "Should be called initBinding()"
            )
        }

    /**
     * 初始化 [viewBinding]
     */
    abstract fun initBinding(): V

    final override val contentView: View
        get() {
            return initBinding().apply {
                _viewBinding = this
            }.root
        }

}