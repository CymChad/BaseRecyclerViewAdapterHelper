package com.chad.library.adapter.base.layoutmanager

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.fullspan.FullSpanAdapterType

/**
 * grid layout manager.
 * Used to achieve full span. Adapter needs to implement [FullSpanAdapterType] interface
 *
 * 网格布局 GridLayoutManager，用于实现满跨度，Adapter 需要实现 [FullSpanAdapterType] 接口
 *
 */
class QuickGridLayoutManager : GridLayoutManager {

    constructor(
        context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    constructor(context: Context, spanCount: Int) : super(context, spanCount)

    constructor(
        context: Context, spanCount: Int,
        @RecyclerView.Orientation orientation: Int, reverseLayout: Boolean
    ) : super(context, spanCount, orientation, reverseLayout)

    private val fullSpanSizeLookup = FullSpanSizeLookup()

    private var adapter: RecyclerView.Adapter<*>? = null

    init {
        fullSpanSizeLookup.originalSpanSizeLookup = spanSizeLookup
        super.setSpanSizeLookup(fullSpanSizeLookup)
    }

    override fun onAdapterChanged(
        oldAdapter: RecyclerView.Adapter<*>?, newAdapter: RecyclerView.Adapter<*>?
    ) {
        adapter = newAdapter
    }

    override fun setSpanSizeLookup(spanSizeLookup: SpanSizeLookup?) {
        fullSpanSizeLookup.originalSpanSizeLookup = spanSizeLookup
    }

    /**
     * 处理全部跨度item的情况
     *
     * @constructor Create empty Full span size lookup
     */
    private inner class FullSpanSizeLookup : SpanSizeLookup() {

        var originalSpanSizeLookup: SpanSizeLookup? = null

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
                            originalSpanSizeLookup?.getSpanSize(pair.second) ?: 1
                        }
                    }
                    else -> originalSpanSizeLookup?.getSpanSize(pair.second) ?: 1
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
                            originalSpanSizeLookup?.getSpanSize(position) ?: 1
                        }
                    }
                    else -> originalSpanSizeLookup?.getSpanSize(position) ?: 1
                }
            }
        }
    }
}