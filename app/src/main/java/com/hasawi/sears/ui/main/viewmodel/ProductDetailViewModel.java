package com.hasawi.sears.ui.main.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hasawi.sears.data.api.Resource;
import com.hasawi.sears.data.api.response.CartResponse;
import com.hasawi.sears.data.api.response.ProductDetailsResponse;
import com.hasawi.sears.data.api.response.SearchedProductDetailsResponse;
import com.hasawi.sears.data.api.response.WishlistResponse;
import com.hasawi.sears.data.repository.ProductRepository;

public class ProductDetailViewModel extends ViewModel {

    private ProductRepository productRepository;

    public ProductDetailViewModel() {
        productRepository = new ProductRepository();
    }

    public MutableLiveData<Resource<ProductDetailsResponse>> getProductDetails(String productName) {
        return productRepository.getProductDetails(productName);
    }


    public MutableLiveData<Resource<SearchedProductDetailsResponse>> getSearchedProductDetails(String objectId) {
        return productRepository.getSearchedProductDetails(objectId);
    }

    public MutableLiveData<Resource<WishlistResponse>> addToWishlist(String productID, String userID, String sessionToken) {
        return productRepository.addToWishlist(productID, userID, sessionToken);
    }

    public MutableLiveData<Resource<CartResponse>> addToCart(String userID, String jsonParams, String sessionToken) {
        return productRepository.addToCart(userID, jsonParams, sessionToken);
    }
}

