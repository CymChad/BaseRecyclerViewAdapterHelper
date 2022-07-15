package com.chad.baserecyclerviewadapterhelper.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.baserecyclerviewadapterhelper.R;
import com.chad.baserecyclerviewadapterhelper.adapter.FooterAdapter;
import com.chad.baserecyclerviewadapterhelper.adapter.HeaderAdapter;
import com.chad.baserecyclerviewadapterhelper.adapter.HeaderAndFooterAdapter;
import com.chad.baserecyclerviewadapterhelper.base.BaseActivity;
import com.chad.baserecyclerviewadapterhelper.data.DataServer;
import com.chad.baserecyclerviewadapterhelper.utils.Tips;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.QuickAdapterHelper;
import com.chad.library.adapter.base.listener.OnItemClickListener;

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public class HeaderAndFooterUseActivity extends BaseActivity {

    private static final int PAGE_SIZE = 3;

    private QuickAdapterHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_universal_recycler);

        setBackBtn();
        setTitle("HeaderAndFooter Use");


        HeaderAndFooterAdapter adapter1 = new HeaderAndFooterAdapter(DataServer.getSampleData(PAGE_SIZE));
        adapter1.setAnimationEnable(true);
        adapter1.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                Tips.show(String.valueOf(position));
            }
        });

        helper = new QuickAdapterHelper.Builder(adapter1)
                .build();

        RecyclerView mRecyclerView = findViewById(R.id.rv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(helper.getAdapter());


        addHeader();

        helper.addFooter(new FooterAdapter(false, adapter -> {
            addFooter();
            return null;
        }));
    }

    private void addHeader() {
        helper.addHeader(new HeaderAdapter(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addHeader();
            }
        }));
    }

    private void addFooter() {
        helper.addFooter(new FooterAdapter(true, adapter -> {
            helper.removeAdapter(adapter);
            return null;
        }));
    }


}
