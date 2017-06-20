package com.allen.kotlinapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity

/**
 * 文 件 名: WelcomeActivity
 * 创 建 人: Allen
 * 创建日期: 2017/6/20 11:28
 * 修改时间：
 * 修改备注：
 */
class WelcomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        Handler().postDelayed({
            val intent = Intent(this@WelcomeActivity, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }, 2000)
    }
}
