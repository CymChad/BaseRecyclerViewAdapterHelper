package com.chad.baserecyclerviewadapterhelper.activity.treenode.adapter;

import android.content.Context;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.chad.baserecyclerviewadapterhelper.R;
import com.chad.baserecyclerviewadapterhelper.entity.MyNodeEntity;
import com.chad.library.adapter.base.BaseNode;
import com.chad.library.adapter.base.BaseTreeNodeAdapter;
import com.chad.library.adapter.base.dragswipe.listener.DragAndSwipeDataCallback;
import com.chad.library.adapter.base.viewholder.QuickViewHolder;

/**
 * @author Dboy233
 */
public class TreeNodeAdapter extends BaseTreeNodeAdapter implements DragAndSwipeDataCallback {

    public final static int TYPE_FOLDER = 1;

    public final static int TYPE_FILE = 2;

    public final static int TYPE_LOAD_MORE = 3;


    public TreeNodeAdapter() {
        addItemType(TYPE_FILE, new OnMultiItemAdapterListener<BaseNode, QuickViewHolder>() {
            @NonNull
            @Override
            public QuickViewHolder onCreate(@NonNull Context context, @NonNull ViewGroup parent,
                    int viewType) {
                return new QuickViewHolder(R.layout.item_file, parent);
            }

            @Override
            public void onBind(@NonNull QuickViewHolder holder, int position,
                    @Nullable BaseNode item) {
                if (item instanceof MyNodeEntity) {
                    holder.setText(R.id.tv, ((MyNodeEntity) item).getName())
                            .setText(R.id.sub_tv, ((MyNodeEntity) item).getTime().toString());
                }
            }
        }).addItemType(TYPE_FOLDER, new OnMultiItemAdapterListener<BaseNode, QuickViewHolder>() {
            @NonNull
            @Override
            public QuickViewHolder onCreate(@NonNull Context context, @NonNull ViewGroup parent,
                    int viewType) {
                return new QuickViewHolder(R.layout.item_folder, parent);
            }

            @Override
            public void onBind(@NonNull QuickViewHolder holder, int position,
                    @Nullable BaseNode item) {
                if (item==null) {
                    return;
                }
                if (item instanceof MyNodeEntity) {
                    holder.setText(R.id.tv, ((MyNodeEntity) item).getName());
                }

                int angle = item.isExpand() ? 0 : -90;
                holder.getView(R.id.iv).setRotation(angle);

                int nodeDeep = getNodeDepth(item);
                switch (nodeDeep) {
                    case 2 -> holder.setImageResource(R.id.iv_head, R.mipmap.head_img1);
                    case 3 -> holder.setImageResource(R.id.iv_head, R.mipmap.head_img2);
                    default -> holder.setImageResource(R.id.iv_head, R.mipmap.head_img0);
                }
            }
        });

        //加载更多
        addItemType(TYPE_LOAD_MORE, new OnMultiItemAdapterListener<BaseNode, QuickViewHolder>() {
            @Override
            public void onBind(@NonNull QuickViewHolder holder, int position,
                    @Nullable BaseNode item) {
            }
            @NonNull
            @Override
            public QuickViewHolder onCreate(@NonNull Context context, @NonNull ViewGroup parent,
                    int viewType) {
                return new QuickViewHolder(R.layout.view_load_more,parent);
            }
        });
    }

    @Override
    public void dataSwap(int fromPosition, int toPosition) {
        swap(fromPosition, toPosition);
    }

    @Override
    public void dataRemoveAt(int position) {
        removeAt(position);
    }
}
