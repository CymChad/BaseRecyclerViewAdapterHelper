package com.chad.baserecyclerviewadapterhelper;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.chad.baserecyclerviewadapterhelper.adapter.PullToRefreshAdapter;
import com.chad.baserecyclerviewadapterhelper.base.BaseActivity;
import com.chad.baserecyclerviewadapterhelper.data.DataServer;
import com.chad.baserecyclerviewadapterhelper.entity.Movie;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public class PullToRefreshUseActivity extends BaseActivity implements BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView mRecyclerView;
    private PullToRefreshAdapter pullToRefreshAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private static final int TOTAL_COUNTER = 18;

    private static final int PAGE_SIZE = 6;

    private int delayMillis = 1000;

    private int mCurrentCounter = 0;

    private boolean isErr;
    private boolean mLoadMoreEndGone = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeLayout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(Color.rgb(47, 223, 189));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        setTitle("Pull TO Refresh Use");
        setBackBtn();
        initAdapter();
//        addHeadView();
    }

//    private void addHeadView() {
//        View headView = getLayoutInflater().inflate(R.layout.head_view, (ViewGroup) mRecyclerView.getParent(), false);
//        ((TextView) headView.findViewById(R.id.tv)).setText("click use custom load view");
//        headView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mLoadMoreEndGone = true;
//                pullToRefreshAdapter.setLoadMoreView(new CustomLoadMoreView());
//                mRecyclerView.setAdapter(pullToRefreshAdapter);
//                Toast.makeText(PullToRefreshUseActivity.this, "use ok!", Toast.LENGTH_LONG).show();
//            }
//        });
//        pullToRefreshAdapter.addHeaderView(headView);
//    }

    @Override
    public void onLoadMoreRequested() {
        mSwipeRefreshLayout.setEnabled(false);
        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mCurrentCounter >= TOTAL_COUNTER) {
//                    pullToRefreshAdapter.loadMoreEnd();//default visible
                    pullToRefreshAdapter.loadMoreEnd(mLoadMoreEndGone);//true is gone,false is visible
                } else {
                    if (isErr) {
                        pullToRefreshAdapter.addData(DataServer.getInstance().getMovieData());
                        mCurrentCounter = pullToRefreshAdapter.getData().size();
                        pullToRefreshAdapter.loadMoreComplete();
                    } else {
                        isErr = true;
                        Toast.makeText(PullToRefreshUseActivity.this, R.string.network_err, Toast.LENGTH_LONG).show();
                        pullToRefreshAdapter.loadMoreFail();

                    }
                }
                mSwipeRefreshLayout.setEnabled(true);
            }

        },delayMillis);
    }

    @Override
    public void onRefresh() {
        pullToRefreshAdapter.setEnableLoadMore(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                List<Movie> movies = new ArrayList<Movie>(pullToRefreshAdapter.getData());
              //  movies.add(1, new Movie("绿巨人", 1, 200));
                pullToRefreshAdapter.updateDataSet(movies);
                isErr = false;
                mCurrentCounter = PAGE_SIZE;
                mSwipeRefreshLayout.setRefreshing(false);
                pullToRefreshAdapter.setEnableLoadMore(true);
            }
        }, delayMillis);
    }

    private void initAdapter() {
        pullToRefreshAdapter = new PullToRefreshAdapter();
        pullToRefreshAdapter
                .openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT)
                .isFirstOnly(true)
                .setDiffUtilDetectMove(false);
//        pullToRefreshAdapter.setAutoLoadMoreSize(3);
        mRecyclerView.setAdapter(pullToRefreshAdapter);
        mCurrentCounter = pullToRefreshAdapter.getData().size();

        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(final BaseQuickAdapter adapter, final View view, final int position) {
                Toast.makeText(PullToRefreshUseActivity.this, Integer.toString(position), Toast.LENGTH_LONG).show();
            }
        });
    }


}
