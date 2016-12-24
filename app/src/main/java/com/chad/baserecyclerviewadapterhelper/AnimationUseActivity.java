package com.chad.baserecyclerviewadapterhelper;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.chad.baserecyclerviewadapterhelper.adapter.AnimationAdapter;
import com.chad.baserecyclerviewadapterhelper.animation.CustomAnimation;
import com.chad.baserecyclerviewadapterhelper.entity.Status;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.kyleduo.switchbutton.SwitchButton;

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 *
 * modify by AllenCoder
 */
public class AnimationUseActivity extends Activity {
    private RecyclerView mRecyclerView;
    private AnimationAdapter animationAdapter;
    private ImageView imgBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adapter_use);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        initAdapter();
        initMenu();
        initView();
    }

    private void initView() {

        imgBtn = (ImageView) findViewById(R.id.img_back);
        imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                finish();
            }
        });
    }

    private void initAdapter() {
        animationAdapter = new AnimationAdapter();
        animationAdapter.openLoadAnimation();
        mRecyclerView.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                String content = null;
                Status status = (Status) adapter.getItem(position);
                switch (view.getId()) {
                    case R.id.img:
                        content = "img:" + status.getUserAvatar();
                        Toast.makeText(AnimationUseActivity.this, content, Toast.LENGTH_LONG).show();
                        break;
                    case R.id.tweetName:
                        content = "name:" + status.getUserName();
                        Toast.makeText(AnimationUseActivity.this, content, Toast.LENGTH_LONG).show();
                        break;
                    case R.id.tweetText:
                        // you have set clickspan .so there should not solve any click event ,just empty
                        break;
                }

            }
        });
        mRecyclerView.setAdapter(animationAdapter);
    }

    private void initMenu() {
        MaterialSpinner spinner = (MaterialSpinner) findViewById(R.id.spinner);
        spinner.setItems("AlphaIn", "ScaleIn", "SlideInBottom", "SlideInLeft", "SlideInRight", "Custom");
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                switch (position) {
                    case 0:
                        animationAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
                        break;
                    case 1:
                        animationAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
                        break;
                    case 2:
                        animationAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);
                        break;
                    case 3:
                        animationAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
                        break;
                    case 4:
                        animationAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_RIGHT);
                        break;
                    case 5:
                        animationAdapter.openLoadAnimation(new CustomAnimation());
                        break;
                    default:
                        break;
                }
                mRecyclerView.setAdapter(animationAdapter);
            }
        });
        SwitchButton switchButton = (SwitchButton) findViewById(R.id.switch_button);
        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {
                if (isChecked){
                    animationAdapter.isFirstOnly(true);
                }else {
                    animationAdapter.isFirstOnly(false);
                }
                animationAdapter.notifyDataSetChanged();
            }
        });

    }

}
