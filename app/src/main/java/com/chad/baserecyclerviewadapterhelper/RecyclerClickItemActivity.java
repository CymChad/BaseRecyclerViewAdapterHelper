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
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.util.TouchEventUtil;

/**
 * create by AllenCoder
 */
public class RecyclerClickItemActivity extends Activity {

    private RecyclerView mRecyclerView;
    private QuickClickAdapter mQuickAdapter;
    private static final int PAGE_SIZE = 10;
    private static String TAG = "RecyclerClickItemActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_click);
        mRecyclerView = (RecyclerView) findViewById(R.id.list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        initAdapter();
        mQuickAdapter.addHeaderView(getHeadView());
        mQuickAdapter.addFooterView(getFootView());

//        /**
//         * Item  clcik
//         */
//
        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {

            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                Toast.makeText(RecyclerClickItemActivity.this, "" + Integer.toString(position), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemChildClick(adapter, view, position);
                switch (view.getId()) {
                    case R.id.tweetAvatar:
                        Toast.makeText(RecyclerClickItemActivity.this, "The " + Integer.toString(position) + " tweetAvatar  is clicked", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.tweetName:
                        Toast.makeText(RecyclerClickItemActivity.this, "The " + Integer.toString(position) + " tweetName  is clicked", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }

            }


            @Override
            public void onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemLongClick(adapter, view, position);
                Toast.makeText(RecyclerClickItemActivity.this, "The " + Integer.toString(position) + " Item is LongClick ", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onItemChildLongClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemChildLongClick(adapter, view, position);
                Toast.makeText(RecyclerClickItemActivity.this, "The " + Integer.toString(position) + "  view itemchild " + "is LongClick " + Integer.toString(position), Toast.LENGTH_SHORT).show();

            }
        });
        /**
         * this is sample code
         */

    }

    private View getHeadView() {
        View view = getLayoutInflater().inflate(R.layout.head_view, null);
        view.findViewById(R.id.tv).setVisibility(View.GONE);
        view.setLayoutParams(new DrawerLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(RecyclerClickItemActivity.this, "click headView", Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }

    private View getFootView() {
        View view = getLayoutInflater().inflate(R.layout.head_view, null);
        view.findViewById(R.id.tv).setVisibility(View.GONE);
        view.setLayoutParams(new DrawerLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(RecyclerClickItemActivity.this, "click FootView", Toast.LENGTH_LONG).show();
                Log.e(TAG, "点击了底部: >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            }
        });
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View v, final MotionEvent event) {
                Log.e(TAG, "onTouch: "+TouchEventUtil.getTouchAction(event.getAction()) );
                return false;
            }
        });
        return view;
    }

    @Override
    public boolean dispatchTouchEvent(final MotionEvent ev) {
        Log.w(TAG, "---------------------------------------------------------------");
        Log.w(TAG, "dispatchTouchEvent: "+ TouchEventUtil.getTouchAction(ev.getAction()));
        return super.dispatchTouchEvent(ev);
    }



    private void initAdapter() {
        mQuickAdapter = new QuickClickAdapter(PAGE_SIZE);
        mQuickAdapter.openLoadAnimation();
        mRecyclerView.setAdapter(mQuickAdapter);
    }

    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        Log.d(TAG, "onTouchEvent: "+TouchEventUtil.getTouchAction(event.getAction()));
        return super.onTouchEvent(event);
    }


}
