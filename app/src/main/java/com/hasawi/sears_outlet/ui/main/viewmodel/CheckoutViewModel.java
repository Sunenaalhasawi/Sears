package com.hasawi.sears_outlet.ui.main.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hasawi.sears_outlet.data.api.Resource;
import com.hasawi.sears_outlet.data.api.response.AddressResponse;
import com.hasawi.sears_outlet.data.api.response.CheckoutResponse;
import com.hasawi.sears_outlet.data.api.response.DeleteAddressResponse;
import com.hasawi.sears_outlet.data.api.response.GetAllAddressResponse;
import com.hasawi.sears_outlet.data.repository.CheckoutRepository;
import com.hasawi.sears_outlet.data.repository.UserAccountRepository;

import java.util.Map;

public class CheckoutViewModel extends ViewModel {

    CheckoutRepository checkoutRepository;
    UserAccountRepository userAccountRepository;

    public CheckoutViewModel() {
        userAccountRepository = new UserAccountRepository();
        checkoutRepository = new CheckoutRepository();
    }

    public MutableLiveData<Resource<CheckoutResponse>> checkout(String userId, String cartId, String couponCode, String sessiontoken, String shippingId) {
        return checkoutRepository.checkout(userId, cartId, couponCode, sessiontoken, shippingId);
    }

    public MutableLiveData<Resource<GetAllAddressResponse>> getAddresses(String userId, String sessiontoken) {
        return userAccountRepository.getAddresses(userId, sessiontoken);
    }

    public MutableLiveData<Resource<AddressResponse>> editAddress(String userId, String addressId, String sessionToken, Map<String, Object> jsonParams) {
        return userAccountRepository.editAddress(userId, addressId, sessionToken, jsonParams);
    }

    public MutableLiveData<Resource<DeleteAddressResponse>> deleteAddress(String addressId, String sessiontoken) {
        return userAccountRepository.deleteAddress(addressId, sessiontoken);
    }

}
