package com.chad.library.adapter.base

import android.content.Context
import android.util.SparseArray
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.R
import java.lang.ref.WeakReference

/**
 * MultiItemType layout.
 * 多类型布局
 *
 */
abstract class BaseMultiItemAdapter<T>(items: List<T> = emptyList()) :
    BaseQuickAdapter<T, RecyclerView.ViewHolder>(items) {

    private val typeViewHolders =
        SparseArray<OnMultiItemAdapterListener<T, RecyclerView.ViewHolder>>(1)

    private var onItemViewTypeListener: OnItemViewTypeListener<T>? = null

    override fun onCreateViewHolder(
        context: Context, parent: ViewGroup, viewType: Int
    ): RecyclerView.ViewHolder {
        val listener = typeViewHolders.get(viewType)
            ?: throw IllegalArgumentException("ViewType: $viewType not found onViewHolderListener，please use addItemType() first!")

        return listener.onCreate(parent.context, parent, viewType).apply {
            itemView.setTag(R.id.BaseQuickAdapter_key_multi, listener)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, item: T) {
        findListener(holder)?.onBind(holder, position, item)
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder, position: Int, item: T, payloads: List<Any>
    ) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position, item)
            return
        }

        findListener(holder)?.onBind(holder, position, item, payloads)
    }

    /**
     * Call this function to add multiTypeItems.
     * 调用此方法，设置多布局
     * @param itemViewType Int
     * @param listener Int
     */
    fun <V : RecyclerView.ViewHolder> addItemType(
        itemViewType: Int, listener: OnMultiItemAdapterListener<T, V>
    ) = apply {
        if (listener is OnMultiItem) {
            listener.weakA = WeakReference(this)
        }
        typeViewHolders.put(
            itemViewType, listener as OnMultiItemAdapterListener<T, RecyclerView.ViewHolder>
        )
    }

    /**
     * 设置 ItemViewType 的监听，根据不同数据类型，返回不同的type值
     *
     * @param listener
     */
    fun onItemViewType(listener: OnItemViewTypeListener<T>?) = apply {
        this.onItemViewTypeListener = listener
    }

    override fun getItemViewType(position: Int, list: List<T>): Int {
        return onItemViewTypeListener?.onItemViewType(position, list)
            ?: super.getItemViewType(position, list)
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)

        findListener(holder)?.onViewAttachedToWindow(holder)
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        findListener(holder)?.onViewDetachedFromWindow(holder)
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        super.onViewRecycled(holder)
        findListener(holder)?.onViewRecycled(holder)
    }

    override fun onFailedToRecycleView(holder: RecyclerView.ViewHolder): Boolean {
        return findListener(holder)?.onFailedToRecycleView(holder) ?: false
    }

    override fun isFullSpanItem(itemType: Int): Boolean {
        return super.isFullSpanItem(itemType) ||
                (typeViewHolders.get(itemType)?.isFullSpanItem(itemType) == true)
    }

    private fun findListener(holder: RecyclerView.ViewHolder) : OnMultiItemAdapterListener<T, RecyclerView.ViewHolder>? {
       return holder.itemView.getTag(R.id.BaseQuickAdapter_key_multi) as? OnMultiItemAdapterListener<T, RecyclerView.ViewHolder>
    }

    /**
     * 多类型布局 Adapter Listener
     *
     * @param T 数据类型
     * @param V ViewHolder 类型
     */
    interface OnMultiItemAdapterListener<T, V : RecyclerView.ViewHolder> {
        fun onCreate(context: Context, parent: ViewGroup, viewType: Int): V

        fun onBind(holder: V, position: Int, item: T?)

        fun onBind(holder: V, position: Int, item: T?, payloads: List<Any>) {
            onBind(holder, position, item)
        }

        fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {}

        fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {}

        fun onViewRecycled(holder: RecyclerView.ViewHolder) {}

        fun onFailedToRecycleView(holder: RecyclerView.ViewHolder): Boolean = false

        fun isFullSpanItem(itemType: Int): Boolean {
            return false
        }
    }

    /**
     * 如果需要一些属性，例如：adapter、context，则使用此抽象类.
     * 如果不需要，则可以直接实现[OnMultiItemAdapterListener]接口.
     *
     * @param T
     * @param V
     * @constructor Create empty On multi item
     */
    abstract class OnMultiItem<T, V : RecyclerView.ViewHolder> : OnMultiItemAdapterListener<T, V> {
        internal var weakA: WeakReference<BaseMultiItemAdapter<T>>? = null

        val adapter: BaseMultiItemAdapter<T>?
            get() = weakA?.get()

        val context: Context?
            get() = weakA?.get()?.context
    }

    fun interface OnItemViewTypeListener<T> {
        /**
         * 根据不同数据类型，返回不同的type值
         *
         * @param position
         * @param list
         * @return
         */
        fun onItemViewType(position: Int, list: List<T>): Int
    }
}