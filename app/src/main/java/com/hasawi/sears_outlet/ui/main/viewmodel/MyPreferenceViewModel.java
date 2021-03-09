package com.hasawi.sears_outlet.ui.main.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hasawi.sears_outlet.data.api.Resource;
import com.hasawi.sears_outlet.data.api.response.BrandsResponse;
import com.hasawi.sears_outlet.data.api.response.GenericSizeResponse;
import com.hasawi.sears_outlet.data.api.response.PreferenceResponse;
import com.hasawi.sears_outlet.data.repository.DynamicDataRepository;

import java.util.Map;

public class MyPreferenceViewModel extends ViewModel {

    DynamicDataRepository dynamicDataRepository;

    public MyPreferenceViewModel() {
        dynamicDataRepository = new DynamicDataRepository();
    }

    public MutableLiveData<Resource<BrandsResponse>> getBrands() {
        return dynamicDataRepository.getBrandList();
    }

    public MutableLiveData<Resource<GenericSizeResponse>> getGenericSizes() {
        return dynamicDataRepository.getGenericSizes();
    }

    public MutableLiveData<Resource<PreferenceResponse>> savePreference(Map<String, Object> jsonParams, String customerID) {
        return dynamicDataRepository.savePreference(jsonParams, customerID);
    }

    public MutableLiveData<Resource<PreferenceResponse>> getPreference(String udid) {
        return dynamicDataRepository.getPreference(udid);
    }
}
