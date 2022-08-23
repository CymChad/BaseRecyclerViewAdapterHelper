package com.chad.baserecyclerviewadapterhelper.activity.animation;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.baserecyclerviewadapterhelper.R;
import com.chad.baserecyclerviewadapterhelper.activity.animation.adapter.AnimationAdapter;
import com.chad.baserecyclerviewadapterhelper.animator.CustomAnimation1;
import com.chad.baserecyclerviewadapterhelper.animator.CustomAnimation2;
import com.chad.baserecyclerviewadapterhelper.animator.CustomAnimation3;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.kyleduo.switchbutton.SwitchButton;

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 * <p>
 * modify by AllenCoder
 */
public class AnimationUseActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private AnimationAdapter mAnimationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adapter_use);
        mRecyclerView = findViewById(R.id.rv_list);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        initAdapter();
        initMenu();
        initView();
    }

    private void initView() {
        ImageView mImgBtn = findViewById(R.id.img_back);
        mImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                finish();
            }
        });
    }

    private void initAdapter() {
        mAnimationAdapter = new AnimationAdapter();
        mAnimationAdapter.setAnimationEnable(true);

        mRecyclerView.setAdapter(mAnimationAdapter);
    }

    private void initMenu() {
        MaterialSpinner spinner = findViewById(R.id.spinner);
        spinner.setItems("AlphaIn", "ScaleIn", "SlideInBottom", "SlideInLeft", "SlideInRight", "Custom1", "Custom2", "Custom3");
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                switch (position) {
                    case 0:
                        mAnimationAdapter.setItemAnimation(BaseQuickAdapter.AnimationType.AlphaIn);
                        break;
                    case 1:
                        mAnimationAdapter.setItemAnimation(BaseQuickAdapter.AnimationType.ScaleIn);
                        break;
                    case 2:
                        mAnimationAdapter.setItemAnimation(BaseQuickAdapter.AnimationType.SlideInBottom);
                        break;
                    case 3:
                        mAnimationAdapter.setItemAnimation(BaseQuickAdapter.AnimationType.SlideInLeft);
                        break;
                    case 4:
                        mAnimationAdapter.setItemAnimation(BaseQuickAdapter.AnimationType.SlideInRight);
                        break;
                    case 5:
                        mAnimationAdapter.setItemAnimation(new CustomAnimation1());
                        break;
                    case 6:
                        mAnimationAdapter.setItemAnimation(new CustomAnimation2());
                        break;
                    case 7:
                        mAnimationAdapter.setItemAnimation(new CustomAnimation3());
                        break;
                    default:
                        break;
                }
                mRecyclerView.setAdapter(mAnimationAdapter);
            }
        });
        //init firstOnly state
        mAnimationAdapter.setAnimationFirstOnly(false);
        SwitchButton switchButton = findViewById(R.id.switch_button);
        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {
                mAnimationAdapter.setAnimationFirstOnly(isChecked);
                mAnimationAdapter.notifyDataSetChanged();
            }
        });

    }


}
