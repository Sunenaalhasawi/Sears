package com.hasawi.sears.data.repository;

import androidx.lifecycle.MutableLiveData;

import com.hasawi.sears.data.api.Resource;
import com.hasawi.sears.data.api.RetrofitApiClient;
import com.hasawi.sears.data.api.response.AddressResponse;
import com.hasawi.sears.data.api.response.GetAllAddressResponse;
import com.hasawi.sears.data.api.response.UserProfileResponse;

import org.json.JSONObject;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserAccountRepository {

    public MutableLiveData<Resource<AddressResponse>> addNewAddress(String userID, Map<String, Object> jsonParams, String sessionToken) {
        MutableLiveData<Resource<AddressResponse>> addResourceMutableLiveData = new MutableLiveData<>();
        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (new JSONObject(jsonParams)).toString());
        Call<AddressResponse> addressResponseCall = RetrofitApiClient.getInstance().getApiInterface().addNewAddress(userID, requestBody, "Bearer " + sessionToken);
        addressResponseCall.enqueue(new Callback<AddressResponse>() {
            @Override
            public void onResponse(Call<AddressResponse> call, Response<AddressResponse> response) {
                if (response.code() != 200) {
                    addResourceMutableLiveData.setValue(Resource.error("Network Error !", null));
                } else if (response.body() != null) {
                    addResourceMutableLiveData.setValue(Resource.success(response.body()));

                }
            }

            @Override
            public void onFailure(Call<AddressResponse> call, Throwable t) {
                addResourceMutableLiveData.setValue(Resource.error(t.getMessage(), null));
            }
        });
        return addResourceMutableLiveData;
    }

    public MutableLiveData<Resource<GetAllAddressResponse>> getAddresses(String userID, String sessionToken) {
        MutableLiveData<Resource<GetAllAddressResponse>> getAllAddressMutableLiveData = new MutableLiveData<>();
        Call<GetAllAddressResponse> getAllAddressResponseCall = RetrofitApiClient.getInstance().getApiInterface().getAddresses(userID, "Bearer " + sessionToken);
        getAllAddressResponseCall.enqueue(new Callback<GetAllAddressResponse>() {
            @Override
            public void onResponse(Call<GetAllAddressResponse> call, Response<GetAllAddressResponse> response) {
                if (response.code() != 200) {
                    getAllAddressMutableLiveData.setValue(Resource.error("Network Error !", null));
                } else if (response.body() != null) {
                    getAllAddressMutableLiveData.setValue(Resource.success(response.body()));

                }
            }

            @Override
            public void onFailure(Call<GetAllAddressResponse> call, Throwable t) {
                getAllAddressMutableLiveData.setValue(Resource.error(t.getMessage(), null));
            }
        });
        return getAllAddressMutableLiveData;
    }

    public MutableLiveData<Resource<AddressResponse>> editAddress(String userID, String addressId, String sessionToken, Map<String, Object> jsonParams) {
        MutableLiveData<Resource<AddressResponse>> editAddressMutableLiveData = new MutableLiveData<>();
        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (new JSONObject(jsonParams)).toString());
        Call<AddressResponse> editAddressResponseCall = RetrofitApiClient.getInstance().getApiInterface().editAddress(userID, addressId, "Bearer " + sessionToken, requestBody);
        editAddressResponseCall.enqueue(new Callback<AddressResponse>() {
            @Override
            public void onResponse(Call<AddressResponse> call, Response<AddressResponse> response) {
                if (response.code() != 200) {
                    editAddressMutableLiveData.setValue(Resource.error("Network Error !", null));
                } else if (response.body() != null) {
                    editAddressMutableLiveData.setValue(Resource.success(response.body()));

                }
            }

            @Override
            public void onFailure(Call<AddressResponse> call, Throwable t) {
                editAddressMutableLiveData.setValue(Resource.error(t.getMessage(), null));
            }
        });
        return editAddressMutableLiveData;
    }

    public MutableLiveData<Resource<UserProfileResponse>> userProfile(String emailId, String sessionToken) {
        MutableLiveData<Resource<UserProfileResponse>> userProfileMutableLiveData = new MutableLiveData<>();
        Call<UserProfileResponse> userProfileResponseCall = RetrofitApiClient.getInstance().getApiInterface().userProfile(emailId, "Bearer " + sessionToken);
        userProfileResponseCall.enqueue(new Callback<UserProfileResponse>() {
            @Override
            public void onResponse(Call<UserProfileResponse> call, Response<UserProfileResponse> response) {
                if (response.code() != 200) {
                    userProfileMutableLiveData.setValue(Resource.error("Network Error !", null));
                } else if (response.body() != null) {
                    userProfileMutableLiveData.setValue(Resource.success(response.body()));

                }
            }

            @Override
            public void onFailure(Call<UserProfileResponse> call, Throwable t) {
                userProfileMutableLiveData.setValue(Resource.error(t.getMessage(), null));
            }
        });
        return userProfileMutableLiveData;
    }
}
