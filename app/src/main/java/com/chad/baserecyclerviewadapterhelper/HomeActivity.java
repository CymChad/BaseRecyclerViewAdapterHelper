package com.chad.baserecyclerviewadapterhelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.chad.baserecyclerviewadapterhelper.adapter.HomeAdapter;
import com.chad.baserecyclerviewadapterhelper.entity.HomeItem;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public class HomeActivity extends Activity {
    private static final Class<?>[] ACTIVITY = {AnimationUseActivity.class, MultipleItemUseActivity.class, HeaderAndFooterUseActivity.class, PullToRefreshUseActivity.class, SectionUseActivity.class, EmptyViewUseActivity.class, ItemDragAndSwipeUseActivity.class,RecyclerClickItemActivity.class};
    private static final String[] TITLE = {"Animation Use", "MultipleItem Use", "HeaderAndFooter Use", "PullToRefresh Use", "Section Use", "EmptyView Use", "ItemDragAndSwipe Use","RecyclerClickItemActivity"};
    private static final String[] COLOR_STR = {"#0dddb8","#0bd4c3","#03cdcd","#00b1c5","#04b2d1","#04b2d1","#04b2d1","#04b2d1"};
    private ArrayList<HomeItem> mDataList;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        initData();
        BaseQuickAdapter homeAdapter = new HomeAdapter( R.layout.home_item_view, mDataList);
        homeAdapter.openLoadAnimation();
        homeAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(HomeActivity.this, ACTIVITY[position]);
                startActivity(intent);
            }
        });
        homeAdapter.setOnRecyclerViewItemLongClickListener(new BaseQuickAdapter.OnRecyclerViewItemLongClickListener() {
            @Override
            public boolean onItemLongClick(View view, int position) {
                Toast.makeText(HomeActivity.this,"onItemLongClickHelper",Toast.LENGTH_LONG).show();
                return true;
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
