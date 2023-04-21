package com.chad.library.adapter.base


/**
 * 树形结构数据适配器。
 * 功能实现:
 *  1. 展开[expand] 可指定展开列表的深度
 *  2. 折叠[collapse] 折叠对应位置的ChildNode
 *  3. 自动展开和折叠[switchState]
 *  4. 添加[addNode],[addNodes] [add**]都可使用
 *  5. 删除[remove],[removeAt] 如果删除的node有child,它的child也将被移除。两种remove中默认实现逻辑
 *  6. 获取节点所在的层级[getNodeDepth]
 *  7. 获取节点所在的父节点的的相对位置[getChildNodeIndex]
 *
 *  使用方式和[BaseMultiItemAdapter]一致。
 *
 *  要注意的是，目前每个不同类型的节点，ViewHolder不能相同
 *
 * @author Dboy233
 */
abstract class BaseTreeNodeAdapter : BaseMultiItemAdapter<BaseNode>() {

    /**
     * 真实的根节点数据
     */
    private data class RootNode(
        override val nodeType: Int = -1,
        override val childNodes: MutableList<BaseNode> = mutableListOf()
    ) : BaseNode()

    /**
     * 根节点。数据必须有始有终。这里就是始。这个对象不会添加到[items]集合中，也不会展示。
     */
    private val rootNode: BaseNode = RootNode()


    init {
        //已经实现此方法
        onItemViewType { position, list ->
            list[position].nodeType
        }
    }


    /**
     * 添加节点
     * [node] 要添加的节点
     * [expandDepth] 如果你新添加的节点存在多个嵌套的子节点，设置展开节点的深度， <= 0 全部不展开
     */
    @JvmOverloads
    fun addNode(node: BaseNode, expandDepth: Int = Int.MAX_VALUE) {
        val newNodes = depthNodes(node, expandDepth, object : DepthFindCallBack {
            override fun accept(deep: Int, node: BaseNode) {
                //当前的深度必须大于0。因为等于0属于最后一层。最后一层没有展开的资格。
                //当上一个条件成立，
                //那么他必须有子节点才能展开。否则不允许。
                node.isExpand = deep > 0 && !node.childNodes.isNullOrEmpty()
            }
        })

        rootNode.childNodes?.add(node)
        node.parentNode = rootNode

        super.addAll(newNodes)
    }

    /**
     * 添加节点
     * [parentNode] 添加node的父node。
     * [childNode] 要添加的节点
     * [childIndex] 添加的node所在的位置,默认最后位置添加
     * [expandDepth] 如果你新添加的节点存在多个嵌套的子节点，设置展开节点的深度， <= 0 全部不展开
     */
    @JvmOverloads
    fun addNode(
        parentNode: BaseNode,
        childNode: BaseNode,
        childIndex: Int = Int.MAX_VALUE,
        expandDepth: Int = Int.MAX_VALUE
    ) {
        childNode.parentNode = parentNode
        parentNode.childNodes?.let {
            //如果下标大于列表个数,就末尾添加
            val index = if (childIndex > it.size) it.size else childIndex
            it.add(index, childNode)
            //计算node在adapter中的位置
            val parentIndex = items.indexOf(parentNode)

            //修正node在adapter中的位置
            //加入插入位置之前有已经展开的Node需要加上所有展开的node，如果没有展开的Node，那么这个返回的fixSize其实等于childIndex的值。
            //其实这个nodeAlreadyExpandSize就是查找在adapter中相对parent的位置
            val fixSize = nodeAlreadyExpandSize(parentNode, childNode)

            val newIndex = parentIndex + 1 + fixSize

            val depthNodes =
                depthNodes(childNode, expandDepth, onFind = object : DepthFindCallBack {
                    override fun accept(deep: Int, node: BaseNode) {
                        node.isExpand = deep > 0 && !node.childNodes.isNullOrEmpty()
                    }
                })

            //只有当父节点是展开状态的时候添加新的节点
            if (parentNode.isExpand) {
                depthNodes.reversed().forEach { newNode ->
                    super.add(newIndex, newNode)
                }
            }
        }
    }

