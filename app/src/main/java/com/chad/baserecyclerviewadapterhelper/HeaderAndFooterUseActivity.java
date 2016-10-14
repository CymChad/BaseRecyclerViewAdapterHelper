package com.chad.baserecyclerviewadapterhelper;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.baserecyclerviewadapterhelper.adapter.QuickAdapter;
import com.chad.library.adapter.base.XQuickAdapter;
import com.chad.library.adapter.base.listener.SimpleOnItemClickListener;

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public class HeaderAndFooterUseActivity extends Activity {

    private RecyclerView mRecyclerView;
    private QuickAdapter mQuickAdapter;
    private static final int PAGE_SIZE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_header_and_footer_use);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        initAdapter();

        View headerView = getView(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mQuickAdapter.addHeaderView(getView(getRemoveHeaderListener(), "click me to remove me"), 0);
            }
        }, "click me to add new header");
        mQuickAdapter.addHeaderView(headerView);

        View footerView = getView(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mQuickAdapter.addFooterView(getView(getRemoveFooterListener(), "click me to remove me"));
            }
        }, "click me to add new footer");
        mQuickAdapter.addFooterView(footerView, 0);

        mRecyclerView.setAdapter(mQuickAdapter);
    }

    private View getView() {
        View view = getLayoutInflater().inflate(R.layout.head_view, null);
        view.findViewById(R.id.tv).setVisibility(View.GONE);
        view.setLayoutParams(new DrawerLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HeaderAndFooterUseActivity.this, "click View", Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }

    private View getView(View.OnClickListener listener, String text) {
        View view = getLayoutInflater().inflate(R.layout.head_view, null);
        view.setLayoutParams(new DrawerLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        ((TextView) view.findViewById(R.id.tv)).setText(text);
        view.setOnClickListener(listener);
        return view;
    }

    private View.OnClickListener getRemoveHeaderListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mQuickAdapter.removeHeaderView(v);
            }
        };
    }

    private View.OnClickListener getRemoveFooterListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mQuickAdapter.removeFooterView(v);
            }
        };
    }

    private void initAdapter() {
        mQuickAdapter = new QuickAdapter(this, PAGE_SIZE);
        mQuickAdapter.openLoadAnimation();
        mRecyclerView.setAdapter(mQuickAdapter);
        mQuickAdapter.setOnItemClickListener(new SimpleOnItemClickListener() {
            @Override public void onItemClick(XQuickAdapter adapter, View view, int position) {
                Toast.makeText(HeaderAndFooterUseActivity.this, "" + Integer.toString(position), Toast.LENGTH_LONG).show();
            }
        });

    }

}
