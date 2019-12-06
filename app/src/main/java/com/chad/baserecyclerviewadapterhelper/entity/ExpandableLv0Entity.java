package com.chad.baserecyclerviewadapterhelper.entity;

import com.chad.library.adapter.base.entity.ExpandableEntity;
import org.jetbrains.annotations.NotNull;
import java.util.List;

/**
 * @author: limuyang
 * @date: 2019-12-06
 * @Description:
 */
public class ExpandableLv0Entity implements ExpandableEntity<ExpandableLv1Entity> {

    private boolean isExpanded;
    private List<ExpandableLv1Entity> subItems;

    private String title;

    public ExpandableLv0Entity(List<ExpandableLv1Entity> subItems, boolean isExpanded) {
        this.subItems = subItems;
        this.isExpanded = isExpanded;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean isExpanded() {
        return isExpanded;
    }

    @Override
    public void setExpanded(boolean isExpanded) {
        this.isExpanded = isExpanded;
    }

    @NotNull
    @Override
    public List<ExpandableLv1Entity> getSubItems() {
        return subItems;
    }

    @Override
    public int getLevel() {
        return 0;
    }
}
