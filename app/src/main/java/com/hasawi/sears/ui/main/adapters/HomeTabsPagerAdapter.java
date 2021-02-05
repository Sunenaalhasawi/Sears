package com.hasawi.sears.ui.main.adapters;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.hasawi.sears.data.api.model.pojo.Category;
import com.hasawi.sears.data.api.response.DynamicUiResponse;
import com.hasawi.sears.ui.main.view.dashboard.home.HomeFragment;

import java.util.HashMap;
import java.util.List;

public class HomeTabsPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    Context context;
    List<Category> mainCategoryList;
    HashMap<String, DynamicUiResponse.UiData> dynamicDataHashMap;


    public HomeTabsPagerAdapter(Context context, FragmentManager fm, int NumOfTabs, List<Category> mainCategoryList, HashMap<String, DynamicUiResponse.UiData> dynamicDataHashMap) {
        super(fm);
        this.context = context;
        this.mNumOfTabs = NumOfTabs;
        this.mainCategoryList = mainCategoryList;
        this.dynamicDataHashMap = dynamicDataHashMap;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mainCategoryList.get(position).getDescriptions().get(0).getCategoryName();
    }

    @Override
    public Fragment getItem(int position) {
//        try {
//            PreferenceHandler preferenceHandler = new PreferenceHandler(context, PreferenceHandler.TOKEN_LOGIN);
//            preferenceHandler.saveData(PreferenceHandler.LOGIN_CATEGORY_ID, mainCategoryList.get(position).getCategoryId());
//            preferenceHandler.saveData(PreferenceHandler.LOGIN_CATEGORY_NAME, mainCategoryList.get(position).getDescriptions().get(0).getCategoryName());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return HomeFragment.newInstance(position, mainCategoryList.get(position).getDescriptions().get(0).getCategoryName(), dynamicDataHashMap.get(mainCategoryList.get(position).getCategoryId() + ""));
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}