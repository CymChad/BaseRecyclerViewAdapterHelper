package com.chad.baserecyclerviewadapterhelper.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.baserecyclerviewadapterhelper.R;
import com.chad.baserecyclerviewadapterhelper.adapter.HomeAdapter;
import com.chad.baserecyclerviewadapterhelper.base.BaseActivity;
import com.chad.baserecyclerviewadapterhelper.entity.HomeEntity;
import com.chad.library.adapter.base.QuickAdapterHelper;

import java.util.ArrayList;


public class DragAndSwipeUseActivity extends BaseActivity {
    private RecyclerView mRVDragAndSwipe;
    private ArrayList<HomeEntity> homeItemData = new ArrayList();
    /**
     * RV适配器
     */

    private HomeAdapter mAdapter;

    private QuickAdapterHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag_and_swipe_use);
        setBackBtn();
        setTitle("Drag And Swipe");
        mRVDragAndSwipe = findViewById(R.id.mRVDragAndSwipe);
        homeItemData.add(new HomeEntity("Default Drag And Swipe", DefaultDragAndSwipeActivity.class, R.mipmap.gv_drag_and_swipe, ""));
        homeItemData.add(new HomeEntity("Manual Drag And Swipe", ManualDragAndSwipeUseActivity.class, R.mipmap.gv_drag_and_swipe, ""));
        mAdapter = new HomeAdapter(homeItemData);
        helper = new QuickAdapterHelper.Builder(mAdapter)
                .build();
        mRVDragAndSwipe.setLayoutManager(new LinearLayoutManager(this));
        mRVDragAndSwipe.setAdapter(helper.getAdapter());
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            HomeEntity item = adapter.getItems().get(position);
            if (!item.isSection()) {
                startActivity(new Intent(DragAndSwipeUseActivity.this, item.getActivity()));
            }
        });
    }
}