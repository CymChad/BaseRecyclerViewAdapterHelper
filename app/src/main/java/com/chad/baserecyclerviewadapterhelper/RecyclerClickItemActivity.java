package com.chad.baserecyclerviewadapterhelper;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chad.baserecyclerviewadapterhelper.adapter.QuickClickAdapter;
import com.chad.library.adapter.base.XQuickAdapter;
import com.chad.library.adapter.base.animation.AlphaInAnimation;
import com.chad.library.adapter.base.listener.OnItemClickListener;

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

        mQuickAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override public void onItemClick(XQuickAdapter adapter, View view, int position) {
                Toast.makeText(RecyclerClickItemActivity.this, "" + Integer.toString(position), Toast.LENGTH_SHORT).show();
            }

            @Override public void onItemLongClick(XQuickAdapter adapter, View view, int position) {
                Toast.makeText(RecyclerClickItemActivity.this,"The " + Integer.toString(position)+ " Item is LongClick ", Toast.LENGTH_SHORT).show();
            }

            @Override public void onItemChildClick(XQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.tweetAvatar:
                        Toast.makeText(RecyclerClickItemActivity.this, "The " + Integer.toString(position)+" tweetAvatar  is clicked", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.tweetName:
                        Toast.makeText(RecyclerClickItemActivity.this, "The " + Integer.toString(position)+" tweetName  is clicked", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }

            @Override public void onItemChildLongClick(XQuickAdapter adapter, View view, int position) {
                Toast.makeText(RecyclerClickItemActivity.this, "The "+ Integer.toString(position)+"  view itemchild " + "is LongClick " + Integer.toString(position), Toast.LENGTH_SHORT).show();
            }
        });
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
                Toast.makeText(RecyclerClickItemActivity.this, "click FootView", Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }
    private void initAdapter() {
        mQuickAdapter = new QuickClickAdapter(this,PAGE_SIZE);
        mQuickAdapter.openLoadAnimation(new AlphaInAnimation());
        mRecyclerView.setAdapter(mQuickAdapter);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

}
