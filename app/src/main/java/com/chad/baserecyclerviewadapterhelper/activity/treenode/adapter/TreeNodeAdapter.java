package com.chad.baserecyclerviewadapterhelper.activity.treenode.adapter;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.baserecyclerviewadapterhelper.R;
import com.chad.baserecyclerviewadapterhelper.entity.FileNodeEntity;
import com.chad.baserecyclerviewadapterhelper.entity.FolderNodeEntity;
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



    private static class VHFolder extends QuickViewHolder {
        public VHFolder(int resId, @NonNull ViewGroup parent) {
            super(resId, parent);
        }
    }

    private static class VHFile extends QuickViewHolder {

        public VHFile(int resId, @NonNull ViewGroup parent) {
            super(resId, parent);
        }
    }

    public TreeNodeAdapter() {

        addItemType(TYPE_FILE, VHFile.class, new OnMultiItemAdapterListener<BaseNode, VHFile>() {
            @NonNull
            @Override
            public VHFile onCreate(@NonNull Context context, @NonNull ViewGroup parent, int viewType) {
                return new VHFile(R.layout.item_file, parent);
            }

            @Override
            public void onBind(@NonNull VHFile holder, int position, @Nullable BaseNode item) {
                if (item instanceof MyNodeEntity) {
                    holder.setText(R.id.tv, ((MyNodeEntity) item).getName())
                            .setText(R.id.sub_tv, ((MyNodeEntity) item).getTime().toString());
                }

//                if (item instanceof FileNodeEntity) {
//                    holder.setText(R.id.tv, ((FileNodeEntity) item).getName())
//                            .setText(R.id.sub_tv, ((FileNodeEntity) item).getTime().toString());
//                }

            }
        }).addItemType(TYPE_FOLDER, VHFolder.class, new OnMultiItemAdapterListener<BaseNode, VHFolder>() {
            @NonNull
            @Override
            public VHFolder onCreate(@NonNull Context context, @NonNull ViewGroup parent, int viewType) {
                return new VHFolder(R.layout.item_folder, parent);
            }

            @Override
            public void onBind(@NonNull VHFolder holder, int position, @Nullable BaseNode item) {
                if (item instanceof MyNodeEntity){
                    holder.setText(R.id.tv, ((MyNodeEntity) item).getName());
                }

//                if (item instanceof FolderNodeEntity) {
//                    holder.setText(R.id.tv, ((FolderNodeEntity) item).getName());
//                }



                int angle = item.isExpand() ? 0 : -90;
                holder.getView(R.id.iv).setRotation(angle);

                int nodeDeep = getNodeDepth(item);
                switch (nodeDeep) {
                    case 2:
                        holder.setImageResource(R.id.iv_head, R.mipmap.head_img1);
                        break;
                    case 3:
                        holder.setImageResource(R.id.iv_head, R.mipmap.head_img2);
                        break;
                    default:
                        holder.setImageResource(R.id.iv_head, R.mipmap.head_img0);
                        break;
                }
            }
        });
    }

    @Override
    public void dataSwap(int fromPosition, int toPosition) {
        swap(fromPosition,toPosition);
    }

    @Override
    public void dataRemoveAt(int position) {
        removeAt(position);
    }
}
