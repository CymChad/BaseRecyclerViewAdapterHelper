package com.chad.baserecyclerviewadapterhelper.activity.headerfooter

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.baserecyclerviewadapterhelper.activity.headerfooter.adapter.FooterAdapter
import com.chad.baserecyclerviewadapterhelper.activity.headerfooter.adapter.HeaderAdapter
import com.chad.baserecyclerviewadapterhelper.activity.headerfooter.adapter.HeaderAndFooterAdapter
import com.chad.baserecyclerviewadapterhelper.base.BaseViewBindingActivity
import com.chad.baserecyclerviewadapterhelper.data.DataServer
import com.chad.baserecyclerviewadapterhelper.databinding.ActivityUniversalRecyclerBinding
import com.chad.baserecyclerviewadapterhelper.utils.Tips
import com.chad.library.adapter.base.QuickAdapterHelper

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
class HeaderAndFooterUseActivity : BaseViewBindingActivity<ActivityUniversalRecyclerBinding>() {

    private lateinit var helper: QuickAdapterHelper

    override fun initBinding(): ActivityUniversalRecyclerBinding =
        ActivityUniversalRecyclerBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding.titleBar.title = "Header And Footer Use"
        viewBinding.titleBar.setOnBackListener { finish() }


        val adapter = HeaderAndFooterAdapter(DataServer.getSampleData(PAGE_SIZE))
        adapter.animationEnable = true
        adapter.setOnItemClickListener { _, _, position ->
            Tips.show("position: $position")
        }


        helper = QuickAdapterHelper.Builder(adapter)
            .build()

        viewBinding.rv.layoutManager = LinearLayoutManager(this)
        viewBinding.rv.adapter = helper.adapter
        addHeader()

        helper.addFooter(
            FooterAdapter(false).setOnItemClickListener { _, _, _ ->
                addFooter()
            }
        )
    }

    private fun addHeader() {
        helper.addHeader(HeaderAdapter().apply {
            setOnItemClickListener { _, _, _ ->
                addHeader()
            }
        })
    }

    private fun addFooter() {
        helper.addFooter(FooterAdapter(true).setOnItemClickListener{ adapter, _, _ ->
            helper.removeAdapter(adapter)
        })
    }

    companion object {
        private const val PAGE_SIZE = 3
    }
}