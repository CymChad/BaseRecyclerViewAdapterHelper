package com.chad.baserecyclerviewadapterhelper.base

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import com.chad.baserecyclerviewadapterhelper.R
import com.chad.baserecyclerviewadapterhelper.utils.statusBarLightMode

abstract class BaseActivity(@LayoutRes layoutRes: Int = 0) : AppCompatActivity(layoutRes) {


    protected open val contentView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        contentView?.let {
            setContentView(it)
        }





        window.statusBarColor = ContextCompat.getColor(this, R.color.spinner_bg)
        window.statusBarLightMode = false


    }


}
