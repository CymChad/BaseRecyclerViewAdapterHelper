package com.chad.baserecyclerviewadapterhelper.adapter;

import com.chad.baserecyclerviewadapterhelper.R;
import com.chad.baserecyclerviewadapterhelper.databinding.ItemMovieBinding;
import com.chad.baserecyclerviewadapterhelper.entity.Movie;
import com.chad.baserecyclerviewadapterhelper.entity.MoviePresenter;
import com.chad.library.adapter.base.BaseDataBindingViewHolder;
import com.chad.library.adapter.base.BaseQuickAdapter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author: limuyang
 * @date: 2019-12-05
 * @Description: DataBinding Adapter
 *
 * 这里的ViewHolder使用 {@link BaseDataBindingViewHolder}
 */
public class DataBindingAdapter extends BaseQuickAdapter<Movie, BaseDataBindingViewHolder<ItemMovieBinding>> {

    private MoviePresenter mPresenter = new MoviePresenter();

    public DataBindingAdapter() {
        super(R.layout.item_movie);
    }

    @Override
    protected void convert(@NotNull BaseDataBindingViewHolder<ItemMovieBinding> helper, @Nullable Movie item) {
        if (item == null) {
            return;
        }

        ItemMovieBinding binding = helper.getBinding();
        if (binding != null) {
            binding.setMovie(item);
            binding.setPresenter(mPresenter);
            binding.executePendingBindings();
        }
    }
}
