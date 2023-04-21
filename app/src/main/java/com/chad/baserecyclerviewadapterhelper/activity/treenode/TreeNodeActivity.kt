package com.chad.baserecyclerviewadapterhelper.activity.treenode

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.baserecyclerviewadapterhelper.activity.treenode.adapter.TreeNodeAdapter
import com.chad.baserecyclerviewadapterhelper.base.BaseViewBindingActivity
import com.chad.baserecyclerviewadapterhelper.databinding.ActivityUniversalRecyclerBinding
import com.chad.baserecyclerviewadapterhelper.entity.FileNodeEntity
import com.chad.baserecyclerviewadapterhelper.entity.FolderNodeEntity
import com.chad.baserecyclerviewadapterhelper.entity.MyNodeEntity
import com.chad.library.adapter.base.BaseNode
import com.chad.library.adapter.base.dragswipe.QuickDragAndSwipe

/**
 * 树形结构演示Adapter
 *
 * @author Dboy233
 */
class TreeNodeActivity : BaseViewBindingActivity<ActivityUniversalRecyclerBinding>() {

    val TAG by lazy { localClassName }

    val mAdapter = TreeNodeAdapter()

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

        viewBinding.rv.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        viewBinding.rv.adapter = mAdapter

        mAdapter.setOnItemClickListener { adapter, view, position ->
            Log.d(TAG, "click: $position")
//            val item:BaseNode? = mAdapter.getItem(position)

            mAdapter.switchState(position, 1)//一层层展开
//            mAdapter.switchState(position)//全部展开
        }

        mAdapter.setOnItemLongClickListener { adapter, view, position ->
            val item = mAdapter.getItem(position) ?: return@setOnItemLongClickListener false
            mAdapter.addNode(item.parentNode!!,  getChildNode(item), getChildNodeIndex(item),1)
            true
        }

        quickDragAndSwipe.attachToRecyclerView(viewBinding.rv)
            .setDataCallback(mAdapter)


//        mAdapter.addNodes(getListDiffClass())

        mAdapter.addNodes(getList(), 0)
//        mAdapter.addNodes(getList(),1)
//        mAdapter.addNodes(getList(),2)
//        mAdapter.addNodes(getList())
//        addNodeDepthTest()

        //必须现有  mAdapter.addNodes(getList()) 才能测这个
//        viewBinding.root.postDelayed({
//            addAllPositionTest()
//        },2000)


        Toast.makeText(this, "长按添加Item,Long press to add Item", Toast.LENGTH_SHORT).show()

        //addAll 测试
        viewBinding.root.postDelayed({
            Toast.makeText(this, "addAll", Toast.LENGTH_LONG).show()
        }, 2000)

    }


    fun addNodeDepthTest() {
        mAdapter.addNode(
            MyNodeEntity(
                "new Node expand depth 1", mutableListOf(
                    MyNodeEntity(
                        "expand test",
                        mutableListOf(MyNodeEntity("not show"))
                    ),
                    MyNodeEntity(
                        "addAll test",
                        mutableListOf(MyNodeEntity("not show empty child dir", mutableListOf()))
                    )
                )
            ), 1
        )
    }

    fun addAllPositionTest() {
        val list = listOf(
            MyNodeEntity(
                "addAll test",
                mutableListOf(MyNodeEntity("addAll test file"))
            ),
            MyNodeEntity(
                "addAll test",
                mutableListOf(MyNodeEntity("empty child dir", mutableListOf()))
            )

        )
        mAdapter.addAll(0, list)
//        mAdapter.addAll(1, list)
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
            )
        )
    }


    /**
     * 节点实体全部分开测试
     */
    private fun getListDiffClass(): List<BaseNode> {
        return listOf<BaseNode>(
            FolderNodeEntity(
                "我的", mutableListOf<BaseNode>(
                    FolderNodeEntity(
                        "我的图片文件夹", mutableListOf<BaseNode>(
                            FileNodeEntity(
                                "美女.png"
                            ),
                            FileNodeEntity(
                                "帅哥.png"
                            ),
                            FolderNodeEntity(
                                "私密图片文件夹", mutableListOf<BaseNode>(
                                    FileNodeEntity(
                                        "凤姐.png"
                                    )
                                )
                            )
                        )
                    ),
                    FolderNodeEntity(
                        "工作文件夹", mutableListOf<BaseNode>(
                            FileNodeEntity(
                                "人生总结.txt"
                            )
                        )
                    )
                )
            ),
            FolderNodeEntity(
                "她的", mutableListOf<BaseNode>(
                    FolderNodeEntity(
                        "她的图片文件夹", mutableListOf(
                            FolderNodeEntity(
                                "私密照", mutableListOf<BaseNode>(
                                    FileNodeEntity(
                                        "黑丝.png"
                                    )
                                )
                            ),
                            FileNodeEntity(
                                "山顶自拍.png"
                            ),
                            FileNodeEntity(
                                "公园自拍.png"
                            ),
                        )
                    ),
                    FolderNodeEntity(
                        "她的工作文件夹", mutableListOf<BaseNode>(
                            FileNodeEntity(
                                "如何轻松工作.txt"
                            )
                        )
                    )
                )
            )
        )
    }
}