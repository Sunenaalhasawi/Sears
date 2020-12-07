package com.hasawi.sears.ui.main.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hasawi.sears.data.api.Resource;
import com.hasawi.sears.data.api.model.pojo.SearchProductListResponse;
import com.hasawi.sears.data.api.response.SearchedProductDetailsResponse;
import com.hasawi.sears.data.repository.ProductRepository;

public class SearchViewModel extends ViewModel {

    ProductRepository productRepository;
    MutableLiveData<SearchProductListResponse> searchProductListResponseMutableLiveData = new MutableLiveData<>();

    public SearchViewModel() {
        this.productRepository = new ProductRepository();
    }

    public MutableLiveData<Resource<SearchProductListResponse>> searchProducts(String searchQuery) {
        return productRepository.searchProducts(searchQuery);
    }

    public MutableLiveData<Resource<SearchedProductDetailsResponse>> getSearchedProductDetails(String objectId) {
        return productRepository.getSearchedProductDetails(objectId);
    }
}
