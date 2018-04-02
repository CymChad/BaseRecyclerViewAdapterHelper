package com.chad.library.adapter.base.provider;

import android.content.Context;

import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * https://github.com/chaychan
 * @author ChayChan
 * @description: The base class of ItemProvider
 * @date 2018/3/21  10:41
 */

public abstract class BaseItemProvider<T> {

    public Context mContext;
    public List<T> mData;

    public abstract void convert(BaseViewHolder helper, T data, int position);

    public abstract void onClick(BaseViewHolder helper, T data, int position);

    public abstract boolean onLongClick(BaseViewHolder helper, T data, int position);
}
