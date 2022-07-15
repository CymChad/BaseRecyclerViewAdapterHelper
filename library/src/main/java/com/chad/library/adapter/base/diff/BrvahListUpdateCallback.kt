package com.chad.library.adapter.base.diff

import androidx.recyclerview.widget.ListUpdateCallback
import com.chad.library.adapter.base.BaseQuickAdapter

class BrvahListUpdateCallback(private val mAdapter: BaseQuickAdapter<*, *>) : ListUpdateCallback {

    override fun onInserted(position: Int, count: Int) {
        mAdapter.notifyItemRangeInserted(position, count)
    }

    override fun onRemoved(position: Int, count: Int) {
        if (mAdapter.itemCount == 0) {
            // 如果注册了加载更多，并且当前itemCount为0，则需要加上loadMore所占用的一行
            mAdapter.notifyItemRangeRemoved(position, count + 1)
        } else {
            mAdapter.notifyItemRangeRemoved(position, count)
        }
    }

    override fun onMoved(fromPosition: Int, toPosition: Int) {
        mAdapter.notifyItemMoved(fromPosition, toPosition)
    }

    override fun onChanged(position: Int, count: Int, payload: Any?) {
        mAdapter.notifyItemRangeChanged(position, count, payload)
    }

}