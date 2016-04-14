package com.chad.baserecyclerviewadapterhelper;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.View;
import android.widget.Toast;

import com.chad.baserecyclerviewadapterhelper.adapter.QuickAdapter;
import com.chad.baserecyclerviewadapterhelper.animation.CustomAnimation;
import com.chad.library.adapter.base.BaseQuickAdapter;

import com.chad.library.adapter.base.animation.BaseAnimation;
import com.jaredrummler.materialspinner.MaterialSpinner;


public class MainActivity extends Activity {
    private RecyclerView recyclerView;
    private QuickAdapter quickAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initMenu();
        recyclerView = (RecyclerView) findViewById(R.id.rv_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // def adapter
        //RecyclerView.Adapter adapter = new DefAdpater(this);
        // quick adapter
        quickAdapter = new QuickAdapter(this);
        quickAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(MainActivity.this,""+position,Toast.LENGTH_LONG).show();
            }
        });
        quickAdapter.setFirstOnly(false);
        quickAdapter.openLoadAnimation();
        recyclerView.setAdapter(quickAdapter);
    }

    private void initMenu() {
        MaterialSpinner spinner = (MaterialSpinner) findViewById(R.id.spinner);
        spinner.setItems("AlphaIn", "ScaleIn", "SlideInBottom", "SlideInLeft", "SlideInRight", "Custom");
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                switch (position) {
                    case 0:
                        quickAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
                        break;
                    case 1:
                        quickAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
                        break;
                    case 2:
                        quickAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);
                        break;
                    case 3:
                        quickAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
                        break;
                    case 4:
                        quickAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_RIGHT);
                        break;
                    case 5:
                        quickAdapter.openLoadAnimation(new CustomAnimation());
                        break;

                }
                recyclerView.setAdapter(quickAdapter);
            }
        });
    }
}
