package com.chad.baserecyclerviewadapterhelper.activity.differ;

import android.os.Bundle;

import androidx.annotation.NonNull;

import com.chad.baserecyclerviewadapterhelper.R;
import com.chad.baserecyclerviewadapterhelper.activity.differ.adapter.DiffUtilAdapter;
import com.chad.baserecyclerviewadapterhelper.base.BaseViewBindingActivity;
import com.chad.baserecyclerviewadapterhelper.data.DataServer;
import com.chad.baserecyclerviewadapterhelper.databinding.ActivityDiffutilBinding;
import com.chad.baserecyclerviewadapterhelper.entity.DiffEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by limuyang
 * Date: 2019/7/14O
 */
public final class DifferActivity extends BaseViewBindingActivity<ActivityDiffutilBinding> {

    private final DiffUtilAdapter mAdapter = new DiffUtilAdapter();

    @NonNull
    @Override
    public ActivityDiffutilBinding initBinding() {
        return ActivityDiffutilBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getViewBinding().titleBar.setTitle("DiffUtil Use");
        getViewBinding().titleBar.setOnBackListener(v -> finish());

        initRv();
        initClick();
    }

    @Override
    protected void onStart() {
        super.onStart();

        // 增加延迟，模拟网络加载
        getViewBinding().diffRv.postDelayed(() -> mAdapter.submitList(DataServer.getDiffUtilDemoEntities()), 1500);
    }


    private void initRv() {
        // 打开空布局功能
        mAdapter.setEmptyViewEnable(true);
        // 传入 空布局 layout id
        mAdapter.setEmptyViewLayout(this, R.layout.loading_view);

        getViewBinding().diffRv.setAdapter(mAdapter);
    }

    private int idAdd = 0;

    private void initClick() {
        getViewBinding().btnChange.setOnClickListener(v -> {
            List<DiffEntity> newData = getNewList();
            mAdapter.submitList(newData);
        });

        getViewBinding().btnAdd.setOnClickListener(v -> {
            mAdapter.add(2, new DiffEntity(
                    1111111 + idAdd,
                    "add - 😊😊Item " + 1111111 + idAdd,
                    "Item " + 0 + " content have change (notifyItemChanged)",
                    "06-12"));
            idAdd++;
        });

        getViewBinding().btnRemove.setOnClickListener(v -> {
            if (2 >= mAdapter.getItems().size()) {
                return;
            }
            mAdapter.removeAt(2);
        });
    }


    /**
     * get new data
     */
    private List<DiffEntity> getNewList() {
        List<DiffEntity> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            /*
            Simulate deletion of data No. 1 and No. 3
            模拟删除1号和3号数据
             */
            if (i == 1 || i == 3) {
                continue;
            }

            /*
            Simulate modification title of data No. 0
            模拟修改0号数据的title
             */
            if (i == 0) {
                list.add(new DiffEntity(
                        i,
                        "😊Item " + i,
                        "This item " + i + " content",
                        "06-12")
                );
                continue;
            }

            /*
            Simulate modification content of data No. 4
            模拟修改4号数据的content发生变化
             */
            if (i == 4) {
                list.add(new DiffEntity(
                        i,
                        "Item " + i,
                        "Oh~~~~~~, Item " + i + " content have change",
                        "06-12")
                );
                continue;
            }

            list.add(new DiffEntity(
                    i,
                    "Item " + i,
                    "This item " + i + " content",
                    "06-12")
            );
        }
        return list;
    }
}
