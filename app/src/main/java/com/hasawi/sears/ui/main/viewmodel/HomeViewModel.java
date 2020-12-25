package com.hasawi.sears.ui.main.viewmodel;

import androidx.lifecycle.ViewModel;

import com.hasawi.sears.data.repository.DynamicDataRepository;

public class HomeViewModel extends ViewModel {

    DynamicDataRepository dynamicDataRepository;

    public HomeViewModel() {
        dynamicDataRepository = new DynamicDataRepository();
    }
}
