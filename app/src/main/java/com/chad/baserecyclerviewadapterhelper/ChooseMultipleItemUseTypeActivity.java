package com.chad.baserecyclerviewadapterhelper;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.chad.baserecyclerviewadapterhelper.base.BaseActivity;

/**
 * https://github.com/chaychan
 *
 * @author ChayChan
 * @description: ChooseMultipleItemUseType
 * @date 2018/3/30  10:14
 */

public class ChooseMultipleItemUseTypeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_multiple_item_use_type);
        setTitle("MultipleItem Use");
        setBackBtn();

        findViewById(R.id.card_view1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChooseMultipleItemUseTypeActivity.this, MultiItemQuickUseActivity.class));
            }
        });

        findViewById(R.id.card_view2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChooseMultipleItemUseTypeActivity.this, MultiItemDelegateUseActivity.class));
            }
        });

        findViewById(R.id.card_view3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChooseMultipleItemUseTypeActivity.this, MultiItemProviderUseActivity.class));
            }
        });
    }
}
