package com.hasawi.sears.data.api.model.pojo;

public class NavigationMenuItem {
    int _ID;
    private String name, image_link;
    private boolean isEnabled;


    public NavigationMenuItem(int _ID, String name, boolean isEnabled) {
        this.name = name;
        this._ID = _ID;
        this.isEnabled = isEnabled;
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
}
