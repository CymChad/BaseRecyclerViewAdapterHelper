package com.chad.baserecyclerviewadapterhelper.adapter;

import android.content.Context;

import com.chad.baserecyclerviewadapterhelper.R;
import com.chad.baserecyclerviewadapterhelper.entity.MultipleItem;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.DraggableController;

import java.util.List;

/**
 * <pre>
 *     @author : xyk
 *     e-mail : yaxiaoke@163.com
 *     time   : 2019/07/25
 *     desc   : 多类型 + 拖拽
 *     version: 1.0
 * </pre>
 */
public class MultipleItemAndDragQuickAdapter extends BaseMultiItemQuickAdapter<MultipleItem, BaseViewHolder> {

    private DraggableController mDraggableController;

    public MultipleItemAndDragQuickAdapter(Context context, List data) {
        super(data);
        addItemType(MultipleItem.TEXT, R.layout.item_text_view);
        addItemType(MultipleItem.IMG, R.layout.item_image_view);
        addItemType(MultipleItem.IMG_TEXT, R.layout.item_img_text_view);
        mDraggableController = new DraggableController(this);

    }

    @Override
    protected void convert(BaseViewHolder helper, MultipleItem item) {
        mDraggableController.initView(helper);
        switch (helper.getItemViewType()) {
            case MultipleItem.TEXT:
                helper.setText(R.id.tv, item.getContent());
                break;
            case MultipleItem.IMG_TEXT:
                switch (helper.getLayoutPosition() %
                        2) {
                    case 0:
                        helper.setImageResource(R.id.iv, R.mipmap.animation_img1);
                        break;
                    case 1:
                        helper.setImageResource(R.id.iv, R.mipmap.animation_img2);
                        break;
                    default:
                        break;

                }
                break;
            default:
                break;
        }
    }

    public DraggableController getDraggableController() {
        return mDraggableController;
    }
}
