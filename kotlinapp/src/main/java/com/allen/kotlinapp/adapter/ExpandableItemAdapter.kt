package com.allen.kotlinapp.adapter

import android.util.Log
import com.allen.kotlinapp.R
import com.allen.kotlinapp.entity.Level0Item
import com.allen.kotlinapp.entity.Level1Item
import com.allen.kotlinapp.entity.Person
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.entity.MultiItemEntity

/**
 * 文 件 名: ExpandableItemAdapter
 * 创 建 人: Allen
 * 创建日期: 2017/6/13 15:12
 * 修改时间：
 * 修改备注：
 */
class ExpandableItemAdapter
/**
 * Same as QuickAdapter#QuickAdapter(Context,int) but with
 * some initialization data.

 * @param data A new list is created out of this one to avoid mutable list
 */
(data: List<MultiItemEntity>) : BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder>(data) {

    init {
        addItemType(TYPE_LEVEL_0, R.layout.item_expandable_lv0)
        addItemType(TYPE_LEVEL_1, R.layout.item_expandable_lv1)
        addItemType(TYPE_PERSON, R.layout.item_expandable_lv2)
    }


    override fun convert(holder: BaseViewHolder, item: MultiItemEntity) {
        when (holder.itemViewType) {
            TYPE_LEVEL_0 -> {
                when (holder.layoutPosition % 3) {
                    0 -> holder.setImageResource(R.id.iv_head, R.mipmap.head_img0)
                    1 -> holder.setImageResource(R.id.iv_head, R.mipmap.head_img1)
                    2 -> holder.setImageResource(R.id.iv_head, R.mipmap.head_img2)
                }
                val lv0 = item as Level0Item
                holder.setText(R.id.title, lv0.title)
                        .setText(R.id.sub_title, lv0.subTitle)
                        .setImageResource(R.id.iv, if (lv0.isExpanded()) R.mipmap.arrow_b else R.mipmap.arrow_r)
                holder.itemView.setOnClickListener {
                    val pos = holder.adapterPosition
                    Log.d(TAG, "Level 0 item pos: " + pos)
                    if (lv0.isExpanded()) {
                        collapse(pos)
                    } else {
                        //                            if (pos % 3 == 0) {
                        //                                expandAll(pos, false);
                        //                            } else {
                        expand(pos)
                        //                            }
                    }
                }
            }
            TYPE_LEVEL_1 -> {
                val lv1 = item as Level1Item
                holder.setText(R.id.title, lv1.title)
                        .setText(R.id.sub_title, lv1.subTitle)
                        .setImageResource(R.id.iv, if (lv1.isExpanded()) R.mipmap.arrow_b else R.mipmap.arrow_r)
                holder.itemView.setOnClickListener {
                    val pos = holder.adapterPosition
                    Log.d(TAG, "Level 1 item pos: " + pos)
                    if (lv1.isExpanded()) {
                        collapse(pos, false)
                    } else {
                        expand(pos, false)
                    }
                }
            }
            TYPE_PERSON -> {
                val person = item as Person
                holder.setText(R.id.tv, person.name + " parent pos: " + getParentPosition(person))
                holder.itemView.setOnClickListener {
                    val cp = getParentPosition(person)
                    (data[cp] as Level1Item).removeSubItem(person)
                    data.removeAt(holder.layoutPosition)
                    notifyItemRemoved(holder.layoutPosition)
                }
            }
        }
    }

    companion object {
        private val TAG = ExpandableItemAdapter::class.java.simpleName

        val TYPE_LEVEL_0 = 0
        val TYPE_LEVEL_1 = 1
        val TYPE_PERSON = 2
    }
}
