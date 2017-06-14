package com.allen.kotlinapp.adapter

import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import com.allen.kotlinapp.R
import com.allen.kotlinapp.data.DataServer
import com.allen.kotlinapp.entity.Status
import com.allen.kotlinapp.util.SpannableStringUtils
import com.allen.kotlinapp.util.ToastUtils
import com.allen.kotlinapp.util.Utils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * 文 件 名: NestAdapter
 * 创 建 人: Allen
 * 创建日期: 2017/6/14 14:02
 * 修改时间：
 * 修改备注：
 */
class NestAdapter : BaseQuickAdapter<Status, BaseViewHolder>(R.layout.layout_nest_item, DataServer.getSampleData(20)) {

    override fun convert(helper: BaseViewHolder, item: Status) {
        helper.addOnClickListener(R.id.tweetText)
        when (helper.layoutPosition % 3) {
            0 -> helper.setImageResource(R.id.img, R.mipmap.animation_img1)
            1 -> helper.setImageResource(R.id.img, R.mipmap.animation_img2)
            2 -> helper.setImageResource(R.id.img, R.mipmap.animation_img3)
        }
        helper.setText(R.id.tweetName, "Hoteis in Rio de Janeiro")
        val msg = "\"He was one of Australia's most of distinguished artistes, renowned for his portraits\""
        (helper.getView<View>(R.id.tweetText) as TextView).setText(SpannableStringUtils.getBuilder(msg).append("landscapes and nedes").setClickSpan(clickableSpan).create())
        (helper.getView<View>(R.id.tweetText) as TextView).movementMethod = LinkMovementMethod.getInstance()
    }

    internal var clickableSpan: ClickableSpan = object : ClickableSpan() {
        override fun onClick(widget: View) {
            ToastUtils.showShortToast("事件触发了 landscapes and nedes")
        }

        override fun updateDrawState(ds: TextPaint) {
            ds.color = Utils.getContext().getResources().getColor(R.color.clickspan_color)
            ds.isUnderlineText = true
        }
    }
}
