package com.chad.library.adapter.base

import androidx.recyclerview.widget.*
import androidx.recyclerview.widget.AsyncListDiffer.ListListener


abstract class BaseDifferAdapter<T, VH : RecyclerView.ViewHolder> (
    config: AsyncDifferConfig<T>, items: List<T>
) : BaseQuickAdapter<T, VH>() {

    constructor(diffCallback: DiffUtil.ItemCallback<T>) : this(diffCallback, emptyList())

    constructor(diffCallback: DiffUtil.ItemCallback<T>, items: List<T>) : this(
        AsyncDifferConfig.Builder(diffCallback).build(), items
    )

    constructor(config: AsyncDifferConfig<T>) : this(config, emptyList())


    private val mDiffer: AsyncListDiffer<T> = AsyncListDiffer(AdapterListUpdateCallback(this), config)


    private val mListener: ListListener<T> = ListListener<T> { previousList, currentList ->
        this.onCurrentListChanged(previousList, currentList)
    }

    init {
        mDiffer.addListListener(mListener)
        mDiffer.submitList(items)
    }

    final override var items: List<T>
        get() = mDiffer.currentList
        set(value) {
            mDiffer.submitList(value)
        }


    open fun onCurrentListChanged(previousList: List<T>, currentList: List<T>) {}

    override fun submitList(list: List<T>?) {
        submitList(list, null)
    }

    fun submitList(list: List<T>?, commitCallback: Runnable?) {
//        mDiffer.submitList(list, runnable)

        val newList = list ?: emptyList()

        val oldDisplayEmptyLayout = displayEmptyView()
        val newDisplayEmptyLayout = displayEmptyView(newList)

        if (oldDisplayEmptyLayout && !newDisplayEmptyLayout) {
            notifyItemRemoved(0)
//            mDiffer.submitList(list, commitCallback)
        } else if (newDisplayEmptyLayout && !oldDisplayEmptyLayout) {
//            notifyItemRangeRemoved(0, items.size)
//            this.items = newList
//            notifyItemInserted(0)

            mDiffer.submitList(list) {
                notifyItemInserted(0)
                commitCallback?.run()
            }

        } else if (oldDisplayEmptyLayout && newDisplayEmptyLayout) {
            notifyItemChanged(0, EMPTY_PAYLOAD)
            mDiffer.submitList(list, commitCallback)
        } else {
            mDiffer.submitList(list, commitCallback)
        }
    }
}