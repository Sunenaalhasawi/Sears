package com.hasawi.sears.data.repository;

import androidx.lifecycle.MutableLiveData;

import com.hasawi.sears.data.api.Resource;
import com.hasawi.sears.data.api.RetrofitApiClient;
import com.hasawi.sears.data.api.response.OrderResponse;

import org.json.JSONObject;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderRepository {
    public MutableLiveData<Resource<OrderResponse>> orderConfirmation(String userID, String addressId, String cartId, String sessionToken, Map<String, Object> jsonParams) {
        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (new JSONObject(jsonParams)).toString());
        MutableLiveData<Resource<OrderResponse>> orderMutableLiveData = new MutableLiveData<>();
        Call<OrderResponse> orderResponseCall = RetrofitApiClient.getInstance().getApiInterface().orderConfirmation(userID, addressId, cartId, "Bearer " + sessionToken, requestBody);
        orderResponseCall.enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                if (response.code() != 200) {
                    orderMutableLiveData.setValue(Resource.error("Network Error !", null));
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
}
