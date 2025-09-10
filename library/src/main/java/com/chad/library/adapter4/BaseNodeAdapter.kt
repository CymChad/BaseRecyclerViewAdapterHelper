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
    private val openedArray = ArrayList<Any>()

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
                        openedArray.add(item)
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

    private fun removeListOpenFlag(list: List<Any>) {
        list.forEach { c ->
            if (isOpened(c)) {
                val index = items.indexOfFirst { it === c }
                if (index > -1) {
                    openedArrayRemove(c)

                    val cList = getChildNodeList(index, c)
                    if (!cList.isNullOrEmpty()) {
                        removeListOpenFlag(cList)
                    }
                }
            }
        }
    }

    private fun openedArrayRemove(item: Any) {
        val index = openedArray.indexOfFirst { it === item }
        if (index > -1) {
            openedArray.removeAt(index)
        }
    }

    /**
     * 打开节点。
     *
     * Open node.
     *
     * @param position
     * @return
     * Is success.
     *
     * 是否成功。
     */
    fun open(position: Int): Boolean {
        val item = items.getOrNull(position) ?: return false

        val child = getChildNodeList(position, item)
        if (child != null) {
            if (openedArray.firstOrNull { it === item } == null) {
                openedArray.add(item)
            }

            addAll(position + 1, child)
            return true
        }

        return false
    }

    /**
     * 关闭节点。
     *
     * Close node.
     *
     * @param position
     * @return
     * Is success.
     *
     * 是否成功。
     */
    @JvmOverloads
    fun close(position: Int, positionPayload: Any? = null): Boolean {
        if (position < 0) return false
        val item = items.getOrNull(position) ?: return false

        val childList = getChildNodeList(position, item)
        if (!childList.isNullOrEmpty()) {
            openedArrayRemove(item)


            val last = childList.last()
            if (isOpened(last)) {
                // 最后一个节点是打开的
                val node = findLastChild(last)
                val index = items.indexOfFirst { it === node }
                if (index > -1 && index > position) {
                    removeListOpenFlag(childList)
                    notifyItemChanged(position, positionPayload)
                    removeAtRange(IntRange(position + 1, index))
                    return true
                }
            } else {
                // 最后一个节点是关闭的
                val index = items.indexOfFirst { it === last }
                if (index > -1 && index > position) {
                    removeListOpenFlag(childList)
                    notifyItemChanged(position, positionPayload)
                    removeAtRange(IntRange(position + 1, index))
                    return true
                }
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
        openedArray.toMutableList().forEach { opened ->
            close(items.indexOfFirst { it === opened })
        }
        openedArray.clear()
    }

    /**
     * 打开或关闭节点。
     *
     * Open or close.
     *
     * @param position The data location of the adapter.
     *                 adapter 的数据位置.
     * @return
     * Is success.
     *
     * 是否成功。
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
     * @return
     */
    fun isOpened(item: Any?): Boolean {
        if (item == null) return false
        return openedArray.firstOrNull { it === item } != null
    }

    fun isOpenedAt(position: Int): Boolean {
        return isOpened(items.getOrNull(position))
    }
}