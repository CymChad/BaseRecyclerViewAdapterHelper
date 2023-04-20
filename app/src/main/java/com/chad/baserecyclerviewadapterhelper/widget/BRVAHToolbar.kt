package com.chad.baserecyclerviewadapterhelper.widget

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import com.chad.baserecyclerviewadapterhelper.R
import com.chad.baserecyclerviewadapterhelper.databinding.LayoutToolBarBinding

class BRVAHToolbar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    private val binding = LayoutToolBarBinding.inflate(LayoutInflater.from(context), this)

    var title: String?
        get() = binding.tvTitle.text.toString()
        set(value) {
            binding.tvTitle.text = value
        }

    init {
        setBackgroundColor(ContextCompat.getColor(context, R.color.spinner_bg))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            elevation = context.dpF(10)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(
            widthMeasureSpec, MeasureSpec.makeMeasureSpec(context.dp(44), MeasureSpec.EXACTLY)
        )
    }

    fun setOnBackListener(listener: OnClickListener) {
        binding.ivBack.setOnClickListener(listener)
    }

    private companion object {
        fun Context.dp(value: Int): Int {
            return (value * this.resources.displayMetrics.density + 0.5f).toInt()
        }

        fun Context.dpF(value: Int): Float {
            return value * this.resources.displayMetrics.density + 0.5f
        }
    }

}