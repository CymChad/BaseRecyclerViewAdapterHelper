package com.chad.baserecyclerviewadapterhelper.adapter;

import android.support.annotation.Nullable;

import com.chad.baserecyclerviewadapterhelper.adapter.provider.ImgItemProvider;
import com.chad.baserecyclerviewadapterhelper.adapter.provider.TextImgItemProvider;
import com.chad.baserecyclerviewadapterhelper.adapter.provider.TextItemProvider;
import com.chad.baserecyclerviewadapterhelper.entity.NormalMultipleEntity;
import com.chad.library.adapter.base.MultipleItemRvAdapter;

import java.util.List;

/**
 * https://github.com/chaychan
 * @author ChayChan
 * @description: MultipleItemRvAdapter demo
 * @date 2018/3/30  11:28
 */

public class DemoMultipleItemRvAdapter extends MultipleItemRvAdapter<NormalMultipleEntity> {

    public static final int TYPE_TEXT = 100;
    public static final int TYPE_IMG = 200;
    public static final int TYPE_TEXT_IMG = 300;

    public DemoMultipleItemRvAdapter(@Nullable List<NormalMultipleEntity> data) {
        super(data);

        //构造函数若有传其他参数可以在调用finishInitialize()之前进行赋值，赋值给全局变量
        //这样getViewType()和registerItemProvider()方法中可以获取到传过来的值
        //getViewType()中可能因为某些业务逻辑，需要将某个值传递过来进行判断，返回对应的viewType
        //registerItemProvider()中可以将值传递给ItemProvider

        //If the constructor has other parameters, it needs to be assigned before calling finishInitialize() and assigned to the global variable
        // This getViewType () and registerItemProvider () method can get the value passed over
        // getViewType () may be due to some business logic, you need to pass a value to judge, return the corresponding viewType
        //RegisterItemProvider() can pass value to ItemProvider

        finishInitialize();
    }

    @Override
    protected int getViewType(NormalMultipleEntity entity) {
        //根据实体类判断并返回对应的viewType，具体判断逻辑因业务不同，这里这是简单通过判断type属性
        //According to the entity class to determine and return the corresponding viewType,
        //the specific judgment logic is different because of the business, here is simply by judging the type attribute
        if (entity.type == NormalMultipleEntity.SINGLE_TEXT){
            return TYPE_TEXT;
        }else if (entity.type == NormalMultipleEntity.SINGLE_IMG){
            return TYPE_IMG;
        }else if (entity.type == NormalMultipleEntity.TEXT_IMG){
            return TYPE_TEXT_IMG;
        }
        return 0;
    }

    @Override
    public void registerItemProvider() {
        //注册相关的条目provider
        //Register related entries provider
        mProviderDelegate.registerProvider(new TextItemProvider());
        mProviderDelegate.registerProvider(new ImgItemProvider());
        mProviderDelegate.registerProvider(new TextImgItemProvider());
    }
}
