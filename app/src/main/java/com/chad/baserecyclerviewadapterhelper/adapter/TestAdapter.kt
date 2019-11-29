package com.chad.baserecyclerviewadapterhelper.adapter

import com.chad.baserecyclerviewadapterhelper.R
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.module.LoadMoreModule

/**
 * adapter
 */
class TestAdapter(list: MutableList<TestData>?) : BaseQuickAdapter<TestData, BaseViewHolder>(R.layout.layout_animation, list),LoadMoreModule {

    init {
        addItemChildClickViewIds(R.id.img)
    }

    override fun convert(helper: BaseViewHolder, item: TestData?) {
        helper.setText(R.id.tweetName, item?.title)
                .setText(R.id.tweetText, item?.content)
    }
}
