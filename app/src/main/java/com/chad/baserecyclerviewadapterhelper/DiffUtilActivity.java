package com.chad.baserecyclerviewadapterhelper;

import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.chad.baserecyclerviewadapterhelper.adapter.DiffUtilAdapter;
import com.chad.baserecyclerviewadapterhelper.base.BaseActivity;
import com.chad.baserecyclerviewadapterhelper.data.DataServer;
import com.chad.baserecyclerviewadapterhelper.entity.Movie;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;

public class DiffUtilActivity extends BaseActivity
implements  SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView mRecyclerView;
    private DiffUtilAdapter mDiffUtilAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeLayout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(Color.rgb(47, 223, 189));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        setTitle("Refresh to test DiffUtil");
        setBackBtn();
        initAdapter();

    }



    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mDiffUtilAdapter.updateDataSet( DataServer.getInstance().updateMovies(mDiffUtilAdapter.getData()));
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, 1000);
    }

    private void initAdapter() {
        mDiffUtilAdapter = new DiffUtilAdapter();
        mDiffUtilAdapter
                .setDiffUtilCallback(new CustomDiffCallback())
                .setDiffUtilDetectMove(false)
                .openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT)
                .isFirstOnly(true);
        mRecyclerView.setAdapter(mDiffUtilAdapter);

        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(final BaseQuickAdapter adapter, final View view, final int position) {
                Toast.makeText(DiffUtilActivity.this, Integer.toString(position), Toast.LENGTH_LONG).show();
            }
        });
    }

    static class CustomDiffCallback extends BaseQuickAdapter.DiffUtilCallback<Movie>{

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            Movie oldItem = getOldItem(oldItemPosition);
            Movie newItem = getNewItem(newItemPosition);
            return oldItem.name.equals(newItem.name);
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            Movie oldItem = getOldItem(oldItemPosition);
            Movie newItem = getNewItem(newItemPosition);
            return oldItem.price == newItem.price && oldItem.length == newItem.length;
        }

        @Nullable
        @Override
        public Object getChangePayload(int oldItemPosition, int newItemPosition) {
            //实现这个回调,更加精确的进行局部更新,可以用来解决局部刷新Item时,item闪烁问题,但相对来说,实现复杂度增加.
            Movie oldItem = getOldItem(oldItemPosition);
            Movie newItem = getNewItem(newItemPosition);
            Bundle payload = new Bundle();
            if (oldItem.length != newItem.length) {
                payload.putInt(Movie.KEY_LENGTH,newItem.length);
            }
            if (oldItem.price != newItem.price) {
                payload.putInt(Movie.KEY_PRICE, newItem.price);
            }
            return payload;
        }
    }

}
