package com.chad.library.adapter.base.loadState

import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.fullspan.FullSpanAdapterType

/**
 * Load state Adapter
 * 加载状态的父类，"加载更多"、"向上加载"都继承于此
 *
 */
abstract class LoadStateAdapter<VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>(),
    FullSpanAdapterType {
    /**
     * Changing this property will immediately notify the Adapter to change the item it's
     * presenting.
     * [LoadState.None] is the initial state.
     *
     * 要在适配器中显示的 LoadState。更改此属性将立即通知适配器更改 item 的样式。
     * [LoadState.None] 为初始状态。
     */
    var loadState: LoadState = LoadState.None
        set(loadState) {
            if (field != loadState) {
                val oldState = field

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

                loadStateListeners.forEach {
                    it.loadState(oldState, loadState)
                }
            }
        }

    /**
     * Is it loading.
     *
     * 是否加载中
     */
    val isLoading: Boolean
        get() {
            return loadState == LoadState.Loading
        }

    var recyclerView: RecyclerView? = null
        private set

    private val loadStateListeners = ArrayList<LoadStateListener>(0)

    final override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return onCreateViewHolder(parent, loadState)
    }

    final override fun onBindViewHolder(holder: VH, position: Int) {
        onBindViewHolder(holder, loadState)
    }

    final override fun onBindViewHolder(holder: VH, position: Int, payloads: MutableList<Any>) {
        super.onBindViewHolder(holder, position, payloads)
    }

    final override fun getItemViewType(position: Int): Int = getStateViewType(loadState)

    final override fun getItemCount(): Int = if (displayLoadStateAsItem(loadState)) 1 else 0

    @CallSuper
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        this.recyclerView = recyclerView
    }

    @CallSuper
    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        this.recyclerView = null
    }

    /**
     * Called to create a ViewHolder for the given LoadState.
     *
     * 调用此方法，为 LoadState 创建 ViewHolder。
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
     * 调用此方法，将 LoadState 状态绑定至 ViewHolder
     *
     * @param loadState LoadState to display.
     *
     * @see [getItemViewType]
     * @see [displayLoadStateAsItem]
     */
    abstract fun onBindViewHolder(holder: VH, loadState: LoadState)

    /**
     * Override this method to use different view types per LoadState.
     * 重写此方法以对每个 LoadState 使用不同的 View 类型。
     *
     * By default, this LoadStateAdapter only uses a single view type.
     */
    open fun getStateViewType(loadState: LoadState): Int = 0

    /**
     * Returns true if the LoadState should be displayed as a list item when active.
     *
     * By default, [LoadState.Loading] and [LoadState.Error] present as list items, others do not.
     *
     *
     * 如果 LoadState 在激活时需要显示item，则返回 true。
     * 默认情况下，[LoadState.Loading] 和 [LoadState.Error] 将会显示，其他则不显示。
     */
    open fun displayLoadStateAsItem(loadState: LoadState): Boolean {
        return loadState is LoadState.Loading || loadState is LoadState.Error
    }

    fun addLoadStateListener(listener: LoadStateListener) {
        loadStateListeners.add(listener)
    }

    fun removeLoadStateListener(listener: LoadStateListener) {
        loadStateListeners.remove(listener)
    }

    fun interface LoadStateListener {
        fun loadState(previousState: LoadState, currentState: LoadState)
    }
}
