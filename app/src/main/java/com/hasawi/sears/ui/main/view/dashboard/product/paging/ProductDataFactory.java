package com.hasawi.sears.ui.main.view.dashboard.product.paging;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import org.json.JSONArray;

public class ProductDataFactory extends DataSource.Factory {

    private MutableLiveData<ProductDataSource> mutableLiveData;
    private ProductDataSource productDataSource;
    private String categoryId = "", pageNo = "";
    private JSONArray filterArray;
    private String sort = "";

    public ProductDataFactory(String categoryId, JSONArray filterArray, String pageNo, String sort) {
        this.categoryId = categoryId;
        this.pageNo = pageNo;
        this.filterArray = filterArray;
        this.sort = sort;
        this.mutableLiveData = new MutableLiveData<ProductDataSource>();
    }

    @Override
    public DataSource create() {
        productDataSource = new ProductDataSource(categoryId, filterArray, pageNo, sort);
        mutableLiveData.postValue(productDataSource);
        return productDataSource;
    }

//    public void setNewCategory(String categoryId,String pageNo){
//        productDataSource = new ProductDataSource(categoryId, pageNo);
//        mutableLiveData.postValue(productDataSource);
//    }


    public MutableLiveData<ProductDataSource> getMutableLiveData() {
        return mutableLiveData;
    }
}