package com.chad.library.adapter.base.util;

import android.support.annotation.LayoutRes;
import android.util.SparseIntArray;

import java.util.List;

/**
 * help you to achieve multi type easily
 * <p>
 * Created by tysheng
 * Date: 2017/4/6 08:41.
 * Email: tyshengsx@gmail.com
 * <p>
 * <p>
 * more information: https://github.com/CymChad/BaseRecyclerViewAdapterHelper/issues/968
 */

public abstract class MultiTypeDelegate<T> {

    private static final int DEFAULT_VIEW_TYPE = -0xff;
    private SparseIntArray layouts;
    private boolean autoMode, selfMode;

    public MultiTypeDelegate(SparseIntArray layouts) {
        this.layouts = layouts;
    }

    public MultiTypeDelegate() {
    }

    public final int getDefItemViewType(List<T> data, int position) {
        T item = data.get(position);
        return item != null ? getItemType(item) : DEFAULT_VIEW_TYPE;
    }

    /**
     * get the item type from specific entity.
     *
     * @param t entity
     * @return item type
     */
    protected abstract int getItemType(T t);

    public final int getLayoutId(int viewType) {
        return this.layouts.get(viewType);
    }

    private void addItemType(int type, @LayoutRes int layoutResId) {
        if (this.layouts == null) {
            this.layouts = new SparseIntArray();
        }
        this.layouts.put(type, layoutResId);
    }

    /**
     * auto increase type vale, start from 0.
     *
     * @param layoutResIds layout id arrays
     * @return MultiTypeDelegate
     */
    public MultiTypeDelegate registerItemTypeAutoIncrease(@LayoutRes int... layoutResIds) {
        autoMode = true;
        checkMode(selfMode);
        for (int i = 0; i < layoutResIds.length; i++) {
            addItemType(i, layoutResIds[i]);
        }
        return this;
    }

    /**
     * set your own type one by one.
     *
     * @param type        type value
     * @param layoutResId layout id
     * @return MultiTypeDelegate
     */
    public MultiTypeDelegate registerItemType(int type, @LayoutRes int layoutResId) {
        selfMode = true;
        checkMode(autoMode);
        addItemType(type, layoutResId);
        return this;
    }

    private void checkMode(boolean mode) {
        if (mode) {
            throw new RuntimeException("Don't mess two register mode");
        }
    }
}