    /**
     * 添加节点列表
     * - [data] 需要添加的所有子节点。初始化数据的时候可以使用
     * - [expandDepth] 如果你新添加的节点存在多个嵌套的子节点，设置展开节点的深度， <= 0 全部不展开
     */
    fun addNodes(data: List<BaseNode>, expandDepth: Int = Int.MAX_VALUE) {
        val newData = mutableListOf<BaseNode>()
        for (itemEntry in data) {
            //将新节点添加到根节点
            rootNode.childNodes?.add(itemEntry)
            itemEntry.parentNode = rootNode

            newData.addAll(depthNodes(itemEntry, expandDepth, onFind = object : DepthFindCallBack {
                override fun accept(deep: Int, node: BaseNode) {
                    //当前的深度必须大于0。因为等于0属于最后一层。最后一层没有展开的资格。
                    //当上一个条件成立，
                    //那么他必须有子节点才能展开。否则不允许。
                    node.isExpand = deep > 0 && !node.childNodes.isNullOrEmpty()
                }
            }))
        }
        //添加新的数据
        super.addAll(newData)
    }

    /**
     * 设置新的集合
     */
    override fun submitList(list: List<BaseNode>?) {
        rootNode.childNodes?.clear()
        addNodes(list ?: mutableListOf())
    }

    /**
     * 在[rootNode]的子节点末尾添加
     */
    override fun add(data: BaseNode) {
        addNode(data)
    }

    /**
     * 在[rootNode]的子节点末尾添加多个
     */
    override fun addAll(newCollection: Collection<BaseNode>) {
        addNodes(newCollection.toMutableList())
    }

    /**
     * 在[rootNode]指定位置根节点下添加
     */
    override fun add(position: Int, data: BaseNode) {
        val childSize = rootNode.childNodes?.size ?: 0
        if (position > childSize) throw IndexOutOfBoundsException("root node child size ${childSize}, position $position")
        addNode(rootNode, data, position)
    }

    /**
     * 在[rootNode]指定位置根节点下添加多个
     */
    override fun addAll(position: Int, newCollection: Collection<BaseNode>) {
        val childSize = rootNode.childNodes?.size ?: 0
        if (position > childSize) throw IndexOutOfBoundsException("root node child size ${childSize}, position $position")

        newCollection.forEachIndexed { index, newNode ->
            addNode(rootNode, newNode, position + index)
        }

    }

    /**
     * 如果对于数据的删除有自己的处理逻辑,重写此方法.
     * 查看[removeNodeFromParentNode],[removeNodeChildren]的使用说明
     */
    override fun remove(data: BaseNode) {
        //将节点和它的父节点分离
        removeNodeFromParentNode(data)
        //如果要移除的Node已经展开并且有子节点，将子节点一并移除
        removeNodeChildren(data)
        //从adapter items 中移除
        super.remove(data)
    }


    override fun removeAt(position: Int) {
        //将节点和它的父节点分离
        val node = removeNodeFromParentNode(position) ?: return
        //如果要移除的Node已经展开并且有子节点，将子节点一并移除
        removeNodeChildren(node)
        //从adapter items 中移除
        super.removeAt(position)
    }


    /**
     * 获取当前节点的深度。
     * @return 1 = 为第一层 此节点位于[RootNode]下
     */
    fun getNodeDepth(node: BaseNode): Int {
        return internalGetNodeDepth(node)
    }

    /**
     * 获取node所在父节点下的位置
     * - [node] 需要获取位置的节点
     */
    fun getChildNodeIndex(node: BaseNode): Int {
        return node.parentNode?.childNodes?.indexOfFirst {
            it == node
        } ?: -1
    }

    /**
     * 切换
     */
    @JvmOverloads
    fun switchState(position: Int, depth: Int = Int.MAX_VALUE) {
        val item = getItem(position) ?: return
        if (item.isExpand) {
            collapse(position)
        } else {
            expand(position, depth)
        }
    }


