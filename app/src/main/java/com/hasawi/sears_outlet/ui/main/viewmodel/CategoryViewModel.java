package com.hasawi.sears_outlet.ui.main.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hasawi.sears_outlet.data.api.Resource;
import com.hasawi.sears_outlet.data.api.response.MainCategoryResponse;
import com.hasawi.sears_outlet.data.repository.DynamicDataRepository;

public class CategoryViewModel extends ViewModel {

    DynamicDataRepository dynamicDataRepository;

    public CategoryViewModel() {
        dynamicDataRepository = new DynamicDataRepository();
    }

    public MutableLiveData<Resource<MainCategoryResponse>> getCategoryTree() {
        return dynamicDataRepository.getMainCategories();
    }
}
