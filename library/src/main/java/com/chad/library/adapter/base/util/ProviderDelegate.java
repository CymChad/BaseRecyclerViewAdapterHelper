package com.chad.library.adapter.base.util;

import android.util.SparseArray;

import com.chad.library.adapter.base.annotation.ItemProviderTag;
import com.chad.library.adapter.base.provider.BaseItemProvider;

/**
 * https://github.com/chaychan
 * @author ChayChan
 * @date 2018/3/21  11:04
 */

public class ProviderDelegate {

    private SparseArray<BaseItemProvider> mItemProviders = new SparseArray<>();

    public void registerProvider(BaseItemProvider provider){
        if (provider == null){
            throw new ItemProviderAnnotationException("ItemProviderTag not def layout");
        }

        int viewType = provider.viewType();
        if (mItemProviders.get(viewType) == null){
            mItemProviders.put(viewType,provider);
        }
    }

    public SparseArray<BaseItemProvider> getItemProviders(){
        return mItemProviders;
    }

}
