package com.chad.library.adapter.base.vh;

import com.chad.library.R;

/**
 * Created by BlingBling on 2016/10/11.
 */

public class SimpleLoadMoreView extends LoadMoreView {
    @Override public int getLoadMoreViewLayoutId() {
        return R.layout.def_loading;
    }

    @Override public boolean isLoadEndGone() {
        return false;
    }

    @Override protected void onLoading(BaseViewHolder holder) {
        holder.setVisible(R.id.load_more_loading_view, true);
        holder.setVisible(R.id.load_more_load_fail_view, false);
        holder.setVisible(R.id.load_more_load_end_view, false);
    }

    @Override protected void onLoadError(BaseViewHolder holder) {
        holder.setVisible(R.id.load_more_loading_view, false);
        holder.setVisible(R.id.load_more_load_fail_view, true);
        holder.setVisible(R.id.load_more_load_end_view, false);
    }

    @Override protected void onLoadEnd(BaseViewHolder holder) {
        holder.setVisible(R.id.load_more_loading_view, false);
        holder.setVisible(R.id.load_more_load_fail_view, false);
        holder.setVisible(R.id.load_more_load_end_view, true);
    }
}
