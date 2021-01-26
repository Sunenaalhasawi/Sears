package com.hasawi.sears.data.api.model.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HomeSectionDetail {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("grid")
    @Expose
    private Integer grid;
    @SerializedName("containerHeight")
    @Expose
    private Double containerHeight;
    @SerializedName("sortOrder")
    @Expose
    private Integer sortOrder;
    @SerializedName("homeSectionElements")
    @Expose
    private List<HomeSectionElement> homeSectionElements = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getGrid() {
        return grid;
    }

    public void setGrid(Integer grid) {
        this.grid = grid;
    }

    public List<HomeSectionElement> getHomeSectionElements() {
        return homeSectionElements;
    }

    public void setHomeSectionElements(List<HomeSectionElement> homeSectionElements) {
        this.homeSectionElements = homeSectionElements;
    }

    public Double getContainerHeight() {
        return containerHeight;
    }

    public void setContainerHeight(Double containerHeight) {
        this.containerHeight = containerHeight;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }
}
