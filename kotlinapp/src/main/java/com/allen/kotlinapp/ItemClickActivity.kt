package com.allen.kotlinapp

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.MotionEvent
import android.widget.Toast
import com.allen.kotlinapp.adapter.ItemClickAdapter
import com.allen.kotlinapp.base.BaseActivity
import com.allen.kotlinapp.entity.ClickEntity
import com.chad.library.adapter.base.BaseQuickAdapter
import java.util.*

/**
 * 文 件 名: ItemClickActivity
 * 创 建 人: Allen
 * 创建日期: 2017/6/20 11:20
 * 修改时间：
 * 修改备注：
 */
class ItemClickActivity : BaseActivity() {

    private var mRecyclerView: RecyclerView? = null
    private var adapter: ItemClickAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setBackBtn()
        setTitle("ItemClickActivity Activity")
        setContentView(R.layout.activity_item_click)
        mRecyclerView = findViewById(R.id.list) as RecyclerView
        mRecyclerView?.layoutManager = LinearLayoutManager(this)
        initAdapter()
        adapter?.onItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter, view, position ->
            Log.d(TAG, "onItemClick: ")
            Toast.makeText(this@ItemClickActivity, "onItemClick" + position, Toast.LENGTH_SHORT).show()
        }
        adapter?.onItemLongClickListener = BaseQuickAdapter.OnItemLongClickListener { adapter, view, position ->
            Log.d(TAG, "onItemLongClick: ")
            Toast.makeText(this@ItemClickActivity, "onItemLongClick" + position, Toast.LENGTH_SHORT).show()
            true
        }
        adapter?.onItemChildClickListener = BaseQuickAdapter.OnItemChildClickListener { adapter, view, position ->
            Log.d(TAG, "onItemChildClick: ")
            Toast.makeText(this@ItemClickActivity, "onItemChildClick" + position, Toast.LENGTH_SHORT).show()
        }
        adapter?.onItemChildLongClickListener = BaseQuickAdapter.OnItemChildLongClickListener { adapter, view, position ->
            Log.d(TAG, "onItemChildLongClick: ")
            Toast.makeText(this@ItemClickActivity, "onItemChildLongClick" + position, Toast.LENGTH_SHORT).show()
            true
        }
        /**
         * you can also use this way to solve your click Event
         */
        //        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
        //            /**
        //             * Callback method to be invoked when an item in this AdapterView has
        //             * been clicked.
        //             *
        //             * @param view     The view within the AdapterView that was clicked (this
        //             *                 will be a view provided by the adapter)
        //             * @param position The position of the view in the adapter.
        //             */
        //            @Override
        //            public void onSimpleItemClick(final BaseQuickAdapter adapter, final View view, final int position) {
        //                Log.d(TAG, "SimpleOnItemClick: ");
        //
        //            }
        //            /**
        //             * callback method to be invoked when an chidview in this view has been
        //             * click and held
        //             *
        //             * @param view     The view whihin the AbsListView that was clicked
        //             * @param position The position of the view int the adapter
        //             * @return true if the callback consumed the long click ,false otherwise
        //             */
        //            @Override
        //            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        //                Logger.d("onItemChildClick "+position+" be click");
        //                Toast.makeText(ItemClickActivity.this, "onItemChildClick" + position, Toast.LENGTH_SHORT).show();
        //
        //            }
        //
        //            /**
        //             * Callback method to be invoked when an item in this view has been clicked and held.
        //             * @param adapter
        //             * @param view
        //             * @param position
        //             */
        //            @Override
        //            public void onItemLongClick(final BaseQuickAdapter adapter, final View view, final int position) {
        //                Toast.makeText(ItemClickActivity.this, "onItemLongClick" + position, Toast.LENGTH_SHORT).show();
        //            }
        //            /**
        //             * Callback method to be invoked when an itemchild in this view has been clicked and held.
        //             * @param adapter
        //             * @param view
        //             * @param position
        //             */
        //            @Override
        //            public void onItemChildLongClick(final BaseQuickAdapter adapter, final View view, final int position) {
        //                Toast.makeText(ItemClickActivity.this, "onItemChildLongClick" + position, Toast.LENGTH_SHORT).show();
        //            }
        //        });


    }

    private fun initAdapter() {
        val data = ArrayList<ClickEntity>()
        data.add(ClickEntity(ClickEntity.CLICK_ITEM_VIEW))
        data.add(ClickEntity(ClickEntity.CLICK_ITEM_CHILD_VIEW))
        data.add(ClickEntity(ClickEntity.LONG_CLICK_ITEM_VIEW))
        data.add(ClickEntity(ClickEntity.LONG_CLICK_ITEM_CHILD_VIEW))
        data.add(ClickEntity(ClickEntity.NEST_CLICK_ITEM_CHILD_VIEW))
        adapter = ItemClickAdapter(data)
        adapter?.openLoadAnimation()
        mRecyclerView?.adapter = adapter
    }


    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        return super.dispatchTouchEvent(ev)
    }

    companion object {
        private val PAGE_SIZE = 10
        private val TAG = "ItemClickActivity"
    }

}
