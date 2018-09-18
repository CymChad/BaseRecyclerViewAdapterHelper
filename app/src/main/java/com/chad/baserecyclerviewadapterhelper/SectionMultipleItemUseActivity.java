package com.chad.baserecyclerviewadapterhelper;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.chad.baserecyclerviewadapterhelper.adapter.SectionMultipleItemAdapter;
import com.chad.baserecyclerviewadapterhelper.base.BaseActivity;
import com.chad.baserecyclerviewadapterhelper.data.DataServer;
import com.chad.baserecyclerviewadapterhelper.entity.SectionMultipleItem;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

/**
 * to get SectionMultipleItem you need follow two things
 * 1.create entity which extend SectionMultiEntity
 * 2.create adapter which extend BaseSectionMultiItemQuickAdapter
 */
public class SectionMultipleItemUseActivity extends BaseActivity {
    private RecyclerView mRecyclerView;
    private List<SectionMultipleItem> mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section_uer);
        setBackBtn();
        setTitle("SectionMultiple Use");
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 1. create entityList which item data extend SectionMultiEntity
        mData = DataServer.getSectionMultiData();

        // create adapter which extend BaseSectionMultiItemQuickAdapter provide your headerResId
        SectionMultipleItemAdapter sectionAdapter = new SectionMultipleItemAdapter(R.layout.def_section_head, mData);
        sectionAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                SectionMultipleItem item = (SectionMultipleItem) adapter.getData().get(position);
                switch (view.getId()) {
                    case R.id.card_view:
                        // 获取主体item相应数据给后期使用
                        if (item.getVideo() != null) {
                            Toast.makeText(SectionMultipleItemUseActivity.this, item.getVideo().getName(), Toast.LENGTH_LONG).show();
                        }
                        break;
                    default:
                        Toast.makeText(SectionMultipleItemUseActivity.this, "OnItemChildClickListener " + position, Toast.LENGTH_LONG).show();
                        break;

                }
            }
        });
        mRecyclerView.setAdapter(sectionAdapter);
    }
}
