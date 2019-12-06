package com.chad.baserecyclerviewadapterhelper.entity;

import com.chad.library.adapter.base.entity.ExpandableEntity;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: limuyang
 * @date: 2019-12-06
 * @Description:
 */
public class ExpandableLv1Entity implements ExpandableEntity {

    private String title;

    public ExpandableLv1Entity(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public boolean isExpanded() {
        return false;
    }

    @Override
    public void setExpanded(boolean isExpanded) {
    }

    @NotNull
    @Override
    public List getSubItems() {
        return new ArrayList();
    }

    @Override
    public int getLevel() {
        return 1;
    }
}
