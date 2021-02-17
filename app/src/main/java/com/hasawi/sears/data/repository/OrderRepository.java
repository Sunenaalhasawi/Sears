package com.hasawi.sears.data.repository;

import androidx.lifecycle.MutableLiveData;

import com.hasawi.sears.data.api.Resource;
import com.hasawi.sears.data.api.RetrofitApiClient;
import com.hasawi.sears.data.api.response.OrderHistoryResponse;
import com.hasawi.sears.data.api.response.OrderResponse;

import org.json.JSONObject;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderRepository {
    public MutableLiveData<Resource<OrderResponse>> orderConfirmation(String userID, String addressId, String cartId, String sessionToken, Map<String, Object> jsonParams) {
        String jsonParamString = new JSONObject(jsonParams).toString();
        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonParamString);
        MutableLiveData<Resource<OrderResponse>> orderMutableLiveData = new MutableLiveData<>();
        Call<OrderResponse> orderResponseCall = RetrofitApiClient.getInstance().getApiInterface().orderConfirmation(userID, addressId, cartId, "Bearer " + sessionToken, requestBody);
        orderResponseCall.enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                if (response.code() != 200) {
                    orderMutableLiveData.setValue(Resource.error(response.message(), null));
                } else if (response.body() != null) {
                    orderMutableLiveData.setValue(Resource.success(response.body()));

                }
            }

            @Override
            public void onFailure(Call<OrderResponse> call, Throwable t) {
                orderMutableLiveData.setValue(Resource.error(t.getMessage(), null));
            }
        });
        return orderMutableLiveData;
    }

    public MutableLiveData<Resource<OrderHistoryResponse>> orderHistory(String customerId, String sessionToken) {
        MutableLiveData<Resource<OrderHistoryResponse>> orderHistoryMutableLiveData = new MutableLiveData<>();
        Call<OrderHistoryResponse> orderHistoryResponseCall = RetrofitApiClient.getInstance().getApiInterface().orderHistory(customerId, "Bearer " + sessionToken);
        orderHistoryResponseCall.enqueue(new Callback<OrderHistoryResponse>() {
            @Override
            public void onResponse(Call<OrderHistoryResponse> call, Response<OrderHistoryResponse> response) {
                if (response.code() != 200) {
                    orderHistoryMutableLiveData.setValue(Resource.error(response.message(), null));
                } else if (response.body() != null) {
                    orderHistoryMutableLiveData.setValue(Resource.success(response.body()));

                }
            }

            @Override
            public void onFailure(Call<OrderHistoryResponse> call, Throwable t) {
                orderHistoryMutableLiveData.setValue(Resource.error(t.getMessage(), null));
            }
        });
        return orderHistoryMutableLiveData;
    }
}
