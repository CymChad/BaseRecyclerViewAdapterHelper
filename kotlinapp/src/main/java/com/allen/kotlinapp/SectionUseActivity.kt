package com.allen.kotlinapp

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Toast
import com.allen.kotlinapp.adapter.SectionAdapter
import com.allen.kotlinapp.base.BaseActivity
import com.allen.kotlinapp.data.DataServer
import com.allen.kotlinapp.entity.MySection
import com.chad.library.adapter.base.BaseQuickAdapter

/**
 * 文 件 名: SectionUseActivity
 * 创 建 人: Allen
 * 创建日期: 2017/6/20 11:24
 * 修改时间：
 * 修改备注：
 */
class SectionUseActivity : BaseActivity() {
    private var mRecyclerView: RecyclerView? = null
    private var mData: List<MySection>? = null

    protected override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_section_uer)
        setBackBtn()
        setTitle("Section Use")
        mRecyclerView = findViewById(R.id.rv_list) as RecyclerView
        mRecyclerView!!.layoutManager = GridLayoutManager(this, 2)
        mData = DataServer.getSampleData()
        val sectionAdapter = SectionAdapter(R.layout.item_section_content, R.layout.def_section_head, mData!!)

        sectionAdapter.setOnItemClickListener(BaseQuickAdapter.OnItemClickListener { adapter, view, position ->
            val mySection = mData!![position]
            if (mySection.isHeader)
                Toast.makeText(this@SectionUseActivity, mySection.header, Toast.LENGTH_LONG).show()
            else
                Toast.makeText(this@SectionUseActivity, mySection.t.name, Toast.LENGTH_LONG).show()
        })
        sectionAdapter.setOnItemChildClickListener(BaseQuickAdapter.OnItemChildClickListener { adapter, view, position -> Toast.makeText(this@SectionUseActivity, "onItemChildClick" + position, Toast.LENGTH_LONG).show() })
        mRecyclerView!!.adapter = sectionAdapter
    }


}
