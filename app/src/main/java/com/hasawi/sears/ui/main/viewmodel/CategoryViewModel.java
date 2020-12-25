package com.hasawi.sears.ui.main.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hasawi.sears.data.api.Resource;
import com.hasawi.sears.data.api.response.MainCategoryResponse;
import com.hasawi.sears.data.repository.DynamicDataRepository;

public class CategoryViewModel extends ViewModel {

    DynamicDataRepository dynamicDataRepository;

    public CategoryViewModel() {
        dynamicDataRepository = new DynamicDataRepository();
    }

    public MutableLiveData<Resource<MainCategoryResponse>> getCategoryTree() {
        return dynamicDataRepository.getMainCategories();
    }
}
