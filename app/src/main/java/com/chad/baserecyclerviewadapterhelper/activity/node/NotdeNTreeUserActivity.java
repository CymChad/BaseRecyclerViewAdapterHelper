package com.chad.baserecyclerviewadapterhelper.activity.node;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.baserecyclerviewadapterhelper.R;
import com.chad.baserecyclerviewadapterhelper.adapter.node.tree.NodeNTreeAdapter;
import com.chad.baserecyclerviewadapterhelper.adapter.node.tree.NodeTreeAdapter;
import com.chad.baserecyclerviewadapterhelper.base.BaseActivity;
import com.chad.baserecyclerviewadapterhelper.entity.node.tree.FirstNode;
import com.chad.baserecyclerviewadapterhelper.entity.node.tree.SecondNode;
import com.chad.baserecyclerviewadapterhelper.entity.node.tree.ThirdNode;
import com.chad.baserecyclerviewadapterhelper.utils.Tips;
import com.chad.library.adapter.base.entity.node.BaseNode;
import com.chad.library.adapter.base.listener.GridSpanSizeLookup;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : lipengbp
 * @email : lipengbo@okay.cn
 * @date : 2022/08/28 15:41
 * @desc : 描述
 */
public class NotdeNTreeUserActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private NodeNTreeAdapter adapter = new NodeNTreeAdapter();

    private List<BaseNode> data=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_node_n_tree);
        setBackBtn();
        setTitle("Node Use (Tree)");

        mRecyclerView = findViewById(R.id.rv_list);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,2));
        data=getEntity();
        mRecyclerView.setAdapter(adapter);
        adapter.setList(data);

        // 模拟新增node
        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                SecondNode seNode = new SecondNode(new ArrayList<BaseNode>(), "Second Node(This is added)");
                List<BaseNode> thirdNodeList1 = new ArrayList<>();
                for (int i = 0; i < 8; i++) {
                    ThirdNode node = new ThirdNode("Third Node (This is added)" + i);
                    thirdNodeList1.add(node);
                }
                seNode.getChildNode().addAll(thirdNodeList1);
                SecondNode seNode2 = new SecondNode(new ArrayList<BaseNode>(), "Second Node(This is added)");
                List<BaseNode> thirdNodeList2 = new ArrayList<>();
                for (int i = 0; i < 8; i++) {
                    ThirdNode node = new ThirdNode("Third Node (This is added)" + i);
                    thirdNodeList2.add(node);
                }
                seNode2.getChildNode().addAll(thirdNodeList2);
                List<SecondNode> nodes = new ArrayList<>();
                nodes.add(seNode);
                nodes.add(seNode2);
                //第一个夫node，位置为子node的3号位置
                adapter.nodeAddData(adapter.getData().get(0), 2, nodes);
//                adapter.nodeSetData(adapter.getData().get(0), 2, seNode2);
//                adapter.nodeReplaceChildData(adapter.getData().get(0), nodes);
                Tips.show("新插入了两个node", Toast.LENGTH_LONG);
//                adapter.nodeRemoveData(adapter.getData().get(0), 0);
//                Tips.show("删除一个Node节点", Toast.LENGTH_LONG);
            }
        }, 2000);
    }

    private List<BaseNode> getEntity() {
        List<BaseNode> list = new ArrayList<>();
        for (int i = 0; i < 8; i++) {

            List<BaseNode> secondNodeList = new ArrayList<>();
            for (int n = 0; n <= 5; n++) {

                List<BaseNode> thirdNodeList = new ArrayList<>();
                for (int t = 0; t <= 10; t++) {
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
