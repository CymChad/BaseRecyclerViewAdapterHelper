package com.chad.library.adapter.base.provider

import com.chad.library.adapter.base.BaseNodeAdapter
import com.chad.library.adapter.base.BaseProviderMultiAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.entity.node.BaseNode

abstract class BaseNodeProvider<VH : BaseViewHolder>
    : BaseItemProvider<BaseNode, VH>() {

    override fun getAdapter(): BaseNodeAdapter<VH>? {
        return super.getAdapter() as? BaseNodeAdapter<VH>
    }

//    @LayoutRes
//    open val sectionHeaderLayoutId: Int = 0
//
//    @LayoutRes
//    open val sectionFooterLayoutId: Int = 0

//    abstract val sectionItemType: SectionItemType
}

enum class SectionItemType {
    Header, Footer, Item
}