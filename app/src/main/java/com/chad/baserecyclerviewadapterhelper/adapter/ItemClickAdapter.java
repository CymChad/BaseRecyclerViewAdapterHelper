package com.chad.baserecyclerviewadapterhelper.adapter;

import com.chad.baserecyclerviewadapterhelper.R;
import com.chad.baserecyclerviewadapterhelper.entity.ClickEntity;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 *
 */
public class ItemClickAdapter extends BaseMultiItemQuickAdapter<ClickEntity, BaseViewHolder> {

    public ItemClickAdapter(List<ClickEntity> data) {
//        super(R.layout.item_list_merchandise, data);
        super(data);
        addItemType(ClickEntity.CLICK_ITEM_VIEW, R.layout.item_text_view);
        addItemType(ClickEntity.CLICK_ITEM_CHILD_VIEW, R.layout.item_image_view);
        addItemType(ClickEntity.LONG_CLICK_ITEM_VIEW, R.layout.item_image_view);
        addItemType(ClickEntity.LONG_CLICK_ITEM_CHILD_VIEW, R.layout.item_image_view);
    }


    @Override
    protected void convert(final BaseViewHolder helper, final ClickEntity item) {
//        helper.addOnClickListener(R.id.iv_num_add)
//                .addOnClickListener(R.id.iv_num_reduce)
//                .addOnClickListener(R.id.iv_photo);
        switch (helper.getItemViewType()) {
            case ClickEntity.CLICK_ITEM_VIEW:
                break;
            case ClickEntity.CLICK_ITEM_CHILD_VIEW:
                // set img data
                break;
            case ClickEntity.LONG_CLICK_ITEM_VIEW:
                break;
            case ClickEntity.LONG_CLICK_ITEM_CHILD_VIEW:
                break;
        }
    }
}
