package com.chad.baserecyclerviewadapterhelper.adapter;

import androidx.databinding.DataBindingUtil;

import com.chad.baserecyclerviewadapterhelper.R;
import com.chad.baserecyclerviewadapterhelper.databinding.ItemMovieBinding;
import com.chad.baserecyclerviewadapterhelper.entity.Movie;
import com.chad.baserecyclerviewadapterhelper.entity.MoviePresenter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author: limuyang
 * @date: 2019-12-05
 * @Description: DataBinding Adapter
 *
 */
public class DataBindingAdapter extends BaseQuickAdapter<Movie, BaseViewHolder> {

    private MoviePresenter mPresenter = new MoviePresenter();

    public DataBindingAdapter() {
        super(R.layout.item_movie);
    }

    @Override
    protected void onItemViewHolderCreated(@NotNull BaseViewHolder viewHolder, int viewType) {
        // 绑定 view
        DataBindingUtil.bind(viewHolder.itemView);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, @Nullable Movie item) {
        if (item == null) {
            return;
        }

        // 获取 Binding
        ItemMovieBinding binding = helper.getBinding();
        if (binding != null) {
            binding.setMovie(item);
            binding.setPresenter(mPresenter);
            binding.executePendingBindings();
        }
    }
}
