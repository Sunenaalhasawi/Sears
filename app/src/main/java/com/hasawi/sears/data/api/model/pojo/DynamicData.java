package com.hasawi.sears.data.api.model.pojo;

import java.util.List;

public class DynamicData {
    private int VIEW_TYPE;
    private Data data;

    public DynamicData(int VIEW_TYPE, Data data) {
        this.VIEW_TYPE = VIEW_TYPE;
        this.data = data;
    }

    public int getVIEW_TYPE() {
        return VIEW_TYPE;
    }

    public void setVIEW_TYPE(int VIEW_TYPE) {
        this.VIEW_TYPE = VIEW_TYPE;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {
        List<Integer> gridBannerList;
        List<Product> newProductList;
        private String singleBanner;

        public Data(String singleBanner, List<Integer> gridBannerList, List<Product> newProductList) {
            this.singleBanner = singleBanner;
            this.gridBannerList = gridBannerList;
            this.newProductList = newProductList;
        }

        public String getSingleBanner() {
            return singleBanner;
        }

        public void setSingleBanner(String singleBanner) {
            this.singleBanner = singleBanner;
        }

        public List<Integer> getGridBannerList() {
            return gridBannerList;
        }

        public void setGridBannerList(List<Integer> gridBannerList) {
            this.gridBannerList = gridBannerList;
        }

        public List<Product> getNewProductList() {
            return newProductList;
        }

        public void setNewProductList(List<Product> newProductList) {
            this.newProductList = newProductList;
        }
    }
}
