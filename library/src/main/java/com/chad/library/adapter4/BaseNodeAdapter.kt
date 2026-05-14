package com.chad.library.adapter4

import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

/**
 * @author LiMuYang
 * @date 2025/9/2
 * @description
 *
 * 树节点类型列表适配器。
 *
 * Tree node type list adapter.
 */
abstract class BaseNodeAdapter(config: AsyncDifferConfig<Any>?) : BaseQuickAdapter<Any, RecyclerView.ViewHolder>(config = config) {

    constructor(diffCallback: DiffUtil.ItemCallback<Any>) : this(
        AsyncDifferConfig.Builder(diffCallback).build()
    )

    constructor() : this(null)

    /**
     * 以 isSameNode() 为匹配语义的节点集合，
     * 避免 data class 的 equals/hashCode 导致不同逻辑节点被合并。
     */
    private inner class NodeSet {
        private val nodes = mutableListOf<Any>()

        fun contains(node: Any): Boolean = nodes.any { isSameNode(it, node) }

        fun add(node: Any) {
            nodes.removeAll { isSameNode(it, node) }
            nodes.add(node)
        }

        fun remove(node: Any) {
            nodes.removeAll { isSameNode(it, node) }
        }

        fun clear() = nodes.clear()

        fun toList(): List<Any> = nodes.toList()

        fun replaceAll(source: NodeSet) {
            nodes.clear()
            nodes.addAll(source.nodes)
        }

        fun migrateTo(allNewNodes: List<Any>, target: NodeSet) {
            nodes.forEach { oldNode ->
                allNewNodes.find { isSameNode(it, oldNode) }?.let { target.add(it) }
            }
        }
    }

    /**
     * 记录打开的节点。
     */
    private val openedSet = NodeSet()

    /**
     * 记录用户通过单点 close() 明确关闭的节点，
     * 阻止 submitList 时 isInitialOpen() 再次展开。
     * closeAll() 不写入此集合。
     */
    private val closedSet = NodeSet()

    /**
     * 使用 isSameNode() 在 items 中查找节点位置。
     */
    private fun indexOfNode(node: Any): Int {
        return items.indexOfFirst { isSameNode(it, node) }
    }

    /**
     * 获取子节点列表。
     *
     * Get child node list.
     *
     * @param position
     *
     * @return
     * 子节点列表，如果没有，请返回null。
     *
     * List of child nodes, if not, return null.
     */
    abstract fun getChildNodeList(position: Int, parent: Any): List<Any>?

    /**
     * 设置数据时，默认需要打开的节点。
     *
     * 注意：此方法会被调用多次，包括对原始列表和递归展开的子节点。
     * position 参数表示在正在构建的列表中的位置，而非原始输入列表的位置。
     * 如果你只需要判断原始根节点，请同时检查 item 是否在原始列表中。
     *
     * When submitList, the nodes that need to be opened by default.
     *
     * Note: This method will be called multiple times, including for the original list
     * and recursively expanded child nodes. The position parameter represents the position
     * in the list being constructed, not the original input list. If you only want to
     * check the original root nodes, also check if the item is in the original list.
     *
     * @param position 在当前正在构建的列表中的位置 / Position in the list being constructed
     * @param item 节点对象 / Node object
     * @return 是否需要初始展开 / Whether to initially expand
     */
    abstract fun isInitialOpen(position: Int, item: Any): Boolean

    /**
     * 判断两个节点是否是同一个逻辑节点。
     *
     * 注意：此方法必须能够唯一标识一个节点，即同一棵树中不应有两个不同的节点
     * 被此方法判定为相同，否则它们会共享展开/关闭状态，导致行为异常。
     * 例如，不应仅用 title 匹配，而应使用能区分不同分支节点的唯一标识符（如绝对路径或全局 ID）。
     *
     * 默认使用 === 比较引用。
     * 如果新数据是新的对象实例，子类应该重写此方法，提供自定义的节点匹配逻辑。
     *
     * Determine if two nodes are the same logical node.
     *
     * Note: This method must uniquely identify a node within the tree.
     * Two different nodes must never be considered equal, otherwise they will share
     * expanded/collapsed state and cause unexpected behavior.
     * Do not match by title alone — use a unique identifier (e.g. absolute path or global ID).
     *
     * By default uses === to compare references.
     * If the new data is a new object instance, subclasses should override this method
     * to provide custom node matching logic.
     *
     * @param item1 节点1 / Node 1
     * @param item2 节点2 / Node 2
     * @return 是否是同一个节点 / Whether they are the same node
     */
    protected open fun isSameNode(item1: Any, item2: Any): Boolean {
        return item1 === item2
    }

