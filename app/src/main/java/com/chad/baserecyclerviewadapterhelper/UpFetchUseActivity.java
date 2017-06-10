package com.chad.baserecyclerviewadapterhelper;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.baserecyclerviewadapterhelper.adapter.UpFetchAdapter;
import com.chad.baserecyclerviewadapterhelper.base.BaseActivity;
import com.chad.baserecyclerviewadapterhelper.entity.Movie;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by tysheng
 * Date: 2017/5/25 10:41.
 * Email: tyshengsx@gmail.com
 */

public class UpFetchUseActivity extends BaseActivity {
    RecyclerView mRecyclerView;
    UpFetchAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBackBtn();
        setTitle("UpFetch Use");
        setContentView(R.layout.activity_data_binding_use);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv);
        mAdapter = new UpFetchAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setNewData(genData());
        mAdapter.setUpFetchEnable(true);
        /**
         * start fetch when scroll to position 2, default is 1.
         */
        mAdapter.setStartUpFetchPosition(2);
        mAdapter.setUpFetchListener(new BaseQuickAdapter.UpFetchListener() {
            @Override
            public void onUpFetch() {
                startUpFetch();
            }
        });
    }

    private int count;

    private void startUpFetch() {
        count++;
        /**
         * set fetching on when start network request.
         */
        mAdapter.setUpFetching(true);
        /**
         * get data from internet.
         */
        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mAdapter.addData(0, genData());
                /**
                 * set fetching off when network request ends.
                 */
                mAdapter.setUpFetching(false);
                /**
                 * set fetch enable false when you don't need anymore.
                 */
                if (count > 5) {
                    mAdapter.setUpFetchEnable(false);
                }
            }
        }, 300);
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
