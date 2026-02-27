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
     * 记录打开的节点。
     *
     * Set of open nodes.
     */
    private val openedSet = HashSet<Any>()

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
     * 判断两个节点是否是同一个逻辑节点
     * 默认使用 === 比较引用
     * 如果新数据是新的对象实例，子类应该重写此方法，提供自定义的节点匹配逻辑
     * （例如根据节点 ID 或其他唯一标识符进行匹配）
     *
     * Determine if two nodes are the same logical node
     * By default uses === to compare references
     * If the new data is a new object instance, subclasses should override this method
     * to provide custom node matching logic (e.g., match by node ID or other unique identifier)
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
            super.submitList(list, commitCallback)
            return
        }

        // 处理展开状态
        // Handle expanded states
        if (clearOpenStates) {
            // 清空之前的展开状态，完全重新计算
            // Clear previous expanded states and completely recalculate
            openedSet.clear()
        } else {
            // 保留展开状态：将旧数据的展开状态递归映射到新数据
            // Preserve expanded states: recursively map old data's expanded states to new data
            val oldOpenedSet = openedSet.toSet()
            val newOpenedSet = HashSet<Any>()

            // 递归遍历新数据的所有节点，包括子节点
            // Recursively traverse all nodes in new data, including child nodes
            val allNewNodes = ArrayList<Any>()
            collectAllNodes(list, allNewNodes)

            oldOpenedSet.forEach { oldNode ->
                // 在所有新节点中查找匹配的节点（包括子节点）
                // Find matching node in all new nodes (including child nodes)
                val matchedNode = allNewNodes.find { isSameNode(it, oldNode) }
                if (matchedNode != null) {
                    // 找到匹配的节点，保留展开状态
                    // Found matching node, preserve expanded state
                    newOpenedSet.add(matchedNode)
                }
            }
            openedSet.clear()
            openedSet.addAll(newOpenedSet)
        }

        val newList = ArrayList<Any>(list)

        // 使用索引遍历，动态处理新添加的元素
        // Use index traversal to dynamically handle newly added elements
        var index = 0
        while (index < newList.size) {
            val item = newList[index]

            // 只对不在 openedSet 中的节点使用 isInitialOpen 判断
            // Only use isInitialOpen for nodes not in openedSet
            if (!openedSet.contains(item)) {
                if (isInitialOpen(index, item)) {
                    getChildNodeList(index, item)?.let { children ->
                        openedSet.add(item)

                        // 在当前位置之后插入子节点
                        // Insert child nodes after the current position
                        var insertPosition = index + 1
                        children.forEach { child ->
                            newList.add(insertPosition, child)
                            insertPosition++
                        }
                    }
                }
            } else {
                // 节点已经在 openedSet 中（用户手动展开或之前保留的），需要添加子节点
                // Node is already in openedSet (manually expanded by user or preserved), need to add child nodes
                getChildNodeList(index, item)?.let { children ->
                    // 在当前位置之后插入子节点
                    // Insert child nodes after the current position
                    var insertPosition = index + 1
                    children.forEach { child ->
                        newList.add(insertPosition, child)
                        insertPosition++
                    }
                }
            }

            // 继续遍历，包括新添加的子节点
            // Continue traversing, including newly added child nodes
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

            // 递归收集子节点
            // Recursively collect child nodes
            val children = getChildNodeList(result.size - 1, node)
            if (!children.isNullOrEmpty()) {
                collectAllNodes(children, result)
            }
        }
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
            val childList = getChildNodeList(items.indexOfFirst { it === node }, node)

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
     * Remove open flags for all nodes in the list (recursive)
     * Note: This method should be called before removing the node list from items
     *
     * @param list 节点列表 / The node list
     */
    private fun removeListOpenFlag(list: List<Any>) {
        list.forEach { c ->
            if (isOpened(c)) {
                openedSet.remove(c)

                val cList = getChildNodeList(items.indexOfFirst { it === c }, c)
                if (!cList.isNullOrEmpty()) {
                    removeListOpenFlag(cList)
                }
            }
        }
    }

    /**
     * 打开节点。
     *
     * Open node.
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
        if (child != null) {
            // 使用 HashSet 的 contains 方法，性能 O(1)
            if (!openedSet.contains(item)) {
                openedSet.add(item)
                notifyItemChanged(position, positionPayload)
                addAll(position + 1, child)
                return true
            }
        }

        return false
    }

    /**
     * 关闭节点。
     *
     * Close node.
     *
     * @param position 节点位置 / Node position
     * @param positionPayload 用于刷新的 payload / Payload for refresh
     * @return 是否成功 / Is success
     */
    @JvmOverloads
    fun close(position: Int, positionPayload: Any? = null): Boolean {
        if (position < 0) return false
        val item = items.getOrNull(position) ?: return false

        val childList = getChildNodeList(position, item)
        if (!childList.isNullOrEmpty()) {
            // 先移除打开标记
            openedSet.remove(item)

            val last = childList.last()
            val index = if (isOpened(last)) {
                // 最后一个节点是打开的，需要递归找到最后的子节点
                // The last node is opened, need to recursively find the last child
                val node = findLastChild(last)
                items.indexOfFirst { it === node }
            } else {
                // 最后一个节点是关闭的
                // The last node is closed
                items.indexOfFirst { it === last }
            }

            if (index > -1 && index > position) {
                // 在移除前，先清除所有子节点的打开标记
                // Before removing, clear open flags for all child nodes
                removeListOpenFlag(childList)
                notifyItemChanged(position, positionPayload)
                removeAtRange(IntRange(position + 1, index))
                return true
            }
        }

        return false
    }

    /**
     * 关闭全部的节点
     *
     * Close all.
     */
    fun closeAll() {
        // 创建副本遍历，避免并发修改异常
        // Create a copy for iteration to avoid concurrent modification exception
        openedSet.toList().forEach { opened ->
            val position = items.indexOfFirst { it === opened }
            if (position >= 0) {
                close(position)
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
