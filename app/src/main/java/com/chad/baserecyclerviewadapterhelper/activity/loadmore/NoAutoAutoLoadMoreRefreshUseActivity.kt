package com.chad.baserecyclerviewadapterhelper.activity.loadmore;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.baserecyclerviewadapterhelper.R;
import com.chad.baserecyclerviewadapterhelper.activity.loadmore.adapter.CustomLoadMoreAdapter;
import com.chad.baserecyclerviewadapterhelper.activity.headerfooter.adapter.HeaderAdapter;
import com.chad.baserecyclerviewadapterhelper.activity.loadmore.adapter.LoadMoreAdapter;
import com.chad.baserecyclerviewadapterhelper.base.BaseActivity;
import com.chad.baserecyclerviewadapterhelper.data.DataServer;
import com.chad.baserecyclerviewadapterhelper.entity.Status;
import com.chad.baserecyclerviewadapterhelper.utils.Tips;
import com.chad.library.adapter.base.QuickAdapterHelper;
import com.chad.library.adapter.base.loadState.LoadState;
import com.chad.library.adapter.base.loadState.trailing.TrailingLoadStateAdapter;

import java.util.List;


/**
 * 不进行自动加载更多
 */
public class NoAutoAutoLoadMoreRefreshUseActivity extends BaseActivity {

    static class PageInfo {
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

    private static final int PAGE_SIZE = 5;

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LoadMoreAdapter mAdapter;

    private final PageInfo pageInfo = new PageInfo();

    private QuickAdapterHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_more);

        setTitle("No Auto LoadMore Use");
        setBackBtn();

        mRecyclerView = findViewById(R.id.rv_list);
        mSwipeRefreshLayout = findViewById(R.id.swipeLayout);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        initAdapter();
        addHeadView();
        initRefreshLayout();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // 进入页面，刷新数据
        mAdapter.setEmptyViewLayout(this, R.layout.loading_view);
        mSwipeRefreshLayout.setRefreshing(true);
        refresh();
    }

    private void initAdapter() {
        mAdapter = new LoadMoreAdapter();
        mAdapter.setAnimationEnable(true);

        // 自定义"加载更多"的样式
        CustomLoadMoreAdapter loadMoreAdapter = new CustomLoadMoreAdapter();
        loadMoreAdapter.setOnLoadMoreListener(new TrailingLoadStateAdapter.OnTrailingListener() {
            @Override
            public void onLoad() {
                request();
            }

            @Override
            public void onFailRetry() {
                request();
            }

            @Override
            public boolean isAllowLoading() {
                // 下拉刷新的适合，不允许进行"加载更多"
                return !mSwipeRefreshLayout.isRefreshing();
            }
        });
        //——————————————————————
        // 关闭"自动加载更多"，需要在初始化的时候进行设置，使用期间不可更改
        //——————————————————————
        loadMoreAdapter.setAutoLoadMore(false);


        helper = new QuickAdapterHelper.Builder(mAdapter).setTrailingLoadStateAdapter(loadMoreAdapter).build();


        mRecyclerView.setAdapter(helper.getAdapter());
    }

    private void addHeadView() {
        HeaderAdapter headerAdapter = new HeaderAdapter(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addHeadView();
            }
        });

        helper.addHeader(headerAdapter);
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
     * 刷新
     */
    private void refresh() {
        // 下拉刷新，需要重置页数
        pageInfo.reset();
        request();
    }


    /**
     * 请求数据
     */
    private void request() {
//        helper.setTrailingLoadState(LoadState.Loading.INSTANCE);

        new Request(pageInfo.page, new RequestCallBack() {
            @Override
            public void success(List<Status> data) {
                mSwipeRefreshLayout.setRefreshing(false);

                if (pageInfo.isFirstPage()) {
                    //如果是加载的第一页数据，用 setData()
                    mAdapter.submitList(data);
                } else {
                    //不是第一页，则用add
                    mAdapter.addAll(data);
                }

                if (pageInfo.page >= PAGE_SIZE) {
                    // 如果数据彻底加载完毕, 显示没有更多数据布局
                    helper.setTrailingLoadState(new LoadState.NotLoading(true));
                    Tips.show("no more data");
                } else {
                    helper.setTrailingLoadState(new LoadState.NotLoading(false));
                }

                // page加一
                pageInfo.nextPage();
            }

            @Override
            public void fail(Exception e) {
                Tips.show(getResources().getString(R.string.network_err));
                mSwipeRefreshLayout.setRefreshing(false);

                helper.setTrailingLoadState(new LoadState.Error(e));
            }
        }).start();
    }


    /**
     * 模拟加载数据的类，不用特别关注
     */
    static class Request extends Thread {
        private final int mPage;
        private final RequestCallBack mCallBack;
        private final Handler mHandler;

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
                Thread.sleep(1800);
            } catch (InterruptedException ignored) {
            }

            if (mPage == 2 && mFirstError) {
                mFirstError = false;
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mCallBack.fail(new RuntimeException("load fail"));
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