package com.hasawi.sears.ui.main.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hasawi.sears.data.api.Resource;
import com.hasawi.sears.data.api.response.MainCategoryResponse;
import com.hasawi.sears.data.repository.DynamicDataRepository;

public class UserPreferenceViewModel extends ViewModel {

    DynamicDataRepository dynamicDataRepository;

    public UserPreferenceViewModel() {
        dynamicDataRepository = new DynamicDataRepository();
    }

    public MutableLiveData<Resource<MainCategoryResponse>> getMainCateogries() {
        return dynamicDataRepository.getMainCategories();
    }
}
