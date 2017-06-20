package com.allen.kotlinapp.adapter

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Toast
import com.allen.kotlinapp.R
import com.allen.kotlinapp.entity.ClickEntity
import com.allen.kotlinapp.util.Utils
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.orhanobut.logger.Logger

/**
 * 文 件 名: ItemClickAdapter
 * 创 建 人: Allen
 * 创建日期: 2017/6/14 14:01
 * 修改时间：
 * 修改备注：
 */
class ItemClickAdapter(data: List<ClickEntity>) : BaseMultiItemQuickAdapter<ClickEntity, BaseViewHolder>(data), BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.OnItemChildClickListener {
    internal var nestAdapter: NestAdapter? = null

    init {
        addItemType(ClickEntity.CLICK_ITEM_VIEW, R.layout.item_click_view)
        addItemType(ClickEntity.CLICK_ITEM_CHILD_VIEW, R.layout.item_click_childview)
        addItemType(ClickEntity.LONG_CLICK_ITEM_VIEW, R.layout.item_long_click_view)
        addItemType(ClickEntity.LONG_CLICK_ITEM_CHILD_VIEW, R.layout.item_long_click_childview)
        addItemType(ClickEntity.NEST_CLICK_ITEM_CHILD_VIEW, R.layout.item_nest_click)

    }


    override fun convert(helper: BaseViewHolder, item: ClickEntity) {

        when (helper.itemViewType) {
            ClickEntity.CLICK_ITEM_VIEW -> {
                helper.addOnClickListener(R.id.btn)
            }
            ClickEntity.CLICK_ITEM_CHILD_VIEW -> {
                helper.addOnClickListener(R.id.iv_num_reduce).addOnClickListener(R.id.iv_num_add)
                        .addOnLongClickListener(R.id.iv_num_reduce).addOnLongClickListener(R.id.iv_num_add)
            }
            ClickEntity.LONG_CLICK_ITEM_VIEW -> {
                helper.addOnLongClickListener(R.id.btn)
            }
            ClickEntity.LONG_CLICK_ITEM_CHILD_VIEW -> {
                helper.addOnLongClickListener(R.id.iv_num_reduce).addOnLongClickListener(R.id.iv_num_add)
                        .addOnClickListener(R.id.iv_num_reduce).addOnClickListener(R.id.iv_num_add)
            }
            ClickEntity.NEST_CLICK_ITEM_CHILD_VIEW -> {
                helper.setNestView(R.id.item_click) // u can set nestview id
                val recyclerView = helper.getView<RecyclerView>(R.id.nest_list)
                recyclerView.layoutManager = LinearLayoutManager(helper.itemView.context, LinearLayoutManager.VERTICAL, false)
                recyclerView.setHasFixedSize(true)

                nestAdapter = NestAdapter()
                nestAdapter?.setOnItemClickListener(this)
                nestAdapter?.setOnItemChildClickListener(this)
                recyclerView.adapter = nestAdapter
            }
        }
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        Toast.makeText(Utils.getContext(), "childView click", Toast.LENGTH_SHORT).show()
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        Logger.d("嵌套RecycleView item 收到: 点击了第 $position 一次")
        Toast.makeText(Utils.getContext(), "嵌套RecycleView item 收到: 点击了第 $position 一次", Toast.LENGTH_SHORT).show()
    }
}
