package com.chad.library.adapter.base.entity


/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
interface SectionEntity : MultiItemEntity {

    val isHeader: Boolean

    override val itemType: Int
        get() = if (isHeader) HEADER_TYPE else NORMAL_TYPE

    companion object {
        const val NORMAL_TYPE = -100
        const val HEADER_TYPE = -99
    }
}
