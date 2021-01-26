package com.hasawi.sears.data.api.model;

import android.graphics.drawable.Drawable;

public class NavigationMenuItem {
    int _ID;
    private String name;
    private Drawable drawable;
    private boolean isEnabled;


    public NavigationMenuItem(int _ID, String name, Drawable drawable, boolean isEnabled) {
        this.name = name;
        this._ID = _ID;
        this.isEnabled = isEnabled;
        this.drawable = drawable;
    }

    public int get_ID() {
        return _ID;
    }

    public void set_ID(int _ID) {
        this._ID = _ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }
}
