//package com.chad.baserecyclerviewadapterhelper.activity;
//
//import android.animation.ValueAnimator;
//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//
//import androidx.annotation.NonNull;
//import androidx.core.content.ContextCompat;
//import androidx.recyclerview.widget.ItemTouchHelper;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.chad.baserecyclerviewadapterhelper.R;
//import com.chad.baserecyclerviewadapterhelper.adapter.DragAndSwipeAdapter;
//import com.chad.baserecyclerviewadapterhelper.base.BaseActivity;
//import com.chad.baserecyclerviewadapterhelper.utils.Tips;
//import com.chad.library.adapter.base.BaseQuickAdapter;
//import com.chad.library.adapter.base.listener.OnItemClickListener;
//import com.chad.library.adapter.base.listener.OnItemDragListener;
//import com.chad.library.adapter.base.listener.OnItemSwipeListener;
//import com.chad.library.adapter.base.viewholder.BaseViewHolder;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class DragAndSwipeUseActivity extends BaseActivity {
//
//    private RecyclerView mRecyclerView;
//    private DragAndSwipeAdapter mAdapter;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_universal_recycler);
//
//        setBackBtn();
//        setTitle("Drag And Swipe");
//
//        mRecyclerView = findViewById(R.id.rv);
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//        // 拖拽监听
//        OnItemDragListener listener = new OnItemDragListener() {
//            @Override
//            public void onItemDragStart(RecyclerView.ViewHolder viewHolder, int pos) {
//                Log.d(TAG, "drag start");
//                final BaseViewHolder holder = ((BaseViewHolder) viewHolder);
//
//                // 开始时，item背景色变化，demo这里使用了一个动画渐变，使得自然
//                int startColor = Color.WHITE;
//                int endColor = Color.rgb(245, 245, 245);
//                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//                    ValueAnimator v = ValueAnimator.ofArgb(startColor, endColor);
//                    v.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                        @Override
//                        public void onAnimationUpdate(ValueAnimator animation) {
//                            holder.itemView.setBackgroundColor((int)animation.getAnimatedValue());
//                        }
//                    });
//                    v.setDuration(300);
//                    v.start();
//                }
//            }
//
//            @Override
//            public void onItemDragMoving(RecyclerView.ViewHolder source, int from, RecyclerView.ViewHolder target, int to) {
//                Log.d(TAG, "move from: " + source.getAdapterPosition() + " to: " + target.getAdapterPosition());
//            }
//
//            @Override
//            public void onItemDragEnd(RecyclerView.ViewHolder viewHolder, int pos) {
//                Log.d(TAG, "drag end");
//                final BaseViewHolder holder = ((BaseViewHolder) viewHolder);
//                // 结束时，item背景色变化，demo这里使用了一个动画渐变，使得自然
//                int startColor = Color.rgb(245, 245, 245);
//                int endColor = Color.WHITE;
//                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//                    ValueAnimator v = ValueAnimator.ofArgb(startColor, endColor);
//                    v.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                        @Override
//                        public void onAnimationUpdate(ValueAnimator animation) {
//                            holder.itemView.setBackgroundColor((int)animation.getAnimatedValue());
//                        }
//                    });
//                    v.setDuration(300);
//                    v.start();
//                }
//            }
//        };
//
//        // 侧滑监听
//        OnItemSwipeListener onItemSwipeListener = new OnItemSwipeListener() {
//            @Override
//            public void onItemSwipeStart(RecyclerView.ViewHolder viewHolder, int pos) {
//                Log.d(TAG, "view swiped start: " + pos);
//                BaseViewHolder holder = ((BaseViewHolder) viewHolder);
//            }
//
//            @Override
//            public void clearView(RecyclerView.ViewHolder viewHolder, int pos) {
//                Log.d(TAG, "View reset: " + pos);
//                BaseViewHolder holder = ((BaseViewHolder) viewHolder);
//            }
//
//            @Override
//            public void onItemSwiped(RecyclerView.ViewHolder viewHolder, int pos) {
//                Log.d(TAG, "View Swiped: " + pos);
//            }
//
//            @Override
//            public void onItemSwipeMoving(Canvas canvas, RecyclerView.ViewHolder viewHolder, float dX, float dY, boolean isCurrentlyActive) {
//                canvas.drawColor(ContextCompat.getColor(DragAndSwipeUseActivity.this, R.color.color_light_blue));
//            }
//        };
//
//        List<String> mData = generateData(50);
//        mAdapter = new DragAndSwipeAdapter(mData);
//
//        mAdapter.getDraggableModule().setSwipeEnabled(true);
//        mAdapter.getDraggableModule().setDragEnabled(true);
//        mAdapter.getDraggableModule().setOnItemDragListener(listener);
//        mAdapter.getDraggableModule().setOnItemSwipeListener(onItemSwipeListener);
//        mAdapter.getDraggableModule().getItemTouchHelperCallback().setSwipeMoveFlags(ItemTouchHelper.START | ItemTouchHelper.END);
//        //mAdapter.getDraggableModule().getItemTouchHelperCallback().setDragMoveFlags(ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.UP | ItemTouchHelper.DOWN);
//        mRecyclerView.setAdapter(mAdapter);
//
//
//        mAdapter.setOnItemClickListener(new OnItemClickListener() {
//            @Override
//            public void onItemClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
//                Tips.show("点击了：" + position);
//            }
//        });
//    }
//
//    private List<String> generateData(int size) {
//        ArrayList<String> data = new ArrayList<>(size);
//        for (int i = 0; i < size; i++) {
//            data.add("item " + i);
//        }
//        return data;
//    }
//}
