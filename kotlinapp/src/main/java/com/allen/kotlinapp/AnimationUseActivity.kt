package com.allen.kotlinapp

import android.app.Activity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.ImageView
import android.widget.Toast
import com.allen.kotlinapp.adapter.AnimationAdapter
import com.allen.kotlinapp.animation.CustomAnimation
import com.allen.kotlinapp.entity.Status
import com.chad.library.adapter.base.BaseQuickAdapter
import com.jaredrummler.materialspinner.MaterialSpinner
import com.kyleduo.switchbutton.SwitchButton

/**
 * 文 件 名: AnimationUseActivity
 * 创 建 人: Allen
 * 创建日期: 2017/6/13 14:00
 * 修改时间：
 * 修改备注：
 */


class AnimationUseActivity : Activity() {
    private var mRecyclerView: RecyclerView? = null
    private var mAnimationAdapter: AnimationAdapter? = null
    private var mImgBtn: ImageView? = null
    private val mFirstPageItemCount = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adapter_use)
        mRecyclerView = findViewById(R.id.rv_list) as RecyclerView
        mRecyclerView!!.setHasFixedSize(true)
        mRecyclerView!!.layoutManager = LinearLayoutManager(this)
        initAdapter()
        initMenu()
        initView()
    }

    private fun initView() {

        mImgBtn = findViewById(R.id.img_back) as ImageView
        mImgBtn!!.setOnClickListener { finish() }
    }

    private fun initAdapter() {
        mAnimationAdapter = AnimationAdapter()
        mAnimationAdapter!!.openLoadAnimation()
        mAnimationAdapter!!.setNotDoAnimationCount(mFirstPageItemCount)
        mAnimationAdapter!!.onItemChildClickListener = BaseQuickAdapter.OnItemChildClickListener { adapter, view, position ->
            var content: String? = null
            val status = adapter.getItem(position) as Status?
            when (view.id) {
                R.id.img -> {
                    content = "img:" + status?.userAvatar
                    Toast.makeText(this@AnimationUseActivity, content, Toast.LENGTH_LONG).show()
                }
                R.id.tweetName -> {
                    content = "name:" + status!!.userName
                    Toast.makeText(this@AnimationUseActivity, content, Toast.LENGTH_LONG).show()
                }
                R.id.tweetText -> {
                    content = "tweetText:" + status!!.userName
                    Toast.makeText(this@AnimationUseActivity, content, Toast.LENGTH_LONG).show()
                }
            }// you have set clickspan .so there should not solve any click event ,just empty
        }
        mRecyclerView!!.adapter = mAnimationAdapter
    }

    private fun initMenu() {
        val spinner = findViewById(R.id.spinner) as MaterialSpinner
        spinner.setItems("AlphaIn", "ScaleIn", "SlideInBottom", "SlideInLeft", "SlideInRight", "Custom")
        spinner.setOnItemSelectedListener { view, position, id, item ->
            when (position) {
                0 -> mAnimationAdapter!!.openLoadAnimation(BaseQuickAdapter.ALPHAIN)
                1 -> mAnimationAdapter!!.openLoadAnimation(BaseQuickAdapter.SCALEIN)
                2 -> mAnimationAdapter!!.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM)
                3 -> mAnimationAdapter!!.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT)
                4 -> mAnimationAdapter!!.openLoadAnimation(BaseQuickAdapter.SLIDEIN_RIGHT)
                5 -> mAnimationAdapter!!.openLoadAnimation(CustomAnimation())
                else -> {
                }
            }
            mRecyclerView!!.adapter = mAnimationAdapter
        }
        mAnimationAdapter!!.isFirstOnly(false)//init firstOnly state
        val switchButton = findViewById(R.id.switch_button) as SwitchButton
        switchButton.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                mAnimationAdapter!!.isFirstOnly(true)
            } else {
                mAnimationAdapter!!.isFirstOnly(false)
            }
            mAnimationAdapter!!.notifyDataSetChanged()
        }

    }

}
