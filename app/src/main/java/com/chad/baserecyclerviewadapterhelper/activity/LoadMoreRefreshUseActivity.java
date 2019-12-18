package com.chad.baserecyclerviewadapterhelper.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.baserecyclerviewadapterhelper.R;
import com.chad.baserecyclerviewadapterhelper.adapter.LoadMoreAdapter;
import com.chad.baserecyclerviewadapterhelper.base.BaseActivity;
import com.chad.baserecyclerviewadapterhelper.data.DataServer;
import com.chad.baserecyclerviewadapterhelper.entity.Status;
import com.chad.baserecyclerviewadapterhelper.loadmore.CustomLoadMoreView;
import com.chad.baserecyclerviewadapterhelper.utils.Tips;
import com.chad.library.adapter.base.listener.OnLoadMoreListener;

import java.util.List;


/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public class LoadMoreRefreshUseActivity extends BaseActivity {

    class PageInfo {
        int page = 0;

        void nextPage() {
            page++;
        }

        void reset() {
            page = 0;
        }

        boolean isFirstPage() {
            return page == 0;
        }
    }

    private static final int PAGE_SIZE = 6;

    private SwitchCompat       switchCompat;
    private RecyclerView       mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LoadMoreAdapter    mAdapter;

    private PageInfo pageInfo = new PageInfo();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_more);

        setTitle("LoadMore Refresh Use");
        setBackBtn();

        switchCompat = findViewById(R.id.autoLoadMoreSwitch);
        mRecyclerView = findViewById(R.id.rv_list);
        mSwipeRefreshLayout = findViewById(R.id.swipeLayout);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        initAdapter();
        addHeadView();
        initRefreshLayout();
        initLoadMore();
        initSwitch();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // 进入页面，刷新数据
        mSwipeRefreshLayout.setRefreshing(true);
        refresh();
    }

    private void initAdapter() {
        mAdapter = new LoadMoreAdapter();
        mAdapter.setAnimationEnable(true);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void addHeadView() {
        View headView = getLayoutInflater().inflate(R.layout.head_view, (ViewGroup) mRecyclerView.getParent(), false);
        headView.findViewById(R.id.iv).setVisibility(View.GONE);
        ((TextView) headView.findViewById(R.id.tv)).setText("Change Custom LoadView");
        headView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.setNewData(null);
                mAdapter.getLoadMoreModule().setLoadMoreView(new CustomLoadMoreView());
                mRecyclerView.setAdapter(mAdapter);
                Tips.show("Change Complete");

                mSwipeRefreshLayout.setRefreshing(true);
                refresh();
            }
        });
        mAdapter.addHeaderView(headView);
    }

    private void initRefreshLayout() {
        mSwipeRefreshLayout.setColorSchemeColors(Color.rgb(47, 223, 189));
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
    }

    /**
     * 初始化加载更多
     */
    private void initLoadMore() {
        mAdapter.getLoadMoreModule().setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                loadMore();
            }
        });
        mAdapter.getLoadMoreModule().setAutoLoadMore(true);
    }

    private void initSwitch() {
        switchCompat.setChecked(mAdapter.getLoadMoreModule().isAutoLoadMore());
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mAdapter.getLoadMoreModule().setAutoLoadMore(isChecked);
                if (isChecked) {
                    switchCompat.setText("自动加载（开）");
                } else {
                    switchCompat.setText("自动加载（关）");
                }
            }
        });
    }

    /**
     * 刷新
     */
    private void refresh() {
        // 这里的作用是防止下拉刷新的时候还可以上拉加载
        mAdapter.getLoadMoreModule().setEnableLoadMore(false);
        // 下拉刷新，需要重置页数
        pageInfo.reset();
        request();
    }

    /**
     * 加载更多
     */
    private void loadMore() {
        request();
    }

    /**
     * 请求数据
     */
    private void request() {
        new Request(pageInfo.page, new RequestCallBack() {
            @Override
            public void success(List<Status> data) {
                mSwipeRefreshLayout.setRefreshing(false);
                mAdapter.getLoadMoreModule().setEnableLoadMore(true);

                if (pageInfo.isFirstPage()) {
                    //如果是加载的第一页数据，用setNew
                    mAdapter.setNewData(data);
                } else {
                    //不是第一页，则用add
                    mAdapter.addData(data);
                }

                if (data.size() < PAGE_SIZE) {
                    //第一页如果不够一页就不显示没有更多数据布局
                    mAdapter.getLoadMoreModule().loadMoreEnd();
                    Tips.show("no more data");
                } else {
                    mAdapter.getLoadMoreModule().loadMoreComplete();
                }

                // page加一
                pageInfo.nextPage();
            }

            @Override
            public void fail(Exception e) {
                Tips.show(getResources().getString(R.string.network_err));
                mSwipeRefreshLayout.setRefreshing(false);
                mAdapter.getLoadMoreModule().setEnableLoadMore(true);

                mAdapter.getLoadMoreModule().loadMoreFail();
            }
        }).start();
    }


    /**
     * 模拟加载数据的类，不用特别关注
     */
    static class Request extends Thread {
        private int             mPage;
        private RequestCallBack mCallBack;
        private Handler         mHandler;

        private static boolean mFirstPageNoMore;
        private static boolean mFirstError = true;

        public Request(int page, RequestCallBack callBack) {
            mPage = page;
            mCallBack = callBack;
            mHandler = new Handler(Looper.getMainLooper());
        }

        @Override
        public void run() {
            try {
                Thread.sleep(800);
            } catch (InterruptedException e) {
            }

            if (mPage == 2 && mFirstError) {
                mFirstError = false;
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mCallBack.fail(new RuntimeException("fail"));
                    }
                });
            } else {
                int size = PAGE_SIZE;
                if (mPage == 1) {
                    if (mFirstPageNoMore) {
                        size = 1;
                    }
                    mFirstPageNoMore = !mFirstPageNoMore;
                    if (!mFirstError) {
                        mFirstError = true;
                    }
                } else if (mPage == 4) {
                    size = 1;
                }

                final int dataSize = size;
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mCallBack.success(DataServer.getSampleData(dataSize));
                    }
                });
            }
        }
    }

    interface RequestCallBack {
        /**
         * 模拟加载成功
         *
         * @param data 数据
         */
        void success(List<Status> data);

        /**
         * 模拟加载失败
         *
         * @param e 错误信息
         */
        void fail(Exception e);
    }
}