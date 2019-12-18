package com.chad.baserecyclerviewadapterhelper.entity.node.section;

import androidx.annotation.DrawableRes;
import com.chad.library.adapter.base.entity.node.BaseNode;
import org.jetbrains.annotations.Nullable;
import java.util.List;

public class ItemNode extends BaseNode {

    private int img;
    private String name;

    public ItemNode(@DrawableRes int img, String name) {
        this.img = img;
        this.name = name;
    }

    @DrawableRes
    public int getImg() {
        return img;
    }

    public void setImg(@DrawableRes int img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Nullable
    @Override
    public List<BaseNode> getChildNode() {
        return null;
    }
}
