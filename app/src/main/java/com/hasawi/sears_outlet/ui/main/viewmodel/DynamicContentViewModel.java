package com.hasawi.sears_outlet.ui.main.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hasawi.sears_outlet.data.api.Resource;
import com.hasawi.sears_outlet.data.api.response.DynamicContentResponse;
import com.hasawi.sears_outlet.data.repository.DynamicDataRepository;

public class DynamicContentViewModel extends ViewModel {

    DynamicDataRepository dynamicDataRepository;

    public DynamicContentViewModel() {
        dynamicDataRepository = new DynamicDataRepository();
    }

    public MutableLiveData<Resource<DynamicContentResponse>> getDynamicWebviewContent(String name) {
        return dynamicDataRepository.getDynamicWebviewContent(name);
    }
}
