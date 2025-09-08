package com.chad.baserecyclerviewadapterhelper.activity.headerfooter

import android.os.Bundle
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.baserecyclerviewadapterhelper.activity.headerfooter.adapter.FooterAdapter
import com.chad.baserecyclerviewadapterhelper.activity.headerfooter.adapter.HeaderAdapter
import com.chad.baserecyclerviewadapterhelper.activity.headerfooter.adapter.HeaderAndFooterAdapter
import com.chad.baserecyclerviewadapterhelper.base.BaseViewBindingActivity
import com.chad.baserecyclerviewadapterhelper.data.DataServer
import com.chad.baserecyclerviewadapterhelper.databinding.ActivityUniversalRecyclerBinding
import com.chad.baserecyclerviewadapterhelper.utils.Tips
import com.chad.library.adapter4.QuickAdapterHelper

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
class HeaderAndFooterUseActivity : BaseViewBindingActivity<ActivityUniversalRecyclerBinding>() {

    private lateinit var helper: QuickAdapterHelper

    override fun initBinding(): ActivityUniversalRecyclerBinding =
        ActivityUniversalRecyclerBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ViewCompat.setOnApplyWindowInsetsListener(viewBinding.titleBar) { view, insets ->
            val bar = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updatePadding(top = bar.top)
            insets
        }

        viewBinding.titleBar.title = "Header And Footer Use"
        viewBinding.titleBar.setOnBackListener { finish() }


        val adapter = HeaderAndFooterAdapter(DataServer.getSampleData(PAGE_SIZE))
        adapter.setOnItemClickListener { _, _, position ->
            Tips.show("position: $position")
        }


        helper = QuickAdapterHelper.Builder(adapter)
            .build()

        viewBinding.rv.layoutManager = LinearLayoutManager(this)
        viewBinding.rv.adapter = helper.adapter
        addHeader()

        helper.addAfterAdapter(
            FooterAdapter(false).setOnItemClickListener { _, _, _ ->
                addFooter()
            }
        )
    }

    private fun addHeader() {
        helper.addBeforeAdapter(0, HeaderAdapter().apply {
            setOnItemClickListener { _, _, _ ->
                addHeader()
            }
        })
    }

    private fun addFooter() {
        helper.addAfterAdapter(FooterAdapter(true).setOnItemClickListener{ adapter, _, _ ->
            helper.removeAdapter(adapter)
        })
    }

    companion object {
        private const val PAGE_SIZE = 3
    }
}