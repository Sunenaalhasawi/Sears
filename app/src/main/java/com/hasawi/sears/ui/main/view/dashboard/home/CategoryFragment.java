package com.hasawi.sears.ui.main.view.dashboard.home;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.hasawi.sears.R;
import com.hasawi.sears.data.api.model.pojo.Category;
import com.hasawi.sears.databinding.FragmentCategoriesBinding;
import com.hasawi.sears.ui.base.BaseFragment;
import com.hasawi.sears.ui.main.adapters.CategoryGridAdapter;
import com.hasawi.sears.ui.main.adapters.SubCategoryAdapter;
import com.hasawi.sears.ui.main.listeners.RecyclerviewSingleChoiceClickListener;
import com.hasawi.sears.ui.main.view.DashboardActivity;
import com.hasawi.sears.ui.main.view.dashboard.product.paging.ProductListingFragment;
import com.hasawi.sears.ui.main.viewmodel.CategoryViewModel;
import com.hasawi.sears.utils.PreferenceHandler;

import java.util.ArrayList;
import java.util.List;

public class CategoryFragment extends BaseFragment implements RecyclerviewSingleChoiceClickListener {
    FragmentCategoriesBinding fragmentCategoriesBinding;
    CategoryViewModel categoryViewModel;
    String selectedMainCategoryId = "";
    DashboardActivity dashboardActivity;
    CategoryGridAdapter categoryGridAdapter;
    ArrayList<Category> currentGridList = new ArrayList<>();
    private ArrayList<Category> subCategoryArrayList = new ArrayList<>();
    private SubCategoryAdapter subCategoryAdapter;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_categories;
    }

    @Override
    protected void setup() {
        fragmentCategoriesBinding = (FragmentCategoriesBinding) viewDataBinding;
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        dashboardActivity = (DashboardActivity) getActivity();
        PreferenceHandler preferenceHandler = new PreferenceHandler(getContext(), PreferenceHandler.TOKEN_LOGIN);
        selectedMainCategoryId = preferenceHandler.getData(PreferenceHandler.LOGIN_CATEGORY_ID, "");
        callCategoryApi();
        fragmentCategoriesBinding.gridViewCategories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                if (currentGridList != null) {
                    String selectedCategoryId = currentGridList.get(position).getCategoryId();
                    String selectedCategoryName = currentGridList.get(position).getDescriptions().get(0).getCategoryName();
                    redirectToProductListing(selectedCategoryId, selectedCategoryName);
                }
            }
        });

    }

    private void callCategoryApi() {
        fragmentCategoriesBinding.progressBar.setVisibility(View.VISIBLE);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        fragmentCategoriesBinding.recyclerViewSubCategories.setLayoutManager(horizontalLayoutManager);

        categoryViewModel.getCategoryTree().observe(this, mainCategoryResponseResource -> {
            switch (mainCategoryResponseResource.status) {
                case SUCCESS:
                    List<Category> allCategoryList = mainCategoryResponseResource.data.getMainCategories();
                    for (int i = 0; i < allCategoryList.size(); i++) {
                        if (allCategoryList.get(i).getCategoryId().equals(selectedMainCategoryId)) {
                            subCategoryArrayList = (ArrayList<Category>) allCategoryList.get(i).getCategories();
                            break;
                        }
                    }
                    subCategoryAdapter = new SubCategoryAdapter(getActivity(), subCategoryArrayList);
                    subCategoryAdapter.setOnItemClickListener(this);
                    fragmentCategoriesBinding.recyclerViewSubCategories.setAdapter(subCategoryAdapter);
                    if (subCategoryArrayList.size() > 0) {
                        currentGridList = (ArrayList<Category>) subCategoryArrayList.get(0).getCategories();
                        categoryGridAdapter = new CategoryGridAdapter(dashboardActivity, currentGridList);
                        fragmentCategoriesBinding.gridViewCategories.setAdapter(categoryGridAdapter);
                    }

                    break;
                case LOADING:
                    break;
                case ERROR:
                    Toast.makeText(dashboardActivity, mainCategoryResponseResource.message, Toast.LENGTH_SHORT).show();
                    break;
            }
            fragmentCategoriesBinding.progressBar.setVisibility(View.GONE);
        });

    }


    @Override
    public void onItemClickListener(int position, View view) {
        subCategoryAdapter.selectedItem();
        fragmentCategoriesBinding.progressBar.setVisibility(View.VISIBLE);
        String selectedSubCategoryId = "", selectedSubCategoryName = "";
        try {
            selectedSubCategoryId = subCategoryArrayList.get(position).getCategoryId();
            selectedSubCategoryName = subCategoryArrayList.get(position).getDescriptions().get(0).getCategoryName();
        } catch (Exception e) {
            e.printStackTrace();
        }
//            for (int i=0;i<subCategoryArrayList.size();i++){
//                if(subCategoryArrayList.get(i).getCategoryId()==selectedSubCategoryId){
        currentGridList = (ArrayList<Category>) subCategoryArrayList.get(position).getCategories();
        if (currentGridList == null || currentGridList.size() == 0) {
            redirectToProductListing(selectedSubCategoryId, selectedSubCategoryName);
        } else {
            categoryGridAdapter = new CategoryGridAdapter(dashboardActivity, currentGridList);
            fragmentCategoriesBinding.gridViewCategories.setAdapter(categoryGridAdapter);
            fragmentCategoriesBinding.progressBar.setVisibility(View.GONE);
        }

    }

    private void redirectToProductListing(String categoryId, String categoryName) {
        fragmentCategoriesBinding.progressBar.setVisibility(View.GONE);
        Bundle bundle = new Bundle();
        bundle.putString("category_id", categoryId);
        bundle.putString("category_name", categoryName);
        dashboardActivity.replaceFragment(R.id.fragment_replacer, new ProductListingFragment(), bundle, true, false);

    }

}

