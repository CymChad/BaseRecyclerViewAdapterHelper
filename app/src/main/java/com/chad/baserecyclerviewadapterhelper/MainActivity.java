package com.chad.baserecyclerviewadapterhelper;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.chad.baserecyclerviewadapterhelper.adapter.QuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;


public class MainActivity extends Activity {
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.rv_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//      recyclerView.setAdapter(new DefAdpater(this));
        QuickAdapter quickAdapter = new QuickAdapter(this);
        quickAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(MainActivity.this,position+"",Toast.LENGTH_LONG).show();
            }
        });
        recyclerView.setAdapter(quickAdapter);
    }
}
