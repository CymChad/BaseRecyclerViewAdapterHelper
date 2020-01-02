package com.chad.baserecyclerviewadapterhelper.activity.node;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.baserecyclerviewadapterhelper.R;
import com.chad.baserecyclerviewadapterhelper.adapter.node.tree.NodeTreeAdapter;
import com.chad.baserecyclerviewadapterhelper.base.BaseActivity;
import com.chad.baserecyclerviewadapterhelper.entity.node.tree.FirstNode;
import com.chad.baserecyclerviewadapterhelper.entity.node.tree.SecondNode;
import com.chad.baserecyclerviewadapterhelper.entity.node.tree.ThirdNode;
import com.chad.baserecyclerviewadapterhelper.utils.Tips;
import com.chad.library.adapter.base.entity.node.BaseNode;

import java.util.ArrayList;
import java.util.List;

public class NodeTreeUseActivity extends BaseActivity {
    private RecyclerView    mRecyclerView;
    private NodeTreeAdapter adapter = new NodeTreeAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_node_tree);
        setBackBtn();
        setTitle("Node Use (Tree)");

        mRecyclerView = findViewById(R.id.rv_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(adapter);

        adapter.setNewData(getEntity());

        // 模拟新增node
        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                SecondNode seNode = new SecondNode(new ArrayList<BaseNode>(), "Second Node(This is added)");
                //第一个夫node，位置为子node的3号位置
                adapter.nodeAddData(adapter.getData().get(0), 3, seNode);
                Tips.show("新增node");
            }
        }, 3000);
    }

    private List<BaseNode> getEntity() {
        List<BaseNode> list = new ArrayList<>();
        for (int i = 0; i < 8; i++) {

            List<BaseNode> secondNodeList = new ArrayList<>();
            for (int n = 0; n <= 5; n++) {

                List<BaseNode> thirdNodeList = new ArrayList<>();
                for (int t = 0; t <= 3; t++) {
                    ThirdNode node = new ThirdNode("Third Node " + t);
                    thirdNodeList.add(node);
                }

                SecondNode seNode = new SecondNode(thirdNodeList, "Second Node " + n);
                secondNodeList.add(seNode);
            }

            FirstNode entity = new FirstNode(secondNodeList, "First Node " + i);

            // 模拟 默认第0个是展开的
            entity.setExpanded(i == 0);

            list.add(entity);
        }
        return list;
    }
}
