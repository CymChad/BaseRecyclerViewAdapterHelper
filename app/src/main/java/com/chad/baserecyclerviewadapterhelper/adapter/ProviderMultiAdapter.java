package com.chad.baserecyclerviewadapterhelper.adapter;

import com.chad.baserecyclerviewadapterhelper.adapter.provider.ImgItemProvider;
import com.chad.baserecyclerviewadapterhelper.adapter.provider.TextImgItemProvider;
import com.chad.baserecyclerviewadapterhelper.adapter.provider.TextItemProvider;
import com.chad.baserecyclerviewadapterhelper.entity.ProviderMultiEntity;
import com.chad.library.adapter.base.BaseProviderMultiAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author: limuyang
 * @date: 2019-12-04
 * @Description:
 */
public class ProviderMultiAdapter extends BaseProviderMultiAdapter<ProviderMultiEntity, BaseViewHolder> {

    public ProviderMultiAdapter() {
        super();
        addItemProvider(new ImgItemProvider());
        addItemProvider(new TextImgItemProvider());
        addItemProvider(new TextItemProvider());
    }

    /**
     * 自行根据数据、位置等内容，返回 item 类型
     * @param data
     * @param position
     * @return
     */
    @Override
    protected int getItemType(@NotNull List<? extends ProviderMultiEntity> data, int position) {
        switch (position % 3) {
            case 0:
                return ProviderMultiEntity.IMG;
            case 1:
                return ProviderMultiEntity.TEXT;
            case 2:
                return ProviderMultiEntity.IMG_TEXT;
            default:
                break;
        }
        return 0;
    }
}
