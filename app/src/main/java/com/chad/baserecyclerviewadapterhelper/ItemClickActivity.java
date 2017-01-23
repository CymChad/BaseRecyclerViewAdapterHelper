package com.chad.baserecyclerviewadapterhelper;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.chad.baserecyclerviewadapterhelper.adapter.ItemClickAdapter;
import com.chad.baserecyclerviewadapterhelper.base.BaseActivity;
import com.chad.baserecyclerviewadapterhelper.entity.ClickEntity;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

public class ItemClickActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private ItemClickAdapter itemClickAdapter;
    private static final int PAGE_SIZE = 10;
    private static String TAG = "RecyclerClickItemActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBackBtn();
        setTitle("ItemClickActivity Activity");
        setContentView(R.layout.activity_item_click);
        mRecyclerView = (RecyclerView) findViewById(R.id.list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        initAdapter();
        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            /**
             * Callback method to be invoked when an item in this AdapterView has
             * been clicked.
             *
             * @param view     The view within the AdapterView that was clicked (this
             *                 will be a view provided by the adapter)
             * @param position The position of the view in the adapter.
             */
            @Override
            public void onSimpleItemClick(final BaseQuickAdapter adapter, final View view, final int position) {
                Log.d(TAG, "SimpleOnItemClick: ");
                Toast.makeText(ItemClickActivity.this, "onSimpleItemClick" + position, Toast.LENGTH_SHORT).show();
            }
            /**
             * callback method to be invoked when an chidview in this view has been
             * click and held
             *
             * @param view     The view whihin the AbsListView that was clicked
             * @param position The position of the view int the adapter
             * @return true if the callback consumed the long click ,false otherwise
             */
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Logger.d("onItemChildClick "+position+" be click");
                Toast.makeText(ItemClickActivity.this, "onItemChildClick" + position, Toast.LENGTH_SHORT).show();

            }

            /**
             * Callback method to be invoked when an item in this view has been clicked and held.
             * @param adapter
             * @param view
             * @param position
             */
            @Override
            public void onItemLongClick(final BaseQuickAdapter adapter, final View view, final int position) {
                Toast.makeText(ItemClickActivity.this, "onItemLongClick" + position, Toast.LENGTH_SHORT).show();
            }
            /**
             * Callback method to be invoked when an itemchild in this view has been clicked and held.
             * @param adapter
             * @param view
             * @param position
             */
            @Override
            public void onItemChildLongClick(final BaseQuickAdapter adapter, final View view, final int position) {
                Toast.makeText(ItemClickActivity.this, "onItemChildLongClick" + position, Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void initAdapter() {
        List<ClickEntity> data = new ArrayList<>();
        data.add(new ClickEntity(ClickEntity.CLICK_ITEM_VIEW));
        data.add(new ClickEntity(ClickEntity.CLICK_ITEM_CHILD_VIEW));
        data.add(new ClickEntity(ClickEntity.LONG_CLICK_ITEM_VIEW));
        data.add(new ClickEntity(ClickEntity.LONG_CLICK_ITEM_CHILD_VIEW));
        data.add(new ClickEntity(ClickEntity.NEST_CLICK_ITEM_CHILD_VIEW));
        itemClickAdapter = new ItemClickAdapter(data);
        itemClickAdapter.openLoadAnimation();
        mRecyclerView.setAdapter(itemClickAdapter);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

}
