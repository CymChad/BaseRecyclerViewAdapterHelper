package com.chad.baserecyclerviewadapterhelper.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.recyclerview.widget.RecyclerView;

import com.chad.baserecyclerviewadapterhelper.R;
import com.chad.baserecyclerviewadapterhelper.adapter.diffUtil.DiffDemoCallback;
import com.chad.baserecyclerviewadapterhelper.adapter.diffUtil.DiffUtilAdapter;
import com.chad.baserecyclerviewadapterhelper.base.BaseActivity;
import com.chad.baserecyclerviewadapterhelper.data.DataServer;
import com.chad.baserecyclerviewadapterhelper.entity.DiffUtilDemoEntity;
import com.chad.library.adapter.base.diff.BrvahAsyncDifferConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by limuyang
 * Date: 2019/7/14
 */
public class DiffUtilActivity extends BaseActivity {
    private RecyclerView mRecyclerView;
    private Button itemChangeBtn;
    private Button notifyChangeBtn;

    private DiffUtilAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diffutil);
        setBackBtn();
        setTitle("DiffUtil Use");

        findView();
        initRv();
        initClick();

    }

    private void findView() {
        mRecyclerView = findViewById(R.id.diff_rv);
        itemChangeBtn = findViewById(R.id.item_change_btn);
        notifyChangeBtn = findViewById(R.id.notify_change_btn);
    }

    private void initRv() {
        mAdapter = new DiffUtilAdapter(DataServer.getDiffUtilDemoEntities());
        mRecyclerView.setAdapter(mAdapter);

        View view = getLayoutInflater().inflate(R.layout.head_view, mRecyclerView, false);
        view.findViewById(R.id.iv).setVisibility(View.GONE);
        mAdapter.addHeaderView(view);

        // 必须设置Diff Callback
        mAdapter.setDiffCallback(new DiffDemoCallback());

        BrvahAsyncDifferConfig config =  new BrvahAsyncDifferConfig.Builder(new DiffDemoCallback())
                .setMainThreadExecutor()
                .setBackgroundThreadExecutor()
                .build();
        mAdapter.setDiffConfig(config);
    }

    private void initClick() {
        itemChangeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<DiffUtilDemoEntity> newData = getNewList();
                mAdapter.setDiffNewData(newData);
            }
        });

        notifyChangeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // change item 0
                mAdapter.getData().set(0, new DiffUtilDemoEntity(
                        1,
                        "😊😊Item " + 0,
                        "Item " + 0 + " content have change (notifyItemChanged)",
                        "06-12"));
                mAdapter.notifyItemChanged(0 + mAdapter.getHeaderLayoutCount(), DiffUtilAdapter.ITEM_0_PAYLOAD);
            }
        });
    }


    /**
     * get new data
     *
     * @return
     */
    private List<DiffUtilDemoEntity> getNewList() {
        List<DiffUtilDemoEntity> list = new ArrayList<>();
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
                list.add(new DiffUtilDemoEntity(
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
                list.add(new DiffUtilDemoEntity(
                        i,
                        "Item " + i,
                        "Oh~~~~~~, Item " + i + " content have change",
                        "06-12")
                );
                continue;
            }

            list.add(new DiffUtilDemoEntity(
                    i,
                    "Item " + i,
                    "This item " + i + " content",
                    "06-12")
            );
        }
        return list;
    }
}
