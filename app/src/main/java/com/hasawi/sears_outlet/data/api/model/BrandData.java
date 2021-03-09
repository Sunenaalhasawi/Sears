package com.hasawi.sears_outlet.data.api.model;

import com.hasawi.sears_outlet.data.api.model.pojo.Manufacture;

public class BrandData {

    private String index;
    private String name;
    private Manufacture manufacture;

    public BrandData() {
    }

    public BrandData(String name, Manufacture manufacture) {
        this.name = name;
        this.manufacture = manufacture;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public Manufacture getManufacture() {
        return manufacture;
    }

    public void setManufacture(Manufacture manufacture) {
        this.manufacture = manufacture;
    }
}
