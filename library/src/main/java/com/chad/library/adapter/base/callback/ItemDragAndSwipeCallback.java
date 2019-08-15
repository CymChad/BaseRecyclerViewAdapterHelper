package com.chad.library.adapter.base.callback;

import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.chad.library.R;
import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.DraggableController;
import com.chad.library.adapter.base.listener.IDraggableListener;


/**
 * @author luoxw
 * @date 2016/6/20
 */
public class ItemDragAndSwipeCallback extends ItemTouchHelper.Callback {


    private IDraggableListener mDraggableListener;
    private BaseItemDraggableAdapter mBaseItemDraggableAdapter;
    private float mMoveThreshold = 0.1f;
    private float mSwipeThreshold = 0.7f;


    private int mDragMoveFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
    private int mSwipeMoveFlags = ItemTouchHelper.END;

    public ItemDragAndSwipeCallback(BaseItemDraggableAdapter adapter) {
        mBaseItemDraggableAdapter = adapter;
    }

    public ItemDragAndSwipeCallback(DraggableController draggableController) {
        mDraggableListener = draggableController;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        if (mBaseItemDraggableAdapter != null) {
            return mBaseItemDraggableAdapter.isItemDraggable() && !mBaseItemDraggableAdapter.hasToggleView();
        } else if (mDraggableListener != null) {
            return mDraggableListener.isItemDraggable() && !mDraggableListener.hasToggleView();
        }
        return false;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        if (mBaseItemDraggableAdapter != null) {
            return mBaseItemDraggableAdapter.isItemSwipeEnable();
        } else if (mDraggableListener != null) {
            return mDraggableListener.isItemSwipeEnable();
        }
        return false;
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG
                && !isViewCreateByAdapter(viewHolder)) {
            if (mBaseItemDraggableAdapter != null) {
                mBaseItemDraggableAdapter.onItemDragStart(viewHolder);
            } else if (mDraggableListener != null) {
                mDraggableListener.onItemDragStart(viewHolder);
            }
            viewHolder.itemView.setTag(R.id.BaseQuickAdapter_dragging_support, true);
        } else if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE
                && !isViewCreateByAdapter(viewHolder)) {
            if (mBaseItemDraggableAdapter != null) {
                mBaseItemDraggableAdapter.onItemSwipeStart(viewHolder);
            } else if (mDraggableListener != null) {
                mDraggableListener.onItemSwipeStart(viewHolder);
            }
            viewHolder.itemView.setTag(R.id.BaseQuickAdapter_swiping_support, true);
        }
        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        if (isViewCreateByAdapter(viewHolder)) {
            return;
        }

        if (viewHolder.itemView.getTag(R.id.BaseQuickAdapter_dragging_support) != null
                && (Boolean) viewHolder.itemView.getTag(R.id.BaseQuickAdapter_dragging_support)) {
            if (mBaseItemDraggableAdapter != null) {
                mBaseItemDraggableAdapter.onItemDragEnd(viewHolder);
            } else if (mDraggableListener != null) {
                mDraggableListener.onItemDragEnd(viewHolder);
            }
            viewHolder.itemView.setTag(R.id.BaseQuickAdapter_dragging_support, false);
        }
        if (viewHolder.itemView.getTag(R.id.BaseQuickAdapter_swiping_support) != null
                && (Boolean) viewHolder.itemView.getTag(R.id.BaseQuickAdapter_swiping_support)) {
            if (mBaseItemDraggableAdapter != null) {
                mBaseItemDraggableAdapter.onItemSwipeClear(viewHolder);
            } else if (mDraggableListener != null) {
                mDraggableListener.onItemSwipeClear(viewHolder);
            }
            viewHolder.itemView.setTag(R.id.BaseQuickAdapter_swiping_support, false);
        }
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        if (isViewCreateByAdapter(viewHolder)) {
            return makeMovementFlags(0, 0);
        }

        return makeMovementFlags(mDragMoveFlags, mSwipeMoveFlags);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder source, @NonNull RecyclerView.ViewHolder target) {
        return source.getItemViewType() == target.getItemViewType();
    }

    @Override
    public void onMoved(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder source, int fromPos, @NonNull RecyclerView.ViewHolder target, int toPos, int x, int y) {
        super.onMoved(recyclerView, source, fromPos, target, toPos, x, y);
        if (mBaseItemDraggableAdapter != null) {
            mBaseItemDraggableAdapter.onItemDragMoving(source, target);
        } else if (mDraggableListener != null) {
            mDraggableListener.onItemDragMoving(source, target);
        }
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        if (!isViewCreateByAdapter(viewHolder)) {
            if (mBaseItemDraggableAdapter != null) {
                mBaseItemDraggableAdapter.onItemSwiped(viewHolder);
            } else if (mDraggableListener != null) {
                mDraggableListener.onItemSwiped(viewHolder);
            }
        }
    }

    @Override
    public float getMoveThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
        return mMoveThreshold;
    }

    @Override
    public float getSwipeThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
        return mSwipeThreshold;
    }

    /**
     * Set the fraction that the user should move the View to be considered as swiped.
     * The fraction is calculated with respect to RecyclerView's bounds.
     * <p>
     * Default value is .5f, which means, to swipe a View, user must move the View at least
     * half of RecyclerView's width or height, depending on the swipe direction.
     *
     * @param swipeThreshold A float value that denotes the fraction of the View size. Default value
     *                       is .8f .
     */
    public void setSwipeThreshold(float swipeThreshold) {
        mSwipeThreshold = swipeThreshold;
    }


    /**
     * Set the fraction that the user should move the View to be considered as it is
     * dragged. After a view is moved this amount, ItemTouchHelper starts checking for Views
     * below it for a possible drop.
     *
     * @param moveThreshold A float value that denotes the fraction of the View size. Default value is
     *                      .1f .
     */
    public void setMoveThreshold(float moveThreshold) {
        mMoveThreshold = moveThreshold;
    }

    /**
     * <p>Set the drag movement direction.</p>
     * <p>The value should be ItemTouchHelper.UP, ItemTouchHelper.DOWN, ItemTouchHelper.LEFT, ItemTouchHelper.RIGHT or their combination.</p>
     * You can combine them like ItemTouchHelper.UP | ItemTouchHelper.DOWN, it means that the item could only move up and down when dragged.
     *
     * @param dragMoveFlags the drag movement direction. Default value is ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT.
     */

    public void setDragMoveFlags(int dragMoveFlags) {
        mDragMoveFlags = dragMoveFlags;
    }

    /**
     * <p>Set the swipe movement direction.</p>
     * <p>The value should be ItemTouchHelper.START, ItemTouchHelper.END or their combination.</p>
     * You can combine them like ItemTouchHelper.START | ItemTouchHelper.END, it means that the item could swipe to both left or right.
     *
     * @param swipeMoveFlags the swipe movement direction. Default value is ItemTouchHelper.END.
     */
    public void setSwipeMoveFlags(int swipeMoveFlags) {
        mSwipeMoveFlags = swipeMoveFlags;
    }

    @Override
    public void onChildDrawOver(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                                float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDrawOver(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE
                && !isViewCreateByAdapter(viewHolder)) {
            View itemView = viewHolder.itemView;

            c.save();
            if (dX > 0) {
                c.clipRect(itemView.getLeft(), itemView.getTop(),
                        itemView.getLeft() + dX, itemView.getBottom());
                c.translate(itemView.getLeft(), itemView.getTop());
            } else {
                c.clipRect(itemView.getRight() + dX, itemView.getTop(),
                        itemView.getRight(), itemView.getBottom());
                c.translate(itemView.getRight() + dX, itemView.getTop());
            }
            if (mBaseItemDraggableAdapter != null) {
                mBaseItemDraggableAdapter.onItemSwiping(c, viewHolder, dX, dY, isCurrentlyActive);
            } else if (mDraggableListener != null) {
                mDraggableListener.onItemSwiping(c, viewHolder, dX, dY, isCurrentlyActive);
            }
            c.restore();

        }
    }

    private boolean isViewCreateByAdapter(@NonNull RecyclerView.ViewHolder viewHolder) {
        int type = viewHolder.getItemViewType();
        return type == BaseQuickAdapter.HEADER_VIEW || type == BaseQuickAdapter.LOADING_VIEW
                || type == BaseQuickAdapter.FOOTER_VIEW || type == BaseQuickAdapter.EMPTY_VIEW;
    }
}
