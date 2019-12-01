package com.chad.library.adapter.base

import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.provider.BaseItemProvider
import java.lang.ref.WeakReference

/**
 * 当有多种条目的时候，避免在convert()中做太多的业务逻辑，把逻辑放在对应的 ItemProvider 中。
 * 适用于以下情况：
 * 1、实体类不方便扩展，此Adapter的数据类型可以是任意类型，只需要在[getItemType]中返回对应类型
 * 2、item 类型较多，在convert()中管理起来复杂
 *
 * @param T data 数据类型
 * @param VH : BaseViewHolder
 * @constructor
 */
abstract class BaseProviderMultiAdapter<T, VH : BaseViewHolder>(data: MutableList<T>? = null) :
        BaseDelegateMultiAdapter<T, VH>(data) {

    private val mItemProviders by lazy { SparseArray<BaseItemProvider<T, VH>>() }

    /**
     * 返回 item 类型
     * @param data List<T>
     * @param position Int
     * @return Int
     */
    protected abstract fun getItemType(data: List<T>, position: Int): Int

    override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int): VH {
        val provider = mItemProviders.get(viewType)
        checkNotNull(provider) { "No such provider found，please use addItemProvider() first!" }
        return createBaseViewHolder(parent, provider.layoutId)
    }

    override fun getDefItemViewType(position: Int): Int {
        return getItemType(data, position)
    }

    override fun convert(helper: VH, item: T?) {
        val itemViewType = helper.itemViewType
        val provider = mItemProviders.get(itemViewType)

        provider.context = helper.itemView.context

        provider.convert(helper, item)
    }

    override fun convert(helper: VH, item: T?, payloads: List<Any>) {
        val itemViewType = helper.itemViewType
        val provider = mItemProviders.get(itemViewType)

        provider.context = helper.itemView.context

        provider.convert(helper, item, payloads)
    }

    override fun bindViewClickListener(viewHolder: VH) {
        if (getOnItemClickListener() == null) {
            //如果没有设置点击监听，则回调给 itemProvider
            //Callback to itemProvider if no click listener is set
            viewHolder.itemView.setOnClickListener(View.OnClickListener {
                var position = viewHolder.adapterPosition
                if (position == RecyclerView.NO_POSITION) {
                    return@OnClickListener
                }
                position -= getHeaderLayoutCount()

                val itemViewType = viewHolder.itemViewType
                val provider = mItemProviders.get(itemViewType)

                provider.onClick(viewHolder, data[position], position)
            })
        }
        if (getOnItemLongClickListener() == null) {
            //如果没有设置长按监听，则回调给itemProvider
            // If you do not set a long press listener, callback to the itemProvider
            viewHolder.itemView.setOnLongClickListener(View.OnLongClickListener {
                var position = viewHolder.adapterPosition
                if (position == RecyclerView.NO_POSITION) {
                    return@OnLongClickListener false
                }
                position -= getHeaderLayoutCount()

                val itemViewType = viewHolder.itemViewType
                val provider = mItemProviders.get(itemViewType)
                provider.onLongClick(viewHolder, data[position], position)
            })
        }

        super.bindViewClickListener(viewHolder)
    }

    /**
     * 必须通过此方法，添加 provider
     * @param provider BaseItemProvider
     */
    fun addItemProvider(provider: BaseItemProvider<T, VH>) {
        provider.weakAdapter = WeakReference(this)
        val viewType = provider.itemViewType
        mItemProviders.put(viewType, provider)
    }
}