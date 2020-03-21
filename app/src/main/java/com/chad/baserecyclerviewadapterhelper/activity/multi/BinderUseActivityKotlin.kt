package com.chad.baserecyclerviewadapterhelper.activity.multi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.baserecyclerviewadapterhelper.R
import com.chad.baserecyclerviewadapterhelper.base.BaseActivity
import com.chad.baserecyclerviewadapterhelper.databinding.ActivityMultipleItemUseBinding
import com.chad.baserecyclerviewadapterhelper.databinding.ItemImgTextViewBinding
import com.chad.baserecyclerviewadapterhelper.databinding.ItemMovieBinding
import com.chad.baserecyclerviewadapterhelper.entity.*
import com.chad.baserecyclerviewadapterhelper.utils.Tips
import com.chad.library.adapter.base.BaseBinderAdapter
import com.chad.library.adapter.base.binder.BaseItemBinder
import com.chad.library.adapter.base.binder.QuickDataBindingItemBinder
import com.chad.library.adapter.base.binder.QuickItemBinder
import com.chad.library.adapter.base.binder.QuickViewBindingItemBinder
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import java.util.*

/**
 * @author: limuyang
 * @date: 2019-12-04
 * @Description:
 */
class BinderUseActivityKotlin : BaseActivity() {
    private val adapter = BaseBinderAdapter()
    private lateinit var binding: ActivityMultipleItemUseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMultipleItemUseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setTitle("BaseMultiItemQuickAdapter")
        setBackBtn()
        initRv()
    }

    private fun initRv() {

        // 添加 itemBinder, 各种创建方式如下
        adapter.addItemBinder(ImageItemBinder()) // QuickItemBinder
                .addItemBinder(ImageTextItemBinder(), ImageTextItemBinder.Differ()) // QuickViewBindingItemBinder, 并且注册了 Diff
                .addItemBinder(MovieItemBinder()) // QuickDataBindingItemBinder
                .addItemBinder(ContentItemBinder()) // BaseItemBinder

        // 添加了一个头部而已，可以忽略
        val headView = layoutInflater.inflate(R.layout.head_view, null, false)
        headView.findViewById<View>(R.id.iv).visibility = View.GONE
        headView.setOnClickListener { Tips.show("HeaderView") }
        adapter.setHeaderView(headView)


        binding.rvList.layoutManager = LinearLayoutManager(this)
        binding.rvList.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        val random = Random()

        // 模拟设置数据，数据顺序随意组合
        val data: MutableList<Any> = ArrayList()
        data.add(ImageEntity())
        data.add(ImageEntity())
        data.add(Video(1, "", "Video 1"))
        data.add(Video(2, "", "Video 2"))
        data.add(Movie("Chad 1", 0, random.nextInt(5) + 10, "He was one of Australia's most distinguished artistes"))
        data.add(ImageEntity())
        data.add(Movie("Chad 2", 0, random.nextInt(5) + 10, "This movie is exciting"))
        data.add(ImageEntity())
        data.add(Video(3, "", "Video 3"))
        data.add(Video(4, "", "Video 4"))
        data.add(ContentEntity("Content 1"))
        data.add(ContentEntity("Content 2"))

        // 设置新数据
        adapter.setList(data)

        // 延迟3秒以后执行，模拟Diff刷新数据
        binding.rvList.postDelayed({
            // 模拟需要 Diff 的新数据
            // 先拷贝出数据
            val data: MutableList<Any> = ArrayList(adapter.data)

            // 修改数据
            data.add(0, ImageEntity())
            data.add(2, Video(8, "", "new Video 1.1"))
            data.add(ImageEntity())

            // 使用 diif 刷新数据
            adapter.setDiffNewData(data)
        }, 3000)
    }

    /************************************* 创建 ItemBinder ***************************************/

    /**
     * 使用 layout，快速创建Binder
     */
    private class ImageItemBinder : QuickItemBinder<ImageEntity>() {

        override fun getLayoutId(): Int {
            return R.layout.item_image_view
        }

        override fun convert(holder: BaseViewHolder, data: ImageEntity) {
            // 设置数据
        }

        /**
         * 点击事件
         */
        override fun onClick(holder: BaseViewHolder, view: View, data: ImageEntity, position: Int) {
            Toast.makeText(context, "click index: $position", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * 使用 ViewBinding，快速创建Binder
     */
    private class ImageTextItemBinder : QuickViewBindingItemBinder<Video, ItemImgTextViewBinding>() {
        override fun onCreateViewBinding(layoutInflater: LayoutInflater, parent: ViewGroup, viewType: Int): ItemImgTextViewBinding {
            return ItemImgTextViewBinding.inflate(layoutInflater, parent, false)
        }

        override fun convert(holder: BinderVBHolder<ItemImgTextViewBinding>, data: Video) {
            holder.viewBinding.tv.text = data.name
        }

        /**
         * 如果需要 Diff，可以自行实现如下内容
         */
        class Differ : DiffUtil.ItemCallback<Video>() {
            override fun areItemsTheSame(oldItem: Video, newItem: Video): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Video, newItem: Video): Boolean {
                return oldItem.name == newItem.name
            }
        }
    }

    /**
     * 使用 DataBinding，快速创建Binder
     */
    private class MovieItemBinder : QuickDataBindingItemBinder<Movie, ItemMovieBinding>() {
        private val mPresenter = MoviePresenter()
        override fun onCreateDataBinding(layoutInflater: LayoutInflater, parent: ViewGroup, viewType: Int): ItemMovieBinding {
            return ItemMovieBinding.inflate(layoutInflater, parent, false)
        }

        override fun convert(holder: BinderDataBindingHolder<ItemMovieBinding>, data: Movie) {
            val binding = holder.dataBinding
            binding.movie = data
            binding.presenter = mPresenter
            binding.executePendingBindings()
        }
    }

    /**
     * 使用最基础的 BaseItemBinder 创建 Binder
     */
    private class ContentItemBinder : BaseItemBinder<ContentEntity, BaseViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_text_view, parent, false)
            return BaseViewHolder(view)
        }

        override fun convert(holder: BaseViewHolder, data: ContentEntity) {
            holder.setText(R.id.tv, data.content)
        }
    }
}