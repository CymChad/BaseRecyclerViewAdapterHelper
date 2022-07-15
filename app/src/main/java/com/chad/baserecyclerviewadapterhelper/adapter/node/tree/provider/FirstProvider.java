//package com.chad.baserecyclerviewadapterhelper.adapter.node.tree.provider;
//
//import android.view.View;
//import android.view.animation.DecelerateInterpolator;
//import android.widget.ImageView;
//
//import androidx.core.view.ViewCompat;
//
//import com.chad.baserecyclerviewadapterhelper.R;
//import com.chad.baserecyclerviewadapterhelper.adapter.node.tree.NodeTreeAdapter;
//import com.chad.baserecyclerviewadapterhelper.entity.node.tree.FirstNode;
//import com.chad.library.adapter.base.entity.node.BaseNode;
//import com.chad.library.adapter.base.provider.BaseNodeProvider;
//import com.chad.library.adapter.base.viewholder.BaseViewHolder;
//
//import org.jetbrains.annotations.NotNull;
//
//import java.util.List;
//
//public class FirstProvider extends BaseNodeProvider {
//
//    @Override
//    public int getItemViewType() {
//        return 1;
//    }
//
//    @Override
//    public int getLayoutId() {
//        return R.layout.item_node_first;
//    }
//
//    @Override
//    public void convert(@NotNull BaseViewHolder helper, @NotNull BaseNode data) {
//        FirstNode entity = (FirstNode) data;
//        helper.setText(R.id.title, entity.getTitle());
//        helper.setImageResource(R.id.iv, R.mipmap.arrow_r);
//
//        setArrowSpin(helper, data, false);
//    }
//
//    @Override
//    public void convert(@NotNull BaseViewHolder helper, @NotNull BaseNode data, @NotNull List<?> payloads) {
//        for (Object payload : payloads) {
//            if (payload instanceof Integer && (int) payload == NodeTreeAdapter.EXPAND_COLLAPSE_PAYLOAD) {
//                // 增量刷新，使用动画变化箭头
//                setArrowSpin(helper, data, true);
//            }
//        }
//    }
//
//    private void setArrowSpin(BaseViewHolder helper, BaseNode data, boolean isAnimate) {
//        FirstNode entity = (FirstNode) data;
//
//        ImageView imageView = helper.getView(R.id.iv);
//
//        if (entity.isExpanded()) {
//            if (isAnimate) {
//                ViewCompat.animate(imageView).setDuration(200)
//                        .setInterpolator(new DecelerateInterpolator())
//                        .rotation(0f)
//                        .start();
//            } else {
//                imageView.setRotation(0f);
//            }
//        } else {
//            if (isAnimate) {
//                ViewCompat.animate(imageView).setDuration(200)
//                        .setInterpolator(new DecelerateInterpolator())
//                        .rotation(90f)
//                        .start();
//            } else {
//                imageView.setRotation(90f);
//            }
//        }
//    }
//
//    @Override
//    public void onClick(@NotNull BaseViewHolder helper, @NotNull View view, BaseNode data, int position) {
//        // 这里使用payload进行增量刷新（避免整个item刷新导致的闪烁，不自然）
//        getAdapter().expandOrCollapse(position, true, true, NodeTreeAdapter.EXPAND_COLLAPSE_PAYLOAD);
//    }
//}
