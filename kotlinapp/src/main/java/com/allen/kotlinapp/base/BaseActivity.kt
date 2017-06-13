package com.allen.kotlinapp.base

import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.chad.library.R
import com.orhanobut.logger.Logger

/**
 * 文 件 名: DataBindingUseActivity
 * 创 建 人: Allen
 * 创建日期: 2017/6/13 14:59
 * 修改时间：
 * 修改备注：
 */
class BaseActivity : AppCompatActivity() {
    /**
     * 日志输出标志getSupportActionBar().
     */
    private var title: TextView? = null
    private var back: ImageView? = null
    protected val TAG = this.javaClass.getSimpleName()

    protected fun setTitle(msg: String) {
        if (title != null) {
            title!!.text = msg
        }
    }

    /**
     * sometime you want to define back event
     */
    protected fun setBackBtn() {
        if (back != null) {
            back!!.visibility = View.VISIBLE
            back!!.setOnClickListener { finish() }
        } else {
            Logger.t(TAG).e("back is null ,please check out")
        }

    }

    protected fun setBackClickListener(l: View.OnClickListener) {
        if (back != null) {
            back!!.visibility = View.VISIBLE
            back!!.setOnClickListener(l)
        } else {
            Logger.t(TAG).e("back is null ,please check out")
        }

    }

    private var rootLayout: LinearLayout? = null

    protected override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 经测试在代码里直接声明透明状态栏更有效
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
        // 这句很关键，注意是调用父类的方法
        super.setContentView(R.layout.activity_base)
        initToolbar()
    }

    private fun initToolbar() {
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        if (toolbar != null) {
            setSupportActionBar(toolbar)
        }
        if (getSupportActionBar() != null) {
            // Enable the Up button
            getSupportActionBar()!!.setDisplayHomeAsUpEnabled(false)
            getSupportActionBar()!!.setDisplayShowTitleEnabled(false)

        }
        back = findViewById(R.id.img_back) as ImageView
        title = findViewById(R.id.title) as TextView
    }


    override fun setContentView(layoutId: Int) {
        setContentView(View.inflate(this, layoutId, null))
    }

    override fun setContentView(view: View) {
        rootLayout = findViewById(R.id.root_layout) as LinearLayout
        if (rootLayout == null) return
        rootLayout!!.addView(view, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
        initToolbar()
    }
}
