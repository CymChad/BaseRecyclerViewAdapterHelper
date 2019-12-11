package com.chad.baserecyclerviewadapterhelper.adapter.node.provider;

import android.view.View;
import com.chad.baserecyclerviewadapterhelper.R;
import com.chad.baserecyclerviewadapterhelper.entity.node.RootFooterNode;
import com.chad.baserecyclerviewadapterhelper.utils.Tips;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.node.BaseNode;
import com.chad.library.adapter.base.provider.BaseNodeProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RootFooterNodeProvider extends BaseNodeProvider<BaseViewHolder> {

    public RootFooterNodeProvider() {
        addChildClickViewIds(R.id.footerTv);
    }

    @Override
    public int getItemViewType() {
        return 2;
    }

    @Override
    public int getLayoutId() {
        return R.layout.node_footer;
    }

    @Override
    public void convert(@NotNull BaseViewHolder helper, @Nullable BaseNode data) {
        RootFooterNode entity = (RootFooterNode) data;
        helper.setText(R.id.footerTv, entity.getTitle());
    }

    @Override
    public void onChildClick(@NotNull BaseViewHolder helper, @NotNull View view, BaseNode data, int position) {
        if (view.getId() == R.id.footerTv) {
            Tips.show("Footer Node Click : " + position);
        }
    }
}
