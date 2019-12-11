package com.chad.baserecyclerviewadapterhelper.activity.node;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.chad.baserecyclerviewadapterhelper.R;
import com.chad.baserecyclerviewadapterhelper.activity.multi.MultiItemDelegateUseActivity;
import com.chad.baserecyclerviewadapterhelper.activity.multi.MultiItemProviderUseActivity;
import com.chad.baserecyclerviewadapterhelper.base.BaseActivity;

public class ChooseNodeUseTypeActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_node_use_type);
        setTitle("Node Use");
        setBackBtn();

        findViewById(R.id.card_view1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChooseNodeUseTypeActivity.this, NodeSectionUseActivity.class));
            }
        });

        findViewById(R.id.card_view2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChooseNodeUseTypeActivity.this, MultiItemDelegateUseActivity.class));
            }
        });

        findViewById(R.id.card_view3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChooseNodeUseTypeActivity.this, MultiItemProviderUseActivity.class));
            }
        });
    }
}
