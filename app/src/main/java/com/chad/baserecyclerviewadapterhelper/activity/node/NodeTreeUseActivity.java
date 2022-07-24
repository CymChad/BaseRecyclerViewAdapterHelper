package com.chad.baserecyclerviewadapterhelper.activity.node;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.baserecyclerviewadapterhelper.R;
import com.chad.baserecyclerviewadapterhelper.adapter.node.FirstNodeAdapter;
import com.chad.baserecyclerviewadapterhelper.adapter.node.NodeEntityAdapter;
import com.chad.baserecyclerviewadapterhelper.base.BaseActivity;
import com.chad.baserecyclerviewadapterhelper.entity.NodeEntity;
import com.chad.library.adapter.base.QuickNodeHelper;
import com.chad.library.adapter.base.SingleItemAdapter;

import java.util.ArrayList;
import java.util.List;

public class NodeTreeUseActivity extends BaseActivity {
    private RecyclerView    mRecyclerView;

    private QuickNodeHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_node_tree);
        setBackBtn();
        setTitle("Node Use (Tree)");

        helper = new QuickNodeHelper.Builder().build();
        helper.register(NodeEntity.class, new QuickNodeHelper.OnNodeListener<NodeEntity>() {
            @NonNull
            @Override
            public RecyclerView.Adapter<?> createAdapter(NodeEntity item) {
                NodeEntityAdapter adapter = new NodeEntityAdapter(item);

                adapter.setOnItemClickListener(new SingleItemAdapter.OnItemClickListener<NodeEntity>() {
                    @Override
                    public void onItemClick(@NonNull SingleItemAdapter<NodeEntity, ?> adapter, @NonNull View view, int position) {
                        helper.expand();
                    }
                });
                return adapter;
            }

            @NonNull
            @Override
            public List<?> nodeChildren(NodeEntity item) {
                return item.getChildList();
            }
        }).register(NodeEntity.FirstNode.class, new QuickNodeHelper.OnNodeListener<NodeEntity.FirstNode>() {
            @NonNull
            @Override
            public RecyclerView.Adapter<?> createAdapter(NodeEntity.FirstNode item) {
                return new FirstNodeAdapter(item);
            }

            @NonNull
            @Override
            public List<?> nodeChildren(NodeEntity.FirstNode item) {
                return item.getChildList();
            }
        });

        mRecyclerView = findViewById(R.id.rv_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(helper.getAdapter());


        helper.submitList(getEntity(), false);

//        // 模拟新增node
//        mRecyclerView.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                SecondNode seNode = new SecondNode(new ArrayList<BaseNode>(), "Second Node(This is added)");
//                SecondNode seNode2 = new SecondNode(new ArrayList<BaseNode>(), "Second Node(This is added)");
//                List<SecondNode> nodes = new ArrayList<>();
//                nodes.add(seNode);
//                nodes.add(seNode2);
//                //第一个夫node，位置为子node的3号位置
//                adapter.nodeAddData(adapter.getData().get(0), 2, nodes);
////                adapter.nodeSetData(adapter.getData().get(0), 2, seNode2);
////                adapter.nodeReplaceChildData(adapter.getData().get(0), nodes);
//                Tips.show("新插入了两个node", Toast.LENGTH_LONG);
//            }
//        }, 2000);
    }

    private List<NodeEntity> getEntity() {
        List<NodeEntity> list = new ArrayList<>();
        for (int i = 0; i < 8; i++) {

            List<NodeEntity> nodeEntityList = new ArrayList<>();

            List<NodeEntity.FirstNode> firstNodeList = new ArrayList<>();
            for (int n = 0; n <= 5; n++) {

                List<NodeEntity.FirstNode.SecondNode> secondNodeList = new ArrayList<>();
                for (int t = 0; t <= 3; t++) {
                    NodeEntity.FirstNode.SecondNode node = new NodeEntity.FirstNode.SecondNode("SecondNode " + t);
                    secondNodeList.add(node);
                }

                NodeEntity.FirstNode node = new NodeEntity.FirstNode("FirstNode " + n, secondNodeList);
                firstNodeList.add(node);
            }

            NodeEntity entity = new NodeEntity("NodeEntity " + i, firstNodeList);
            list.add(entity);
        }
        return list;
    }
}
