package com.hasawi.sears.ui.main.view.home.user_details;

import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.hasawi.sears.R;
import com.hasawi.sears.data.api.model.pojo.Category;
import com.hasawi.sears.databinding.FragmentSelectMainCategoryBinding;
import com.hasawi.sears.ui.base.BaseFragment;
import com.hasawi.sears.ui.main.adapters.MainCategoryAdapter;
import com.hasawi.sears.ui.main.listeners.RecyclerItemClickListener;
import com.hasawi.sears.ui.main.view.DashboardActivity;
import com.hasawi.sears.ui.main.viewmodel.UserPreferenceViewModel;
import com.hasawi.sears.utils.PreferenceHandler;

import java.util.ArrayList;

public class SelectMainCategoryFragment extends BaseFragment {
    FragmentSelectMainCategoryBinding selectMainCategoryBinding;
    UserPreferenceActivity userPreferenceActivity;
    UserPreferenceViewModel userPreferenceViewModel;
    MainCategoryAdapter mainCategoryAdapter;
    Category currentSelectedCategory;
    private ArrayList<Category> categoryList = new ArrayList<>();

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_select_main_category;
    }

    @Override
    protected void setup() {
        selectMainCategoryBinding = (FragmentSelectMainCategoryBinding) viewDataBinding;
        userPreferenceActivity = (UserPreferenceActivity) getActivity();
        userPreferenceViewModel = userPreferenceActivity.getUserPreferenceViewModel();
        selectMainCategoryBinding.recyclerViewMainCategories.setLayoutManager(new LinearLayoutManager(getActivity()));
        userPreferenceActivity.showProgressBar(true);
        userPreferenceViewModel.getMainCateogries().observe(getActivity(), mainCategoryResponseResource -> {
            switch (mainCategoryResponseResource.status) {
                case SUCCESS:
                    categoryList = (ArrayList<Category>) mainCategoryResponseResource.data.getMainCategories();
                    mainCategoryAdapter = new MainCategoryAdapter(getContext(), categoryList);
                    selectMainCategoryBinding.recyclerViewMainCategories.setAdapter(mainCategoryAdapter);
                    break;
                case LOADING:
                    break;
                case ERROR:
                    Toast.makeText(getActivity(), mainCategoryResponseResource.message, Toast.LENGTH_SHORT).show();
                    break;
            }
            userPreferenceActivity.showProgressBar(false);
        });

        selectMainCategoryBinding.recyclerViewMainCategories.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                try {
                    currentSelectedCategory = categoryList.get(position);
                    PreferenceHandler preferenceHandler = new PreferenceHandler(getContext(), PreferenceHandler.TOKEN_LOGIN);
                    preferenceHandler.saveData(PreferenceHandler.LOGIN_CATEGORY_ID, currentSelectedCategory.getCategoryId());
                    preferenceHandler.saveData(PreferenceHandler.LOGIN_CATEGORY_NAME, currentSelectedCategory.getDescriptions().get(0).getCategoryName());

                    Intent intent = new Intent(userPreferenceActivity, DashboardActivity.class);
                    startActivity(intent);
                    userPreferenceActivity.finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }));
    }
}
