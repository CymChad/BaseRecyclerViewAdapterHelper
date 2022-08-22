package com.chad.library.adapter.base.diff

import android.support.v7.util.ListUpdateCallback
import com.chad.library.adapter.base.BaseQuickAdapter

class BrvahListUpdateCallback(private val mAdapter: BaseQuickAdapter<*, *>) : ListUpdateCallback {

    override fun onInserted(position: Int, count: Int) {
        mAdapter.notifyItemRangeInserted(position + mAdapter.headerLayoutCount, count)
    }

    override fun onRemoved(position: Int, count: Int) {
        if (mAdapter.mLoadMoreModule?.hasLoadMoreView() == true && mAdapter.itemCount == 0) {
            // 如果注册了加载更多，并且当前itemCount为0，则需要加上loadMore所占用的一行
            mAdapter.notifyItemRangeRemoved(position + mAdapter.headerLayoutCount, count + 1)
        } else {
            mAdapter.notifyItemRangeRemoved(position + mAdapter.headerLayoutCount, count)
        }
    }

    override fun onMoved(fromPosition: Int, toPosition: Int) {
        mAdapter.notifyItemMoved(fromPosition + mAdapter.headerLayoutCount, toPosition + mAdapter.headerLayoutCount)
    }

    override fun onChanged(position: Int, count: Int, payload: Any?) {
        mAdapter.notifyItemRangeChanged(position + mAdapter.headerLayoutCount, count, payload)
    }

}