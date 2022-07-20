package com.chad.library.adapter.base.layoutmanager

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.fullspan.FullSpanAdapterType

class BrvahGridLayoutManager : GridLayoutManager {

    constructor(
        context: Context, attrs: AttributeSet?, defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    constructor(context: Context, spanCount: Int) : super(context, spanCount)

    constructor(
        context: Context, spanCount: Int,
        @RecyclerView.Orientation orientation: Int, reverseLayout: Boolean
    ) : super(context, spanCount, orientation, reverseLayout)

    private var adapter: RecyclerView.Adapter<*>? = null

    init {
        spanSizeLookup = FullSpanSizeLookup()
    }

    override fun onAdapterChanged(
        oldAdapter: RecyclerView.Adapter<*>?, newAdapter: RecyclerView.Adapter<*>?
    ) {
        adapter = newAdapter
    }

    private inner class FullSpanSizeLookup : SpanSizeLookup() {

        override fun getSpanSize(position: Int): Int {
            val adapter = adapter ?: return 1

            if (adapter is ConcatAdapter) {
                val pair = adapter.getWrappedAdapterAndPosition(position)

                return when (val wrappedAdapter = pair.first) {
                    is FullSpanAdapterType -> {
                        spanCount
                    }
                    is BaseQuickAdapter<*, *> -> {
                        val type = wrappedAdapter.getItemViewType(pair.second)

                        if (wrappedAdapter.isFullSpanItem(type)) {
                            spanCount
                        } else {
                            1
                        }
                    }
                    else -> 1
                }
            } else {
                return when (adapter) {
                    is FullSpanAdapterType -> {
                        spanCount
                    }
                    is BaseQuickAdapter<*, *> -> {
                        val type = adapter.getItemViewType(position)

                        if (adapter.isFullSpanItem(type)) {
                            spanCount
                        } else {
                            1
                        }
                    }
                    else -> 1
                }
            }
        }


    }
}