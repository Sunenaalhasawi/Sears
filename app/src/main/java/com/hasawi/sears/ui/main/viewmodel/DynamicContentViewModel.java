package com.hasawi.sears.ui.main.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hasawi.sears.data.api.Resource;
import com.hasawi.sears.data.api.response.DynamicContentResponse;
import com.hasawi.sears.data.repository.DynamicDataRepository;

public class DynamicContentViewModel extends ViewModel {

    DynamicDataRepository dynamicDataRepository;

    public DynamicContentViewModel() {
        dynamicDataRepository = new DynamicDataRepository();
    }

    public MutableLiveData<Resource<DynamicContentResponse>> getDynamicWebviewContent(String name) {
        return dynamicDataRepository.getDynamicWebviewContent(name);
    }
}
