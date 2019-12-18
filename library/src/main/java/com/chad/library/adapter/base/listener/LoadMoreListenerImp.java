package com.chad.library.adapter.base.listener;

import androidx.annotation.Nullable;

/**
 * @author: limuyang
 * @date: 2019-12-03
 * @Description: LoadMore需要设置的接口。使用java定义，以兼容java写法
 */
public interface LoadMoreListenerImp {

    void setOnLoadMoreListener(@Nullable OnLoadMoreListener listener);
}