    override fun submitList(list: List<Any>?, commitCallback: Runnable?) {
        this.submitList(list, false, commitCallback)
    }

    /**
     * 提交列表数据
     *
     * @param list 数据列表 / Data list
     * @param commitCallback 回调 / Callback
     * @param clearOpenStates 是否清空之前的展开状态。
     * false，保留用户的展开状态。
     * 如果设置为 true，会重新根据 isInitialOpen 判断展开状态。
     *
     * Submit list data
     *
     * @param clearOpenStates Whether to clear previous expanded states.
     * false, preserving user expanded states.
     * If set to true, will re-determine expanded states based on isInitialOpen.
     */
    fun submitList(
        list: List<Any>?,
        clearOpenStates: Boolean,
        commitCallback: Runnable? = null,
    ) {
        if (list.isNullOrEmpty()) {
            openedSet.clear()
            closedSet.clear()
            super.submitList(list, commitCallback)
            return
        }

        if (clearOpenStates) {
            openedSet.clear()
            closedSet.clear()
        } else {
            // 递归遍历新数据的所有节点（包括子节点）
            val allNewNodes = mutableListOf<Any>()
            collectAllNodes(list, allNewNodes)

            val newOpened = NodeSet()
            val newClosed = NodeSet()
            openedSet.migrateTo(allNewNodes, newOpened)
            closedSet.migrateTo(allNewNodes, newClosed)

            openedSet.replaceAll(newOpened)
            closedSet.replaceAll(newClosed)
        }

        val newList = ArrayList<Any>(list)

        var index = 0
        while (index < newList.size) {
            val item = newList[index]

            if (openedSet.contains(item)) {
                appendChildren(newList, index, item)
            } else if (!closedSet.contains(item) && isInitialOpen(index, item)) {
                if (appendChildren(newList, index, item)) {
                    openedSet.add(item)
                }
            }

            index++
        }

        super.submitList(newList, commitCallback)
    }

    /**
     * 递归收集树中的所有节点（包括所有层级的子节点）
     *
     * Recursively collect all nodes in the tree (including child nodes at all levels)
     *
     * @param nodes 节点列表 / Node list
     * @param result 结果集合 / Result collection
     */
    private fun collectAllNodes(nodes: List<Any>, result: MutableList<Any>) {
        nodes.forEach { node ->
            result.add(node)

            val children = getChildNodeList(result.size - 1, node)
            if (!children.isNullOrEmpty()) {
                collectAllNodes(children, result)
            }
        }
    }

    /**
     * 将 item 的非空 children 插入到 list 的 position + 1 位置。
     * @return 是否有子节点被插入
     */
    private fun appendChildren(
        list: MutableList<Any>,
        position: Int,
        item: Any,
    ): Boolean {
        val children = getChildNodeList(position, item) ?: return false
        if (children.isEmpty()) return false
        var insertPos = position + 1
        children.forEach { child ->
            list.add(insertPos, child)
            insertPos++
        }
        return true
    }

    /**
     * 查找最后一个可见子节点（递归）
     *
     * Find the last visible child node (recursive)
     *
     * @param node 要查找的节点 / The node to find
     * @return 最后一个子节点 / The last child node
     */
    private fun findLastChild(node: Any): Any {
        if (isOpened(node)) {
            val childList = getChildNodeList(indexOfNode(node), node)

            return if (childList.isNullOrEmpty()) {
                node
            } else {
                findLastChild(childList.last())
            }
        } else {
            return node
        }
    }

    /**
     * 移除列表中所有节点的打开标记（递归）
     * 注意：此方法需要在从 items 中移除节点列表之前调用
     *
     * @param list 节点列表 / The node list
     */
    private fun removeListOpenFlag(list: List<Any>) {
        list.forEach { c ->
            if (openedSet.contains(c)) {
                openedSet.remove(c)

                val cList = getChildNodeList(indexOfNode(c), c)
                if (!cList.isNullOrEmpty()) {
                    removeListOpenFlag(cList)
                }
            }
        }
    }

