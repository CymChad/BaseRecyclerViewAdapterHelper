package com.chad.library.adapter.base.entity.node

import java.util.*

abstract class BaseNode {
    /**
     * 重写此方法，获取子节点。如果没有子节点，返回 null 或者 空数组
     */
    abstract val childNode: MutableList<BaseNode>?

    /**
     * 获取node节点的唯一tag。
     * 可重写，默认实现使用UUID
     * @return String
     */
    open fun nodeTag(): String = UUID.randomUUID().toString()

}