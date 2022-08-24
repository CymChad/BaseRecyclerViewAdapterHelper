package com.chad.baserecyclerviewadapterhelper.activity.dragswipe;


import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.baserecyclerviewadapterhelper.R;
import com.chad.baserecyclerviewadapterhelper.activity.dragswipe.adapter.DragAndSwipeAdapter;
import com.chad.baserecyclerviewadapterhelper.base.BaseActivity;
import com.chad.baserecyclerviewadapterhelper.utils.Tips;
import com.chad.baserecyclerviewadapterhelper.utils.VibratorUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.QuickAdapterHelper;
import com.chad.library.adapter.base.dragswipe.QuickDragAndSwipe;
import com.chad.library.adapter.base.dragswipe.listener.OnItemDragListener;
import com.chad.library.adapter.base.dragswipe.listener.OnItemSwipeListener;
import com.chad.library.adapter.base.viewholder.QuickViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * 默认实现拖动与侧滑效果
 * Drag and Drag effects are implemented by default
 */
public class DefaultDragAndSwipeActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private DragAndSwipeAdapter mAdapter;
    private QuickAdapterHelper helper;
    QuickDragAndSwipe quickDragAndSwipe = new QuickDragAndSwipe()
            .setDragMoveFlags(ItemTouchHelper.UP | ItemTouchHelper.DOWN)
            .setSwipeMoveFlags(ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_universal_recycler);

        setBackBtn();
        setTitle("Default Drag And Swipe");

        mRecyclerView = findViewById(R.id.rv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 拖拽监听
        OnItemDragListener listener = new OnItemDragListener() {
            @Override
            public void onItemDragStart(@NonNull RecyclerView.ViewHolder viewHolder, int pos) {
                VibratorUtils.INSTANCE.vibrate(getApplicationContext());
                Log.d(TAG, "drag start");
                final QuickViewHolder holder = ((QuickViewHolder) viewHolder);
                // 开始时，item背景色变化，demo这里使用了一个动画渐变，使得自然
                int startColor = Color.WHITE;
                int endColor = Color.rgb(245, 245, 245);
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ValueAnimator v = ValueAnimator.ofArgb(startColor, endColor);
                    v.addUpdateListener(animation -> holder.itemView.setBackgroundColor((int) animation.getAnimatedValue()));
                    v.setDuration(300);
                    v.start();
                }
            }

            @Override
            public void onItemDragMoving(@NonNull RecyclerView.ViewHolder source, int from, @NonNull RecyclerView.ViewHolder target, int to) {
                Log.d(TAG, "move from: " + source.getBindingAdapterPosition() + " to: " + target.getBindingAdapterPosition());
            }

            @Override
            public void onItemDragEnd(@NonNull RecyclerView.ViewHolder viewHolder, int pos) {
                Log.d(TAG, "drag end");
                final QuickViewHolder holder = ((QuickViewHolder) viewHolder);
                // 结束时，item背景色变化，demo这里使用了一个动画渐变，使得自然
                int startColor = Color.rgb(245, 245, 245);
                int endColor = Color.WHITE;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ValueAnimator v = ValueAnimator.ofArgb(startColor, endColor);
                    v.addUpdateListener(animation -> holder.itemView.setBackgroundColor((int) animation.getAnimatedValue()));
                    v.setDuration(300);
                    v.start();
                }
            }
        };

        OnItemSwipeListener swipeListener = new OnItemSwipeListener() {
            @Override
            public void onItemSwipeStart(RecyclerView.ViewHolder viewHolder, int pos) {
                Log.d(TAG, "onItemSwipeStart");
            }

            @Override
            public void onItemSwipeEnd(RecyclerView.ViewHolder viewHolder, int pos) {
                Log.d(TAG,  "onItemSwipeEnd");
            }

            @Override
            public void onItemSwiped(RecyclerView.ViewHolder viewHolder, int pos) {
                Log.d(TAG,  "onItemSwiped");
            }

            @Override
            public void onItemSwipeMoving(Canvas canvas, RecyclerView.ViewHolder viewHolder, float dX, float dY, boolean isCurrentlyActive) {
                Log.d(TAG, "onItemSwipeMoving");
            }
        };

        List<String> mData = generateData(50);
        mAdapter = new DragAndSwipeAdapter();
        helper = new QuickAdapterHelper.Builder(mAdapter)
                .build();
        mRecyclerView.setAdapter(helper.getAdapter());
        mAdapter.submitList(mData);

        // 滑动事件
        quickDragAndSwipe.attachToRecyclerView(mRecyclerView)
                .setDataCallback(mAdapter)
                .setItemDragListener(listener)
                .setItemSwipeListener(swipeListener);

        // 点击事件
        mAdapter.setOnItemClickListener((BaseQuickAdapter.OnItemClickListener) (adapter, view, position) -> Tips.show("点击了：" + position));
    }

    private List<String> generateData(int size) {
        ArrayList<String> data = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            data.add("item " + i);
        }
        return data;
    }
}