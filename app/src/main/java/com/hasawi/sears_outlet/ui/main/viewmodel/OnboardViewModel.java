package com.hasawi.sears_outlet.ui.main.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hasawi.sears_outlet.data.api.Resource;
import com.hasawi.sears_outlet.data.api.response.BrandsResponse;
import com.hasawi.sears_outlet.data.api.response.GenericSizeResponse;
import com.hasawi.sears_outlet.data.api.response.MainCategoryResponse;
import com.hasawi.sears_outlet.data.api.response.PreferenceResponse;
import com.hasawi.sears_outlet.data.repository.DynamicDataRepository;

import java.util.Map;

public class OnboardViewModel extends ViewModel {

    DynamicDataRepository dynamicDataRepository;

    public OnboardViewModel() {
        dynamicDataRepository = new DynamicDataRepository();
    }

    public MutableLiveData<Resource<MainCategoryResponse>> getMainCateogries() {
        return dynamicDataRepository.getMainCategories();
    }

    public MutableLiveData<Resource<BrandsResponse>> getBrands() {
        return dynamicDataRepository.getBrandList();
    }

    public MutableLiveData<Resource<GenericSizeResponse>> getGenericSizes() {
        return dynamicDataRepository.getGenericSizes();
    }

    public MutableLiveData<Resource<PreferenceResponse>> savePreference(Map<String, Object> jsonParams, String udid) {
        return dynamicDataRepository.savePreference(jsonParams, udid);
    }
}
