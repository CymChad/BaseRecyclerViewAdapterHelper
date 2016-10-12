package com.chad.baserecyclerviewadapterhelper;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.baserecyclerviewadapterhelper.adapter.QuickAdapter;
import com.chad.baserecyclerviewadapterhelper.animation.CustomAnimation;
import com.chad.baserecyclerviewadapterhelper.entity.Status;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.animation.AlphaInAnimation;
import com.chad.library.adapter.base.animation.ScaleInAnimation;
import com.chad.library.adapter.base.animation.SlideInBottomAnimation;
import com.chad.library.adapter.base.animation.SlideInLeftAnimation;
import com.chad.library.adapter.base.animation.SlideInRightAnimation;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.List;

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public class AnimationUseActivity extends Activity {
    private RecyclerView mRecyclerView;
    private QuickAdapter mQuickAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adapter_use);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        initAdapter();
        initMenu();
    }

    private void initAdapter() {
        mQuickAdapter = new QuickAdapter(this);
//        addHeadView();
        addFooterView();
        mQuickAdapter.openLoadAnimation(new AlphaInAnimation());
//        mRecyclerView.addOnItemTouchListener(new OnItemChildClickListener() {
//            @Override
//            public void SimpleOnItemChildClick(BaseQuickAdapter adapter, View view, int position) {
//                String content = null;
//                Status status = (Status) adapter.getItem(position);
//                switch (view.getId()) {
//                    case R.id.tweetAvatar:
//                        content = "img:" + status.getUserAvatar();
//                        break;
//                    case R.id.tweetName:
//                        content = "name:" + status.getUserName();
//                        break;
//                }
//                Toast.makeText(AnimationUseActivity.this, content, Toast.LENGTH_LONG).show();
//            }
//        });
        mRecyclerView.setAdapter(mQuickAdapter);
        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter adapter, View view, int position) {
                Toast.makeText(AnimationUseActivity.this, mQuickAdapter.getData().get(position).getUserName() + "-" + Integer.toString(position), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void initMenu() {
        MaterialSpinner spinner = (MaterialSpinner) findViewById(R.id.spinner);
        spinner.setItems("AlphaIn", "ScaleIn", "SlideInBottom", "SlideInLeft", "SlideInRight", "Custom");
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                switch (position) {
                    case 0:
                        mQuickAdapter.openLoadAnimation(new AlphaInAnimation());
                        break;
                    case 1:
                        mQuickAdapter.openLoadAnimation(new ScaleInAnimation());
                        break;
                    case 2:
                        mQuickAdapter.openLoadAnimation(new SlideInBottomAnimation());
                        break;
                    case 3:
                        mQuickAdapter.openLoadAnimation(new SlideInLeftAnimation());
                        break;
                    case 4:
                        mQuickAdapter.openLoadAnimation(new SlideInRightAnimation());
                        break;
                    case 5:
                        mQuickAdapter.openLoadAnimation(new CustomAnimation());
                        break;
                    default:
                        break;
                }
                mRecyclerView.setAdapter(mQuickAdapter);
            }
        });
        MaterialSpinner spinnerFirstOnly = (MaterialSpinner) findViewById(R.id.spinner_first_only);
        spinnerFirstOnly.setItems("isFirstOnly(true)", "isFirstOnly(false)");
        spinnerFirstOnly.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                switch (position) {
                    case 0:
                        mQuickAdapter.isFirstOnly(true);
                        break;
                    case 1:
                        mQuickAdapter.isFirstOnly(false);
                        break;
                    default:
                        break;
                }
                mQuickAdapter.notifyDataSetChanged();
            }
        });
    }

    private void addHeadView() {
        View headView = getLayoutInflater().inflate(R.layout.head_view, (ViewGroup) mRecyclerView.getParent(), false);
        ((TextView) headView.findViewById(R.id.tv)).setText("header");
        mQuickAdapter.addHeaderView(headView);
    }

    private void addFooterView() {
        View headView = getLayoutInflater().inflate(R.layout.head_view, (ViewGroup) mRecyclerView.getParent(), false);
        ((TextView) headView.findViewById(R.id.tv)).setText("footer");
        mQuickAdapter.addFooterView(headView);
    }

    public void click(View view) {
        i++;
        List<Status> list = new ArrayList<>();
        Status status = new Status();
        status.setUserName("Chad" + i);
        status.setCreatedAt("04/05/" + i);
        status.setRetweet(i % 2 == 0);
        status.setUserAvatar("https://avatars1.githubusercontent.com/u/7698209?v=3&s=460");
        status.setText("BaseRecyclerViewAdpaterHelper https://www.recyclerview.org");
        list.add(status);

//        i++;
//        status = new Status();
//        status.setUserName("Chad" + i);
//        status.setCreatedAt("04/05/" + i);
//        status.setRetweet(i % 2 == 0);
//        status.setUserAvatar("https://avatars1.githubusercontent.com/u/7698209?v=3&s=460");
//        status.setText("BaseRecyclerViewAdpaterHelper https://www.recyclerview.org");
//        list.add(status);

        mQuickAdapter.addData(0, list);
    }

    int i = 0;
}
