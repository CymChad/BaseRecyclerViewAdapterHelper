package com.chad.baserecyclerviewadapterhelper;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chad.baserecyclerviewadapterhelper.adapter.QuickClickAdapter;
import com.chad.library.adapter.base.helper.OnRecyclerViewClickHelper;

/**
 * create by AllenCoder
 */
public class RecyclerClickItemActivity extends Activity {

    private RecyclerView mRecyclerView;
    private QuickClickAdapter mQuickAdapter;
    private static final int PAGE_SIZE = 30000;
    private static String TAG ="RecyclerClickItemActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_header_and_footer_use);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        initAdapter();
        mQuickAdapter.addHeaderView(getView());
        mQuickAdapter.addFooterView(getView());
        mRecyclerView.setAdapter(mQuickAdapter);
        /**
         * Item  clcik
         */

        mRecyclerView.addOnItemTouchListener(new OnRecyclerViewClickHelper(mRecyclerView,mQuickAdapter){

            @Override
            public void onItemClick(View view, int position) {
                Log.e(TAG, "onItemClick: 收到点击事件 "+position);
                Toast.makeText(RecyclerClickItemActivity.this, "" + Integer.toString(position), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemChildClickHelper(View view, int position) {
                super.onItemChildClickHelper(view, position);
                Log.w(TAG, "onItemChildClickHelper: ");
                Log.d(TAG, "onItemChildClickHelper: "+view.getId());
                switch (view.getId()){
                    case R.id.tweetAvatar:
                        Log.e(TAG, "R.id.tweetAvatar: 收到点击事件 "+position);
                        Toast.makeText(RecyclerClickItemActivity.this, "R.id.tweetAvatar: 收到点击事件" + Integer.toString(position), Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.tweetName:
                        Log.e(TAG, "R.id.tweetName: 收到点击事件 "+position);
                        Toast.makeText(RecyclerClickItemActivity.this, "R.id.tweetName: 收到点击事件" + Integer.toString(position), Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onItemLongClickHelper(View view, int position) {
                super.onItemLongClickHelper(view, position);
                Log.e(TAG, "R.id.tweetName: 收到点击事件 "+position);
                Toast.makeText(RecyclerClickItemActivity.this, "收到长按事件" + Integer.toString(position), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private View getView() {
        View view = getLayoutInflater().inflate(R.layout.head_view, null);
        view.findViewById(R.id.tv).setVisibility(View.GONE);
        view.setLayoutParams(new DrawerLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(RecyclerClickItemActivity.this, "click View", Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }

    private void initAdapter() {
        mQuickAdapter = new QuickClickAdapter(PAGE_SIZE);
        mQuickAdapter.openLoadAnimation();
        mRecyclerView.setAdapter(mQuickAdapter);
    }



    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.w(TAG, "RecyclerView  dispatchTouchEvent: "+ev.getY());
        Log.w(TAG, "RecyclerView  dispatchTouchEvent: "+ev.getRawY());
        return super.dispatchTouchEvent(ev);
    }
}
