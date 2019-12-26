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

abstract class BaseNodeAdapter(data: MutableList<BaseNode>? = null)
    : BaseProviderMultiAdapter<BaseNode>(data) {

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

    override fun remove(position: Int) {
        if (position >= data.size) {
            return
        }

        //被移除的item数量
        var removeCount = 0

        val node = this.data[position]
        //移除子项
        if (!node.childNode.isNullOrEmpty()) {
            val items = flatData(node.childNode!!)
            this.data.removeAll(items)
            removeCount = items.size
        }
        //移除node自己
        this.data.removeAt(position)
        removeCount += 1

        // 移除脚部
        if (node is NodeFooterImp && node.footerNode != null) {
            this.data.removeAt(position)
            removeCount += 1
        }

        notifyItemRangeRemoved(position + getHeaderLayoutCount(), removeCount)
        compatibilityDataSizeChanged(0)
    }

    override fun setData(index: Int, data: BaseNode) {
        val flatData = flatData(arrayListOf(data))
        flatData.forEachIndexed { i, baseNode ->
            this.data[index + i] = baseNode
        }
        notifyItemRangeChanged(index + getHeaderLayoutCount(), flatData.size)
    }

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

    /*************************** 重写数据设置方法 END ***************************/

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
                         notify: Boolean = true): Int {
        val node = this.data[position]

        if (node is BaseExpandNode && node.isExpanded) {
            val adapterPosition = position + getHeaderLayoutCount()

            node.isExpanded = false
            if (node.childNode.isNullOrEmpty()) {
                notifyItemChanged(adapterPosition)
                return 0
            }
            val items = flatData(node.childNode!!, if (isChangeChildCollapse) false else null)
            val size = items.size
            this.data.removeAll(items)
            if (notify) {
                if (animate) {
                    notifyItemChanged(adapterPosition)
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
                       notify: Boolean = true): Int {
        val node = this.data[position]

        if (node is BaseExpandNode && !node.isExpanded) {
            val adapterPosition = position + getHeaderLayoutCount()

            node.isExpanded = true
            if (node.childNode.isNullOrEmpty()) {
                notifyItemChanged(adapterPosition)
                return 0
            }
            val items = flatData(node.childNode!!, if (isChangeChildExpand) true else null)
            val size = items.size
            this.data.addAll(position + 1, items)
            if (notify) {
                if (animate) {
                    notifyItemChanged(adapterPosition)
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
                 notify: Boolean = true): Int {
        return collapse(position, false, animate, notify)
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
               notify: Boolean = true): Int {
        return expand(position, false, animate, notify)
    }

    /**
     * 收起或展开Node
     * @param position Int
     * @param animate Boolean
     * @param notify Boolean
     */
    @JvmOverloads
    fun expandOrCollapse(@IntRange(from = 0) position: Int, animate: Boolean = true, notify: Boolean = true): Int {
        val node = this.data[position]
        if (node is BaseExpandNode) {
            return if (node.isExpanded) {
                collapse(position, false, animate, notify)
            } else {
                expand(position, false, animate, notify)
            }
        }
        return 0
    }

    @JvmOverloads
    fun expandAndChild(@IntRange(from = 0) position: Int, animate: Boolean = true, notify: Boolean = true): Int {
        return expand(position, true, animate, notify)
    }

    @JvmOverloads
    fun collapseAndChild(@IntRange(from = 0) position: Int, animate: Boolean = true, notify: Boolean = true): Int {
        return collapse(position, true, animate, notify)
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
                               notify: Boolean = true) {

        val parentPosition = findParentNode(position)
        if (parentPosition != -1) {
            // 如果是子节点
            childExpandAndCollapseOther(position, parentPosition, isExpandedChild, isCollapseChild, animate, notify)
            return
        }

        val count = expand(position, isExpandedChild, animate, notify)

        // 当前 position 之前有 node 收起以后，position的位置就会变化
        var newPosition = position

        //如果此 position 之前有 node
        if (position > 0) {
            // 在此之前的 node 总数
            val beforeAllSize = position
            // 记录被折叠了 node 的总数
            var collapseSize = 0
            var i = 0
            // 循环添加： 总数 - 折叠后的总数即为此前还剩余的 node 总数。
            do {
                collapseSize += collapse(i, isCollapseChild, animate, notify)
                i++
                println("-------->> $beforeAllSize  i: $i   size: $collapseSize")
            } while (i < (beforeAllSize - collapseSize))

            newPosition = position - collapseSize
        }

        //如果此 position 之后有 node
        println("22-------->> $newPosition   i: ${(newPosition + count + 1)}   size: ${data.size}")
        if ((newPosition + count + 1) < data.size) {
            // 后面的 node 总数 = 总data的size - （展开位置的索引 + 展开的数量 + 1）
//            val afterAllSize = data.size - position - count - 1
//            var collapseSize = 0
            // 位置索引 + 展开的数量 + 1 即为循环开始的索引
            var i = newPosition + count + 1
            while (i < (data.size)) {
                println("11-------->>   i: $i   size: ${data.size}")
                collapse(i, isCollapseChild, animate, notify)
                i++
            }

        }
    }

    private fun childExpandAndCollapseOther(@IntRange(from = 0) position: Int,
                                            parentPosition: Int,
                                            isExpandedChild: Boolean = false,
                                            isCollapseChild: Boolean = true,
                                            animate: Boolean = true,
                                            notify: Boolean = true) {
        val count = expand(position, isExpandedChild, animate, notify)

        val firstPosition: Int
//        var dataSize: Int
        if (parentPosition == -1) {
            firstPosition = 0
        } else {
            firstPosition = parentPosition + 1
        }

        // 当前 position 之前有 node 收起以后，position的位置就会变化
        var newPosition = position


        val bSize = position - firstPosition
        //如果此 position 之前有 node
        if (bSize > 0) {
            // TODO
            // 在此之前的 node 总数
            val beforeAllSize = position
            // 记录被折叠了 node 的总数
            var collapseSize = 0
            var i = 0
            // 循环添加： 总数 - 折叠后的总数即为此前还剩余的 node 总数。
            do {
                collapseSize += collapse(i, isCollapseChild, animate, notify)
                i++
                println("-------->> $beforeAllSize  i: $i   size: $collapseSize")
            } while (i < (beforeAllSize - collapseSize))

            newPosition = position - collapseSize
        }

        //如果此 position 之后有 node
        println("22-------->> $newPosition   i: ${(newPosition + count + 1)}   size: ${data.size}")
        if ((newPosition + count + 1) < data.size) {
            var i = newPosition + count + 1
            while (i < (data.size)) {
                println("11-------->>   i: $i   size: ${data.size}")
                collapse(i, isCollapseChild, animate, notify)
                i++
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