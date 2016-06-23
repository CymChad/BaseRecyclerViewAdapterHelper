package com.chad.library.adapter.base.callback;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.chad.library.adapter.base.BaseQuickAdapter;

/**
 * Created by luoxw on 2016/6/20.
 */
public class ItemDraggableCallback extends ItemTouchHelper.Callback {

    private static final String TAG = "ItemDraggableCallback";

    private BaseQuickAdapter mAdapter;

    private static final float THRESHOLD_SWIPE = 0.7f;
    private static final float THRESHOLD_MOVE = 0.1f;

    private int mActionState = ItemTouchHelper.ACTION_STATE_IDLE;

    public ItemDraggableCallback(BaseQuickAdapter adapter) {
        mAdapter = adapter;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return mAdapter.isItemSwipeEnable();
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
            mAdapter.onItemDragStart(viewHolder);
            mActionState = actionState;
        } else if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            mAdapter.onItemSwipeStart(viewHolder);
            mActionState = actionState;
        }
        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);

        if (mActionState == ItemTouchHelper.ACTION_STATE_DRAG) {
            mAdapter.onItemDragEnd(viewHolder);
        } else if (mActionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            mAdapter.onItemSwipeClear(viewHolder);
        }
        mActionState = ItemTouchHelper.ACTION_STATE_IDLE;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        int swipeFlags = ItemTouchHelper.END;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder source, RecyclerView.ViewHolder target) {
        if (source.getItemViewType() != target.getItemViewType()) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onMoved(RecyclerView recyclerView, RecyclerView.ViewHolder source, int fromPos, RecyclerView.ViewHolder target, int toPos, int x, int y) {
        mAdapter.onItemDragMoving(source, target);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        mAdapter.onItemSwiped(viewHolder);
    }

    @Override
    public float getMoveThreshold(RecyclerView.ViewHolder viewHolder) {
        return THRESHOLD_MOVE;
    }

    @Override
    public float getSwipeThreshold(RecyclerView.ViewHolder viewHolder) {
        return THRESHOLD_SWIPE;
    }
}
