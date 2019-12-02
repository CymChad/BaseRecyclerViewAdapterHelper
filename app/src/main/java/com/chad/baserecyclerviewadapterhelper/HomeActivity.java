package com.chad.baserecyclerviewadapterhelper;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.baserecyclerviewadapterhelper.adapter.HomeAdapter;
import com.chad.baserecyclerviewadapterhelper.entity.HomeItem;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.Arrays;
import java.util.List;

import kotlin.Unit;
import kotlin.jvm.functions.Function3;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mRecyclerView = findViewById(R.id.rv_list);
        initRv();
    }

    private void initRv() {
        HomeAdapter homeAdapter = new HomeAdapter(R.layout.home_item_view, getHomeItemData());
//        homeAdapter.openLoadAnimation();
        View top = getLayoutInflater().inflate(R.layout.top_view, (ViewGroup) mRecyclerView.getParent(), false);
        homeAdapter.addHeaderView(top);
        homeAdapter.setOnItemClickListener(new Function3<BaseQuickAdapter<?, ?>, View, Integer, Unit>() {
            @Override
            public Unit invoke(BaseQuickAdapter<?, ?> adapter, View view, Integer position) {
                HomeItem item = (HomeItem) adapter.getItem(position);
//                Intent intent = new Intent(HomeActivity.this, item.getActivity());
//                startActivity(intent);
                return null;
            }
        });

        mRecyclerView.setAdapter(homeAdapter);
    }

    private List<HomeItem> getHomeItemData() {
        return Arrays.asList(
                new HomeItem("Animation", null, R.mipmap.gv_animation),
                new HomeItem("MultipleItem", null, R.mipmap.gv_multipleltem),
                new HomeItem("Header/Footer", null, R.mipmap.gv_header_and_footer),
                new HomeItem("PullToRefresh", null, R.mipmap.gv_pulltorefresh),
                new HomeItem("Section", null, R.mipmap.gv_section),
                new HomeItem("EmptyView", null, R.mipmap.gv_empty),
                new HomeItem("DragAndSwipe", null, R.mipmap.gv_drag_and_swipe),
                new HomeItem("MultipleDragAndSwipe", null, R.mipmap.gv_drag_and_swipe),
                new HomeItem("ItemClick", null, R.mipmap.gv_item_click),
                new HomeItem("ExpandableItem", null, R.mipmap.gv_expandable),
                new HomeItem("DataBinding", null, R.mipmap.gv_databinding),
                new HomeItem("UpFetchData", null, R.drawable.gv_up_fetch),
                new HomeItem("SectionMultipleItem", null, R.mipmap.gv_multipleltem),
                new HomeItem("DiffUtil", null, R.mipmap.gv_databinding)
        );
    }
}
