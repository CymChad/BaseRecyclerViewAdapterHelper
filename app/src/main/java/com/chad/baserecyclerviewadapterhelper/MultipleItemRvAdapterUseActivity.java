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
                if (type == NormalMultipleEntity.SINGLE_TEXT) {
                    return MultipleItem.TEXT_SPAN_SIZE;
                } else if (type == NormalMultipleEntity.SINGLE_IMG) {
                    return MultipleItem.IMG_SPAN_SIZE;
                } else {
                    return MultipleItem.IMG_TEXT_SPAN_SIZE;
                }
            }
        });

/**
 *      The click event is distributed to the BaseItemProvider and can be overridden.
 *      if you need register itemchild click longClick
 *      you need to use https://github.com/CymChad/BaseRecyclerViewAdapterHelper/wiki/Add-OnItemClickLister#use-it-item-child-long-click
 */
  /*      @Override
        protected void convert(BaseViewHolder helper, Status item) {
            helper.setText(R.id.tweetName, item.getUserName())
                    .setText(R.id.tweetText, item.getText())
                    .setText(R.id.tweetDate, item.getCreatedAt())
                    .setVisible(R.id.tweetRT, item.isRetweet())
                    .addOnLongClickListener(R.id.tweetText)
                    .linkify(R.id.tweetText);

        }
        adapter.setOnItemChildLongClickListener(new BaseQuickAdapter.OnItemChildLongClickListener() {
            @Override
            public void onItemChildLongClick(BaseQuickAdapter adapter, View view, int position) {
                Log.d(TAG, "onItemChildLongClick: ");
                Toast.makeText(ItemClickActivity.this, "onItemChildLongClick" + position, Toast.LENGTH_SHORT).show();
            }
        });*/
//        multipleItemAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                Log.d(TAG, "onItemClick: ");
//                Toast.makeText(MultipleItemRvAdapterUseActivity.this, "onItemClick" + position, Toast.LENGTH_SHORT).show();
//            }
//        });
//        multipleItemAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
//            @Override
//            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
//                Log.d(TAG, "onItemClick: ");
//                Toast.makeText(MultipleItemRvAdapterUseActivity.this, "onItemChildClick" + view.getId(), Toast.LENGTH_SHORT).show();
//            }
//        });
        mRecyclerView.setAdapter(multipleItemAdapter);
    }
}
