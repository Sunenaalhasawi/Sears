package com.hasawi.sears_outlet.ui.main.view.onboard;

import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;

import com.hasawi.sears_outlet.R;
import com.hasawi.sears_outlet.data.api.model.pojo.Category;
import com.hasawi.sears_outlet.databinding.FragmentOnboardCategoryBinding;
import com.hasawi.sears_outlet.ui.base.BaseFragment;
import com.hasawi.sears_outlet.ui.main.viewmodel.OnboardViewModel;
import com.hasawi.sears_outlet.utils.PreferenceHandler;

import java.util.ArrayList;
import java.util.List;

public class SelectCategoryFragment extends BaseFragment implements View.OnClickListener {
    FragmentOnboardCategoryBinding fragmentOnboardCategoryBinding;
    OnBoardActivity onBoardActivity;
    OnboardViewModel onboardViewModel;
    Category currentSelectedCategory;
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
        fragmentOnboardCategoryBinding.btnMen.setOnClickListener(this);
        fragmentOnboardCategoryBinding.btnWomen.setOnClickListener(this);
        fragmentOnboardCategoryBinding.btnHome.setOnClickListener(this);
        fragmentOnboardCategoryBinding.btnSports.setOnClickListener(this);
        fragmentOnboardCategoryBinding.btnKids.setOnClickListener(this);
        fragmentOnboardCategoryBinding.btnDone.setOnClickListener(this);
    }

    private void setUi() {
        if (categoryList.size() > 0) {
            for (int i = 0; i < categoryList.size(); i++) {
                String categoryName = categoryList.get(i).getDescriptions().get(0).getCategoryName();
                if (categoryName.equalsIgnoreCase("Men")) {
                    fragmentOnboardCategoryBinding.btnMen.setVisibility(View.VISIBLE);
                }
                if (categoryName.equalsIgnoreCase("Women")) {
                    fragmentOnboardCategoryBinding.btnMen.setVisibility(View.VISIBLE);
                }
                if (categoryName.equalsIgnoreCase("Kids")) {
                    fragmentOnboardCategoryBinding.btnMen.setVisibility(View.VISIBLE);
                }
                if (categoryName.equalsIgnoreCase("Home")) {
                    fragmentOnboardCategoryBinding.btnMen.setVisibility(View.VISIBLE);
                }
                if (categoryName.equalsIgnoreCase("Sports")) {
                    fragmentOnboardCategoryBinding.btnMen.setVisibility(View.VISIBLE);
                }
            }
        }
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
            case R.id.btnWomen:
                if (fragmentOnboardCategoryBinding.btnWomen.getBackground() == getResources().getDrawable(R.drawable.grey_outlined_rounded_rectangle_20dp)) {
                    fragmentOnboardCategoryBinding.btnWomen.setBackground(getResources().getDrawable(R.drawable.bright_blue_rounded_rectangle_20dp));
                    fragmentOnboardCategoryBinding.btnWomen.setTextColor(getResources().getColor(R.color.white));
                } else {
                    fragmentOnboardCategoryBinding.btnWomen.setBackground(getResources().getDrawable(R.drawable.grey_outlined_rounded_rectangle_20dp));
                    fragmentOnboardCategoryBinding.btnWomen.setTextColor(getResources().getColor(R.color.cart_grey));
                }
                currentSelectedCategory = getSelectedCategory("Women");
                break;
            case R.id.btnMen:
                if (fragmentOnboardCategoryBinding.btnMen.getBackground() == getResources().getDrawable(R.drawable.grey_outlined_rounded_rectangle_20dp)) {
                    fragmentOnboardCategoryBinding.btnMen.setBackground(getResources().getDrawable(R.drawable.bright_blue_rounded_rectangle_20dp));
                    fragmentOnboardCategoryBinding.btnMen.setTextColor(getResources().getColor(R.color.white));
                } else {
                    fragmentOnboardCategoryBinding.btnMen.setBackground(getResources().getDrawable(R.drawable.grey_outlined_rounded_rectangle_20dp));
                    fragmentOnboardCategoryBinding.btnMen.setTextColor(getResources().getColor(R.color.cart_grey));
                }
                currentSelectedCategory = getSelectedCategory("Men");

                break;
            case R.id.btnKids:
                if (fragmentOnboardCategoryBinding.btnKids.getBackground() == getResources().getDrawable(R.drawable.grey_outlined_rounded_rectangle_20dp)) {
                    fragmentOnboardCategoryBinding.btnKids.setBackground(getResources().getDrawable(R.drawable.bright_blue_rounded_rectangle_20dp));
                    fragmentOnboardCategoryBinding.btnKids.setTextColor(getResources().getColor(R.color.white));
                } else {
                    fragmentOnboardCategoryBinding.btnKids.setBackground(getResources().getDrawable(R.drawable.grey_outlined_rounded_rectangle_20dp));
                    fragmentOnboardCategoryBinding.btnKids.setTextColor(getResources().getColor(R.color.cart_grey));
                }
                currentSelectedCategory = getSelectedCategory("Kids");
                break;
            case R.id.btnHome:
                currentSelectedCategory = getSelectedCategory("Home");
                break;
            case R.id.btnSports:
                currentSelectedCategory = getSelectedCategory("Sports");
                break;
            case R.id.tvSkip:
                onBoardActivity.redirectToHomePage();
                break;
            case R.id.btnDone:
                try {
                    currentSelectedCategory = categoryList.get(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                saveValues();
                showNextPage();
                break;
            default:
                break;
        }

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
}
