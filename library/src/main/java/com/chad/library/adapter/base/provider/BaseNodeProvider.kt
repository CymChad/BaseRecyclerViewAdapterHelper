package com.chad.library.adapter.base.provider

import com.chad.library.adapter.base.BaseNodeAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.entity.node.BaseNode

abstract class BaseNodeProvider<VH : BaseViewHolder> : BaseItemProvider<BaseNode, VH>() {

    override fun getAdapter(): BaseNodeAdapter<VH>? {
        return super.getAdapter() as? BaseNodeAdapter<VH>
    }

}