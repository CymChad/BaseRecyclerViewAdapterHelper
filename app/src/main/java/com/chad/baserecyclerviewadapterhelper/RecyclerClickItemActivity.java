package com.chad.baserecyclerviewadapterhelper;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chad.baserecyclerviewadapterhelper.adapter.QuickClickAdapter;
import com.chad.baserecyclerviewadapterhelper.data.DataServer;
import com.chad.baserecyclerviewadapterhelper.entity.Status;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;

import java.util.List;

/**
 * create by AllenCoder
 */
public class RecyclerClickItemActivity extends Activity {

    private RecyclerView mRecyclerView;
    private QuickClickAdapter mQuickAdapter;
    private static final int PAGE_SIZE = 100;
    private static String TAG = "RecyclerClickItemActivity";
    private List<Status> sampleData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_click);
        mRecyclerView = (RecyclerView) findViewById(R.id.list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        initAdapter();
//        mQuickAdapter.addHeaderView(getHeadView());
//        mQuickAdapter.addFooterView(getFootView());

//        /**
//         * Item  clcik
//         */
//
        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {

            @Override
            public void SimpleOnItemClick(BaseQuickAdapter adapter, View view, int position) {
                Toast.makeText(RecyclerClickItemActivity.this, "" + Integer.toString(position), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemChildClick(adapter, view, position);
                Log.d(TAG, "onItemChildClick: "+view.getId());
                Log.d(TAG, "R.id.btn1: "+R.id.btn1);
                Log.d(TAG, "R.id.btn2: "+R.id.btn2);
               Status status= sampleData.get(position);
                switch (view.getId()){
                   case  R.id.btn1:
                       Log.e(TAG, "onItemChildClick: 点击了 R.id.btn1" );
                       Toast.makeText(RecyclerClickItemActivity.this,"点击了 bt1", Toast.LENGTH_SHORT).show();
                       status.setShow(false);
                    break;
                    case  R.id.btn2:
                        Log.e(TAG, "onItemChildClick: 点击了 R.id.btn2" );
                        Toast.makeText(RecyclerClickItemActivity.this,"点击了 bt2", Toast.LENGTH_SHORT).show();
                        status.setShow(true);
                    break;
                }
                adapter.notifyItemChanged(position);
            }


            @Override
            public void onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemLongClick(adapter, view, position);


            }

            @Override
            public void onItemChildLongClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemChildLongClick(adapter, view, position);
                Log.d(TAG, "onItemChildClick: "+view.getId());
                Log.d(TAG, "R.id.btn1: "+R.id.btn1);
                Log.d(TAG, "R.id.btn2: "+R.id.btn2);
                Status status= sampleData.get(position);
                switch (view.getId()){
                    case  R.id.btn1:
                        Log.e(TAG, "onItemChildClick: 点击了 R.id.btn1" );
                        Toast.makeText(RecyclerClickItemActivity.this,"点击了 bt1", Toast.LENGTH_SHORT).show();
                        status.setShow(false);
                        break;
                    case  R.id.btn2:
                        Log.e(TAG, "onItemChildClick: 点击了 R.id.btn2" );
                        Toast.makeText(RecyclerClickItemActivity.this,"点击了 bt2", Toast.LENGTH_SHORT).show();
                        status.setShow(true);
                        break;
                }
                adapter.notifyItemChanged(position);

            }
        });
        /**
         * this is sample code
         */
   /*     mRecyclerView.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void SimpleOnItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Toast.makeText(RecyclerClickItemActivity.this, "" + Integer.toString(position), Toast.LENGTH_SHORT).show();

            }
        });

        mRecyclerView.addOnItemTouchListener(new OnItemLongClickListener() {
            @Override
            public void SimpleOnItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                Toast.makeText(RecyclerClickItemActivity.this, "" + Integer.toString(position), Toast.LENGTH_SHORT).show();

            }
        });
        mRecyclerView.addOnItemTouchListener(new OnItemChildLongClickListener() {
            @Override
            public void SimpleOnItemChildLongClick(BaseQuickAdapter adapter, View view, int position) {
                Toast.makeText(RecyclerClickItemActivity.this, "" + Integer.toString(position), Toast.LENGTH_SHORT).show();
            }
        });
        mRecyclerView.addOnItemTouchListener(new SimpleClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Toast.makeText(RecyclerClickItemActivity.this, "" + Integer.toString(position), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                Toast.makeText(RecyclerClickItemActivity.this, "" + Integer.toString(position), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Toast.makeText(RecyclerClickItemActivity.this, "" + Integer.toString(position), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemChildLongClick(BaseQuickAdapter adapter, View view, int position) {
                Toast.makeText(RecyclerClickItemActivity.this, "" + Integer.toString(position), Toast.LENGTH_SHORT).show();
            }
        });*/
    }

    private View getHeadView() {
        View view = getLayoutInflater().inflate(R.layout.head_view, null);
        view.findViewById(R.id.tv).setVisibility(View.GONE);
        view.setLayoutParams(new DrawerLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(RecyclerClickItemActivity.this, "click headView", Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }
    private View getFootView() {
        View view = getLayoutInflater().inflate(R.layout.head_view, null);
        view.findViewById(R.id.tv).setVisibility(View.GONE);
        view.setLayoutParams(new DrawerLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(RecyclerClickItemActivity.this, "click FootView", Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }
    private void initAdapter() {
        sampleData = DataServer.getSampleData(1000);
        mQuickAdapter = new QuickClickAdapter(sampleData);
        mQuickAdapter.openLoadAnimation();
        mRecyclerView.setAdapter(mQuickAdapter);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

}
