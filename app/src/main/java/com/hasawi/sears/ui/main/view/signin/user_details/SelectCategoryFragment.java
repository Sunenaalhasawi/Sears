package com.hasawi.sears.ui.main.view.signin.user_details;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.hasawi.sears.R;
import com.hasawi.sears.data.api.model.pojo.Category;
import com.hasawi.sears.databinding.FragmentSelectCategoryBinding;
import com.hasawi.sears.ui.base.BaseFragment;
import com.hasawi.sears.ui.main.adapters.SelectUserCategoryAdapter;
import com.hasawi.sears.ui.main.listeners.RecyclerviewSingleChoiceClickListener;
import com.hasawi.sears.ui.main.viewmodel.SelectUserDetailsViewModel;
import com.hasawi.sears.utils.PreferenceHandler;

import java.util.ArrayList;

public class SelectCategoryFragment extends BaseFragment implements RecyclerviewSingleChoiceClickListener, View.OnClickListener {

    FragmentSelectCategoryBinding fragmentSelectCategoryBinding;
    SelectUserDetailsActivity selectUserDetailsActivity;
    LinearLayoutManager horizontalLayoutManager;
    SelectUserCategoryAdapter selectUserCategoryAdapter;
    SelectUserDetailsViewModel selectUserDetailsViewModel;
    String selectedCategoryId = "";
    Category currentSelectedCategory;
    private ArrayList<Category> categoryList = new ArrayList<>();
    private Bundle dataBundle = new Bundle();

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_select_category;
    }

    @Override
    protected void setup() {
        fragmentSelectCategoryBinding = (FragmentSelectCategoryBinding) viewDataBinding;
        selectUserDetailsActivity = (SelectUserDetailsActivity) getActivity();
        selectUserDetailsViewModel = selectUserDetailsActivity.getSelectUserDetailsViewModel();
        fragmentSelectCategoryBinding.btnContinue.setOnClickListener(this);
        fragmentSelectCategoryBinding.tvLanguage.setOnClickListener(this);
        fragmentSelectCategoryBinding.tvGender.setOnClickListener(this);
        fragmentSelectCategoryBinding.tvProductCategory.setOnClickListener(this);
        fragmentSelectCategoryBinding.tvSize.setOnClickListener(this);

        try {
            dataBundle = getArguments();
            if (dataBundle == null)
                dataBundle = new Bundle();
        } catch (Exception e) {
            e.printStackTrace();
        }


        horizontalLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        fragmentSelectCategoryBinding.recyclerViewCategories.setLayoutManager(horizontalLayoutManager);

//        if(selectUserDetailsViewModel.getCategoriesList()==null){
        fragmentSelectCategoryBinding.progressBar.setVisibility(View.VISIBLE);
        selectUserDetailsViewModel.getDynamicData().observe(getActivity(), dynamicDataResponseResource -> {
            switch (dynamicDataResponseResource.status) {
                case SUCCESS:
                    selectUserDetailsViewModel.setDynamicData(new MutableLiveData<>(dynamicDataResponseResource.data.getData()));
                    categoryList = (ArrayList<Category>) dynamicDataResponseResource.data.getData().getCategories();
                    selectUserCategoryAdapter = new SelectUserCategoryAdapter(getContext(), categoryList);
                    selectUserCategoryAdapter.setOnItemClickListener(this);
                    fragmentSelectCategoryBinding.recyclerViewCategories.setAdapter(selectUserCategoryAdapter);
                    break;
                case LOADING:
                    break;
                case ERROR:
                    Toast.makeText(selectUserDetailsActivity, dynamicDataResponseResource.message, Toast.LENGTH_SHORT).show();
                    break;
            }
            fragmentSelectCategoryBinding.progressBar.setVisibility(View.GONE);
        });
//        }
//        else {
//            categoryList = (ArrayList<Category>) selectUserDetailsViewModel.getCategoriesList();
//            selectUserCategoryAdapter = new SelectUserCategoryAdapter(getContext(), categoryList);
//            selectUserCategoryAdapter.setOnItemClickListener(this);
//            fragmentSelectCategoryBinding.recyclerViewCategories.setAdapter(selectUserCategoryAdapter);
//        }


        fragmentSelectCategoryBinding.imageButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (horizontalLayoutManager.findLastCompletelyVisibleItemPosition() < (selectUserCategoryAdapter.getItemCount() - 1)) {
                    horizontalLayoutManager.scrollToPosition(horizontalLayoutManager.findLastCompletelyVisibleItemPosition() + 1);
                }
            }
        });


        fragmentSelectCategoryBinding.scrollView.post(new Runnable() {
            @Override
            public void run() {
                fragmentSelectCategoryBinding.scrollView.fullScroll(View.FOCUS_RIGHT);
            }
        });


    }

    @Override
    public void onItemClickListener(int position, View view) {
        selectUserCategoryAdapter.selectedItem();
        try {
            currentSelectedCategory = categoryList.get(position);
            selectedCategoryId = currentSelectedCategory.getCategoryId();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View v) {
        try {
            dataBundle.putString("category_id", selectedCategoryId);
            dataBundle.putString("category_name", currentSelectedCategory.getDescriptions().get(0).getCategoryName());
            PreferenceHandler preferenceHandler = new PreferenceHandler(getContext(), PreferenceHandler.TOKEN_LOGIN);
            preferenceHandler.saveData(PreferenceHandler.LOGIN_CATEGORY_ID, selectedCategoryId);
            preferenceHandler.saveData(PreferenceHandler.LOGIN_CATEGORY_NAME, currentSelectedCategory.getDescriptions().get(0).getCategoryName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        switch (v.getId()) {
            case R.id.tvLanguage:
                selectUserDetailsActivity.replaceFragment(new SelectLanguageFragment(), dataBundle);
                break;
            case R.id.tvGender:
                selectUserDetailsActivity.replaceFragment(new SelectGenderFragment(), dataBundle);
                break;
            case R.id.tv_product_category:
                selectUserDetailsActivity.replaceFragment(new SelectCategoryFragment(), dataBundle);
                break;
            case R.id.tv_size:
                selectUserDetailsActivity.replaceFragment(new SelectSizeFragment(), dataBundle);
                break;
            case R.id.btnContinue:
                if (!selectedCategoryId.equalsIgnoreCase(""))
                    selectUserDetailsActivity.replaceFragment(new SelectSizeFragment(), dataBundle);
                break;
            default:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        selectTextView();
        try {
//            selectUserCategoryAdapter = new SelectUserCategoryAdapter(getContext(), (ArrayList<Category>) selectUserDetailsViewModel.getCategoriesList());
            SelectUserCategoryAdapter.setsSelected(-1);
//            fragmentSelectCategoryBinding.recyclerViewCategories.setAdapter(selectUserCategoryAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void selectTextView() {
        fragmentSelectCategoryBinding.tvProductCategory.setBackground(getResources().getDrawable(R.drawable.blue_rounded_rectangle_8dp));
        fragmentSelectCategoryBinding.tvProductCategory.setTextColor(getResources().getColor(R.color.white));
        fragmentSelectCategoryBinding.tvProductCategory.setCompoundDrawablesWithIntrinsicBounds(R.drawable.translation_white, 0, 0, 0);
    }
}
