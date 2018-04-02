package com.chad.baserecyclerviewadapterhelper;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.baserecyclerviewadapterhelper.adapter.DemoMultipleItemRvAdapter;
import com.chad.baserecyclerviewadapterhelper.base.BaseActivity;
import com.chad.baserecyclerviewadapterhelper.data.DataServer;
import com.chad.baserecyclerviewadapterhelper.entity.MultipleItem;
import com.chad.baserecyclerviewadapterhelper.entity.NormalMultipleEntity;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

/**
 * https://github.com/chaychan
 *
 * @author ChayChan
 * @description: MultipleItemRvAdapter's usage
 * @date 2018/3/30  10:54
 */

public class MultipleItemRvAdapterUseActivity extends BaseActivity {

    private List<NormalMultipleEntity> mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple_item_use);

        setTitle("MultipleItemRvAdapter");
        setBackBtn();

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);
        GridLayoutManager manager = new GridLayoutManager(this, 4);
        mRecyclerView.setLayoutManager(manager);

        mData = DataServer.getNormalMultipleEntities();
        DemoMultipleItemRvAdapter multipleItemAdapter = new DemoMultipleItemRvAdapter(mData);

        multipleItemAdapter.setSpanSizeLookup(new BaseQuickAdapter.SpanSizeLookup() {
            @Override
            public int getSpanSize(GridLayoutManager gridLayoutManager, int position) {
                int type = mData.get(position).type;
                if (type == NormalMultipleEntity.SINGLE_TEXT){
                    return MultipleItem.TEXT_SPAN_SIZE;
                }else if (type == NormalMultipleEntity.SINGLE_IMG){
                    return MultipleItem.IMG_SPAN_SIZE;
                }else{
                    return MultipleItem.IMG_TEXT_SPAN_SIZE;
                }
            }
        });

        mRecyclerView.setAdapter(multipleItemAdapter);
    }
}
