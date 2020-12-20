package com.hasawi.sears.data.api.model;

import android.graphics.drawable.Drawable;

public class DynamicGridItem {

    private String itemName;
    private Drawable itemDrawable;

    public DynamicGridItem(String itemName, Drawable itemDrawable) {
        this.itemName = itemName;
        this.itemDrawable = itemDrawable;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Drawable getItemDrawable() {
        return itemDrawable;
    }

    public void setItemDrawable(Drawable itemDrawable) {
        this.itemDrawable = itemDrawable;
    }
}
