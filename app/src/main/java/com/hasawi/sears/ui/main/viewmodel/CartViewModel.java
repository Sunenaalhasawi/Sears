package com.hasawi.sears.ui.main.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hasawi.sears.data.api.Resource;
import com.hasawi.sears.data.api.response.AddressResponse;
import com.hasawi.sears.data.api.response.CartResponse;
import com.hasawi.sears.data.repository.CheckoutRepository;
import com.hasawi.sears.data.repository.ProductRepository;
import com.hasawi.sears.data.repository.UserAccountRepository;

import java.util.Map;

public class CartViewModel extends ViewModel {
    ProductRepository productRepository;
    UserAccountRepository userAccountRepository;
    CheckoutRepository checkoutRepository;

    public CartViewModel() {
        productRepository = new ProductRepository();
        userAccountRepository = new UserAccountRepository();
        checkoutRepository = new CheckoutRepository();
    }

    public MutableLiveData<Resource<CartResponse>> getCartItems(String userID, String sessiontoken) {
        return productRepository.getCartItems(userID, sessiontoken);
    }

    public MutableLiveData<Resource<CartResponse>> removeItemFromCart(String userId, String cartItemId, String sessiontoken) {
        return productRepository.removeFromCart(userId, cartItemId, sessiontoken);
    }


    public MutableLiveData<Resource<AddressResponse>> addNewAddress(String userId, Map<String, Object> jsonParams, String sessiontoken) {
        return userAccountRepository.addNewAddress(userId, jsonParams, sessiontoken);
    }

    public MutableLiveData<Resource<CartResponse>> addToCart(String userID, String jsonParams, String sessionToken) {
        return productRepository.addToCart(userID, jsonParams, sessionToken);
    }

//    public MutableLiveData<Resource<CheckoutResponse>> checkout(String userId, String cartId, String sessiontoken) {
//        return checkoutRepository.checkout(userId, cartId, sessiontoken);
//    }
}
