package com.hasawi.sears.data.repository;

import androidx.lifecycle.MutableLiveData;

import com.hasawi.sears.data.api.Resource;
import com.hasawi.sears.data.api.RetrofitApiClient;
import com.hasawi.sears.data.api.model.pojo.Category;
import com.hasawi.sears.data.api.response.DynamicDataResponse;
import com.hasawi.sears.data.api.response.LanguageResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DynamicDataRepository {

    public MutableLiveData<Resource<LanguageResponse>> getLanguagesLiveData() {
        MutableLiveData<Resource<LanguageResponse>> mutableLiveDataLanguageResponse = new MutableLiveData<>();
        Call<LanguageResponse> languageResponseCall = RetrofitApiClient.getInstance().getApiInterface().getLanguages();
        languageResponseCall.enqueue(new Callback<LanguageResponse>() {
            @Override
            public void onResponse(Call<LanguageResponse> call, Response<LanguageResponse> response) {
                if (response.code() != 200) {
                    mutableLiveDataLanguageResponse.setValue(Resource.error("Something Went Wrong !", null));
                } else if (response.body() != null) {
                    LanguageResponse languageResponse = response.body();
                    if (languageResponse != null && languageResponse.getData() != null) {
                        mutableLiveDataLanguageResponse.postValue(Resource.success(languageResponse));
                    }
                }
            }

            @Override
            public void onFailure(Call<LanguageResponse> call, Throwable t) {
                mutableLiveDataLanguageResponse.setValue(Resource.error(t.getMessage(), null));
            }
        });

        return mutableLiveDataLanguageResponse;
    }

    public MutableLiveData<Resource<DynamicDataResponse>> getDynamicLiveData() {

        MutableLiveData<Resource<DynamicDataResponse>> dynamicLiveData = new MutableLiveData<>();
        Call<DynamicDataResponse> dynamicDataResponseCall = RetrofitApiClient.getInstance().getApiInterface().getDynamicData();
        dynamicDataResponseCall.enqueue(new Callback<DynamicDataResponse>() {
            @Override
            public void onResponse(Call<DynamicDataResponse> call, Response<DynamicDataResponse> response) {
                DynamicDataResponse dynamicDataResponse = response.body();
                if (response.code() != 200) {
                    dynamicLiveData.setValue(Resource.error("Something Went Wrong. Please Try Again !", null));
                } else if (dynamicDataResponse != null) {
                    List<String> genderList = dynamicDataResponse.getData().getGender();
                    List<String> sizeList = dynamicDataResponse.getData().getSizes();
                    List<Category> categoryList = dynamicDataResponse.getData().getCategories();
                    DynamicDataResponse.Data data = new DynamicDataResponse.Data();
                    data.setGender(genderList);
                    data.setSizes(sizeList);
                    data.setCategories(categoryList);
                    dynamicLiveData.setValue(Resource.success(response.body()));
                }
            }

            @Override
            public void onFailure(Call<DynamicDataResponse> call, Throwable t) {
                dynamicLiveData.setValue(Resource.error(t.getMessage(), null));

            }
        });
        return dynamicLiveData;
    }
}
