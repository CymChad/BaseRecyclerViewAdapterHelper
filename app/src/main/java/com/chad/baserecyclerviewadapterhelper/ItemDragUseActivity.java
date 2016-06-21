package com.chad.baserecyclerviewadapterhelper;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;

import com.chad.baserecyclerviewadapterhelper.adapter.ItemDragAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.callback.ItemDraggableCallback;
import com.chad.library.adapter.base.listener.OnItemDragListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luoxw on 2016/6/20.
 */
public class ItemDragUseActivity extends Activity {
    private static final String TAG = "ItemDragUseActivity";
    private RecyclerView mRecyclerView;
    private List<String> mData;
    private ItemDragAdapter mAdapter;
    private ItemTouchHelper mItemTouchHelper;
    private ItemDraggableCallback mItemDraggableCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_touch_use);

        mRecyclerView = (RecyclerView)findViewById(R.id.rv_list);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mData = generateData(50);
        OnItemDragListener listener = new OnItemDragListener() {
            @Override
            public void onItemDragStart(RecyclerView.ViewHolder viewHolder) {
                Log.d(TAG, "drag start");
                BaseViewHolder holder = ((BaseViewHolder)viewHolder);
                holder.setTextColor(R.id.tv, Color.WHITE);
                ((CardView)viewHolder.itemView).setCardBackgroundColor(ContextCompat.getColor(ItemDragUseActivity.this, R.color.color_light_blue));
            }

            @Override
            public void onItemDragMoving(RecyclerView.ViewHolder source, RecyclerView.ViewHolder target) {
                Log.d(TAG, "move from: " + source.getAdapterPosition() + " to: " + target.getAdapterPosition());
            }

            @Override
            public void onItemDragEnd(RecyclerView.ViewHolder viewHolder) {
                Log.d(TAG, "drag end");
                BaseViewHolder holder = ((BaseViewHolder)viewHolder);
                holder.setTextColor(R.id.tv, Color.BLACK);
                ((CardView)viewHolder.itemView).setCardBackgroundColor(Color.WHITE);
            }
        };
        mAdapter = new ItemDragAdapter(mData);
        mItemDraggableCallback = new ItemDraggableCallback(mAdapter);
        mItemTouchHelper = new ItemTouchHelper(mItemDraggableCallback);
        mAdapter.enableDragItem(mItemTouchHelper);
        mAdapter.setOnItemDragListener(listener);
        mRecyclerView.setAdapter(mAdapter);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    private List<String> generateData(int size) {
        ArrayList<String> data = new ArrayList(size);
        for (int i = 0; i < size; i++) {
            data.add("item " + i);
        }
        return data;
    }

}
