package com.chad.baserecyclerviewadapterhelper.activity.dragswipe;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.baserecyclerviewadapterhelper.R;
import com.chad.baserecyclerviewadapterhelper.activity.home.adapter.HomeAdapter;
import com.chad.baserecyclerviewadapterhelper.base.BaseViewBindingActivity;
import com.chad.baserecyclerviewadapterhelper.databinding.ActivityUniversalRecyclerBinding;
import com.chad.baserecyclerviewadapterhelper.entity.HomeEntity;

import java.util.ArrayList;


public class DragAndSwipeUseActivity extends BaseViewBindingActivity<ActivityUniversalRecyclerBinding> {
    private final ArrayList<HomeEntity> homeItemData = new ArrayList<>();

    @NonNull
    @Override
    public ActivityUniversalRecyclerBinding initBinding() {
        return ActivityUniversalRecyclerBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewCompat.setOnApplyWindowInsetsListener(getViewBinding().getRoot(), new OnApplyWindowInsetsListener() {
            @Override
            public @NonNull WindowInsetsCompat onApplyWindowInsets(@NonNull View v, @NonNull WindowInsetsCompat insets) {
                Insets bar = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                getViewBinding().titleBar.updateFakeBarHeight(bar.top);
                return insets;
            }
        });

        getViewBinding().titleBar.setTitle("Drag And Swipe");
        getViewBinding().titleBar.setOnBackListener(v -> finish());

        // 设置数据
        homeItemData.add(new HomeEntity("Default Drag And Swipe", DefaultDragAndSwipeActivity.class, R.mipmap.gv_drag_and_swipe, ""));
        homeItemData.add(new HomeEntity("Manual Drag And Swipe", ManualDragAndSwipeUseActivity.class, R.mipmap.gv_drag_and_swipe, ""));
        homeItemData.add(new HomeEntity("Head Drag And Swipe", HeaderDragAndSwipeActivity.class, R.mipmap.gv_drag_and_swipe, ""));
        homeItemData.add(new HomeEntity("Diff Drag And Swipe", DragAndSwipeDifferActivity.class, R.mipmap.gv_drag_and_swipe, ""));

        /*
         * RV适配器
         */
        HomeAdapter mAdapter = new HomeAdapter(homeItemData);


        getViewBinding().rv.setLayoutManager(new LinearLayoutManager(this));
        getViewBinding().rv.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            HomeEntity item = adapter.getItems().get(position);
            if (!item.isSection()) {
                startActivity(new Intent(DragAndSwipeUseActivity.this, item.getActivity()));
            }
        });
    }
}