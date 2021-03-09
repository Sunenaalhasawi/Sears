package com.hasawi.sears_outlet.ui.main.view.onboard;

import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.hasawi.sears_outlet.R;
import com.hasawi.sears_outlet.data.api.model.pojo.Category;
import com.hasawi.sears_outlet.databinding.FragmentOnboardCategoryBinding;
import com.hasawi.sears_outlet.ui.base.BaseFragment;
import com.hasawi.sears_outlet.ui.main.adapters.OnBoardCategoryAdapter;
import com.hasawi.sears_outlet.ui.main.listeners.RecyclerviewSingleChoiceClickListener;
import com.hasawi.sears_outlet.ui.main.viewmodel.OnboardViewModel;
import com.hasawi.sears_outlet.utils.PreferenceHandler;
import com.hasawi.sears_outlet.utils.dialogs.GeneralDialog;

import java.util.ArrayList;
import java.util.List;

public class SelectCategoryFragment extends BaseFragment implements View.OnClickListener, RecyclerviewSingleChoiceClickListener {
    FragmentOnboardCategoryBinding fragmentOnboardCategoryBinding;
    OnBoardActivity onBoardActivity;
    OnboardViewModel onboardViewModel;
    Category currentSelectedCategory;
    OnBoardCategoryAdapter onBoardCategoryAdapter;
    private ArrayList<Category> categoryList = new ArrayList<>();

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_onboard_category;
    }

    @Override
    protected void setup() {

        fragmentOnboardCategoryBinding = (FragmentOnboardCategoryBinding) viewDataBinding;
        onBoardActivity = (OnBoardActivity) getActivity();
        onBoardActivity.showProgressBar(true);
        onboardViewModel = new ViewModelProvider(this).get(OnboardViewModel.class);
        // Create a grid layout with two columns
        GridLayoutManager layoutManager = new GridLayoutManager(onBoardActivity, 3);

        // Create a custom SpanSizeLookup where the first item spans both columns
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return 1;
            }
        });
        fragmentOnboardCategoryBinding.recyclerviewCategories.setLayoutManager(layoutManager);
        onboardViewModel.getMainCateogries().observe(getActivity(), mainCategoryResponseResource -> {
            switch (mainCategoryResponseResource.status) {
                case SUCCESS:
                    List<Category> allCategoryList = mainCategoryResponseResource.data.getMainCategories();
                    for (int i = 0; i < allCategoryList.size(); i++) {
                        if (allCategoryList.get(i).getParentId() == null) {
                            categoryList.add(allCategoryList.get(i));
                        }
                    }
                    setUi();
                    break;
                case LOADING:
                    break;
                case ERROR:
                    Toast.makeText(getActivity(), mainCategoryResponseResource.message, Toast.LENGTH_SHORT).show();
                    break;
            }
            onBoardActivity.showProgressBar(false);
        });

        fragmentOnboardCategoryBinding.tvSkip.setOnClickListener(this);
        fragmentOnboardCategoryBinding.btnDone.setOnClickListener(this);
    }

    private void setUi() {
        onBoardCategoryAdapter = new OnBoardCategoryAdapter(getContext(), categoryList);
        onBoardCategoryAdapter.setOnItemClickListener(this);
        fragmentOnboardCategoryBinding.recyclerviewCategories.setAdapter(onBoardCategoryAdapter);
    }

    private Category getSelectedCategory(String name) {
        try {
            for (int i = 0; i < categoryList.size(); i++) {
                if (categoryList.get(i).getDescriptions().get(0).getCategoryName().contains(name)) {
                    return categoryList.get(i);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvSkip:
                onBoardActivity.redirectToHomePage();
                break;
            case R.id.btnDone:
                if (currentSelectedCategory != null) {
                    saveValues();
                    showNextPage();
                } else {
                    GeneralDialog generalDialog = new GeneralDialog("Error", "Please select any category to proceed?");
                    generalDialog.show(getParentFragmentManager(), "GENERAL_DIALOG");
                }
                break;
            default:
                break;
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        OnBoardCategoryAdapter.setsSelected(-1);
    }

    private void showNextPage() {
        onBoardActivity.replaceFragment(new SelectSizeFragment(), null);
    }

    public void saveValues() {
        PreferenceHandler preferenceHandler = new PreferenceHandler(getContext(), PreferenceHandler.TOKEN_LOGIN);
        preferenceHandler.saveData(PreferenceHandler.LOGIN_CATEGORY_ID, currentSelectedCategory.getCategoryId());
        preferenceHandler.saveData(PreferenceHandler.LOGIN_CATEGORY_NAME, currentSelectedCategory.getDescriptions().get(0).getCategoryName());
        preferenceHandler.saveData(PreferenceHandler.HAS_CATEGORY_PAGE_SHOWN, true);
    }

    @Override
    public void onItemClickListener(int position, View view) {
        onBoardCategoryAdapter.selectedItem();
        currentSelectedCategory = categoryList.get(position);
    }
}
