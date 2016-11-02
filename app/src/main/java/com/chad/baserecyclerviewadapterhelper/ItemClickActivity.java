package com.chad.baserecyclerviewadapterhelper;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;

import com.chad.baserecyclerviewadapterhelper.adapter.ItemClickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;

public class ItemClickActivity extends Activity {

    private RecyclerView mRecyclerView;
    private ItemClickAdapter itemClickAdapter;
    private static final int PAGE_SIZE = 10;
    private static String TAG = "RecyclerClickItemActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_click);
        mRecyclerView = (RecyclerView) findViewById(R.id.list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        initAdapter();
        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(final BaseQuickAdapter adapter, final View view, final int position) {

            }
        });
    }

    private void initAdapter() {
        itemClickAdapter = new ItemClickAdapter(null);
        itemClickAdapter.openLoadAnimation();
        mRecyclerView.setAdapter(itemClickAdapter);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

}
