package com.chad.baserecyclerviewadapterhelper.activity.home

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ConcatAdapter
import com.chad.baserecyclerviewadapterhelper.R
import com.chad.baserecyclerviewadapterhelper.activity.animation.AnimationUseActivity
import com.chad.baserecyclerviewadapterhelper.activity.databinding.DataBindingUseActivity
import com.chad.baserecyclerviewadapterhelper.activity.differ.DifferActivity
import com.chad.baserecyclerviewadapterhelper.activity.dragswipe.DragAndSwipeUseActivity
import com.chad.baserecyclerviewadapterhelper.activity.emptyview.EmptyViewUseActivity
import com.chad.baserecyclerviewadapterhelper.activity.headerfooter.HeaderAndFooterUseActivity
import com.chad.baserecyclerviewadapterhelper.activity.home.adapter.HomeAdapter
import com.chad.baserecyclerviewadapterhelper.activity.home.adapter.HomeTopHeaderAdapter
import com.chad.baserecyclerviewadapterhelper.activity.itemclick.ItemClickActivity
import com.chad.baserecyclerviewadapterhelper.activity.loadmore.AutoLoadMoreRefreshUseActivity
import com.chad.baserecyclerviewadapterhelper.activity.loadmore.NoAutoAutoLoadMoreRefreshUseActivity
import com.chad.baserecyclerviewadapterhelper.activity.scene.GroupDemoActivity
import com.chad.baserecyclerviewadapterhelper.activity.treenode.TreeNodeActivity
import com.chad.baserecyclerviewadapterhelper.activity.upfetch.UpFetchUseActivity
import com.chad.baserecyclerviewadapterhelper.databinding.ActivityHomeBinding
import com.chad.baserecyclerviewadapterhelper.entity.HomeEntity
import com.chad.library.adapter.base.QuickAdapterHelper

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    /**
     * RV适配器
     */
    private val homeAdapter by lazy(LazyThreadSafetyMode.NONE) {
        HomeAdapter(homeItemData)
    }

    private val helper by lazy(LazyThreadSafetyMode.NONE) {
        QuickAdapterHelper.Builder(homeAdapter)
            .build()
            .addBeforeAdapter(HomeTopHeaderAdapter())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 从 QuickAdapterHelper 获取 adapter，设置给 RecycleView
        binding.recyclerView.adapter = helper.adapter

        // item 点击事件
        homeAdapter.setOnItemClickListener { adapter, _, position ->
            val item = adapter.items[position]
            if (!item.isSection) {
                startActivity(Intent(this@HomeActivity, item.activity))
            }
        }
    }

    private val homeItemData: ArrayList<HomeEntity>
        get() = arrayListOf(
            HomeEntity(sectionTitle = "BaseQuickAdapter 基础功能"),
            HomeEntity("Animation", AnimationUseActivity::class.java, R.mipmap.gv_animation),
            HomeEntity(
                "Header/Footer",
                HeaderAndFooterUseActivity::class.java,
                R.mipmap.gv_header_and_footer
            ),
            HomeEntity("EmptyView", EmptyViewUseActivity::class.java, R.mipmap.gv_empty),
            HomeEntity("ItemClick", ItemClickActivity::class.java, R.mipmap.gv_item_click),
            HomeEntity("DataBinding", DataBindingUseActivity::class.java, R.mipmap.gv_databinding),
            HomeEntity("DiffUtil", DifferActivity::class.java, R.mipmap.gv_databinding),

//
            HomeEntity(sectionTitle = "功能模块"),
            HomeEntity("LoadMore(Auto)", AutoLoadMoreRefreshUseActivity::class.java, R.mipmap.gv_pulltorefresh),
            HomeEntity("LoadMore", NoAutoAutoLoadMoreRefreshUseActivity::class.java, R.mipmap.gv_pulltorefresh),
            HomeEntity("DragAndSwipe", DragAndSwipeUseActivity::class.java, R.mipmap.gv_drag_and_swipe),
            HomeEntity("UpFetch", UpFetchUseActivity::class.java, R.drawable.gv_up_fetch),


            HomeEntity(sectionTitle = "场景演示"),
            HomeEntity("Group（ConcatAdapter）", GroupDemoActivity::class.java, R.mipmap.gv_animation),
            HomeEntity("Tree Node", TreeNodeActivity::class.java, R.mipmap.gv_expandable),
        )
}