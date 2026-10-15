package com.chad.library.adapter4

import android.app.Activity
import android.content.Context
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.loadState.LoadState
import com.chad.library.adapter4.loadState.leading.LeadingLoadStateAdapter
import com.chad.library.adapter4.loadState.trailing.TrailingLoadStateAdapter
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35])
class QuickAdapterHelperTest {
    @Test
    fun clearingAfterAdaptersRestoresContentPreload() {
        val content = TextAdapter(40)
        var loads = 0
        val helper = QuickAdapterHelper.Builder(content)
            .setTrailingLoadStateAdapter(object : TrailingLoadStateAdapter.OnTrailingListener {
                override fun onLoad() { loads++ }
                override fun onFailRetry() {}
            })
            .setTrailPreloadSize(2)
            .build()
        helper.addAfterAdapter(TextAdapter(1))
        helper.addAfterAdapter(TextAdapter(1))
        val controller = Robolectric.buildActivity(Activity::class.java).setup()
        try {
            val recycler = attach(controller.get(), helper)
            helper.clearAfterAdapters()
            helper.clearAfterAdapters()
            helper.trailingLoadState = LoadState.NotLoading(false)
            recycler.scrollToPosition(content.itemCount - 1)
            layout(recycler)
            assertEquals(1, loads)
            assertEquals(LoadState.Loading, helper.trailingLoadState)
            assertEquals(emptyList<BaseQuickAdapter<*, *>>(), helper.afterAdapterList)
        } finally {
            controller.pause().stop().destroy()
        }
    }

    @Test
    fun clearingBeforeAdaptersRestoresContentPreload() {
        val content = TextAdapter(40)
        var loads = 0
        val helper = QuickAdapterHelper.Builder(content)
            .setLeadingLoadStateAdapter(object : LeadingLoadStateAdapter.OnLeadingListener {
                override fun onLoad() { loads++ }
            })
            .setLeadPreloadSize(2)
            .build()
        helper.addBeforeAdapter(TextAdapter(1))
        helper.addBeforeAdapter(TextAdapter(1))
        val controller = Robolectric.buildActivity(Activity::class.java).setup()
        try {
            val recycler = attach(controller.get(), helper)
            recycler.scrollToPosition(helper.adapter.itemCount - 1)
            layout(recycler)
            helper.clearBeforeAdapters()
            helper.clearBeforeAdapters()
            helper.leadingLoadState = LoadState.NotLoading(false)
            recycler.scrollToPosition(0)
            layout(recycler)
            assertEquals(1, loads)
            assertEquals(LoadState.Loading, helper.leadingLoadState)
            assertEquals(emptyList<BaseQuickAdapter<*, *>>(), helper.beforeAdapterList)
        } finally {
            controller.pause().stop().destroy()
        }
    }

    private fun attach(activity: Activity, helper: QuickAdapterHelper): RecyclerView {
        val recycler = RecyclerView(activity).apply {
            layoutManager = LinearLayoutManager(activity)
            itemAnimator = null
            adapter = helper.adapter
        }
        activity.setContentView(recycler)
        layout(recycler)
        return recycler
    }

    private fun layout(recycler: RecyclerView) {
        recycler.measure(
            View.MeasureSpec.makeMeasureSpec(320, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(300, View.MeasureSpec.EXACTLY)
        )
        recycler.layout(0, 0, 320, 300)
        shadowOf(Looper.getMainLooper()).idle()
    }

    private class TextAdapter(count: Int) : BaseQuickAdapter<String, RecyclerView.ViewHolder>(
        List(count) { "row-$it" }
    ) {
        override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int) =
            object : RecyclerView.ViewHolder(TextView(context).apply {
                layoutParams = RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100)
            }) {}

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, item: String?) {
            (holder.itemView as TextView).text = item
        }
    }
}
