package com.chad.baserecyclerviewadapterhelper.activity.dragswipe;


import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.baserecyclerviewadapterhelper.R;
import com.chad.baserecyclerviewadapterhelper.activity.home.adapter.HomeAdapter;
import com.chad.baserecyclerviewadapterhelper.base.BaseActivity;
import com.chad.baserecyclerviewadapterhelper.entity.HomeEntity;

import java.util.ArrayList;


public class DragAndSwipeUseActivity extends BaseActivity {
    private final ArrayList<HomeEntity> homeItemData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag_and_swipe_use);
        setBackBtn();
        setTitle("Drag And Swipe");
        RecyclerView mRVDragAndSwipe = findViewById(R.id.mRVDragAndSwipe);

        // 设置数据
        homeItemData.add(new HomeEntity("Default Drag And Swipe", DefaultDragAndSwipeActivity.class, R.mipmap.gv_drag_and_swipe, ""));
        homeItemData.add(new HomeEntity("Manual Drag And Swipe", ManualDragAndSwipeUseActivity.class, R.mipmap.gv_drag_and_swipe, ""));
        homeItemData.add(new HomeEntity("Head Drag And Swipe", HeaderDragAndSwipeActivity.class, R.mipmap.gv_drag_and_swipe, ""));
        homeItemData.add(new HomeEntity("Diff Drag And Swipe", DragAndSwipeDifferActivity.class, R.mipmap.gv_drag_and_swipe, ""));

        /*
         * RV适配器
         */
        HomeAdapter mAdapter = new HomeAdapter(homeItemData);


        mRVDragAndSwipe.setLayoutManager(new LinearLayoutManager(this));
        mRVDragAndSwipe.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            HomeEntity item = adapter.getItems().get(position);
            if (!item.isSection()) {
                startActivity(new Intent(DragAndSwipeUseActivity.this, item.getActivity()));
            }
        });
    }
}