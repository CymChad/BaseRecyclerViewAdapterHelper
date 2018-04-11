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

public abstract class BaseItemProvider<T,V extends BaseViewHolder> {

    public Context mContext;
    public List<T> mData;

    public abstract void convert(V helper, T data, int position);

    //子类若想实现条目点击事件则重写该方法
    //Subclasses override this method if you want to implement an item click event
    public void onClick(V helper, T data, int position){};

    //子类若想实现条目长按事件则重写该方法
    //Subclasses override this method if you want to implement an item long press event
    public boolean onLongClick(V helper, T data, int position){return false;};

    //如果没有使用注解返回layout则重写该方法
    //Rewrite this method if you did not use annotations to return layout
    public int getLayout(){
        return 0;
    }

    //如果没有使用注解返回viewType则重写该方法
    //Rewrite this method if you did not use annotations to return viewType
    public int getViewType(){
        return 0;
    }
}
