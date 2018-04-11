package com.chad.library.adapter.base.provider;

import android.content.Context;

import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * https://github.com/chaychan
 *
 * @author ChayChan
 * @description: The base class of ItemProvider
 * @date 2018/3/21  10:41
 */

public abstract class BaseItemProvider<T, V extends BaseViewHolder> {

    public Context mContext;
    public List<T> mData;

    /**
     *
     * @return
     */
    public abstract int viewType();

    /**
     *
     * @return
     */
    public abstract int layout();

    public abstract void convert(V helper, T data, int position);

}
