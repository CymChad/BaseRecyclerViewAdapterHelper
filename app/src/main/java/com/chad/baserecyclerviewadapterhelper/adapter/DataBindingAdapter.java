package com.chad.baserecyclerviewadapterhelper.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.View;
import android.view.ViewGroup;

import com.chad.baserecyclerviewadapterhelper.BR;
import com.chad.baserecyclerviewadapterhelper.R;
import com.chad.baserecyclerviewadapterhelper.entity.Movie;
import com.chad.baserecyclerviewadapterhelper.entity.MoviePresenter;
import com.chad.baserecyclerviewadapterhelper.entity.Person;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by luoxiongwen on 16/10/24.
 */

public class DataBindingAdapter extends BaseQuickAdapter<Movie, DataBindingAdapter.PersonViewHolder> {

    private MoviePresenter mPresenter;

    public DataBindingAdapter(int layoutResId, List<Movie> data) {
        super(layoutResId, data);

        mPresenter = new MoviePresenter();
    }

    @Override
    protected void convert(PersonViewHolder helper, Movie item) {
        ViewDataBinding binding = helper.getBinding();
        binding.setVariable(BR.movie, item);
        binding.setVariable(BR.presenter, mPresenter);
        binding.executePendingBindings();
    }

    @Override
    protected PersonViewHolder createBaseViewHolder(View view) {
        return new PersonViewHolder(view);
    }

    @Override
    protected View getItemView(int layoutResId, ViewGroup parent) {
        ViewDataBinding binding = DataBindingUtil.inflate(mLayoutInflater, layoutResId, parent, false);
        View view = binding.getRoot();
        view.setTag(R.id.BaseQuickAdapter_databinding_support, binding);
        return view;
    }

    public class PersonViewHolder extends BaseViewHolder {

        public PersonViewHolder(View view) {
            super(view);
        }

        public ViewDataBinding getBinding() {
            return (ViewDataBinding)getConvertView().getTag(R.id.BaseQuickAdapter_databinding_support);
        }
    }
}
