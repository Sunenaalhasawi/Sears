package com.hasawi.sears.ui.main.view.signin.user_details;

import android.os.Bundle;
import android.view.View;

import com.hasawi.sears.R;
import com.hasawi.sears.databinding.FragmentSelectGenderBinding;
import com.hasawi.sears.ui.base.BaseFragment;
import com.hasawi.sears.ui.main.viewmodel.SelectUserDetailsViewModel;

public class SelectGenderFragment extends BaseFragment implements View.OnClickListener {

    FragmentSelectGenderBinding fragmentSelectGenderBinding;
    SelectUserDetailsActivity selectUserDetailsActivity;
    private String selectedGender = "";
    private Bundle dataBundle = new Bundle();
    private SelectUserDetailsViewModel selectUserDetailsViewModel;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_select_gender;
    }

    @Override
    protected void setup() {
        fragmentSelectGenderBinding = (FragmentSelectGenderBinding) viewDataBinding;
        selectUserDetailsActivity = (SelectUserDetailsActivity) getActivity();
        selectUserDetailsViewModel = selectUserDetailsActivity.getSelectUserDetailsViewModel();
        fragmentSelectGenderBinding.constraintLayoutMale.setOnClickListener(this);
        fragmentSelectGenderBinding.constraintLayoutFemale.setOnClickListener(this);
        fragmentSelectGenderBinding.btnContinue.setOnClickListener(this);
        fragmentSelectGenderBinding.tvLanguage.setOnClickListener(this);
        fragmentSelectGenderBinding.tvGender.setOnClickListener(this);
        fragmentSelectGenderBinding.tvProductCategory.setOnClickListener(this);
        fragmentSelectGenderBinding.tvSize.setOnClickListener(this);

        try {
            dataBundle = getArguments();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        selectTextView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnContinue:
                dataBundle.putString("gender", selectedGender);
                selectUserDetailsActivity.replaceFragment(new SelectCategoryFragment(), dataBundle);
                break;
            case R.id.constraintLayoutMale:
                selectMale();
                break;
            case R.id.constraintLayoutFemale:
                selectFemale();
                break;
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
            default:
                break;
        }
    }

    private void selectMale() {
        fragmentSelectGenderBinding.constraintLayoutMale.setBackground(getActivity().getResources().getDrawable(R.drawable.blue_rounded_rectangle_12dp_corner));
        fragmentSelectGenderBinding.tvMale.setTextColor(getResources().getColor(R.color.white));
        fragmentSelectGenderBinding.constraintLayoutFemale.setBackground(getActivity().getResources().getDrawable(R.drawable.blue_outlined_rounded_rectangle_12dp));
        fragmentSelectGenderBinding.txtFemale.setTextColor(getResources().getColor(R.color.txt_clr_blue));
        selectedGender = "Male";
    }

    private void selectFemale() {
        fragmentSelectGenderBinding.constraintLayoutFemale.setBackground(getActivity().getResources().getDrawable(R.drawable.blue_rounded_rectangle_12dp_corner));
        fragmentSelectGenderBinding.txtFemale.setTextColor(getResources().getColor(R.color.white));
        fragmentSelectGenderBinding.constraintLayoutMale.setBackground(getActivity().getResources().getDrawable(R.drawable.blue_outlined_rounded_rectangle_12dp));
        fragmentSelectGenderBinding.tvMale.setTextColor(getResources().getColor(R.color.txt_clr_blue));
        selectedGender = "Female";
    }

    public void selectTextView() {
        fragmentSelectGenderBinding.tvGender.setBackground(getResources().getDrawable(R.drawable.blue_rounded_rectangle_8dp));
        fragmentSelectGenderBinding.tvGender.setTextColor(getResources().getColor(R.color.white));
        fragmentSelectGenderBinding.tvGender.setCompoundDrawablesWithIntrinsicBounds(R.drawable.translation_white, 0, 0, 0);
    }
}
