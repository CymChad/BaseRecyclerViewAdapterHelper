package com.chad.baserecyclerviewadapterhelper;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.baserecyclerviewadapterhelper.adapter.QuickAdapter;
import com.chad.baserecyclerviewadapterhelper.data.DataServer;
import com.chad.library.adapter.base.XQuickAdapter;
import com.chad.library.adapter.base.animation.AlphaInAnimation;
import com.chad.library.adapter.base.listener.SimpleOnItemClickListener;


/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public class PullToRefreshUseActivity extends Activity implements XQuickAdapter.OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView mRecyclerView;
    private QuickAdapter mQuickAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private View notLoadingView;

    private static final int TOTAL_COUNTER = 18;

    private static final int PAGE_SIZE = 6;

    private int delayMillis = 2000;

    private int mCurrentCounter = 0;

    private boolean isErr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeLayout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        initAdapter();
        addHeadView();

        mRecyclerView.setAdapter(mQuickAdapter);
    }

    private void addHeadView() {
        View headView = getLayoutInflater().inflate(R.layout.head_view, (ViewGroup) mRecyclerView.getParent(), false);
        ((TextView) headView.findViewById(R.id.tv)).setText("click use custom loading view");
        mQuickAdapter.addHeaderView(headView);
    }

    private void addFooterView() {
        View footerView = getLayoutInflater().inflate(R.layout.head_view, (ViewGroup) mRecyclerView.getParent(), false);
        ((TextView) footerView.findViewById(R.id.tv)).setText("footer view");
        mQuickAdapter.addFooterView(footerView);
    }

    @Override
    public void onLoadMoreRequested() {
        mSwipeRefreshLayout.setEnabled(false);
        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mCurrentCounter >= TOTAL_COUNTER) {
                    mQuickAdapter.loadMoreEnd();
                } else {
                    if (isErr) {
                        mQuickAdapter.addData(DataServer.getSampleData(PAGE_SIZE));
                        mCurrentCounter = mQuickAdapter.getData().size();
                        mQuickAdapter.loadMoreComplete();
                    } else {
                        isErr = true;
                        Toast.makeText(PullToRefreshUseActivity.this, R.string.network_err, Toast.LENGTH_LONG).show();
                        mQuickAdapter.loadMoreError();
                    }
                }
                mSwipeRefreshLayout.setEnabled(true);
            }

        }, delayMillis);
    }

    @Override
    public void onRefresh() {
        mQuickAdapter.setEnableLoadMore(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mCurrentCounter = PAGE_SIZE;
                mQuickAdapter.setNewData(DataServer.getSampleData(4));

                mSwipeRefreshLayout.setRefreshing(false);
                isErr = false;
                mQuickAdapter.setEnableLoadMore(true);
            }
        }, delayMillis);
    }

    private void initAdapter() {
        mQuickAdapter = new QuickAdapter(this, PAGE_SIZE);
        mQuickAdapter.setOnLoadMoreListener(this);

        addFooterView();
        mQuickAdapter.openLoadAnimation(new AlphaInAnimation());


        mRecyclerView.setAdapter(mQuickAdapter);
        mCurrentCounter = mQuickAdapter.getData().size();
        mQuickAdapter.setOnItemClickListener(new SimpleOnItemClickListener() {
            @Override public void onItemClick(XQuickAdapter adapter, View view, int position) {
                Toast.makeText(PullToRefreshUseActivity.this, Integer.toString(position), Toast.LENGTH_LONG).show();
            }

        });
    }


}
