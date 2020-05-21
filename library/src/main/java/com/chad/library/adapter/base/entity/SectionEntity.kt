package com.chad.library.adapter.base.entity


/**
 * 带头部布局的实体类接口
 * 实体类请继承此接口；如果使用java，请使用[JSectionEntity]抽象类
 */
interface SectionEntity : MultiItemEntity {

    val isHeader: Boolean

    /**
     * 用于返回item类型，除了头布局外，默认只有[NORMAL_TYPE]一种布局
     * 如果需要实现 item 多布局，请重写此方法，返回自己的type
     */
    override val itemType: Int
        get() = if (isHeader) HEADER_TYPE else NORMAL_TYPE

    companion object {
        const val NORMAL_TYPE = -100
        const val HEADER_TYPE = -99
    }
}
