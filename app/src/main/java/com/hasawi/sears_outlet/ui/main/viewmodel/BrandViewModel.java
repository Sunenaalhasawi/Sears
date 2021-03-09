package com.hasawi.sears_outlet.ui.main.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hasawi.sears_outlet.data.api.Resource;
import com.hasawi.sears_outlet.data.api.response.BrandsResponse;
import com.hasawi.sears_outlet.data.repository.DynamicDataRepository;

public class BrandViewModel extends ViewModel {
    DynamicDataRepository dynamicDataRepository;

    public BrandViewModel() {
        dynamicDataRepository = new DynamicDataRepository();
    }

    public MutableLiveData<Resource<BrandsResponse>> getBrands() {
        return dynamicDataRepository.getBrandList();
    }
}
