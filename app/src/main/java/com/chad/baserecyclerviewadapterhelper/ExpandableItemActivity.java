package com.chad.baserecyclerviewadapterhelper;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.baserecyclerviewadapterhelper.adapter.ExpandableItemAdapter;
import com.chad.baserecyclerviewadapterhelper.base.BaseActivity;
import com.chad.baserecyclerviewadapterhelper.entity.ExpandableLv0Entity;
import com.chad.baserecyclerviewadapterhelper.entity.ExpandableLv1Entity;
import com.chad.baserecyclerviewadapterhelper.utils.Tips;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.ExpandableEntity;
import com.chad.library.adapter.base.listener.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: limuyang
 * @date: 2019-12-06
 * @Description:
 */
public class ExpandableItemActivity extends BaseActivity {

    private RecyclerView          mRecyclerView;
    private ExpandableItemAdapter adapter = new ExpandableItemAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_universal_recycler);

        setBackBtn();
        setTitle("ExpandableItem Activity");

        mRecyclerView = findViewById(R.id.rv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mRecyclerView.setAdapter(adapter);
        adapter.setNewData(createData());

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ExpandableEntity entity = (ExpandableEntity) adapter.getData().get(position);
                if (entity instanceof ExpandableLv0Entity) {
                    ExpandableLv0Entity lv0Entity = (ExpandableLv0Entity) entity;
                    if (lv0Entity.isExpanded()) {
                        adapter.getExpandableModule().collapse(position + adapter.getHeaderLayoutCount());
                    } else {
                        adapter.getExpandableModule().expand(position + adapter.getHeaderLayoutCount());
                    }
                } else if (entity instanceof ExpandableLv1Entity) {
                    Tips.show("Level 1  ");
                }
            }
        });
    }

    private List<ExpandableEntity> createData() {
        List<ExpandableEntity> lv0 = new ArrayList<>();
        for (int i = 0; i <= 10; i++) {
            List<ExpandableLv1Entity> lv1 = new ArrayList<>();
            for (int n = 0; i <= 2; i++) {
                ExpandableLv1Entity lv1Entity = new ExpandableLv1Entity("   Level 1 - pos: " + n);
                lv1.add(lv1Entity);
            }

            ExpandableLv0Entity lv0Entity = new ExpandableLv0Entity(lv1, false);
            lv0Entity.setTitle("Level 0 - pos: " + i);
            lv0.add(lv0Entity);
        }

        return lv0;
    }
}
