package com.chad.library.adapter.base.entity.node

/**
 * 跟随内容收起展开的页脚
 * 使用 ExpandableNodeFooterImpl 时需要
 */
interface ExpandableNodeFooterImpl : NodeFooterImp {

    /**
     * 是否跟随子节点变换
     */
    val isExpandable: Boolean

    /**
     * footerNode 需要返回固定的 item
     * 不能像demo一样每次return new RootFooterNode("");
     * 否则收起时会删除失败，可以通过 by lazy 等固定 RootNode 中的返回值
     */
    override val footerNode: BaseNode?
}