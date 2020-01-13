package com.chad.library.adapter.base

import android.view.ViewGroup
import androidx.annotation.IntRange
import androidx.recyclerview.widget.DiffUtil
import com.chad.library.adapter.base.entity.node.BaseExpandNode
import com.chad.library.adapter.base.entity.node.BaseNode
import com.chad.library.adapter.base.entity.node.NodeFooterImp
import com.chad.library.adapter.base.provider.BaseItemProvider
import com.chad.library.adapter.base.provider.BaseNodeProvider
import com.chad.library.adapter.base.viewholder.BaseViewHolder

abstract class BaseNodeAdapter(nodeList: MutableList<BaseNode>? = null)
    : BaseProviderMultiAdapter<BaseNode>(null) {

    private val fullSpanNodeTypeSet = HashSet<Int>()

    init {
        if (!nodeList.isNullOrEmpty()) {
            val flatData = flatData(nodeList)
            this.data.addAll(flatData)
        }
    }


    /**
     * 添加 node provider
     * @param provider BaseItemProvider
     */
    fun addNodeProvider(provider: BaseNodeProvider) {
        addItemProvider(provider)
    }

    /**
     * 添加需要铺满的 node provider
     * @param provider BaseItemProvider
     */
    fun addFullSpanNodeProvider(provider: BaseNodeProvider) {
        fullSpanNodeTypeSet.add(provider.itemViewType)
        addItemProvider(provider)
    }

    /**
     * 添加脚部 node provider
     * 铺满一行或者一列
     * @param provider BaseItemProvider
     */
    fun addFooterNodeProvider(provider: BaseNodeProvider) {
        addFullSpanNodeProvider(provider)
    }

    /**
     * 请勿直接通过此方法添加 node provider！
     * @param provider BaseItemProvider<BaseNode, VH>
     */
    override fun addItemProvider(provider: BaseItemProvider<BaseNode>) {
        if (provider is BaseNodeProvider) {
            super.addItemProvider(provider)
        } else {
            throw IllegalStateException("Please add BaseNodeProvider, no BaseItemProvider!")
        }
    }

    override fun isFixedViewType(type: Int): Boolean {
        return super.isFixedViewType(type) || fullSpanNodeTypeSet.contains(type)
    }

    override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return super.onCreateDefViewHolder(parent, viewType).apply {
            if (fullSpanNodeTypeSet.contains(viewType)) {
                setFullSpan(this)
            }
        }
    }

    /*************************** 重写数据设置方法 ***************************/

    override fun setNewData(data: MutableList<BaseNode>?) {
        if (data == this.data) {
            return
        }
        super.setNewData(flatData(data ?: arrayListOf()))
    }

    /**
     * 如果需要对某节点下的子节点进行数据操作，请使用[nodeAddData]！
     *
     * @param position Int 整个 data 的 index
     * @param data BaseNode
     */
    override fun addData(position: Int, data: BaseNode) {
        addData(position, arrayListOf(data))
    }

    override fun addData(data: BaseNode) {
        addData(arrayListOf(data))
    }

    override fun addData(position: Int, newData: Collection<BaseNode>) {
        val nodes = flatData(newData)
        super.addData(position, nodes)
    }

    override fun addData(newData: Collection<BaseNode>) {
        val nodes = flatData(newData)
        super.addData(nodes)
    }

    /**
     * 如果需要对某节点下的子节点进行数据操作，请使用[nodeRemoveData]！
     *
     * @param position Int 整个 data 的 index
     */
    override fun remove(position: Int) {
        val removeCount = removeAt(position)
        notifyItemRangeRemoved(position + headerLayoutCount, removeCount)
        compatibilityDataSizeChanged(0)
    }

    /**
     * 如果需要对某节点下的子节点进行数据操作，请使用[nodeSetData]！
     * @param index Int
     * @param data BaseNode
     */
    override fun setData(index: Int, data: BaseNode) {
        // 先移除，再添加
        val removeCount = removeAt(index)

        val newFlatData = flatData(arrayListOf(data))
        this.data.addAll(index, newFlatData)

        if (removeCount == newFlatData.size) {
            notifyItemRangeChanged(index + headerLayoutCount, removeCount)
        } else {
            notifyItemRangeRemoved(index + headerLayoutCount, removeCount)
            notifyItemRangeInserted(index + headerLayoutCount, newFlatData.size)

//        notifyItemRangeChanged(index + getHeaderLayoutCount(), max(removeCount, newFlatData.size)
        }
    }

    /**
     * 替换整个列表数据，如果需要对某节点下的子节点进行替换，请使用[nodeReplaceChildData]！
     * @param newData Collection<BaseNode>
     */
    override fun replaceData(newData: Collection<BaseNode>) {
        // 不是同一个引用才清空列表
        if (newData != this.data) {
            super.replaceData(flatData(newData))
        }
    }

    override fun setDiffNewData(newData: MutableList<BaseNode>?) {
        if (hasEmptyView()) {
            setNewData(newData)
            return
        }
        super.setDiffNewData(flatData(newData ?: arrayListOf()))
    }

    override fun setDiffNewData(diffResult: DiffUtil.DiffResult, newData: MutableList<BaseNode>) {
        if (hasEmptyView()) {
            setNewData(newData)
            return
        }
        super.setDiffNewData(diffResult, flatData(newData))
    }

    /**
     * 从数组中移除
     * @param position Int
     * @return Int 被移除的数量
     */
    private fun removeAt(position: Int): Int {
        if (position >= data.size) {
            return 0
        }
        // 记录被移除的item数量
        var removeCount = 0

        // 先移除子项
        removeCount = removeChildAt(position)

        // 移除node自己
        this.data.removeAt(position)
        removeCount += 1

        val node = this.data[position]
        // 移除脚部
        if (node is NodeFooterImp && node.footerNode != null) {
            this.data.removeAt(position)
            removeCount += 1
        }
        return removeCount
    }

    private fun removeChildAt(position: Int): Int {
        if (position >= data.size) {
            return 0
        }
        // 记录被移除的item数量
        var removeCount = 0

        val node = this.data[position]
        // 移除子项
        if (!node.childNode.isNullOrEmpty()) {
            if (node is BaseExpandNode) {
                if (node.isExpanded) {
                    val items = flatData(node.childNode!!)
                    this.data.removeAll(items)
                    removeCount = items.size
                }
            } else {
                val items = flatData(node.childNode!!)
                this.data.removeAll(items)
                removeCount = items.size
            }
        }
        return removeCount
    }

    /*************************** 重写数据设置方法 END ***************************/


    /*************************** Node 数据操作 ***************************/

    /**
     * 对指定的父node，添加子node
     * @param parentNode BaseNode 父node
     * @param data BaseNode 子node
     */
    fun nodeAddData(parentNode: BaseNode, data: BaseNode) {
        parentNode.childNode?.let {
            it.add(data)

            if (parentNode is BaseExpandNode && !parentNode.isExpanded) {
                return
            }

            val parentIndex = this.data.indexOf(parentNode)
            val childIndex = it.size
            addData(parentIndex + childIndex, data)
        }
    }

    /**
     * 对指定的父node，在指定位置添加子node
     * @param parentNode BaseNode 父node
     * @param childIndex Int 此位置是相对于其childNodes数据的位置！并不是整个data
     * @param data BaseNode 添加的数据
     */
    fun nodeAddData(parentNode: BaseNode, childIndex: Int, data: BaseNode) {
        parentNode.childNode?.let {
            it.add(childIndex, data)

            if (parentNode is BaseExpandNode && !parentNode.isExpanded) {
                return
            }

            val parentIndex = this.data.indexOf(parentNode)
            val pos = parentIndex + 1 + childIndex
            addData(pos, data)
        }
    }

    /**
     * 对指定的父node，在指定位置添加子node集合
     * @param parentNode BaseNode 父node
     * @param childIndex Int 此位置是相对于其childNodes数据的位置！并不是整个data
     * @param newData 添加的数据集合
     */
    fun nodeAddData(parentNode: BaseNode, childIndex: Int, newData: Collection<BaseNode>) {
        parentNode.childNode?.let {
            it.addAll(childIndex, newData)

            if (parentNode is BaseExpandNode && !parentNode.isExpanded) {
                return
            }
            val parentIndex = this.data.indexOf(parentNode)
            val pos = parentIndex + 1 + childIndex
            addData(pos, newData)
        }
    }

    /**
     * 对指定的父node下对子node进行移除
     * @param parentNode BaseNode 父node
     * @param childIndex Int 此位置是相对于其childNodes数据的位置！并不是整个data
     */
    fun nodeRemoveData(parentNode: BaseNode, childIndex: Int) {
        parentNode.childNode?.let {
            if (childIndex >= it.size) {
                return
            }

            if (parentNode is BaseExpandNode && !parentNode.isExpanded) {
                it.removeAt(childIndex)
                return
            }

            val parentIndex = this.data.indexOf(parentNode)
            val pos = parentIndex + 1 + childIndex
            remove(pos)

            it.removeAt(childIndex)
        }
    }

    /**
     * 对指定的父node下对子node进行移除
     * @param parentNode BaseNode 父node
     * @param childNode BaseNode 子node
     */
    fun nodeRemoveData(parentNode: BaseNode, childNode: BaseNode) {
        parentNode.childNode?.let {
            if (parentNode is BaseExpandNode && !parentNode.isExpanded) {
                it.remove(childNode)
                return
            }
            remove(childNode)

            it.remove(childNode)
        }
    }

    /**
     * 改变指定的父node下的子node数据
     * @param parentNode BaseNode
     * @param childIndex Int 此位置是相对于其childNodes数据的位置！并不是整个data
     * @param data BaseNode 新数据
     */
    fun nodeSetData(parentNode: BaseNode, childIndex: Int, data: BaseNode) {
        parentNode.childNode?.let {
            if (childIndex >= it.size) {
                return
            }

            if (parentNode is BaseExpandNode && !parentNode.isExpanded) {
                it[childIndex] = data
                return
            }

            val parentIndex = this.data.indexOf(parentNode)
            val pos = parentIndex + 1 + childIndex
            setData(pos, data)

            it[childIndex] = data
        }
    }

    /**
     * 替换父节点下的子节点集合
     * @param parentNode BaseNode
     * @param newData Collection<BaseNode>
     */
    fun nodeReplaceChildData(parentNode: BaseNode, newData: Collection<BaseNode>) {
        parentNode.childNode?.let {
            if (parentNode is BaseExpandNode && !parentNode.isExpanded) {
                it.clear()
                it.addAll(newData)
                return
            }

            val parentIndex = this.data.indexOf(parentNode)
            val removeCount = removeChildAt(parentIndex)

            it.clear()
            it.addAll(newData)

            val newFlatData = flatData(newData)
            this.data.addAll(parentIndex + 1, newFlatData)

            val positionStart = parentIndex + 1 + headerLayoutCount
            if (removeCount == newFlatData.size) {
                notifyItemRangeChanged(positionStart, removeCount)
            } else {
                notifyItemRangeRemoved(positionStart, removeCount)
                notifyItemRangeInserted(positionStart, newFlatData.size)
            }
//            notifyItemRangeChanged(parentIndex + 1 + getHeaderLayoutCount(), max(removeCount, newFlatData.size))
        }
    }

    /*************************** Node 数据操作 END ***************************/

    /**
     * 将输入的嵌套类型数组循环递归，在扁平化数据的同时，设置展开状态
     * @param list Collection<BaseNode>
     * @param isExpanded Boolean? 如果不需要改变状态，设置为null。true 为展开，false 为收起
     * @return MutableList<BaseNode>
     */
    private fun flatData(list: Collection<BaseNode>, isExpanded: Boolean? = null): MutableList<BaseNode> {
        val newList = ArrayList<BaseNode>()

        for (element in list) {
            newList.add(element)

            if (element is BaseExpandNode) {
                // 如果是展开状态 或者需要设置为展开状态
                if (isExpanded == true || element.isExpanded) {
                    val childNode = element.childNode
                    if (!childNode.isNullOrEmpty()) {
                        val items = flatData(childNode, isExpanded)
                        newList.addAll(items)
                    }
                }
                isExpanded?.let {
                    element.isExpanded = it
                }
            } else {
                val childNode = element.childNode
                if (!childNode.isNullOrEmpty()) {
                    val items = flatData(childNode, isExpanded)
                    newList.addAll(items)
                }
            }

            if (element is NodeFooterImp) {
                element.footerNode?.let {
                    newList.add(it)
                }
            }
        }
        return newList
    }


    /**
     * 收起Node
     * 私有方法，为减少递归复杂度，不对外暴露 isChangeChildExpand 参数，防止错误设置
     *
     * @param position Int
     * @param isChangeChildCollapse Boolean 是否改变子 node 的状态为收起，true 为跟随变为收起，false 表示保持原状态。
     * @param animate Boolean
     * @param notify Boolean
     */
    private fun collapse(@IntRange(from = 0) position: Int,
                         isChangeChildCollapse: Boolean = false,
                         animate: Boolean = true,
                         notify: Boolean = true,
                         parentPayload: Any? = null): Int {
        val node = this.data[position]

        if (node is BaseExpandNode && node.isExpanded) {
            val adapterPosition = position + headerLayoutCount

            node.isExpanded = false
            if (node.childNode.isNullOrEmpty()) {
                notifyItemChanged(adapterPosition, parentPayload)
                return 0
            }
            val items = flatData(node.childNode!!, if (isChangeChildCollapse) false else null)
            val size = items.size
            this.data.removeAll(items)
            if (notify) {
                if (animate) {
                    notifyItemChanged(adapterPosition, parentPayload)
                    notifyItemRangeRemoved(adapterPosition + 1, size)
                } else {
                    notifyDataSetChanged()
                }
            }
            return size
        }
        return 0
    }

    /**
     * 展开Node
     * 私有方法，为减少递归复杂度，不对外暴露 isChangeChildExpand 参数，防止错误设置
     *
     * @param position Int
     * @param isChangeChildExpand Boolean 是否改变子 node 的状态为展开，true 为跟随变为展开，false 表示保持原状态。
     * @param animate Boolean
     * @param notify Boolean
     */
    private fun expand(@IntRange(from = 0) position: Int,
                       isChangeChildExpand: Boolean = false,
                       animate: Boolean = true,
                       notify: Boolean = true,
                       parentPayload: Any? = null): Int {
        val node = this.data[position]

        if (node is BaseExpandNode && !node.isExpanded) {
            val adapterPosition = position + headerLayoutCount

            node.isExpanded = true
            if (node.childNode.isNullOrEmpty()) {
                notifyItemChanged(adapterPosition, parentPayload)
                return 0
            }
            val items = flatData(node.childNode!!, if (isChangeChildExpand) true else null)
            val size = items.size
            this.data.addAll(position + 1, items)
            if (notify) {
                if (animate) {
                    notifyItemChanged(adapterPosition, parentPayload)
                    notifyItemRangeInserted(adapterPosition + 1, size)
                } else {
                    notifyDataSetChanged()
                }
            }
            return size
        }
        return 0
    }

    /**
     * 收起 node
     * @param position Int
     * @param animate Boolean
     * @param notify Boolean
     */
    @JvmOverloads
    fun collapse(@IntRange(from = 0) position: Int,
                 animate: Boolean = true,
                 notify: Boolean = true,
                 parentPayload: Any? = null): Int {
        return collapse(position, false, animate, notify, parentPayload)
    }

    /**
     * 展开 node
     * @param position Int
     * @param animate Boolean
     * @param notify Boolean
     */
    @JvmOverloads
    fun expand(@IntRange(from = 0) position: Int,
               animate: Boolean = true,
               notify: Boolean = true,
               parentPayload: Any? = null): Int {
        return expand(position, false, animate, notify, parentPayload)
    }

    /**
     * 收起或展开Node
     * @param position Int
     * @param animate Boolean
     * @param notify Boolean
     */
    @JvmOverloads
    fun expandOrCollapse(@IntRange(from = 0) position: Int,
                         animate: Boolean = true,
                         notify: Boolean = true,
                         parentPayload: Any? = null): Int {
        val node = this.data[position]
        if (node is BaseExpandNode) {
            return if (node.isExpanded) {
                collapse(position, false, animate, notify, parentPayload)
            } else {
                expand(position, false, animate, notify, parentPayload)
            }
        }
        return 0
    }

    @JvmOverloads
    fun expandAndChild(@IntRange(from = 0) position: Int,
                       animate: Boolean = true,
                       notify: Boolean = true,
                       parentPayload: Any? = null): Int {
        return expand(position, true, animate, notify, parentPayload)
    }

    @JvmOverloads
    fun collapseAndChild(@IntRange(from = 0) position: Int,
                         animate: Boolean = true,
                         notify: Boolean = true,
                         parentPayload: Any? = null): Int {
        return collapse(position, true, animate, notify, parentPayload)
    }

    /**
     * 展开某一个node的时候，折叠其他node
     * @param position Int
     * @param isExpandedChild Boolean 展开的时候，是否展开子项目
     * @param isCollapseChild Boolean 折叠其他node的时候，是否折叠子项目
     * @param animate Boolean
     * @param notify Boolean
     */
    @JvmOverloads
    fun expandAndCollapseOther(@IntRange(from = 0) position: Int,
                               isExpandedChild: Boolean = false,
                               isCollapseChild: Boolean = true,
                               animate: Boolean = true,
                               notify: Boolean = true,
                               expandPayload: Any? = null,
                               collapsePayload: Any? = null) {

        val expandCount = expand(position, isExpandedChild, animate, notify, expandPayload)
        if (expandCount == 0) {
            return
        }

        val parentPosition = findParentNode(position)
        // 当前层级顶部开始位置
        val firstPosition: Int = if (parentPosition == -1) {
            0 // 如果没有父节点，则为最外层，从0开始
        } else {
            parentPosition + 1 // 如果有父节点，则为子节点，从 父节点+1 的位置开始
        }

        // 当前 position 之前有 node 收起以后，position的位置就会变化
        var newPosition = position

        // 在此之前的 node 总数
        val beforeAllSize = position - firstPosition
        // 如果此 position 之前有 node
        if (beforeAllSize > 0) {
            // 从顶部开始位置循环
            var i = firstPosition
            do {
                val collapseSize = collapse(i, isCollapseChild, animate, notify, collapsePayload)
                i++
                // 每次折叠后，重新计算新的 Position
                newPosition -= collapseSize
            } while (i < newPosition)
        }

        // 当前层级最后的位置
        var lastPosition: Int = if (parentPosition == -1) {
            data.size - 1 // 如果没有父节点，则为最外层
        } else {
            val dataSize = data[parentPosition].childNode?.size ?: 0
            parentPosition + dataSize + expandCount // 如果有父节点，则为子节点，父节点 + 子节点数量 + 展开的数量
        }

        //如果此 position 之后有 node
        if ((newPosition + expandCount) < lastPosition) {
            var i = newPosition + expandCount + 1
            while (i <= lastPosition) {
                val collapseSize = collapse(i, isCollapseChild, animate, notify, collapsePayload)
                i++
                lastPosition -= collapseSize
            }

        }

    }


    /**
     * 查找父节点。如果不存在，则返回-1
     * @param node BaseNode
     * @return Int 父节点的position
     */
    fun findParentNode(node: BaseNode): Int {
        val pos = this.data.indexOf(node)
        if (pos == -1 || pos == 0) {
            return -1
        }

        for (i in pos - 1 downTo 0) {
            val tempNode = this.data[i]
            if (tempNode.childNode?.contains(node) == true) {
                return i
            }
        }
        return -1
    }

    fun findParentNode(@IntRange(from = 0) position: Int): Int {
        if (position == 0) {
            return -1
        }
        val node = this.data[position]
        for (i in position - 1 downTo 0) {
            val tempNode = this.data[i]
            if (tempNode.childNode?.contains(node) == true) {
                return i
            }
        }
        return -1
    }
}