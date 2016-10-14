package com.chad.library.adapter.base.vh;

import com.chad.library.R;

/**
 * Created by BlingBling on 2016/10/11.
 */

public class SimpleLoadMoreView extends LoadMoreView {
    @Override public int getLoadMoreViewLayoutId() {
        return R.layout.def_loading;
    }

    @Override public void onStatusChanged(BaseViewHolder holder) {
        switch (getLoadMoreStatus()) {
            case STATUS_LOADING:
                holder.setVisible(R.id.load_more_loading_view, true);
                holder.setVisible(R.id.load_more_load_fail_view, false);
                holder.setVisible(R.id.load_more_load_end_view, false);
                break;
            case STATUS_LOAD_ERROR:
                holder.setVisible(R.id.load_more_loading_view, false);
                holder.setVisible(R.id.load_more_load_fail_view, true);
                holder.setVisible(R.id.load_more_load_end_view, false);
                break;
            case STATUS_LOAD_END:
                holder.setVisible(R.id.load_more_loading_view, false);
                holder.setVisible(R.id.load_more_load_fail_view, false);
                holder.setVisible(R.id.load_more_load_end_view, true);
                break;
        }
    }
}
