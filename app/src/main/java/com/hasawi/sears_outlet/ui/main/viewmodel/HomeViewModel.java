package com.hasawi.sears_outlet.ui.main.viewmodel;

import androidx.lifecycle.ViewModel;

import com.hasawi.sears_outlet.data.repository.DynamicDataRepository;

public class HomeViewModel extends ViewModel {

    DynamicDataRepository dynamicDataRepository;

    public HomeViewModel() {
        dynamicDataRepository = new DynamicDataRepository();
    }
}
