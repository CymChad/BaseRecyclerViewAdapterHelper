package com.chad.library.adapter.base.entity

interface NSectionEntity {
    val subItem: MutableList<NSectionEntity>?

    //    val sectionHeaderLayoutId: Int
//        @LayoutRes
//        get
//
//    val sectionFooterLayoutId: Int
//        @LayoutRes
//        get
    val headerEntity: NSectionEntity?
    val footerEntity: NSectionEntity?

//    val isHaveSubItem: Boolean


//    val isHaveSectionHeader: Boolean
//    val isHaveSectionFooter: Boolean

}