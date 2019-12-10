package com.chad.baserecyclerviewadapterhelper.entity.node;

import androidx.annotation.DrawableRes;
import com.chad.library.adapter.base.entity.node.BaseNode;
import org.jetbrains.annotations.Nullable;
import java.util.List;

public class SecondNode extends BaseNode {

    private int img;
    private String name;

    public SecondNode(@DrawableRes int img, String name) {
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
