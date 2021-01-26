package com.hasawi.sears.ui.main.view.dashboard.product.paging;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import org.json.JSONArray;
import org.json.JSONObject;

public class ProductDataFactory extends DataSource.Factory {

    private MutableLiveData<ProductDataSource> mutableLiveData;
    private ProductDataSource productDataSource;
    private String categoryId = "", pageNo = "";
    private JSONArray filterArray, brandArray, colorArray, sizeArray;
    private String sort = "", userId = "";
    private JSONObject productPayload = null;

    public ProductDataFactory(String categoryId, JSONArray filterArray, JSONArray brandArray, JSONArray colorArray, JSONArray sizeArray, String pageNo, String sort, String userId, JSONObject productPayload) {
        this.categoryId = categoryId;
        this.pageNo = pageNo;
        this.filterArray = filterArray;
        this.brandArray = brandArray;
        this.colorArray = colorArray;
        this.sizeArray = sizeArray;
        this.sort = sort;
        this.userId = userId;
        this.productPayload = productPayload;
        this.mutableLiveData = new MutableLiveData<ProductDataSource>();
    }

    @Override
    public DataSource create() {
        productDataSource = new ProductDataSource(categoryId, filterArray, brandArray, colorArray, sizeArray, pageNo, sort, userId, productPayload);
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