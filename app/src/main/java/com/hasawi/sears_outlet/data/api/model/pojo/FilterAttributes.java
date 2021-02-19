package com.hasawi.sears_outlet.data.api.model.pojo;

import java.util.List;
import java.util.Map;

public class FilterAttributes {

    private Map<String, List<FilterAttributeValues>> filterAttributesMap;


    /**
     * No args constructor for use in serialization
     */
    public FilterAttributes() {
    }

    public FilterAttributes(Map<String, List<FilterAttributeValues>> filterAttributesMap) {
        this.filterAttributesMap = filterAttributesMap;
    }

    public Map<String, List<FilterAttributeValues>> getFilterAttributesMap() {
        return filterAttributesMap;
    }

    public void setFilterAttributesMap(Map<String, List<FilterAttributeValues>> filterAttributesMap) {
        this.filterAttributesMap = filterAttributesMap;
    }


}
