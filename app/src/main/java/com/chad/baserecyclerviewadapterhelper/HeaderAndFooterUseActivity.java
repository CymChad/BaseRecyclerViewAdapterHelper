package com.chad.baserecyclerviewadapterhelper;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chad.baserecyclerviewadapterhelper.adapter.HeaderAndFooterAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public class HeaderAndFooterUseActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private HeaderAndFooterAdapter headerAndFooterAdapter;
    private static final int PAGE_SIZE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Window window = this.getWindow();
        //取消设置透明状态栏,使 ContentView 内容不再覆盖状态栏
//        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//        //设置状态栏颜色
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            window.setStatusBarColor(Color.TRANSPARENT);
//        }
        setContentView(R.layout.activity_header_and_footer_use);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        initAdapter();

        View headerView = getView(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                headerAndFooterAdapter.addHeaderView(getView(getRemoveHeaderListener(), "click me to remove me"), 0);
            }
        }, "click me to add new header");
        headerAndFooterAdapter.addHeaderView(headerView);

        View footerView = getView(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                headerAndFooterAdapter.addFooterView(getView(getRemoveFooterListener(), "click me to remove me"));
            }
        }, "click me to add new footer");
        footerView = getLayoutInflater().inflate(R.layout.footer_view, (ViewGroup) mRecyclerView.getParent(), false);
        headerAndFooterAdapter.addFooterView(footerView, 0);

        mRecyclerView.setAdapter(headerAndFooterAdapter);
//        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

//        CollapsingToolbarLayout collapsingToolbar =
//                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
//        collapsingToolbar.setTitle("HeaderAndFooterUseActivity");

//        loadBackdrop();
    }

//    private void loadBackdrop() {
//        final ImageView imageView = (ImageView) findViewById(R.id.backdrop);
//        Glide.with(this).load(R.mipmap.header_bg).centerCrop().into(imageView);
//    }


    private View getView(View.OnClickListener listener, String text) {
        View view = getLayoutInflater().inflate(R.layout.head_view, (ViewGroup) mRecyclerView.getParent(), false);
        view.setOnClickListener(listener);
        return view;
    }

    private View.OnClickListener getRemoveHeaderListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                headerAndFooterAdapter.removeHeaderView(v);
            }
        };
    }

    private View.OnClickListener getRemoveFooterListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                headerAndFooterAdapter.removeFooterView(v);
            }
        };
    }

    private void initAdapter() {
        headerAndFooterAdapter = new HeaderAndFooterAdapter(PAGE_SIZE);
        headerAndFooterAdapter.openLoadAnimation();
        mRecyclerView.setAdapter(headerAndFooterAdapter);
        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                Toast.makeText(HeaderAndFooterUseActivity.this, "" + Integer.toString(position), Toast.LENGTH_LONG).show();
            }
        });

    }

}