    /**
     * 展开某个位置
     * - [position] 展开的位置，当前item包含多个子节点
     * - [depth] 展开是否包含子节点一起展开。true所有子节点都将展开，false只展开最近子节点
     */
    @JvmOverloads
    fun expand(position: Int, depth: Int = Int.MAX_VALUE) {
        val item = getItem(position) ?: return
        if (item.childNodes.isNullOrEmpty()) {
            return
        }
        if (item.isExpand) return
        //修改展开状态
        item.isExpand = true

        if (depth > 1) {
            item.childNodes?.reversed()?.forEachIndexed { _, node ->
                //对节点的父节点赋值。每次都要确保其不为null才行
                node.parentNode = item
                //查找子节点的深层节点并反转，因为添加位置是从当前position开始。
                //这里的depth-1是因为此时的node已经身处第一层了,向下查找层级就要排除自己这一层
                depthNodes(node, depth - 1, object : DepthFindCallBack {
                    override fun accept(deep: Int, node: BaseNode) {
                        //当前的深度必须大于0。因为等于0属于最后一层。最后一层没有展开的资格。
                        //当上一个条件成立，
                        //那么他必须有子节点才能展开。否则不允许。
                        node.isExpand = deep > 0 && !node.childNodes.isNullOrEmpty()
                    }
                }).reversed().forEach { deepNode ->
                    //将当前需要展开的节点位置添加
                    super.add(position + 1, deepNode)
                }
            }
        } else {
            //遍历最近子节点
            item.childNodes?.forEachIndexed { i, node ->
                //对节点的父节点赋值。每次都要确保其不为null才行
                node.parentNode = item
                //不展开子节点，子节点的展开状态为false
                node.isExpand = false
                //将当前节点添加
                super.add(position + i + 1, node)
            }
        }
        //当然自己也要刷新一下
        notifyItemChanged(position)
    }

    /**
     * 折叠某个文件夹
     * [position] 折叠的Node位置，这个位置是Adapter中的位置
     */
    fun collapse(position: Int) {
        val item = getItem(position) ?: return
        if (!item.isExpand) return
        item.isExpand = false
        if (item.childNodes.isNullOrEmpty()) {
            notifyItemChanged(position)
            return
        }
        //需要删除的item记录
        val needRemoveItem = mutableListOf<BaseNode>()
        //检查从当前位置折叠需要折叠几个item
        internalDetachedInspect(item, needRemoveItem)
        //倒着移除每一个item
        for (i in position + needRemoveItem.size downTo position + 1) {
            super.removeAt(i)
        }
        notifyItemChanged(position)
    }

    /**
     * 从父节点中移除节点
     * - [childNode]要移除的节点
     * - [isolated] 是否将移除的节点孤立，true 它的parentNode将被设置为null.从此成为孤儿，false 依旧保持联系，可以随时回到parent的怀抱
     */
    protected fun removeNodeFromParentNode(childNode: BaseNode, isolated: Boolean = false) {
        childNode.parentNode?.childNodes?.remove(childNode)
        if (isolated) {
            childNode.parentNode = null
        }
    }

    /**
     * 从父节点移除下标位置的节点
     * - [position] 列表数据的下表位置
     * - [isolated] 是否将移除的节点孤立，true 它的parentNode将被设置为null.从此成为孤儿，false 依旧保持联系，可以随时回到parent的怀抱
     */
    protected fun removeNodeFromParentNode(position: Int, isolated: Boolean = false): BaseNode? {
        if (position >= items.size) {
            throw IndexOutOfBoundsException("position: ${position}. size:${items.size}")
        }
        val node = getItem(position) ?: return null
        node.parentNode?.childNodes?.remove(node)
        if (isolated) {
            node.parentNode = null
        }
        return node
    }

    /**
     * 移除节点和节点包含的所有子节点
     * - [node] 要移除的节点
     * - [isolated] 移除它的子节点的时候是否将子节点也完全孤立，断开所有子节点和parentNode的联系。
     *
     * [isolated] = false 其所有子节点都不是真正的被移除只是在[items]中移除，
     * 如果你需要[node]的所有子节点不再展示，需要在外部调用`BaseNode.childNodes?.clear()`。
     * [isolated] = true 其所有树上的节点都将分离。
     */
    protected fun removeNodeChildren(node: BaseNode, isolated: Boolean = false) {
        if (!node.isExpand) {
            return
        }
        if (!node.childNodes.isNullOrEmpty()) {
            val needRemove = mutableListOf<BaseNode>()
            internalDetachedInspect(node, needRemove)
            needRemove.reversed().forEach { childNode ->
                super.remove(childNode)
            }
        }
        if (isolated) {
            depthNodes(node).forEach {
                it.parentNode = null
            }
            node.childNodes?.clear()
        }
    }

