package com.chad.baserecyclerviewadapterhelper.activity.loadmore.adapter

import android.content.Context
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.text.buildSpannedString
import androidx.core.text.inSpans
import androidx.recyclerview.widget.RecyclerView
import com.chad.baserecyclerviewadapterhelper.R
import com.chad.baserecyclerviewadapterhelper.databinding.LayoutAnimationBinding
import com.chad.baserecyclerviewadapterhelper.entity.Status
import com.chad.baserecyclerviewadapterhelper.utils.Tips
import com.chad.library.adapter.base.BaseQuickAdapter

/**
 * @author: limuyang
 * @date: 2019-12-04
 * @Description:
 */
class RecyclerViewAdapter : BaseQuickAdapter<Status, RecyclerViewAdapter.VH>() {
    class VH(
        parent: ViewGroup,
        val viewBinding: LayoutAnimationBinding = LayoutAnimationBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    ) : RecyclerView.ViewHolder(viewBinding.root)

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): VH {
        return VH(parent)
    }

    protected override fun onBindViewHolder(holder: VH, position: Int, item: Status) {
        when (holder.layoutPosition % 3) {
            0 -> holder.viewBinding.img.setImageResource(R.mipmap.animation_img1)
            1 -> holder.viewBinding.img.setImageResource(R.mipmap.animation_img2)
            2 -> holder.viewBinding.img.setImageResource(R.mipmap.animation_img3)
            else -> {}
        }
        holder.viewBinding.tweetName.text =
            "Hoteis in Rio de Janeiro " + position + "  " + item!!.userName
        val msg =
            "\"He was one of Australia's most of distinguished artistes, renowned for his portraits\""
        holder.viewBinding.tweetText.text = buildSpannedString {
            append(msg)
            inSpans(clickableSpan) {
                append("landscapes and nedes")
            }
        }
        holder.viewBinding.tweetText.movementMethod = LinkMovementMethod.getInstance()
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