package com.chad.baserecyclerviewadapterhelper.activity.itemclick

import android.os.Bundle
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.baserecyclerviewadapterhelper.R
import com.chad.baserecyclerviewadapterhelper.activity.itemclick.adapter.ItemClickAdapter
import com.chad.baserecyclerviewadapterhelper.base.BaseViewBindingActivity
import com.chad.baserecyclerviewadapterhelper.databinding.ActivityUniversalRecyclerBinding
import com.chad.baserecyclerviewadapterhelper.entity.ClickEntity
import com.chad.baserecyclerviewadapterhelper.utils.Tips
import com.chad.library.adapter4.util.addOnDebouncedChildClick
import com.chad.library.adapter4.util.setOnDebouncedItemClick

/**
 * @author Allen
 */
class ItemClickActivity : BaseViewBindingActivity<ActivityUniversalRecyclerBinding>() {

    private val adapter: ItemClickAdapter by lazy(LazyThreadSafetyMode.NONE) {
        // 创建数据
        val data = ArrayList<ClickEntity>().apply {
            add(ClickEntity(ClickEntity.CLICK_ITEM_VIEW))
            add(ClickEntity(ClickEntity.CLICK_ITEM_CHILD_VIEW))
            add(ClickEntity(ClickEntity.LONG_CLICK_ITEM_VIEW))
            add(ClickEntity(ClickEntity.LONG_CLICK_ITEM_CHILD_VIEW))
            add(ClickEntity(ClickEntity.LONG_CLICK_ITEM_CHILD_VIEW))
            add(ClickEntity(ClickEntity.LONG_CLICK_ITEM_CHILD_VIEW))
            add(ClickEntity(ClickEntity.LONG_CLICK_ITEM_CHILD_VIEW))
            add(ClickEntity(ClickEntity.LONG_CLICK_ITEM_CHILD_VIEW))
            add(ClickEntity(ClickEntity.LONG_CLICK_ITEM_CHILD_VIEW))
            add(ClickEntity(ClickEntity.LONG_CLICK_ITEM_CHILD_VIEW))
            add(ClickEntity(ClickEntity.LONG_CLICK_ITEM_CHILD_VIEW))
            add(ClickEntity(ClickEntity.LONG_CLICK_ITEM_CHILD_VIEW))
        }
        // 创建Adapter
        ItemClickAdapter(data)
    }

    override fun initBinding(): ActivityUniversalRecyclerBinding =
        ActivityUniversalRecyclerBinding.inflate(layoutInflater)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ViewCompat.setOnApplyWindowInsetsListener(viewBinding.root) { view, insets ->
            val bar = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            viewBinding.titleBar.updateFakeBarHeight(bar.top)
            insets
        }

        viewBinding.titleBar.title = "Item Click Use"
        viewBinding.titleBar.setOnBackListener { finish() }
        viewBinding.rv.layoutManager = LinearLayoutManager(this)
        viewBinding.rv.adapter = adapter

        // 设置点击事件
        adapter.setOnItemClickListener { _, _, position ->
            Tips.show("onItemClick $position")
        }
        // 去除点击抖动的扩展方法
        adapter.setOnDebouncedItemClick {adapter, view, position ->
            Tips.show("onItemClick $position")
        }

        // 设置item 长按事件
        adapter.setOnItemLongClickListener { _, _, position ->
            Tips.show("onItemLongClick $position")
            true
        }

        // 添加子 view 的点击事件
        adapter.addOnItemChildClickListener(R.id.btn) { adapter, view, position ->
            Tips.show("onItemChildClick: $position")
        }
        adapter.addOnItemChildClickListener(R.id.iv_num_reduce) { adapter, view, position ->
            Tips.show("onItemChildClick:  reduce $position")
        }
        adapter.addOnItemChildClickListener(R.id.iv_num_add) { adapter, view, position ->
            Tips.show("onItemChildClick:  add $position")
            adapter.removeAt(position)
        }

        // 添加子 view 的点击事件(去除点击抖动的扩展方法)
        adapter.addOnDebouncedChildClick(R.id.btn) { adapter, view, position ->
            Tips.show("onItemChildClick: $position")
        }

        // 设置子 view 长按事件
        adapter.addOnItemChildLongClickListener(R.id.btn_long) { adapter, view, position ->
            Tips.show("onItemChildLongClick $position")
            true
        }
    }

}