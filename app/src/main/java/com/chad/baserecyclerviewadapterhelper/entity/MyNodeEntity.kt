package com.chad.baserecyclerviewadapterhelper.entity

import com.chad.baserecyclerviewadapterhelper.activity.treenode.adapter.TreeNodeAdapter
import com.chad.library.adapter.base.BaseNode
import java.util.*

/**
 * 可以使用同一个数据类，也可以分开使用例如[FileNodeEntity]和[FolderNodeEntity]。分开会比较麻烦。
 *
 * 可以将子节点数据和父节点数据合并为一个节点
 *
 * @author Dboy233
 */
class MyNodeEntity(
    val name: String? = null,
    //一定要使用可变集合
    val child: MutableList<MyNodeEntity>? = null,
    val time: Date = Date()
) :
    BaseNode() {


    override val nodeType: Int
        get() = if (child == null) {
            TreeNodeAdapter.TYPE_FILE
        } else {
            TreeNodeAdapter.TYPE_FOLDER
        }

    override val childNodes: MutableList<BaseNode>?
        get() = if (child == null) null else child as MutableList<BaseNode>


}