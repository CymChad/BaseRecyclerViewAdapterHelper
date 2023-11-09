package com.chad.baserecyclerviewadapterhelper.activity.treenode

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.baserecyclerviewadapterhelper.activity.home.adapter.HomeTopHeaderAdapter
import com.chad.baserecyclerviewadapterhelper.activity.treenode.adapter.TreeNodeAdapter
import com.chad.baserecyclerviewadapterhelper.base.BaseViewBindingActivity
import com.chad.baserecyclerviewadapterhelper.databinding.ActivityUniversalRecyclerBinding
import com.chad.baserecyclerviewadapterhelper.entity.MyNodeEntity
import com.chad.baserecyclerviewadapterhelper.entity.MyNodeLoadingEntity
import com.chad.library.adapter.base.BaseNode
import com.chad.library.adapter.base.QuickAdapterHelper
import com.chad.library.adapter.base.dragswipe.QuickDragAndSwipe

/**
 * 树形结构演示Adapter
 *
 * @author Dboy233
 */
class TreeNodeActivity : BaseViewBindingActivity<ActivityUniversalRecyclerBinding>() {

//    val TAG by lazy { localClassName }

    private lateinit var helper: QuickAdapterHelper

    val mAdapter = TreeNodeAdapter()

    val loadingNode = MyNodeLoadingEntity()

    private val quickDragAndSwipe = QuickDragAndSwipe()
        .setSwipeMoveFlags(ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)

    override fun initBinding(): ActivityUniversalRecyclerBinding {
        return ActivityUniversalRecyclerBinding.inflate(
            layoutInflater
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding.titleBar.title = "Tree Node"
        viewBinding.titleBar.setOnBackListener { v: View? -> finish() }

        helper = QuickAdapterHelper.Builder(mAdapter)
            .build()
            .addBeforeAdapter(HomeTopHeaderAdapter())

        viewBinding.rv.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        viewBinding.rv.adapter = helper.adapter

        mAdapter.setOnItemClickListener { adapter, view, position ->
            val item = mAdapter.getItem(position)
            if (item?.childNodes != null && item.childNodes!!.isEmpty()) {
                addLoadingNode(item)
            }
            //一层层展开
            mAdapter.switchState(position, 1)
            //全部子节点展开
            //mAdapter.switchState(position)
        }

        mAdapter.setOnItemLongClickListener { adapter, view, position ->
            val item = mAdapter.getItem(position) ?: return@setOnItemLongClickListener false
            mAdapter.addNode(item.parentNode!!, getChildNode(item), getChildNodeIndex(item), 1)
            true
        }

        quickDragAndSwipe.attachToRecyclerView(viewBinding.rv)
            .setDataCallback(mAdapter)

        mAdapter.addNodes(getList(), 0)

        Toast.makeText(this, "长按添加Item,Long press to add Item", Toast.LENGTH_SHORT).show()
    }

    /**
     * 添加更多节点
     */
    private fun addLoadingNode(parentNode: BaseNode) {
        ///添加loading节点
        mAdapter.addNode(parentNode, loadingNode)
        ///模拟异步操作
        viewBinding.root.postDelayed({
            ///移除Loading节点
            mAdapter.remove(loadingNode)
            ///添加新的节点
            mAdapter.addNodes(parentNode,moreNode())
        }, 2000)
    }

    ///获取更多节点
    private fun moreNode(): List<BaseNode> {
        return mutableListOf(
            MyNodeEntity("new node"),
            MyNodeEntity("load more node", mutableListOf()),
            MyNodeEntity("new node")
        )
    }


    private fun getChildNodeIndex(item: BaseNode): Int {
        return mAdapter.getChildNodeIndex(item) + 1
    }

    private fun getChildNode(item: BaseNode): BaseNode {
        return when (item.nodeType) {
            TreeNodeAdapter.TYPE_FILE -> {
                MyNodeEntity(
                    "new file node"
                )
            }

            else -> {
                MyNodeEntity(
                    "new folder node",
                    mutableListOf(
                        MyNodeEntity(
                            "new new folder", mutableListOf(
                                MyNodeEntity(
                                    "new file node"
                                )
                            )
                        )
                    )
                )
            }
        }
    }

    /**
     * 所有节点用同一个实体类
     */
    private fun getList(): List<MyNodeEntity> {
        return listOf(
            MyNodeEntity(
                "My Folders", mutableListOf(
                    MyNodeEntity(
                        "My Pictures folder", mutableListOf(
                            MyNodeEntity("Belle.png"),
                            MyNodeEntity("Handsome_guy.png"),
                            MyNodeEntity(
                                "Private picture folders", mutableListOf(
                                    MyNodeEntity("凤姐.png")
                                )
                            )
                        )
                    ),
                    MyNodeEntity(
                        "Work Folders", mutableListOf(
                            MyNodeEntity("life_summary.txt")
                        )
                    )
                )
            ),
            MyNodeEntity(
                "Her folder", mutableListOf(
                    MyNodeEntity(
                        "Her picture folder", mutableListOf(
                            MyNodeEntity(
                                "Private picture folders", mutableListOf(
                                    MyNodeEntity("Swimsuit.png")
                                )
                            ),
                            MyNodeEntity("Mountaintop.png"),
                            MyNodeEntity("Park.png"),
                        )
                    ),
                    MyNodeEntity(
                        "Her working folder", mutableListOf(
                            MyNodeEntity("how_to_work_easily.txt")
                        )
                    )
                )
            ),
            MyNodeEntity("load more node", mutableListOf())
        )
    }

}