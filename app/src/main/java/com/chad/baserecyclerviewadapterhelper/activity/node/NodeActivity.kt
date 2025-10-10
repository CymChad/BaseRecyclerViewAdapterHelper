package com.chad.baserecyclerviewadapterhelper.activity.node

import android.os.Bundle
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.chad.baserecyclerviewadapterhelper.activity.node.adapter.NodeAdapter
import com.chad.baserecyclerviewadapterhelper.base.BaseViewBindingActivity
import com.chad.baserecyclerviewadapterhelper.data.DataServer
import com.chad.baserecyclerviewadapterhelper.databinding.ActivityNodeBinding

/**
 * @author LiMuYang
 * @date 2025/9/5
 * @description 多节点demo
 */
class NodeActivity : BaseViewBindingActivity<ActivityNodeBinding>() {

    private val adapter = NodeAdapter()

    override fun initBinding(): ActivityNodeBinding {
        return ActivityNodeBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ViewCompat.setOnApplyWindowInsetsListener(viewBinding.root) { view, insets ->
            val bar = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            viewBinding.titleBar.updateFakeBarHeight(bar.top)
            insets
        }

        viewBinding.titleBar.title = "Multi-node Use"
        viewBinding.titleBar.setOnBackListener { finish() }

        viewBinding.rv.adapter = adapter

        adapter.submitList(DataServer.getNodeData())


        viewBinding.btnCloseAll.setOnClickListener {
            adapter.closeAll()
        }
    }
}