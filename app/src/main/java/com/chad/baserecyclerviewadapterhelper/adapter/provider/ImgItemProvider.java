package com.chad.baserecyclerviewadapterhelper.adapter.provider;

import android.widget.Toast;

import com.chad.baserecyclerviewadapterhelper.R;
import com.chad.baserecyclerviewadapterhelper.adapter.DemoMultipleItemRvAdapter;
import com.chad.baserecyclerviewadapterhelper.entity.NormalMultipleEntity;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.annotation.ItemProviderTag;
import com.chad.library.adapter.base.provider.BaseItemProvider;

/**
 * https://github.com/chaychan
 *
 * @author ChayChan
 * @description: Img ItemProvider
 * @date 2018/3/30  11:39
 */

@ItemProviderTag(
        viewType = DemoMultipleItemRvAdapter.TYPE_IMG,
        layout = R.layout.item_image_view
)
public class ImgItemProvider extends BaseItemProvider<NormalMultipleEntity,BaseViewHolder> {

    @Override
    public void convert(BaseViewHolder helper, NormalMultipleEntity data, int position) {
        if (position % 2 == 0) {
            helper.setImageResource(R.id.iv, R.mipmap.animation_img1);
        }else{
            helper.setImageResource(R.id.iv, R.mipmap.animation_img2);
        }
    }

    @Override
    public void onClick(BaseViewHolder helper, NormalMultipleEntity data, int position) {
        Toast.makeText(mContext, "click", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onLongClick(BaseViewHolder helper, NormalMultipleEntity data, int position) {
        Toast.makeText(mContext, "longClick", Toast.LENGTH_SHORT).show();
        return true;
    }
}