    /**
     * 检查当前节点深度，如果没有子节点，返回包含当前节点的list，反之返回包含所有子节点的list
     *
     * 将多级的链表转成单级的列表：
     * ```
     * -- root1
     *      |--child1-1
     *      |--child1-2
     * -- root2
     *      |--child2-1
     *
     * to
     *
     * -root1
     * -child1-1
     * -child1-2
     * -root2
     * -child2-1
     * -child2-2
     * ```
     * [node] 需要遍历深度节点的对象
     * [depth] 当到达指定深度的时候停止遍历
     * [onFind] deepNodes方法不会修改对象的任何属性，如果业务需要对节点属性修改，通过此方法，当添加一个节点的时候就会通知
     */
    private fun depthNodes(
        node: BaseNode,
        depth: Int = Int.MAX_VALUE,
        onFind: DepthFindCallBack? = null
    ): List<BaseNode> {
        return if (node.childNodes.isNullOrEmpty()) {
            onFind?.accept(depth, node)
            listOf(node)//添加自己
        } else {
            val list = mutableListOf<BaseNode>()
            onFind?.accept(depth, node)
            list.add(node)//先把自己添加到节点
            if (depth > 0) {
                //检查直接子节点 子节点已经增加了一个层级了
                node.childNodes?.forEach { childNode ->
                    //为子节点设置父节点
                    childNode.parentNode = node
                    if (childNode.childNodes.isNullOrEmpty()) {
                        //唯一子节点，添加，因为是Node的子节点，所以这里depth-1
                        onFind?.accept(depth - 1, childNode)
                        list.add(childNode)
                    } else {
                        //递归添加剩余子节点
                        list.addAll(depthNodes(childNode, depth - 1, onFind))
                    }
                }
            }
            list
        }
    }

    /**
     * 折叠检查,检查当前item下面有几个需要进行折叠/移除的item
     */
    private fun internalDetachedInspect(node: BaseNode, needRemove: MutableList<BaseNode>) {
        node.childNodes?.forEachIndexed { _, childNode ->
            //记录需要移除的item
            needRemove.add(childNode)
            //只有状态是展开的，并且子节点不为null的才进行再次检查
            if (childNode.isExpand && !childNode.childNodes.isNullOrEmpty()) {
                internalDetachedInspect(childNode, needRemove)
            }
            //当上方结束，修改它的状态
            childNode.isExpand = false
        }
    }

    /**
     *  检查 parentNode 到 stopNode为止一共有几个已经展开的Node
     */
    private fun nodeAlreadyExpandSize(parentNode: BaseNode, stopNode: BaseNode): Int {
        var size = 0
        parentNode.childNodes?.forEach {
            if (it == stopNode) return size
            if (it.isExpand && !it.childNodes.isNullOrEmpty()) {
                size++
                size += nodeAlreadyExpandSize(it, stopNode)
            } else {
                size++
            }
        }
        return size
    }


    /**
     * 获取当前节点深度
     */
    private fun internalGetNodeDepth(node: BaseNode): Int {
        return if (node.parentNode != null) {
            1 + internalGetNodeDepth(node.parentNode!!)
        } else {
            1
        }
    }

    /**
     * 检查数据的末端Node，没有ChildNode视为末端节点其展开状态将一直维持在false
     */
    private val checkTheEndBranches: DepthFindCallBack = object : DepthFindCallBack {
        override fun accept(deep: Int, node: BaseNode) {
            if (node.childNodes.isNullOrEmpty()) {
                node.isExpand = false
            }
        }
    }

    private interface DepthFindCallBack {
        /**
         * - [deep] 深度检测值，当deep=0的时候，不会再检查[node]的子节点信息
         * - [node] 当前深度的节点值
         * 这个接口用于方法[depthNodes]使用。当你传入的depth值为3，此方法[accept]的[deep]的数值变化将是 [3,2,1,0]
         * 为什么是倒着的，因为递归查询每个层级依次递减。当到0的时候结束。
         */
        fun accept(deep: Int, node: BaseNode)
    }

}