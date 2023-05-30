package com.chad.library.adapter.base

/**
 * 继承后只实现 childNode , 返回的集合类型必须是可变集合。
 *
 *
 * @author Dboy233
 */
abstract class BaseNode {
    /**
     * 节点类型
     */
    abstract val nodeType: Int

    /**
     * 展开，折叠状态。外部只允许Get。其真实状态由adapter设置
     */
    var isExpand = true
        internal set

    /**
     * 父节点，外部只允许Get。其真实对象由adapter设置。
     * 虽然其类型为可空，但是adapter在进行数据编排的时候会为每一个item都设置一个parent。
     * 即便是第一个节点也都有一个[BaseTreeNodeAdapter.RootNode]。
     */
    var parentNode: BaseNode? = null
        internal set

    /**
     * 子节点列表.必须是可变集合
     */
    abstract val childNodes: MutableList<BaseNode>?

}