package com.chad.baserecyclerviewadapterhelper;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.baserecyclerviewadapterhelper.adapter.node.NodeAdapter;
import com.chad.baserecyclerviewadapterhelper.animator.CustomAnimation3;
import com.chad.baserecyclerviewadapterhelper.base.BaseActivity;
import com.chad.baserecyclerviewadapterhelper.entity.node.RootNode;
import com.chad.baserecyclerviewadapterhelper.entity.node.SecondNode;
import com.chad.library.adapter.base.entity.node.BaseNode;

import java.util.ArrayList;
import java.util.List;

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public class NodeUseActivity extends BaseActivity {
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section_uer);

        setBackBtn();
        setTitle("Node Use");

        mRecyclerView = findViewById(R.id.rv_list);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
//        mRecyclerView.addItemDecoration(new GridSectionAverageGapItemDecoration(10, 10, 20, 15));

        final NodeAdapter nodeAdapter = new NodeAdapter();
        View view = getLayoutInflater().inflate(R.layout.head_view, mRecyclerView, false);
        nodeAdapter.addHeaderView(view);

        mRecyclerView.setAdapter(nodeAdapter);

        nodeAdapter.setAdapterAnimation(new CustomAnimation3());
        nodeAdapter.setNewData(getEntity());

        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                System.out.println("------------->>>>  " + nodeAdapter.findParentNode(nodeAdapter.getData().get(1)));

            }
        }, 2000);
    }

    private List<BaseNode> getEntity() {
        List<BaseNode> list = new ArrayList<>();
        for (int i = 0; i < 8; i++) {

            //Root Node
            SecondNode itemEntity1 = new SecondNode(R.mipmap.click_head_img_0, "Root " + i + " - SecondNode 0");
            SecondNode itemEntity2 = new SecondNode(R.mipmap.click_head_img_0, "Root " + i + " - SecondNode 1");
            SecondNode itemEntity3 = new SecondNode(R.mipmap.click_head_img_0, "Root " + i + " - SecondNode 2");
            SecondNode itemEntity4 = new SecondNode(R.mipmap.click_head_img_0, "Root " + i + " - SecondNode 3");
            SecondNode itemEntity5 = new SecondNode(R.mipmap.click_head_img_0, "Root " + i + " - SecondNode 4");
            List<BaseNode> items = new ArrayList<>();
            items.add(itemEntity1);
            items.add(itemEntity2);
            items.add(itemEntity3);
            items.add(itemEntity4);
            items.add(itemEntity5);


            RootNode entity = new RootNode(items, "Root Node " + i);

            if (i == 1) {
                entity.setExpanded(false);
            }

            list.add(entity);

        }
        return list;
    }
}
