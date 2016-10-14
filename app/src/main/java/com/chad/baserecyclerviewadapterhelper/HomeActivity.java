package com.chad.baserecyclerviewadapterhelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.baserecyclerviewadapterhelper.adapter.HomeAdapter;
import com.chad.baserecyclerviewadapterhelper.entity.HomeItem;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.XQuickAdapter;
import com.chad.library.adapter.base.listener.SimpleOnItemClickListener;

import java.util.ArrayList;

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public class HomeActivity extends Activity {
    private static final Class<?>[] ACTIVITY = {AnimationUseActivity.class, MultipleItemUseActivity.class, HeaderAndFooterUseActivity.class, PullToRefreshUseActivity.class, SectionUseActivity.class,  ItemDragAndSwipeUseActivity.class, RecyclerClickItemActivity.class, ExpandableUseActivity.class};
    private static final String[] TITLE = {"Animation Use", "MultipleItem Use", "HeaderAndFooter Use", "PullToRefresh Use", "Section Use","ItemDragAndSwipe Use", "RecyclerClickItemActivity", "ExpandableItem Activity"};
    private static final String[] COLOR_STR = {"#0dddb8", "#0bd4c3", "#03cdcd", "#00b1c5", "#04b2d1", "#04b2d1", "#04b2d1", "#04b2d1"};
    private ArrayList<HomeItem> mDataList;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        initData();
        BaseQuickAdapter homeAdapter = new HomeAdapter(this, mDataList);
        homeAdapter.openLoadAnimation();
        homeAdapter.setOnItemClickListener(new SimpleOnItemClickListener() {
            @Override public void onItemClick(XQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(HomeActivity.this, ACTIVITY[position]);
                startActivity(intent);
            }
        });

        mRecyclerView.setAdapter(homeAdapter);
    }

    private void initData() {
        mDataList = new ArrayList<>();
        for (int i = 0; i < TITLE.length; i++) {
            HomeItem item = new HomeItem();
            item.setTitle(TITLE[i]);
            item.setActivity(ACTIVITY[i]);
            item.setColorStr(COLOR_STR[i]);
            mDataList.add(item);
        }
    }

}
