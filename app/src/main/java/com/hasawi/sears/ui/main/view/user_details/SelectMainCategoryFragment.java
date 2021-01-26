package com.hasawi.sears.ui.main.view.user_details;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.hasawi.sears.R;
import com.hasawi.sears.data.api.model.pojo.Category;
import com.hasawi.sears.databinding.FragmentSelectMainCategoryBinding;
import com.hasawi.sears.ui.base.BaseFragment;
import com.hasawi.sears.ui.main.view.DashboardActivity;
import com.hasawi.sears.ui.main.viewmodel.UserPreferenceViewModel;
import com.hasawi.sears.utils.PreferenceHandler;

import java.util.ArrayList;
import java.util.List;

public class SelectMainCategoryFragment extends BaseFragment {
    FragmentSelectMainCategoryBinding selectMainCategoryBinding;
    UserPreferenceActivity userPreferenceActivity;
    UserPreferenceViewModel userPreferenceViewModel;
    //    MainCategoryAdapter mainCategoryAdapter;
    Category currentSelectedCategory;
    FirebaseAnalytics mFirebaseAnalytics;
    private ArrayList<Category> categoryList = new ArrayList<>();
    private AppEventsLogger logger;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_select_main_category;
    }

    @Override
    protected void setup() {
        selectMainCategoryBinding = (FragmentSelectMainCategoryBinding) viewDataBinding;
        userPreferenceActivity = (UserPreferenceActivity) getActivity();
        logger = AppEventsLogger.newLogger(userPreferenceActivity);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(userPreferenceActivity);
        userPreferenceViewModel = userPreferenceActivity.getUserPreferenceViewModel();
//        selectMainCategoryBinding.recyclerViewMainCategories.setLayoutManager(new LinearLayoutManager(getActivity()));
        userPreferenceActivity.showProgressBar(true);
        userPreferenceViewModel.getMainCateogries().observe(getActivity(), mainCategoryResponseResource -> {
            switch (mainCategoryResponseResource.status) {
                case SUCCESS:
                    List<Category> allCategoryList = mainCategoryResponseResource.data.getMainCategories();
                    for (int i = 0; i < allCategoryList.size(); i++) {
                        if (allCategoryList.get(i).getParentId() == null)
                            categoryList.add(allCategoryList.get(i));
                    }
//                    mainCategoryAdapter = new MainCategoryAdapter(getContext(), categoryList);
//                    selectMainCategoryBinding.recyclerViewMainCategories.setAdapter(mainCategoryAdapter);
                    break;
                case LOADING:
                    break;
                case ERROR:
                    Toast.makeText(getActivity(), mainCategoryResponseResource.message, Toast.LENGTH_SHORT).show();
                    break;
            }
            userPreferenceActivity.showProgressBar(false);
        });

//        selectMainCategoryBinding.recyclerViewMainCategories.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
////                redirectToHomePage(position);
//            }
//        }));
        selectMainCategoryBinding.btnSelectWomen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectToHomePage(0);
            }
        });
        selectMainCategoryBinding.btnSelectMen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectToHomePage(1);
            }
        });
        selectMainCategoryBinding.btnSelectKids.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectToHomePage(2);
            }
        });
    }

    private void redirectToHomePage(int position) {
        try {
            currentSelectedCategory = categoryList.get(position);
            PreferenceHandler preferenceHandler = new PreferenceHandler(getContext(), PreferenceHandler.TOKEN_LOGIN);
            preferenceHandler.saveData(PreferenceHandler.LOGIN_CATEGORY_ID, currentSelectedCategory.getCategoryId());
            preferenceHandler.saveData(PreferenceHandler.LOGIN_CATEGORY_NAME, currentSelectedCategory.getDescriptions().get(0).getCategoryName());
            preferenceHandler.saveData(PreferenceHandler.HAS_CATEGORY_PAGE_SHOWN, true);
            logCATEGORY_SELECTEDEvent(currentSelectedCategory.getCategoryId(), currentSelectedCategory.getDescriptions().get(0).getCategoryName());
            Intent intent = new Intent(userPreferenceActivity, DashboardActivity.class);
            startActivity(intent);
            userPreferenceActivity.finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This function assumes logger is an instance of AppEventsLogger and has been
     * created using AppEventsLogger.newLogger() call.
     */
    public void logCATEGORY_SELECTEDEvent(String category_id, String category_name) {
        Bundle params = new Bundle();
        params.putString("category_id", category_id);
        params.putString("category_name", category_name);
        logger.logEvent("CATEGORY_SELECTED", params);
        mFirebaseAnalytics.logEvent("CATEGORY_SELECTED", params);
    }
}
