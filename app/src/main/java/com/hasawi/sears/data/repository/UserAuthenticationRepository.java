package com.hasawi.sears.data.repository;

import androidx.lifecycle.MutableLiveData;

import com.hasawi.sears.data.api.Resource;
import com.hasawi.sears.data.api.RetrofitApiClient;
import com.hasawi.sears.data.api.response.LoginResponse;
import com.hasawi.sears.data.api.response.SignupResponse;

import org.json.JSONObject;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserAuthenticationRepository {

    public MutableLiveData<Resource<SignupResponse>> userSignup(Map<String, Object> inputParams) {
        MutableLiveData<Resource<SignupResponse>> signupResponseMutableLiveData = new MutableLiveData<>();
        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (new JSONObject(inputParams)).toString());
        Call<SignupResponse> signupResponseCall = RetrofitApiClient.getInstance().getApiInterface().signup(requestBody);
        signupResponseCall.enqueue(new Callback<SignupResponse>() {
            @Override
            public void onResponse(Call<SignupResponse> call, Response<SignupResponse> response) {
                if (response.code() == 400)
                    signupResponseMutableLiveData.setValue(Resource.error("Email ID Already Exists", null));
                else if (response.code() != 200) {
                    signupResponseMutableLiveData.setValue(Resource.error("Something went wrong. Please try again!", null));
                } else if (response.body() != null)
                    signupResponseMutableLiveData.setValue(Resource.success(response.body()));

            }

            @Override
            public void onFailure(Call<SignupResponse> call, Throwable t) {
                signupResponseMutableLiveData.setValue(Resource.error(t.getMessage(), null));
            }
        });

        return signupResponseMutableLiveData;
    }

    public MutableLiveData<Resource<LoginResponse>> userLogin(Map<String, Object> inputParams) {
        MutableLiveData<Resource<LoginResponse>> loginResponseMutableLiveData = new MutableLiveData<>();
        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (new JSONObject(inputParams)).toString());
        Call<LoginResponse> loginResponseCall = RetrofitApiClient.getInstance().getApiInterface().login(requestBody);
        loginResponseCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.code() != 200) {
                    loginResponseMutableLiveData.setValue(Resource.error("Something went wrong. Please try again!", null));
                } else if (response.body() != null)
                    loginResponseMutableLiveData.setValue(Resource.success(response.body()));
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                loginResponseMutableLiveData.setValue(Resource.error(t.getMessage(), null));
            }
        });

        return loginResponseMutableLiveData;
    }
}
