package com.chad.library.adapter.base.provider

import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.entity.NSectionEntity

abstract class BaseSectionItemProvider<VH : BaseViewHolder> : BaseItemProvider<NSectionEntity, VH>() {
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