package com.chad.baserecyclerviewadapterhelper.activity.multi;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.baserecyclerviewadapterhelper.R;
import com.chad.baserecyclerviewadapterhelper.adapter.multi.ProviderMultiAdapter;
import com.chad.baserecyclerviewadapterhelper.base.BaseActivity;
import com.chad.baserecyclerviewadapterhelper.data.DataServer;
import com.chad.baserecyclerviewadapterhelper.entity.ProviderMultiEntity;
import java.util.List;

/**
 * @author: limuyang
 * @date: 2019-12-04
 * @Description:
 */
public class MultiItemProviderUseActivity extends BaseActivity {

    private ProviderMultiAdapter adapter = new ProviderMultiAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple_item_use);

        setTitle("BaseMultiItemQuickAdapter");
        setBackBtn();

        initRv();
    }

    private void initRv() {
        RecyclerView mRecyclerView = findViewById(R.id.rv_list);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        final List<ProviderMultiEntity> data = DataServer.getProviderMultiItemData();
        adapter.setList(data);
    }
}
