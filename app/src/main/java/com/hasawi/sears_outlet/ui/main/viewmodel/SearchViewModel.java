package com.hasawi.sears_outlet.ui.main.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hasawi.sears_outlet.data.api.Resource;
import com.hasawi.sears_outlet.data.api.model.pojo.SearchProductListResponse;
import com.hasawi.sears_outlet.data.api.response.SearchedProductDetailsResponse;
import com.hasawi.sears_outlet.data.repository.ProductRepository;

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
