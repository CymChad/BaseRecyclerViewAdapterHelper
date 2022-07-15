//package com.chad.baserecyclerviewadapterhelper.activity.multi;
//
//import android.os.Bundle;
//
//import androidx.recyclerview.widget.GridLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.chad.baserecyclerviewadapterhelper.R;
//import com.chad.baserecyclerviewadapterhelper.adapter.multi.MultipleItemQuickAdapter;
//import com.chad.baserecyclerviewadapterhelper.base.BaseActivity;
//import com.chad.baserecyclerviewadapterhelper.data.DataServer;
//import com.chad.baserecyclerviewadapterhelper.entity.QuickMultipleEntity;
//import com.chad.library.adapter.base.listener.GridSpanSizeLookup;
//
//import java.util.List;
//
//public class MultiItemQuickUseActivity extends BaseActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_multiple_item_use);
//
//        setTitle("BaseMultiItemQuickAdapter");
//        setBackBtn();
//
//        RecyclerView mRecyclerView = findViewById(R.id.rv_list);
//
//        final List<QuickMultipleEntity> data = DataServer.getMultipleItemData();
//        final MultipleItemQuickAdapter multipleItemAdapter = new MultipleItemQuickAdapter(data);
//        final GridLayoutManager manager = new GridLayoutManager(this, 4);
//        mRecyclerView.setLayoutManager(manager);
//        multipleItemAdapter.setGridSpanSizeLookup(new GridSpanSizeLookup() {
//            @Override
//            public int getSpanSize(GridLayoutManager gridLayoutManager, int viewType, int position) {
//                return data.get(position).getSpanSize();
//            }
//        });
//        mRecyclerView.setAdapter(multipleItemAdapter);
//    }
//}
