package com.hasawi.sears.ui.main.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.hasawi.sears.data.api.model.pojo.Category;

import java.util.List;

public class HomeTabsPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    Context context;
    List<Category> mainCategoryList;

    public HomeTabsPagerAdapter(Context context, FragmentManager fm, int NumOfTabs, List<Category> mainCategoryList) {
        super(fm);
        this.context = context;
        this.mNumOfTabs = NumOfTabs;
        this.mainCategoryList = mainCategoryList;
    }

//    @Override
//    public Fragment getItem(int position) {
//        try {
//            PreferenceHandler preferenceHandler = new PreferenceHandler(context, PreferenceHandler.TOKEN_LOGIN);
//            preferenceHandler.saveData(PreferenceHandler.LOGIN_CATEGORY_ID, mainCategoryList.get(position).getCategoryId());
//            preferenceHandler.saveData(PreferenceHandler.LOGIN_CATEGORY_NAME, mainCategoryList.get(position).getDescriptions().get(0).getCategoryName());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return MainFragment.newInstance(position);
//    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return null;
    }
}