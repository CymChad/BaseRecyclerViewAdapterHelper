/**
 * Copyright 2013 Joan Zapata
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.chad.library.adapter.base;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.ViewGroup;

import com.chad.library.adapter.base.vh.BaseViewHolder;

import java.util.List;


/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public abstract class BaseQuickAdapter<T> extends XQuickAdapter<T> {

    public BaseQuickAdapter(Context context, List<T> data) {
        super(context, data);
    }

    @Override protected int getDefItemViewType(int position) {
        return 0;
    }

    @Override protected BaseViewHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
        return createBaseViewHolder(parent, getLayoutResId());
    }

    protected abstract @LayoutRes int getLayoutResId();
}
