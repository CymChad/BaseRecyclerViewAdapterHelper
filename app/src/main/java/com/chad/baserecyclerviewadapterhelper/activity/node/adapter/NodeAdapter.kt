package com.chad.baserecyclerviewadapterhelper.activity.node.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.chad.baserecyclerviewadapterhelper.databinding.ItemNodeLevel1Binding
import com.chad.baserecyclerviewadapterhelper.databinding.ItemNodeLevel2Binding
import com.chad.baserecyclerviewadapterhelper.databinding.ItemNodeLevel3Binding
import com.chad.baserecyclerviewadapterhelper.entity.NodeEntity
import com.chad.library.adapter4.BaseNodeAdapter

/**
 * @author LiMuYang
 * @date 2025/9/5
 * @description
 */
class NodeAdapter : BaseNodeAdapter<NodeAdapter>() {
    override fun getChildNodeList(position: Int, parent: Any): List<Any>? {
        when (parent) {
            is NodeEntity -> {
                // 一级
                return parent.childNode
            }

            is NodeEntity.Level2NodeEntity -> {
                // 二级
                return parent.childNode
            }

            is NodeEntity.Level2NodeEntity.Level3NodeEntity -> {
                // 三级
                return null
            }

            else -> return null
        }
    }

    override fun isInitialOpen(position: Int, parent: Any): Boolean {
        return false
    }

    override fun getItemViewType(position: Int, list: List<Any>): Int {
        val item = list[position]
        when (item) {
            is NodeEntity -> {
                // 一级
                return 1
            }

            is NodeEntity.Level2NodeEntity -> {
                // 二级
                return 2
            }

            is NodeEntity.Level2NodeEntity.Level3NodeEntity -> {
                // 三级
                return 3
            }

            else -> return 0
        }
    }

    override fun onCreateViewHolder(
        context: Context, parent: ViewGroup, viewType: Int,
    ): RecyclerView.ViewHolder {
        return when (viewType) {
            1 -> {
                Level1Hodler(parent).apply {
                    itemView.setOnClickListener {
                        openOrClose(bindingAdapterPosition)
                    }
                }
            }

            2 -> {
                Level2Hodler(parent).apply {
                    itemView.setOnClickListener {
                        openOrClose(bindingAdapterPosition)
                    }
                }
            }

            3 -> {
                Level3Hodler(parent).apply {
                    itemView.setOnClickListener {
                        Toast.makeText(
                            it.context,
                            "Level 3 _ index $bindingAdapterPosition",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            else -> {
                Level1Hodler(parent)
            }
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder, position: Int, item: Any?,
    ) {
        when (holder) {
            is Level1Hodler -> {
                // 一级
                val nodeEntity = item as NodeEntity

                holder.viewBinding.tvTitle.text = nodeEntity.title
            }

            is Level2Hodler -> {
                // 二级
                val nodeEntity = item as NodeEntity.Level2NodeEntity
                holder.viewBinding.tvTitle.text = nodeEntity.title
            }

            is Level3Hodler -> {
                // 三级
                val nodeEntity = item as NodeEntity.Level2NodeEntity.Level3NodeEntity
                holder.viewBinding.tvTitle.text = nodeEntity.title
            }
        }
    }
}


class Level1Hodler(
    parent: ViewGroup,
    val viewBinding: ItemNodeLevel1Binding = ItemNodeLevel1Binding.inflate(
        LayoutInflater.from(parent.context), parent, false
    ),
) : RecyclerView.ViewHolder(viewBinding.root)

class Level2Hodler(
    parent: ViewGroup,
    val viewBinding: ItemNodeLevel2Binding = ItemNodeLevel2Binding.inflate(
        LayoutInflater.from(parent.context), parent, false
    ),
) : RecyclerView.ViewHolder(viewBinding.root)

class Level3Hodler(
    parent: ViewGroup,
    val viewBinding: ItemNodeLevel3Binding = ItemNodeLevel3Binding.inflate(
        LayoutInflater.from(parent.context), parent, false
    ),
) : RecyclerView.ViewHolder(viewBinding.root)