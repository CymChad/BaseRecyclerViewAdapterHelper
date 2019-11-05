package com.chad.baserecyclerviewadapterhelper;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.chad.baserecyclerviewadapterhelper.adapter.TestAdapter;
import com.chad.baserecyclerviewadapterhelper.adapter.TestData;
import com.chad.library.adapter.base.BaseQuickAdapter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function3;

/**
 * Created by limuyang
 * Date: 2019/7/14
 */
public class TestActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private Button       itemChangeBtn;
    private Button       notifyChangeBtn;
    private Button       asyncChangeBtn;

    private TestAdapter mAdapter;

    private ExecutorService fixedThreadPool = Executors.newFixedThreadPool(2);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        findView();
        initRv();
        initClick();
    }

    private void findView() {
        mRecyclerView = findViewById(R.id.diff_rv);
        itemChangeBtn = findViewById(R.id.item_change_btn);
        notifyChangeBtn = findViewById(R.id.notify_change_btn);
        asyncChangeBtn = findViewById(R.id.async_change_btn);
    }

    private void initRv() {
        List<TestData> list = new ArrayList<>();
        list.add(new TestData("0", "content 0"));

        mAdapter = new TestAdapter(null);
        mRecyclerView.setAdapter(mAdapter);

        View view = getLayoutInflater().inflate(R.layout.head_view, mRecyclerView, false);
        view.findViewById(R.id.iv).setVisibility(View.GONE);
        mAdapter.addHeaderView(view);

        mAdapter.setAutoLoadMore(true);
        mAdapter.setOnLoadMoreListener(new Function0<Unit>() {
            @Override
            public Unit invoke() {
                mRecyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.addData(new TestData(mAdapter.getData().size() + "", "content 0"));
                        mAdapter.loadMoreComplete();
                    }
                }, 3000);
                return null;
            }
        });
        mAdapter.setOnItemChildClickListener(new Function3<BaseQuickAdapter<?, ?>, View, Integer, Unit>() {
            @Override
            public Unit invoke(BaseQuickAdapter<?, ?> baseQuickAdapter, View view, Integer integer) {
                Toast.makeText(TestActivity.this, "index " + integer, Toast.LENGTH_SHORT).show();
                return null;
            }
        });

        mAdapter.setNewData(list);
        mAdapter.disableLoadMoreIfNotFullPage();
    }

    private void initClick() {
        itemChangeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        asyncChangeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        notifyChangeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


}
