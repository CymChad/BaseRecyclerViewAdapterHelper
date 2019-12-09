package com.chad.library.adapter.base

import android.view.ViewGroup
import androidx.annotation.IntRange
import com.chad.library.adapter.base.entity.node.BaseExpandNode
import com.chad.library.adapter.base.entity.node.BaseNode
import com.chad.library.adapter.base.entity.node.NodeFooterImp
import com.chad.library.adapter.base.provider.BaseItemProvider
import com.chad.library.adapter.base.provider.BaseNodeProvider
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

abstract class BaseNodeAdapter<VH : BaseViewHolder>(data: MutableList<BaseNode>? = null)
    : BaseProviderMultiAdapter<BaseNode, VH>(data) {

    private val sectionFullSpanSet = HashSet<Int>()
    private val sectionFooterSet = HashSet<Int>()

    init {
        if (data != null) {
            val flatData = flatData(data)
            data.clear()
            data.addAll(flatData)
        }
    }


    /**
     * 必须通过此方法，添加 provider
     * @param provider BaseItemProvider
     */
    fun addNodeProvider(provider: BaseNodeProvider<VH>) {
        addItemProvider(provider)
    }

    fun addFullSpanNodeProvider(provider: BaseNodeProvider<VH>) {
        sectionFullSpanSet.add(provider.itemViewType)
        addItemProvider(provider)
    }

    fun addSectionFooterProvider(provider: BaseNodeProvider<VH>) {
        sectionFooterSet.add(provider.itemViewType)
        addItemProvider(provider)
    }

    override fun addItemProvider(provider: BaseItemProvider<BaseNode, VH>) {
        if (provider is BaseNodeProvider) {
            super.addItemProvider(provider)
        } else {
            throw IllegalStateException("please add BaseNodeProvider, no BaseItemProvider!")
        }
    }

    override fun isFixedViewType(type: Int): Boolean {
        return super.isFixedViewType(type) || sectionFullSpanSet.contains(type) || sectionFooterSet.contains(type)
    }

    override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int): VH {
        val holder = super.onCreateDefViewHolder(parent, viewType)
        if (sectionFullSpanSet.contains(viewType) || sectionFooterSet.contains(viewType)) {
            setFullSpan(holder)
        }
        return holder
    }

//    override fun getDefItemCount(): Int {
//        allDataCount(data, 0)
//        return super.getDefItemCount()
//    }

    override fun setListNewData(data: MutableList<BaseNode>?) {
        this.data = if (data == null) {
            arrayListOf()
        } else {
            flatData(data)
        }
    }



    private fun generateSectionTag(): String? {
        return UUID.randomUUID().toString()
    }

    private fun flatData(list: List<BaseNode>): MutableList<BaseNode> {
        val newList = ArrayList<BaseNode>()

        for (element in list) {
            newList.add(element)

            if (element is BaseExpandNode && !element.isExpanded) {

            } else {
                val childNode = element.childNode
                if (!childNode.isNullOrEmpty()) {
                    val items = flatData(childNode)
                    newList.addAll(items)
                }
            }

            if (element is NodeFooterImp) {
                element.getSectionFooterEntity()?.let {
                    newList.add(it)
                }
            }
        }

        return newList
    }


    /**
     * 收起展开项目
     * @param position Int
     * @param animate Boolean
     * @param notify Boolean
     */
    @JvmOverloads
    fun collapse(@IntRange(from = 0) position: Int, animate: Boolean = true, notify: Boolean = true) {
        val node = this.data[position]


        if (node is BaseExpandNode && node.isExpanded) {
            if (node.childNode.isNullOrEmpty()) {
                node.isExpanded = false
                notifyItemChanged(position)
                return
            }
            val items = flatData(node.childNode!!)
            this.data.removeAll(items)
            notifyItemChanged(position)
            notifyItemRangeRemoved(position + 1, items.size)
        }
    }

    //统计
    private fun allDataCount(list: List<BaseNode>, count: Int): Int {
        var newCount = list.size + count
        list.map {
            if (!it.childNode.isNullOrEmpty()) {
                newCount += allDataCount(it.childNode!!, newCount)
            }
        }
        return newCount
    }
}