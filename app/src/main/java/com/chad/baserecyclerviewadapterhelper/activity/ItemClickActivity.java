package com.chad.baserecyclerviewadapterhelper.activity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.baserecyclerviewadapterhelper.R;
import com.chad.baserecyclerviewadapterhelper.adapter.ItemClickAdapter;
import com.chad.baserecyclerviewadapterhelper.base.BaseActivity;
import com.chad.baserecyclerviewadapterhelper.entity.ClickEntity;
import com.chad.baserecyclerviewadapterhelper.utils.Tips;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BrvahAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Allen
 */
public class ItemClickActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private ItemClickAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_universal_recycler);

        setBackBtn();
        setTitle("ItemClickActivity Activity");

        mRecyclerView = findViewById(R.id.rv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        initAdapter();

        // 设置点击事件
        adapter.setOnItemClickListener(new BrvahAdapter.OnItemClickListener<ClickEntity>() {
            @Override
            public void onItemClick(@NonNull BrvahAdapter<ClickEntity, ?> adapter, @NonNull View view, int position) {
                Tips.show("onItemClick " + position);
            }
        });

        // 设置item 长按事件
        adapter.setOnItemLongClickListener(new BrvahAdapter.OnItemLongClickListener<ClickEntity>() {
            @Override
            public boolean onItemLongClick(@NonNull BrvahAdapter<ClickEntity, ?> adapter, @NonNull View view, int position) {
                Tips.show("onItemLongClick " + position);
                return true;
            }
        });

        // 添加子 view 的点击事件
        adapter.addOnItemChildClickListener(R.id.btn, new BrvahAdapter.OnItemChildClickListener<ClickEntity>() {
            @Override
            public void onItemChildClick(@NonNull BrvahAdapter<ClickEntity, ?> adapter, @NonNull View view, int position) {
                Tips.show("onItemChildClick: " + position);
            }
        });
        adapter.addOnItemChildClickListener(R.id.iv_num_reduce, new BrvahAdapter.OnItemChildClickListener<ClickEntity>() {
            @Override
            public void onItemChildClick(@NonNull BrvahAdapter<ClickEntity, ?> adapter, @NonNull View view, int position) {
                Tips.show("onItemChildClick:  reduce " + position);
            }
        });
        adapter.addOnItemChildClickListener(R.id.iv_num_add, new BrvahAdapter.OnItemChildClickListener<ClickEntity>() {
            @Override
            public void onItemChildClick(@NonNull BrvahAdapter<ClickEntity, ?> adapter, @NonNull View view, int position) {
                Tips.show("onItemChildClick:  add " + position);
            }
        });

        // 设置子 view 长按事件
        adapter.addOnItemChildLongClickListener(R.id.btn_long, new BrvahAdapter.OnItemChildLongClickListener<ClickEntity>() {
            @Override
            public boolean onItemChildLongClick(@NonNull BrvahAdapter<ClickEntity, ?> adapter, @NonNull View view, int position) {
                Tips.show("onItemChildLongClick " + position);
                return true;
            }
        });
    }

    private void initAdapter() {
        List<ClickEntity> data = new ArrayList<>();
        data.add(new ClickEntity(ClickEntity.CLICK_ITEM_VIEW));
        data.add(new ClickEntity(ClickEntity.CLICK_ITEM_CHILD_VIEW));
        data.add(new ClickEntity(ClickEntity.LONG_CLICK_ITEM_VIEW));
        data.add(new ClickEntity(ClickEntity.LONG_CLICK_ITEM_CHILD_VIEW));
        adapter = new ItemClickAdapter(data);
        adapter.setAnimationEnable(true);
        mRecyclerView.setAdapter(adapter);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

}
