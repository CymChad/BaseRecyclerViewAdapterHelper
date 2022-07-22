package com.chad.library.adapter.base

import androidx.recyclerview.widget.*
import androidx.recyclerview.widget.AsyncListDiffer.ListListener


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

    final override var items: MutableList<T>
        get() = mDiffer.currentList
        set(value) {
            mDiffer.submitList(value)
        }

    open fun onCurrentListChanged(previousList: List<T>, currentList: List<T>) {}

    override fun submitList(list: List<T>?) {
        submitList(list, null)
    }

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
        items.toMutableList().also {
            it.add(position, data)
            submitList(it)
        }
    }

    override fun addAll(newCollection: Collection<T>) {
        items.toMutableList().also {
            it.addAll(newCollection)
            submitList(it)
        }
    }

    override fun addAll(position: Int, newCollection: Collection<T>) {
        items.toMutableList().also {
            it.addAll(position, newCollection)
            submitList(it)
        }
    }

    override fun removeAt(position: Int) {
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
}