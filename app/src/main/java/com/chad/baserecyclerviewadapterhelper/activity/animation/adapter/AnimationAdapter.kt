package com.chad.baserecyclerviewadapterhelper.activity.animation.adapter

import android.content.Context
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.text.buildSpannedString
import androidx.core.text.inSpans
import com.chad.baserecyclerviewadapterhelper.R
import com.chad.baserecyclerviewadapterhelper.data.DataServer
import com.chad.baserecyclerviewadapterhelper.entity.Status
import com.chad.baserecyclerviewadapterhelper.utils.ClickableMovementMethod
import com.chad.baserecyclerviewadapterhelper.utils.Tips
import com.chad.library.adapter4.BaseQuickAdapter
import com.chad.library.adapter4.viewholder.QuickViewHolder

/**
 * 文 件 名: AnimationAdapter
 * 创 建 人: Allen
 * 创建日期: 16/12/24 15:33
 * 邮   箱: AllenCoder@126.com
 * 修改时间：
 * 修改备注：
 */
class AnimationAdapter :
    BaseQuickAdapter<Status, QuickViewHolder>(DataServer.getSampleData(100)) {
    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): QuickViewHolder {
        return QuickViewHolder(R.layout.layout_animation, parent)
    }

    override fun onBindViewHolder(holder: QuickViewHolder, position: Int, item: Status?) {
        when (holder.layoutPosition % 3) {
            0 -> holder.setImageResource(R.id.img, R.mipmap.animation_img1)
            1 -> holder.setImageResource(R.id.img, R.mipmap.animation_img2)
            2 -> holder.setImageResource(R.id.img, R.mipmap.animation_img3)
            else -> {}
        }
        holder.setText(R.id.tweetName, "Hoteis in Rio de Janeiro")
        val msg =
            "\"He was one of Australia's most of distinguished artistes, renowned for his portraits\""



        holder.getView<TextView>(R.id.tweetText).text = buildSpannedString {
            append(msg)
            inSpans(clickableSpan) {
                append("landscapes and nedes")
            }
        }
        holder.getView<TextView>(R.id.tweetText).movementMethod = ClickableMovementMethod.getInstance()
        holder.getView<TextView>(R.id.tweetText).isFocusable = false
        holder.getView<TextView>(R.id.tweetText).isClickable = false
        holder.getView<TextView>(R.id.tweetText).isLongClickable = false
    }

    private val clickableSpan: ClickableSpan = object : ClickableSpan() {
        override fun onClick(widget: View) {
            Tips.show("事件触发了 landscapes and nedes")
        }

        override fun updateDrawState(ds: TextPaint) {
            ds.color = ContextCompat.getColor(context, R.color.clickspan_color)
            ds.isUnderlineText = true
        }
    }
}