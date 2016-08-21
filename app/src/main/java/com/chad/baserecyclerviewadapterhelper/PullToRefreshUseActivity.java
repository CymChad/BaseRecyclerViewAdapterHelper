package com.chad.baserecyclerviewadapterhelper;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.baserecyclerviewadapterhelper.adapter.QuickAdapter;
import com.chad.baserecyclerviewadapterhelper.data.DataServer;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;


/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public class PullToRefreshUseActivity extends Activity implements BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView mRecyclerView;
    private QuickAdapter mQuickAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private View notLoadingView;

    private static final int TOTAL_COUNTER = 18;

    private static final int PAGE_SIZE = 6;

    private int delayMillis = 1000;

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
        final View customLoading = getLayoutInflater().inflate(R.layout.custom_loading, (ViewGroup) mRecyclerView.getParent(), false);
        headView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mQuickAdapter.setLoadingView(customLoading);
                mRecyclerView.setAdapter(mQuickAdapter);
                Toast.makeText(PullToRefreshUseActivity.this, "use ok!", Toast.LENGTH_LONG).show();
            }
        });
        mQuickAdapter.addHeaderView(headView);
    }

    @Override
    public void onLoadMoreRequested() {
        mRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                if (mCurrentCounter >= TOTAL_COUNTER) {
                    mQuickAdapter.loadComplete();
                    if (notLoadingView == null) {
                        notLoadingView = getLayoutInflater().inflate(R.layout.not_loading, (ViewGroup) mRecyclerView.getParent(), false);
                    }
                    mQuickAdapter.addFooterView(notLoadingView);
                } else {
                    if (isErr) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mQuickAdapter.addData(DataServer.getSampleData(PAGE_SIZE));
                                mCurrentCounter = mQuickAdapter.getData().size();
                            }
                        }, delayMillis);
                    } else {
                        isErr = true;
                        Toast.makeText(PullToRefreshUseActivity.this, R.string.network_err, Toast.LENGTH_LONG).show();
                        mQuickAdapter.showLoadMoreFailedView();

                    }
                }
            }

        });
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mQuickAdapter.setNewData(DataServer.getSampleData(PAGE_SIZE));
                mQuickAdapter.openLoadMore(PAGE_SIZE);
                mQuickAdapter.removeAllFooterView();
                mCurrentCounter = PAGE_SIZE;
                mSwipeRefreshLayout.setRefreshing(false);
                isErr = false;
            }
        }, delayMillis);
    }

    private void initAdapter() {
        mQuickAdapter = new QuickAdapter(2);
        mQuickAdapter.openLoadAnimation();
        mRecyclerView.setAdapter(mQuickAdapter);
        mCurrentCounter = mQuickAdapter.getData().size();
        mQuickAdapter.setOnLoadMoreListener(this);

        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter adapter, View view, int position) {
                Toast.makeText(PullToRefreshUseActivity.this, Integer.toString(position), Toast.LENGTH_LONG).show();
            }
        });
    }


}
