package com.chad.baserecyclerviewadapterhelper.adapter;

import com.chad.baserecyclerviewadapterhelper.R;
import com.chad.baserecyclerviewadapterhelper.entity.ExpandableLv0Entity;
import com.chad.baserecyclerviewadapterhelper.entity.ExpandableLv1Entity;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.ExpandableEntity;
import com.chad.library.adapter.base.module.ExpandableModule;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author: limuyang
 * @date: 2019-12-06
 * @Description:
 */
public class ExpandableItemAdapter extends BaseQuickAdapter<ExpandableEntity, BaseViewHolder> implements ExpandableModule {

    public ExpandableItemAdapter() {
        super(R.layout.item_draggable_view);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, @Nullable ExpandableEntity item) {
        if (item instanceof ExpandableLv0Entity) {
            ExpandableLv0Entity lv0 = (ExpandableLv0Entity)item;
            helper.setText(R.id.tv, lv0.getTitle());
        } else if (item instanceof ExpandableLv1Entity) {
            ExpandableLv1Entity lv1 = (ExpandableLv1Entity)item;
            helper.setVisible(R.id.iv_head, false);
            helper.setVisible(R.id.iv, false);
            helper.setText(R.id.tv, lv1.getTitle());
        }
    }
}
