package com.hasawi.sears.ui.main.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hasawi.sears.data.api.Resource;
import com.hasawi.sears.data.api.response.AddressResponse;
import com.hasawi.sears.data.api.response.GetAllAddressResponse;
import com.hasawi.sears.data.repository.UserAccountRepository;

import java.util.Map;

public class ShippingAddressViewModel extends ViewModel {
    UserAccountRepository userAccountRepository;

    public ShippingAddressViewModel() {
        userAccountRepository = new UserAccountRepository();
    }

    public MutableLiveData<Resource<AddressResponse>> addNewAddress(String userID, Map<String, Object> jsonParams, String sessionToken) {
        return userAccountRepository.addNewAddress(userID, jsonParams, sessionToken);
    }

    public MutableLiveData<Resource<AddressResponse>> editAddress(String userId, String addressId, String sessionToken, Map<String, Object> jsonParams) {
        return userAccountRepository.editAddress(userId, addressId, sessionToken, jsonParams);
    }

    public MutableLiveData<Resource<GetAllAddressResponse>> getAddresses(String userId, String sessiontoken) {
        return userAccountRepository.getAddresses(userId, sessiontoken);
    }
}
