package com.chad.baserecyclerviewadapterhelper

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.chad.baserecyclerviewadapterhelper.adapter.HomeAdapter
import com.chad.baserecyclerviewadapterhelper.entity.HomeEntity
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    /**
     * RV适配器
     */
    private val homeAdapter by lazy {
        HomeAdapter(homeItemData).apply {
            animationEnable = true

            val top = layoutInflater.inflate(R.layout.top_view, recyclerView, false)
            addHeaderView(top)
            setOnItemClickListener { adapter, _, position ->
                val item = adapter.data[position] as HomeEntity
                if (!item.isHeader) {
                    startActivity(Intent(this@HomeActivity, item.activity))
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        recyclerView.adapter = homeAdapter
    }

    private val homeItemData: ArrayList<HomeEntity>
        get() = arrayListOf(
                HomeEntity(headerTitle = "BaseQuickAdapter 基础功能"),
                HomeEntity("Animation", AnimationUseActivity::class.java, R.mipmap.gv_animation),
                HomeEntity("Header/Footer", HeaderAndFooterUseActivity::class.java, R.mipmap.gv_header_and_footer),
                HomeEntity("EmptyView", EmptyViewUseActivity::class.java, R.mipmap.gv_empty),
                HomeEntity("ItemClick", ItemClickActivity::class.java, R.mipmap.gv_item_click),
                HomeEntity("DataBinding", DataBindingUseActivity::class.java, R.mipmap.gv_databinding),
                HomeEntity("DiffUtil", null, R.mipmap.gv_databinding),

                HomeEntity(headerTitle = "Adapter 类型"),
                HomeEntity("MultipleItem", ChooseMultipleItemUseTypeActivity::class.java, R.mipmap.gv_multipleltem),
                HomeEntity("Quick Section", SectionQuickUseActivity::class.java, R.mipmap.gv_section),
                HomeEntity("Section2", Section2UseActivity::class.java, R.mipmap.gv_section),

                HomeEntity(headerTitle = "功能模块"),


                HomeEntity("PullToRefresh", LoadMoreRefreshUseActivity::class.java, R.mipmap.gv_pulltorefresh),


                HomeEntity("DragAndSwipe", DragAndSwipeUseActivity::class.java, R.mipmap.gv_drag_and_swipe),
                HomeEntity("MultipleDragAndSwipe", null, R.mipmap.gv_drag_and_swipe),

                HomeEntity("ExpandableItem", ExpandableItemActivity::class.java, R.mipmap.gv_expandable),

                HomeEntity("UpFetchData", UpFetchUseActivity::class.java, R.drawable.gv_up_fetch),
                HomeEntity("SectionMultipleItem", null, R.mipmap.gv_multipleltem)
        )
}