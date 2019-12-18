package com.chad.library.adapter.base.diff

import androidx.recyclerview.widget.ListUpdateCallback
import com.chad.library.adapter.base.BaseQuickAdapter

class BrvahListUpdateCallback(private val mAdapter: BaseQuickAdapter<*, *>) : ListUpdateCallback {

    override fun onInserted(position: Int, count: Int) {
        mAdapter.notifyItemRangeInserted(position + mAdapter.getHeaderLayoutCount(), count)
    }

    override fun onRemoved(position: Int, count: Int) {
        mAdapter.notifyItemRangeRemoved(position + mAdapter.getHeaderLayoutCount(), count)
    }

    override fun onMoved(fromPosition: Int, toPosition: Int) {
        mAdapter.notifyItemMoved(fromPosition + mAdapter.getHeaderLayoutCount(), toPosition + mAdapter.getHeaderLayoutCount())
    }

    override fun onChanged(position: Int, count: Int, payload: Any?) {
        mAdapter.notifyItemRangeChanged(position + mAdapter.getHeaderLayoutCount(), count, payload)
    }

}