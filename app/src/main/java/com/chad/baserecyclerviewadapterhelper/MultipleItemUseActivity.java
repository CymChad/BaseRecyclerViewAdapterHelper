package com.chad.baserecyclerviewadapterhelper;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.baserecyclerviewadapterhelper.adapter.MultipleItemAdapter;
import com.chad.baserecyclerviewadapterhelper.data.DataServer;

public class MultipleItemUseActivity extends Activity {
    private RecyclerView mRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple_item_use);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        MultipleItemAdapter multipleItemAdapter = new MultipleItemAdapter(this,DataServer.getStrData(),R.layout.image_view,R.layout.text_view);
        mRecyclerView.setAdapter(multipleItemAdapter);
    }
}
