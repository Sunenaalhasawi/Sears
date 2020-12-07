package com.hasawi.sears.ui.main.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hasawi.sears.data.api.Resource;
import com.hasawi.sears.data.api.response.WishlistResponse;
import com.hasawi.sears.data.repository.ProductRepository;

public class WishListViewModel extends ViewModel {

    ProductRepository productRepository;

    public WishListViewModel() {
        this.productRepository = new ProductRepository();
    }

    public MutableLiveData<Resource<WishlistResponse>> getWishListedProducts(String userID, String sessiontoken) {
        return productRepository.getWishListedProducts(userID, sessiontoken);
    }

    public MutableLiveData<Resource<WishlistResponse>> addToWishlist(String productID, String userID, String sessiontoken) {
        return productRepository.addToWishlist(productID, userID, sessiontoken);
    }
}