    /**
     * 打开节点。
     *
     * @param position 节点位置 / Node position
     * @param positionPayload 用于刷新的 payload / Payload for refresh
     * @return 是否成功 / Is success
     */
    @JvmOverloads
    fun open(position: Int, positionPayload: Any? = null): Boolean {
        if (position < 0) return false
        val item = items.getOrNull(position) ?: return false

        val child = getChildNodeList(position, item)
        if (child.isNullOrEmpty()) return false
        if (openedSet.contains(item)) return false

        openedSet.add(item)
        closedSet.remove(item)
        notifyItemChanged(position, positionPayload)
        addAll(position + 1, collectVisibleChildren(position, child))
        return true
    }

    /**
     * 收集节点展开后应可见的所有子孙节点。
     * 对已在 openedSet 中的子节点，递归加入其子树。
     * 不触发 isInitialOpen()，避免点击展开引入默认展开副作用。
     *
     * @param parentPosition 父节点在 items 中的位置
     * @param children 直接子节点列表
     * @return 应插入的可见节点列表
     */
    private fun collectVisibleChildren(
        parentPosition: Int,
        children: List<Any>,
    ): List<Any> {
        val result = mutableListOf<Any>()
        children.forEach { child ->
            result.add(child)
            if (openedSet.contains(child)) {
                // childPosition = child 在插入完成后在 items 中的预期位置
                val childPosition = parentPosition + result.size
                val grandChildren = getChildNodeList(childPosition, child)
                if (!grandChildren.isNullOrEmpty()) {
                    result.addAll(collectVisibleChildren(childPosition, grandChildren))
                }
            }
        }
        return result
    }

    /**
     * 关闭节点。
     *
     * @param position 节点位置 / Node position
     * @param positionPayload 用于刷新的 payload / Payload for refresh
     * @return 是否成功 / Is success
     */
    @JvmOverloads
    fun close(position: Int, positionPayload: Any? = null): Boolean {
        return closeInternal(position, positionPayload, recordClosed = true)
    }

    private fun closeInternal(
        position: Int,
        positionPayload: Any?,
        recordClosed: Boolean,
    ): Boolean {
        if (position < 0) return false
        val item = items.getOrNull(position) ?: return false

        val childList = getChildNodeList(position, item)
        if (childList.isNullOrEmpty()) return false

        if (!openedSet.contains(item)) return false

        val last = childList.last()
        val index = if (openedSet.contains(last)) {
            indexOfNode(findLastChild(last))
        } else {
            indexOfNode(last)
        }

        if (index > -1 && index > position) {
            openedSet.remove(item)
            if (recordClosed) {
                closedSet.add(item)
            }
            removeListOpenFlag(childList)
            notifyItemChanged(position, positionPayload)
            removeAtRange(IntRange(position + 1, index))
            return true
        }

        return false
    }

    /**
     * 关闭全部的节点。
     * 不写入 closedSet，下次 submitList 时 isInitialOpen() 仍可生效。
     */
    fun closeAll() {
        openedSet.toList().forEach { opened ->
            val position = indexOfNode(opened)
            if (position >= 0) {
                closeInternal(position, null, recordClosed = false)
            }
        }
    }

    /**
     * 打开或关闭节点。
     *
     * Open or close.
     *
     * @param position adapter 的数据位置 / The data location of the adapter
     * @return 是否成功 / Is success
     */
    fun openOrClose(position: Int): Boolean {
        val item = items.getOrNull(position) ?: return false

        return if (isOpened(item)) {
            close(position)
        } else {
            open(position)
        }
    }

    /**
     * 是否打开。
     *
     * Is opened
     *
     * @param item 节点对象 / The node object
     * @return 是否打开 / Whether opened
     */
    fun isOpened(item: Any?): Boolean {
        if (item == null) return false
        return openedSet.contains(item)
    }

    /**
     * 指定位置的节点是否打开。
     *
     * Whether the node at the specified position is opened
     *
     * @param position 位置 / Position
     * @return 是否打开 / Whether opened
     */
    fun isOpenedAt(position: Int): Boolean {
        return isOpened(items.getOrNull(position))
    }
}
