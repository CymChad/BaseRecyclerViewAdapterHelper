package com.chad.baserecyclerviewadapterhelper.activity;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.baserecyclerviewadapterhelper.R;
import com.chad.baserecyclerviewadapterhelper.adapter.UpFetchAdapter;
import com.chad.baserecyclerviewadapterhelper.base.BaseActivity;
import com.chad.baserecyclerviewadapterhelper.entity.Movie;
import com.chad.library.adapter.base.QuickAdapterHelper;
import com.chad.library.adapter.base.loadState.LoadState;
import com.chad.library.adapter.base.loadState.leading.LeadingLoadStateAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author limuyang
 * 2019-12-06
 */
public class UpFetchUseActivity extends BaseActivity {

    private RecyclerView   mRecyclerView;
    private final UpFetchAdapter mAdapter = new UpFetchAdapter();

    private QuickAdapterHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_universal_recycler);

        setBackBtn();
        setTitle("UpFetch Use");

        mRecyclerView = findViewById(R.id.rv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mRecyclerView.setAdapter(mAdapter);

        helper = new QuickAdapterHelper.Builder(mAdapter).setLeadingLoadStateAdapter(new LeadingLoadStateAdapter.OnLeadingListener() {
            @Override
            public void onLoad() {
                requestUoFetch();
            }

            @Override
            public boolean isAllowLoading() {
                return true;
            }
        }).build();

        mRecyclerView.setAdapter(helper.getAdapter());
    }

    @Override
    protected void onStart() {
        super.onStart();

        requestUoFetch();
    }

    private int count = 0;

    private void requestUoFetch() {
        if (count == 0) {
            count++;
            // 首次进入页面，设置数据
            mAdapter.submitList(genData());
            scrollToBottom();
            helper.setLeadingLoadState(new LoadState.NotLoading(false));
            return;
        }

        count++;

        // set fetching on when start network request.
        // 当开始网络请求数据的时候，设置状态为加载中
        helper.setLeadingLoadState(LoadState.Loading.INSTANCE);
        /*
         * get data from internet.
         */
        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mAdapter.addAll(0, genData());

                if (count > 5) {
                    /*
                     * set fetch enable false when you don't need anymore.
                     * 不需要功能后，将其关闭
                     */
                    helper.setLeadingLoadState(new LoadState.NotLoading(true));
                } else {
                    helper.setLeadingLoadState(new LoadState.NotLoading(false));

                }
            }
        }, 600);
    }

    /**
     * 滚动到底部（不带动画）
     */
    private void scrollToBottom() {
        LinearLayoutManager ll = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        ll.scrollToPositionWithOffset(getBottomDataPosition(), 0);
    }

    private int getBottomDataPosition() {
        return mAdapter.getItems().size() - 1;
    }

    private List<Movie> genData() {
        ArrayList<Movie> list = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            String name = "Chad";
            int price = random.nextInt(10) + 10;
            int len = random.nextInt(80) + 60;
            Movie movie = new Movie(name, len, price, "He was one of Australia's most distinguished artistes");
            list.add(movie);
        }
        return list;
    }
}
