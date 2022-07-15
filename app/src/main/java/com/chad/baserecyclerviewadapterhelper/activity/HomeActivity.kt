package com.chad.baserecyclerviewadapterhelper.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.chad.baserecyclerviewadapterhelper.R
import com.chad.baserecyclerviewadapterhelper.adapter.HomeAdapter
import com.chad.baserecyclerviewadapterhelper.adapter.HomeTopHeaderAdapter
import com.chad.baserecyclerviewadapterhelper.databinding.ActivityHomeBinding
import com.chad.baserecyclerviewadapterhelper.entity.HomeEntity
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.QuickAdapterHelper
import com.chad.library.adapter.base.listener.OnItemClickListener

class HomeActivity : AppCompatActivity(), OnItemClickListener {

    private lateinit var binding: ActivityHomeBinding

    /**
     * RV适配器
     */
    private val homeAdapter by lazy(LazyThreadSafetyMode.NONE) {
        HomeAdapter(homeItemData).apply {
            animationEnable = true
            setOnItemClickListener(this@HomeActivity)
        }
    }

    private val helper by lazy(LazyThreadSafetyMode.NONE) {
        QuickAdapterHelper.Builder(homeAdapter)
            .build()
            .addHeader(HomeTopHeaderAdapter())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 从 QuickAdapterHelper 获取 adapter，设置给 RecycleView
        binding.recyclerView.adapter = helper.adapter
    }

    /**
     * 实现的 OnItemClickListener 接口
     * item 点击事件
     */
    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        val item = adapter.items[position] as HomeEntity
        if (!item.isSection) {
            startActivity(Intent(this@HomeActivity, item.activity))
        }
    }

    private val homeItemData: ArrayList<HomeEntity>
        get() = arrayListOf(
                HomeEntity(sectionTitle = "BaseQuickAdapter 基础功能"),
                HomeEntity("Animation", AnimationUseActivity::class.java, R.mipmap.gv_animation),
                HomeEntity("Header/Footer", HeaderAndFooterUseActivity::class.java, R.mipmap.gv_header_and_footer),
                HomeEntity("EmptyView", EmptyViewUseActivity::class.java, R.mipmap.gv_empty),
                HomeEntity("ItemClick", ItemClickActivity::class.java, R.mipmap.gv_item_click),
                HomeEntity("DataBinding", DataBindingUseActivity::class.java, R.mipmap.gv_databinding),
//                HomeEntity("DiffUtil", DiffUtilActivity::class.java, R.mipmap.gv_databinding),

//                HomeEntity(sectionTitle = "Adapter 类型"),
//                HomeEntity("MultipleItem", ChooseMultipleItemUseTypeActivity::class.java, R.mipmap.gv_multipleltem),
//                HomeEntity("Quick Section", SectionQuickUseActivity::class.java, R.mipmap.gv_section),
//                HomeEntity("Node", ChooseNodeUseTypeActivity::class.java, R.mipmap.gv_expandable),
//
                HomeEntity(sectionTitle = "功能模块"),
                HomeEntity("LoadMore", LoadMoreRefreshUseActivity::class.java, R.mipmap.gv_pulltorefresh),
//                HomeEntity("DragAndSwipe", DragAndSwipeUseActivity::class.java, R.mipmap.gv_drag_and_swipe),
//                HomeEntity("UpFetch", UpFetchUseActivity::class.java, R.drawable.gv_up_fetch)


        )
}