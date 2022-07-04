package com.chad.library.adapter.base.loadState

import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class LoadStateAdapter<VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {
    /**
     * LoadState to present in the adapter.
     *
     * null 为初始状态
     * Changing this property will immediately notify the Adapter to change the item it's
     * presenting.
     */
    var loadState: LoadState = LoadState.NotLoading(endOfPaginationReached = false)
        set(loadState) {
            if (field != loadState) {
                val oldItem = displayLoadStateAsItem(field)
                val newItem = displayLoadStateAsItem(loadState)

                if (oldItem && !newItem) {
                    notifyItemRemoved(0)
                } else if (newItem && !oldItem) {
                    notifyItemInserted(0)
                } else if (oldItem && newItem) {
                    notifyItemChanged(0)
                }
                field = loadState
            }
        }

    /**
     * 是否加载中
     */
    val isLoading: Boolean
        get() {
            return loadState == LoadState.Loading
        }

    var recyclerView: RecyclerView? = null
        private set

    final override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return onCreateViewHolder(parent, loadState)
    }

    final override fun onBindViewHolder(holder: VH, position: Int) {
        onBindViewHolder(holder, loadState)
    }

    final override fun getItemViewType(position: Int): Int = getStateViewType(loadState)

    final override fun getItemCount(): Int = if (displayLoadStateAsItem(loadState)) 1 else 0


    @CallSuper
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        this.recyclerView = recyclerView

        val manager = recyclerView.layoutManager
        if (manager is GridLayoutManager) {
            val originalSpanSizeLookup = manager.spanSizeLookup
            val spanCount = manager.spanCount

            manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {

                    val vh = recyclerView.findViewHolderForAdapterPosition(position)
                    val isLoadStateAdapter = vh?.bindingAdapter is LoadStateAdapter

                    if (isLoadStateAdapter) {
                        return spanCount
                    }

                    return originalSpanSizeLookup.getSpanSize(position)
                }

            }
        }
    }

    @CallSuper
    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        this.recyclerView = null
    }

    /**
     * Called to create a ViewHolder for the given LoadState.
     *
     * @param parent The ViewGroup into which the new View will be added after it is bound to
     *               an adapter position.
     * @param loadState The LoadState to be initially presented by the new ViewHolder.
     *
     * @see [getItemViewType]
     * @see [displayLoadStateAsItem]
     */
    abstract fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): VH

    /**
     * Called to bind the passed LoadState to the ViewHolder.
     *
     * @param loadState LoadState to display.
     *
     * @see [getItemViewType]
     * @see [displayLoadStateAsItem]
     */
    abstract fun onBindViewHolder(holder: VH, loadState: LoadState)

    /**
     * Override this method to use different view types per LoadState.
     *
     * By default, this LoadStateAdapter only uses a single view type.
     */
    open fun getStateViewType(loadState: LoadState): Int = 0

    /**
     * Returns true if the LoadState should be displayed as a list item when active.
     *
     * By default, [LoadState.Loading] and [LoadState.Error] present as list items, others do not.
     */
    open fun displayLoadStateAsItem(loadState: LoadState): Boolean {
        return loadState is LoadState.Loading || loadState is LoadState.Error || (loadState is LoadState.NotLoading && !loadState.endOfPaginationReached)
    }

}
