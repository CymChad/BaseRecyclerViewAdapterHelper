package com.chad.library.adapter4

import androidx.recyclerview.widget.RecyclerView

/**
 * @author LiMuYang
 * @date 2025/9/2
 * @description
 *
 * 树节点类型列表适配器。
 *
 *
 */
abstract class BaseNodeAdapter<T : Any> : BaseQuickAdapter<Any, RecyclerView.ViewHolder>() {

    /**
     * 记录打开的节点。
     *
     * Set of open nodes.
     */
    private val openSet = HashSet<Any>()

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
            val iterator = newList.iterator()
            while (iterator.hasNext()) {
                val item = iterator.next()
                // 根据条件添加新元素
                if (isInitialOpen(i, item)) {
                    newList.add(i, item)
                }
                i++
            }

            super.submitList(newList, commitCallback)
            return
        }

        super.submitList(list, commitCallback)
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
    fun open(position: Int) : Boolean{
        val item = items.getOrNull(position) ?: return false

        val child = getChildNodeList(position, item)
        if (child != null) {
            openSet.add(item)
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
     */
    fun close(position: Int) {
        val item = items.getOrNull(position) ?: return

        val childList = getChildNodeList(position, item)
        if (childList != null) {
            openSet.remove(item)

            var count = 0
            childList.forEach { c ->
                if (openSet.contains(c)) {
                    val i = items.indexOfFirst {
                        it == c
                    }
                    if (i > -1) {
                        count += getChildNodeList(i, c)?.size ?: 0
                    }
                    openSet.remove(c)
                }
            }

            removeAtRange(IntRange(position + 1, childList.size + position + count))
        }
    }

    /**
     * 打开或关闭节点。
     *
     * Open or close.
     *
     * @param position
     */
    fun openOrClose(position: Int) {
        val item = items.getOrNull(position) ?: return

        if (openSet.contains(item)) {
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
    fun isOpened(item: Any): Boolean {
        return openSet.contains(item)
    }
}