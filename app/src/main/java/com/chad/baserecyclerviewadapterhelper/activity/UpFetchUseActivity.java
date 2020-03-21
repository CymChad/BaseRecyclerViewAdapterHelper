package com.chad.baserecyclerviewadapterhelper.activity;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.baserecyclerviewadapterhelper.R;
import com.chad.baserecyclerviewadapterhelper.adapter.UpFetchAdapter;
import com.chad.baserecyclerviewadapterhelper.base.BaseActivity;
import com.chad.baserecyclerviewadapterhelper.entity.Movie;
import com.chad.library.adapter.base.listener.OnUpFetchListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author: limuyang
 * @date: 2019-12-06
 * @Description:
 */
public class UpFetchUseActivity extends BaseActivity {

    private RecyclerView   mRecyclerView;
    private UpFetchAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_universal_recycler);

        setBackBtn();
        setTitle("UpFetch Use");

        mRecyclerView = findViewById(R.id.rv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new UpFetchAdapter();
        mRecyclerView.setAdapter(mAdapter);


        mAdapter.getUpFetchModule().setOnUpFetchListener(new OnUpFetchListener() {
            @Override
            public void onUpFetch() {
                requestUoFetch();
            }
        });
        mAdapter.getUpFetchModule().setUpFetchEnable(true);
        mAdapter.setList(genData());
    }

    @Override
    protected void onStart() {
        super.onStart();
        scrollToBottom();
    }

    private int count = 0;

    private void requestUoFetch() {
        count++;

        // set fetching on when start network request.
        // 当开始网络请求数据的时候，设置状态为加载中
        mAdapter.getUpFetchModule().setUpFetching(true);
        /*
         * get data from internet.
         */
        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mAdapter.addData(0, genData());
                /*
                 * set fetching off when network request ends.
                 * 取消状态加载中的状态
                 */
                mAdapter.getUpFetchModule().setUpFetching(false);

                if (count > 5) {
                    /*
                     * set fetch enable false when you don't need anymore.
                     * 不需要功能后，将其关闭
                     */
                    mAdapter.getUpFetchModule().setUpFetchEnable(false);
                }
            }
        }, 300);
    }

    /**
     * 滚动到底部（不带动画）
     */
    private void scrollToBottom() {
        LinearLayoutManager ll = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        ll.scrollToPositionWithOffset(getBottomDataPosition(), 0);
    }

    /**
     * 滚动到底部（带动画）
     */
    private void smoothScrollToBottom() {
        if (mAdapter.getData().isEmpty()) {
            return;
        }
        mRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                mRecyclerView.smoothScrollToPosition(getBottomDataPosition());
            }
        });
    }

    private int getBottomDataPosition() {
        return mAdapter.getHeaderLayoutCount() + mAdapter.getData().size() - 1;
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
