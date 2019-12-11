package com.chad.baserecyclerviewadapterhelper.adapter;

import androidx.annotation.NonNull;

import com.chad.baserecyclerviewadapterhelper.R;
import com.chad.baserecyclerviewadapterhelper.entity.MySection;
import com.chad.baserecyclerviewadapterhelper.entity.Video;
import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public class SectionQuickAdapter extends BaseSectionQuickAdapter<MySection, BaseViewHolder> {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param sectionHeadResId The section head layout id for each item
     * @param layoutResId      The layout resource id of each item.
     * @param data             A new list is created out of this one to avoid mutable list
     */
    public SectionQuickAdapter(int layoutResId, int sectionHeadResId, List<MySection> data) {
        super(sectionHeadResId, data);
        setNormalLayout(layoutResId);

        addChildClickViewIds(R.id.more);
    }

    @Override
    protected void convertHeader(@NotNull BaseViewHolder helper, @Nullable MySection item) {
        if (item != null && item.getObject() instanceof String) {
            helper.setText(R.id.header, (String) item.getObject());
        }
    }



    @Override
    protected void convert(@NonNull BaseViewHolder helper, MySection item) {
        Video video = (Video) item.getObject();
        switch (helper.getLayoutPosition() % 2) {
            case 0:
                helper.setImageResource(R.id.iv, R.mipmap.m_img1);
                break;
            case 1:
                helper.setImageResource(R.id.iv, R.mipmap.m_img2);
                break;
            default:
                break;
        }
        helper.setText(R.id.tv, video.getName());
    }
}
