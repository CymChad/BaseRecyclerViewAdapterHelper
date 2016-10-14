package com.chad.baserecyclerviewadapterhelper.adapter;

import android.content.Context;

import com.chad.baserecyclerviewadapterhelper.R;
import com.chad.baserecyclerviewadapterhelper.entity.MySection;
import com.chad.baserecyclerviewadapterhelper.entity.Video;
import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.vh.BaseViewHolder;

import java.util.List;

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public class SectionAdapter extends BaseSectionQuickAdapter<MySection> {
    public SectionAdapter(Context context, List data) {
        super(context, data);
    }

    @Override protected int getSectionHeadResId() {
        return R.layout.def_section_head;
    }

    @Override protected int getLayoutResId() {
        return R.layout.item_section_content;
    }

    @Override
    protected void convertHead(BaseViewHolder helper, final MySection item) {
        helper.setText(R.id.header, item.header);
        helper.setVisible(R.id.more, item.isMore());
//        helper.setOnClickListener(R.id.more);
    }


    @Override
    protected void convertContent(BaseViewHolder helper, MySection item) {
        Video video = (Video) item.t;
        //helper.setImageUrl(R.id.iv, video.getImg());
        helper.setText(R.id.tv, video.getName());
    }

    @Override protected void onCreateListener(BaseViewHolder holder) {

    }
}
