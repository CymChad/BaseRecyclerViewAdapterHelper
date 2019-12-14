package com.chad.library.adapter.base

import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.provider.BaseItemProvider
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 * 当有多种条目的时候，避免在convert()中做太多的业务逻辑，把逻辑放在对应的 ItemProvider 中。
 * 适用于以下情况：
 * 1、实体类不方便扩展，此Adapter的数据类型可以是任意类型，只需要在[getItemType]中返回对应类型
 * 2、item 类型较多，在convert()中管理起来复杂
 *
 * ViewHolder 由 [BaseItemProvider] 实现，并且每个[BaseItemProvider]可以拥有自己类型的ViewHolder类型。
 *
 * @param T data 数据类型
 * @param VH : BaseViewHolder
 * @constructor
 */
abstract class BaseProviderMultiAdapter<T>(data: MutableList<T>? = null) :
        BaseQuickAdapter<T, BaseViewHolder>(0, data) {

    private val mItemProviders by lazy { SparseArray<BaseItemProvider<T>>() }

    /**
     * 返回 item 类型
     * @param data List<T>
     * @param position Int
     * @return Int
     */
    protected abstract fun getItemType(data: List<T>, position: Int): Int

    /**
     * 必须通过此方法，添加 provider
     * @param provider BaseItemProvider
     */
    open fun addItemProvider(provider: BaseItemProvider<T>) {
        provider.setAdapter(this)
        mItemProviders.put(provider.itemViewType, provider)
    }

    override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val provider = mItemProviders.get(viewType)
        checkNotNull(provider) { "ViewType: $viewType no such provider found，please use addItemProvider() first!" }
        provider.context = parent.context
        return provider.onCreateViewHolder(parent).apply {
            provider.onViewHolderCreated(this)
        }
    }

    override fun getDefItemViewType(position: Int): Int {
        return getItemType(data, position)
    }

    override fun convert(helper: BaseViewHolder, item: T?) {
        val itemViewType = helper.itemViewType
        val provider = mItemProviders.get(itemViewType)

        provider.convert(helper, item)
    }

    override fun convert(helper: BaseViewHolder, item: T?, payloads: List<Any>) {
        val itemViewType = helper.itemViewType
        val provider = mItemProviders.get(itemViewType)

        provider.convert(helper, item, payloads)
    }

    override fun bindViewClickListener(viewHolder: BaseViewHolder, viewType: Int) {
        super.bindViewClickListener(viewHolder, viewType)
        bindClick(viewHolder)
        bindChildClick(viewHolder, viewType)
    }

    protected open fun bindClick(viewHolder: BaseViewHolder) {
        if (getOnItemClickListener() == null) {
            //如果没有设置点击监听，则回调给 itemProvider
            //Callback to itemProvider if no click listener is set
            viewHolder.itemView.setOnClickListener {
                var position = viewHolder.adapterPosition
                if (position == RecyclerView.NO_POSITION) {
                    return@setOnClickListener
                }
                position -= getHeaderLayoutCount()

                val itemViewType = viewHolder.itemViewType
                val provider = mItemProviders.get(itemViewType)

                provider.onClick(viewHolder, it, data[position], position)
            }
        }
        if (getOnItemLongClickListener() == null) {
            //如果没有设置长按监听，则回调给itemProvider
            // If you do not set a long press listener, callback to the itemProvider
            viewHolder.itemView.setOnLongClickListener {
                var position = viewHolder.adapterPosition
                if (position == RecyclerView.NO_POSITION) {
                    return@setOnLongClickListener false
                }
                position -= getHeaderLayoutCount()

                val itemViewType = viewHolder.itemViewType
                val provider = mItemProviders.get(itemViewType)
                provider.onLongClick(viewHolder, it, data[position], position)
            }
        }
    }

    protected open fun bindChildClick(viewHolder: BaseViewHolder, viewType: Int) {
        if (getOnItemChildClickListener() == null) {
            val provider = mItemProviders.get(viewType)
            val ids = provider.getChildClickViewIds()
            ids.forEach { id ->
                viewHolder.itemView.findViewById<View>(id)?.let {
                    if (!it.isClickable) {
                        it.isClickable = true
                    }
                    it.setOnClickListener { v ->
                        var position: Int = viewHolder.adapterPosition
                        if (position == RecyclerView.NO_POSITION) {
                            return@setOnClickListener
                        }
                        position -= getHeaderLayoutCount()
                        provider.onChildClick(viewHolder, v, data[position], position)
                    }
                }
            }
        }
        if (getOnItemChildLongClickListener() == null) {
            val provider = mItemProviders.get(viewType)
            val ids = provider.getChildLongClickViewIds()
            ids.forEach { id ->
                viewHolder.itemView.findViewById<View>(id)?.let {
                    if (!it.isLongClickable) {
                        it.isLongClickable = true
                    }
                    it.setOnLongClickListener { v ->
                        var position: Int = viewHolder.adapterPosition
                        if (position == RecyclerView.NO_POSITION) {
                            return@setOnLongClickListener false
                        }
                        position -= getHeaderLayoutCount()
                        provider.onChildLongClick(viewHolder, v, data[position], position)
                    }
                }
            }
        }
    }
}