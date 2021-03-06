package com.hasawi.sears.ui.main.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hasawi.sears.data.api.Resource;
import com.hasawi.sears.data.api.response.AddressResponse;
import com.hasawi.sears.data.api.response.CheckoutResponse;
import com.hasawi.sears.data.api.response.GetAllAddressResponse;
import com.hasawi.sears.data.repository.CheckoutRepository;
import com.hasawi.sears.data.repository.UserAccountRepository;

import java.util.Map;

public class CheckoutViewModel extends ViewModel {

    CheckoutRepository checkoutRepository;
    UserAccountRepository userAccountRepository;

    public CheckoutViewModel() {
        userAccountRepository = new UserAccountRepository();
        checkoutRepository = new CheckoutRepository();
    }

    public MutableLiveData<Resource<CheckoutResponse>> checkout(String userId, String cartId, String sessiontoken) {
        return checkoutRepository.checkout(userId, cartId, sessiontoken);
    }

    public MutableLiveData<Resource<GetAllAddressResponse>> getAddresses(String userId, String sessiontoken) {
        return userAccountRepository.getAddresses(userId, sessiontoken);
    }

    public MutableLiveData<Resource<AddressResponse>> editAddress(String userId, String addressId, String sessionToken, Map<String, Object> jsonParams) {
        return userAccountRepository.editAddress(userId, addressId, sessionToken, jsonParams);
    }

}
