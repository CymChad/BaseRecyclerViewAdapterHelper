package com.chad.baserecyclerviewadapterhelper.entity

import com.chad.library.adapter.base.entity.SectionEntity

/**
 * @author: limuyang
 * @date: 2019-12-06
 * @Description:
 */
data class HomeEntity(
        val name: String = "",
        val activity: Class<*>? = null,
        val imageResource: Int = 0,
        val headerTitle: String = ""
) : SectionEntity {
    override val isHeader: Boolean
        get() = headerTitle.isNotBlank()
}