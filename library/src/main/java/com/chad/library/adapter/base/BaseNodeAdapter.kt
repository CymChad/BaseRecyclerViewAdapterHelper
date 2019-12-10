package com.chad.library.adapter.base

import android.view.ViewGroup
import androidx.annotation.IntRange
import com.chad.library.adapter.base.entity.node.BaseExpandNode
import com.chad.library.adapter.base.entity.node.BaseNode
import com.chad.library.adapter.base.entity.node.NodeFooterImp
import com.chad.library.adapter.base.provider.BaseItemProvider
import com.chad.library.adapter.base.provider.BaseNodeProvider

abstract class BaseNodeAdapter<VH : BaseViewHolder>(data: MutableList<BaseNode>? = null)
    : BaseProviderMultiAdapter<BaseNode, VH>(data) {

    private val fullSpanNodeTypeSet = HashSet<Int>()

    init {
        if (!data.isNullOrEmpty()) {
            val flatData = flatData(data)
            data.clear()
            data.addAll(flatData)
        }
    }


    /**
     * 添加 node provider
     * @param provider BaseItemProvider
     */
    fun addNodeProvider(provider: BaseNodeProvider<VH>) {
        addItemProvider(provider)
    }

    /**
     * 添加需要铺满的 node provider
     * @param provider BaseItemProvider
     */
    fun addFullSpanNodeProvider(provider: BaseNodeProvider<VH>) {
        fullSpanNodeTypeSet.add(provider.itemViewType)
        addItemProvider(provider)
    }

    /**
     * 添加脚部 node provider
     * 铺满一行或者一列
     * @param provider BaseItemProvider
     */
    fun addFooterNodeProvider(provider: BaseNodeProvider<VH>) {
        addFullSpanNodeProvider(provider)
    }

    /**
     * 请勿直接通过此方法添加 node provider！
     * @param provider BaseItemProvider<BaseNode, VH>
     */
    override fun addItemProvider(provider: BaseItemProvider<BaseNode, VH>) {
        if (provider is BaseNodeProvider) {
            super.addItemProvider(provider)
        } else {
            throw IllegalStateException("please add BaseNodeProvider, no BaseItemProvider!")
        }
    }

    override fun isFixedViewType(type: Int): Boolean {
        return super.isFixedViewType(type) || fullSpanNodeTypeSet.contains(type)
    }

    override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int): VH {
        val holder = super.onCreateDefViewHolder(parent, viewType)
        if (fullSpanNodeTypeSet.contains(viewType)) {
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

    override fun addListData(index: Int, data: BaseNode) {
        this.data.addAll(index, flatData(arrayListOf(data)))
    }

    override fun addListData(data: BaseNode) {
        this.data.addAll(flatData(arrayListOf(data)))
    }

    override fun addListData(newData: Collection<BaseNode>) {
        this.data.addAll(flatData(newData))
    }

    override fun addListData(position: Int, newData: Collection<BaseNode>) {
        this.data.addAll(position, flatData(newData))
    }

    override fun removeListData(position: Int) {
        val node = this.data[position]

        if (!node.childNode.isNullOrEmpty()) {
            val items = flatData(node.childNode!!)
            this.data.removeAll(items)
        }

        this.data.removeAt(position)
    }

    /**
     * 将输入的嵌套类型数组，扁平化
     * @param list List<BaseNode>
     * @return MutableList<BaseNode>
     */
    private fun flatData(list: Collection<BaseNode>): MutableList<BaseNode> {
        val newList = ArrayList<BaseNode>()

        for (element in list) {
            newList.add(element)

            if (element is BaseExpandNode && !element.isExpanded) {
                //什么都不做
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
     * 收起Node
     * @param position Int
     * @param animate Boolean
     * @param notify Boolean
     */
    @JvmOverloads
    fun collapse(@IntRange(from = 0) position: Int, animate: Boolean = true, notify: Boolean = true) {
        val node = this.data[position]
        val adapterPosition = position + getHeaderLayoutCount()

        if (node is BaseExpandNode && node.isExpanded) {
            node.isExpanded = false
            if (node.childNode.isNullOrEmpty()) {
                notifyItemChanged(adapterPosition)
                return
            }
            val items = flatData(node.childNode!!)
            this.data.removeAll(items)
            if (notify) {
                if (animate) {
                    notifyItemChanged(adapterPosition)
                    notifyItemRangeRemoved(adapterPosition + 1, items.size)
                } else {
                    notifyDataSetChanged()
                }
            }
        }
    }

    /**
     * 展开Node
     * @param position Int
     * @param animate Boolean
     * @param notify Boolean
     */
    @JvmOverloads
    fun expand(@IntRange(from = 0) position: Int, animate: Boolean = true, notify: Boolean = true) {
        val node = this.data[position]
        val adapterPosition = position + getHeaderLayoutCount()

        if (node is BaseExpandNode && !node.isExpanded) {
            node.isExpanded = true
            if (node.childNode.isNullOrEmpty()) {
                notifyItemChanged(adapterPosition)
                return
            }
            val items = flatData(node.childNode!!)
            this.data.addAll(position + 1, items)
            if (notify) {
                if (animate) {
                    notifyItemChanged(adapterPosition)
                    notifyItemRangeInserted(adapterPosition + 1, items.size)
                } else {
                    notifyDataSetChanged()
                }
            }

        }
    }

    /**
     * 收起或展开Node
     * @param position Int
     * @param animate Boolean
     * @param notify Boolean
     */
    @JvmOverloads
    fun expandOrCollapse(@IntRange(from = 0) position: Int, animate: Boolean = true, notify: Boolean = true) {
        val node = this.data[position]
        if (node is BaseExpandNode) {
            if (node.isExpanded) {
                collapse(position, animate, notify)
            } else {
                expand(position, animate, notify)
            }
        }
    }


    fun findParentNode(node: BaseNode) {
        //TODO
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