package com.chad.library.adapter.base

import androidx.recyclerview.widget.*
import androidx.recyclerview.widget.AsyncListDiffer.ListListener
import java.util.*

/**
 * Base differ adapter
 *
 * 使用 Differ 的父类。异步执行 Diff 计算，不会有性能问题。
 *
 * @param T 数据类型
 * @param VH ViewHolder 类型
 */
abstract class BaseDifferAdapter<T, VH : RecyclerView.ViewHolder>(
    config: AsyncDifferConfig<T>, items: List<T>
) : BaseQuickAdapter<T, VH>() {

    constructor(diffCallback: DiffUtil.ItemCallback<T>) : this(diffCallback, emptyList())

    constructor(diffCallback: DiffUtil.ItemCallback<T>, items: List<T>) : this(
        AsyncDifferConfig.Builder(diffCallback).build(), items
    )

    constructor(config: AsyncDifferConfig<T>) : this(config, emptyList())


    private val mDiffer: AsyncListDiffer<T> =
        AsyncListDiffer(AdapterListUpdateCallback(this), config)


    private val mListener: ListListener<T> = ListListener<T> { previousList, currentList ->

        val oldDisplayEmptyLayout = displayEmptyView(previousList)
        val newDisplayEmptyLayout = displayEmptyView(currentList)

        if (oldDisplayEmptyLayout && !newDisplayEmptyLayout) {
            notifyItemRemoved(0)
            recyclerView.scrollToPosition(0)
        } else if (newDisplayEmptyLayout && !oldDisplayEmptyLayout) {
            notifyItemInserted(0)
        } else if (oldDisplayEmptyLayout && newDisplayEmptyLayout) {
            notifyItemChanged(0, EMPTY_PAYLOAD)
        }

        this.onCurrentListChanged(previousList, currentList)
    }

    init {
        mDiffer.addListListener(mListener)
        mDiffer.submitList(items)
    }

    /**
     * item 数据集
     */
    final override var items: List<T>
        get() = mDiffer.currentList
        set(value) {
            mDiffer.submitList(value, null)
        }

    /**
     * (Optional) Override this method to monitor changes in the dataset before and after..
     * （可选）重写此方法，监听前后数据集变化
     *
     * @param previousList 原数据集
     * @param currentList 当前数据集
     */
    open fun onCurrentListChanged(previousList: List<T>, currentList: List<T>) {}

    /**
     * Submit List
     * 提交数据集
     *
     * @param list
     */
    override fun submitList(list: List<T>?) {
        mDiffer.submitList(list, null)
    }

    /**
     * Submit list
     * 提交数据集
     *
     * @param list
     * @param commitCallback 数据异步提交完成以后的回掉
     */
    fun submitList(list: List<T>?, commitCallback: Runnable?) {
        mDiffer.submitList(list, commitCallback)
    }

    override operator fun set(position: Int, data: T) {
        items.toMutableList().also {
            it[position] = data
            submitList(it)
        }
    }

    override fun add(data: T) {
        items.toMutableList().also {
            it.add(data)
            submitList(it)
        }
    }

    override fun add(position: Int, data: T) {
        if (position > items.size || position < 0) {
            throw IndexOutOfBoundsException("position: ${position}. size:${items.size}")
        }

        items.toMutableList().also {
            it.add(position, data)
            submitList(it)
        }
    }

    override fun addAll(collection: Collection<T>) {
        items.toMutableList().also {
            it.addAll(collection)
            submitList(it)
        }
    }

    override fun addAll(position: Int, collection: Collection<T>) {
        if (position > items.size || position < 0) {
            throw IndexOutOfBoundsException("position: ${position}. size:${items.size}")
        }

        items.toMutableList().also {
            it.addAll(position, collection)
            submitList(it)
        }
    }

    override fun removeAt(position: Int) {
        if (position >= items.size) {
            throw IndexOutOfBoundsException("position: ${position}. size:${items.size}")
        }

        items.toMutableList().also {
            it.removeAt(position)
            submitList(it)
        }
    }

    override fun remove(data: T) {
        items.toMutableList().also {
            it.remove(data)
            submitList(it)
        }
    }

    override fun swap(fromPosition: Int, toPosition: Int) {
        if (fromPosition in items.indices || toPosition in items.indices) {
            items.toMutableList().also {
                Collections.swap(it, fromPosition, toPosition)
                submitList(it)
            }
        }
    }

    override fun move(fromPosition: Int, toPosition: Int) {
        if (fromPosition in items.indices || toPosition in items.indices) {
            items.toMutableList().also {
                val e = it.removeAt(fromPosition)
                it.add(toPosition, e)
                submitList(it)
            }
        }
    }
}