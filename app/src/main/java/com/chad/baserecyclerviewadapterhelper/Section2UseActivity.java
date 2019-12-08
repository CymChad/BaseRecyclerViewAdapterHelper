package com.chad.baserecyclerviewadapterhelper;

import android.os.Bundle;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.baserecyclerviewadapterhelper.adapter.section.Section2Adapter;
import com.chad.baserecyclerviewadapterhelper.base.BaseActivity;
import com.chad.baserecyclerviewadapterhelper.entity.section.VideoEntity;
import com.chad.baserecyclerviewadapterhelper.entity.section.VideoFooterEntity;
import com.chad.baserecyclerviewadapterhelper.entity.section.VideoHeaderEntity;
import com.chad.baserecyclerviewadapterhelper.entity.section.VideoItemEntity;
import com.chad.library.adapter.base.entity.NSectionEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public class Section2UseActivity extends BaseActivity {
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section_uer);

        setBackBtn();
        setTitle("Section2 Use");

        mRecyclerView = findViewById(R.id.rv_list);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
//        mRecyclerView.addItemDecoration(new GridSectionAverageGapItemDecoration(10, 10, 20, 15));

        final Section2Adapter sectionAdapter = new Section2Adapter();


        mRecyclerView.setAdapter(sectionAdapter);

        sectionAdapter.setNewData(getEntity());

        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                sectionAdapter.collapse(0, true, true);
            }
        }, 3000);
    }

    private List<NSectionEntity> getEntity() {
        List<NSectionEntity> list = new ArrayList<>();
        for (int i = 0; i < 8; i++) {

            //Video Section
            VideoItemEntity itemEntity1 = new VideoItemEntity(R.mipmap.click_head_img_0, "Section " + i + " - item 0");
            VideoItemEntity itemEntity2 = new VideoItemEntity(R.mipmap.click_head_img_0, "Section " + i + " - item 1");
            VideoItemEntity itemEntity3 = new VideoItemEntity(R.mipmap.click_head_img_0, "Section " + i + " - item 2");
            VideoItemEntity itemEntity4 = new VideoItemEntity(R.mipmap.click_head_img_0, "Section " + i + " - item 3");
            VideoItemEntity itemEntity5 = new VideoItemEntity(R.mipmap.click_head_img_0, "Section " + i + " - item 4");
            VideoItemEntity itemEntity6 = new VideoItemEntity(R.mipmap.click_head_img_0, "Section " + i + " - item 5");
            List<NSectionEntity> items = new ArrayList<>();
            items.add(itemEntity1);
            items.add(itemEntity2);
            items.add(itemEntity3);
            items.add(itemEntity4);
            items.add(itemEntity5);
//            items.add(itemEntity6);

            VideoEntity entity = new VideoEntity(items);
            entity.setHeader(new VideoHeaderEntity("Section " + i));
            entity.setFooter(new VideoFooterEntity("Footer " + i));
            list.add(entity);

//            list.addAll(items);
        }
        return list;
    }
}
