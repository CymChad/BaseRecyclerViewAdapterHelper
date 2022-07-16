package com.chad.library.adapter.base.viewholder

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

/**
 * ViewHolder 基类
 */
open class QuickViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    constructor(
        @LayoutRes resId: Int, parent: ViewGroup
    ) : this(LayoutInflater.from(parent.context).inflate(resId, parent, false))

    /**
     * Views indexed with their IDs
     */
    private val views: SparseArray<View> = SparseArray()

    open fun <T : View> getView(@IdRes viewId: Int): T {
        val view = getViewOrNull<T>(viewId)
        checkNotNull(view) { "No view found with id $viewId" }
        return view
    }

    @Suppress("UNCHECKED_CAST")
    open fun <T : View> getViewOrNull(@IdRes viewId: Int): T? {
        val view = views.get(viewId)
        if (view == null) {
            itemView.findViewById<T>(viewId)?.let {
                views.put(viewId, it)
                return it
            }
        }
        return view as? T
    }

    fun <T : View> Int.findView(): T? {
        return itemView.findViewById(this)
    }

    open fun setText(@IdRes viewId: Int, value: CharSequence?): QuickViewHolder = apply {
        getView<TextView>(viewId).text = value
    }

    open fun setText(@IdRes viewId: Int, @StringRes strId: Int): QuickViewHolder? = apply {
        getView<TextView>(viewId).setText(strId)
    }

    open fun setTextColor(@IdRes viewId: Int, @ColorInt color: Int): QuickViewHolder = apply {
        getView<TextView>(viewId).setTextColor(color)
    }

    open fun setTextColorRes(@IdRes viewId: Int, @ColorRes colorRes: Int): QuickViewHolder = apply {
        getView<TextView>(viewId).setTextColor(ContextCompat.getColor(itemView.context, colorRes))
    }

    fun setImageResource(
        @IdRes viewId: Int, @DrawableRes imageResId: Int
    ): QuickViewHolder = apply {
        getView<ImageView>(viewId).setImageResource(imageResId)
    }

    fun setImageDrawable(@IdRes viewId: Int, drawable: Drawable?): QuickViewHolder = apply {
        getView<ImageView>(viewId).setImageDrawable(drawable)
    }

    fun setImageBitmap(@IdRes viewId: Int, bitmap: Bitmap?): QuickViewHolder = apply {
        getView<ImageView>(viewId).setImageBitmap(bitmap)
    }

    fun setBackgroundColor(@IdRes viewId: Int, @ColorInt color: Int): QuickViewHolder = apply {
        getView<View>(viewId).setBackgroundColor(color)
    }

    fun setBackgroundResource(
        @IdRes viewId: Int,
        @DrawableRes backgroundRes: Int
    ): QuickViewHolder = apply {
        getView<View>(viewId).setBackgroundResource(backgroundRes)
    }

    fun setVisible(@IdRes viewId: Int, isVisible: Boolean): QuickViewHolder = apply {
        val view = getView<View>(viewId)
        view.visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
    }

    fun setGone(@IdRes viewId: Int, isGone: Boolean): QuickViewHolder = apply {
        val view = getView<View>(viewId)
        view.visibility = if (isGone) View.GONE else View.VISIBLE
    }

    fun setEnabled(@IdRes viewId: Int, isEnabled: Boolean): QuickViewHolder = apply {
        getView<View>(viewId).isEnabled = isEnabled
    }


}

