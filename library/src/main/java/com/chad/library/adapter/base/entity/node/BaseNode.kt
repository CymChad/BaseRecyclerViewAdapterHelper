package com.chad.library.adapter.base.entity.node

abstract class BaseNode {

    /**
     * 重写此方法，获取子节点。如果没有子节点，返回 null 或者 空数组
     *
     * 如果返回 null，则无法对子节点的数据进行新增和删除等操作
     */
    abstract val childNode: MutableList<BaseNode>?

}