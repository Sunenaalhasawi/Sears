package com.hasawi.sears.ui.main.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hasawi.sears.data.api.Resource;
import com.hasawi.sears.data.api.model.pojo.Category;
import com.hasawi.sears.data.api.response.DynamicDataResponse;
import com.hasawi.sears.data.api.response.LanguageResponse;
import com.hasawi.sears.data.repository.DynamicDataRepository;

import java.util.List;

public class SelectUserDetailsViewModel extends ViewModel {
    DynamicDataRepository dynamicDataRepository;
    MutableLiveData<DynamicDataResponse.Data> dynamicData = new MutableLiveData<>();

    public SelectUserDetailsViewModel() {
        dynamicDataRepository = new DynamicDataRepository();
    }

    public MutableLiveData<Resource<LanguageResponse>> getLanguages() {
        return dynamicDataRepository.getLanguagesLiveData();
    }


    public MutableLiveData<Resource<DynamicDataResponse>> getDynamicData() {
        return dynamicDataRepository.getDynamicLiveData();
    }

    public void setDynamicData(MutableLiveData<DynamicDataResponse.Data> dynamicData) {
        this.dynamicData = dynamicData;
    }

    public List<String> getSizeList() {
        return dynamicData != null ? dynamicData.getValue().getSizes() : null;
    }

    public List<Category> getCategoriesList() {
        if (dynamicData.getValue() != null)
            return dynamicData.getValue().getCategories();
        else
            return null;
    }
}
