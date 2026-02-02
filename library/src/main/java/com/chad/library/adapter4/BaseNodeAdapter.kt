package com.chad.library.adapter4

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
abstract class BaseNodeAdapter : BaseQuickAdapter<Any, RecyclerView.ViewHolder>() {

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
     * When submitList, the nodes that need to be opened by default.
     *
     * @param position
     * @param parent
     * @return
     */
    abstract fun isInitialOpen(position: Int, parent: Any): Boolean

    override fun submitList(list: List<Any>?, commitCallback: Runnable?) {
        if (!list.isNullOrEmpty()) {

            val newList = ArrayList<Any>(list)

            var i = 0
            val iterator = newList.listIterator()
            while (iterator.hasNext()) {
                val item = iterator.next()
                // 根据条件添加新元素
                if (isInitialOpen(i, item)) {
                    getChildNodeList(i, item)?.let {
                        openedSet.add(item)
                        it.forEach { child ->
                            iterator.add(child)
                        }
                    }
                }
                i++
            }

            super.submitList(newList, commitCallback)
            return
        }

        super.submitList(list, commitCallback)
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
        // 修复：不需要手动 clear，close() 方法内部已经会移除标记
        // Fix: No need to manually clear, close() method already removes the flag
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
