package com.hasawi.sears.ui.main.view.signin.user_details;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.hasawi.sears.R;
import com.hasawi.sears.databinding.FragmentSelectGeneralSizeBinding;
import com.hasawi.sears.ui.base.BaseFragment;
import com.hasawi.sears.ui.main.adapters.SelectGeneralSizeAdapter;
import com.hasawi.sears.ui.main.adapters.SelectProductAdapter;
import com.hasawi.sears.ui.main.adapters.SelectProductSizeAdapter;
import com.hasawi.sears.ui.main.listeners.RecyclerviewSingleChoiceClickListener;
import com.hasawi.sears.ui.main.view.DashboardActivity;
import com.hasawi.sears.ui.main.viewmodel.SelectSizeViewModel;
import com.hasawi.sears.ui.main.viewmodel.SelectUserDetailsViewModel;

import java.util.ArrayList;

public class SelectSizeFragment extends BaseFragment implements RecyclerviewSingleChoiceClickListener, View.OnClickListener {

    FragmentSelectGeneralSizeBinding fragmentSelectSizeBinding;
    SelectUserDetailsActivity selectUserDetailsActivity;
    LinearLayoutManager productLinearLayoutManager, sizeLinearLayoutManager;
    SelectSizeViewModel selectSizeViewModel;
    SelectProductAdapter selectProductAdapter;
    SelectProductSizeAdapter selectProductSizeAdapter;
    SelectUserDetailsViewModel selectUserDetailsViewModel;
    ArrayList<String> sizeList = new ArrayList<>();
    SelectGeneralSizeAdapter sizeAdapter;
    String selectedSize = "";
    private LinearLayoutManager horizontalLayoutManager;
    private Bundle dataBundle = new Bundle();

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_select_general_size;
    }

    @Override
    protected void setup() {
        fragmentSelectSizeBinding = (FragmentSelectGeneralSizeBinding) viewDataBinding;
        selectUserDetailsActivity = (SelectUserDetailsActivity) getActivity();
        selectUserDetailsViewModel = selectUserDetailsActivity.getSelectUserDetailsViewModel();
        fragmentSelectSizeBinding.tvLanguage.setOnClickListener(this);
        fragmentSelectSizeBinding.tvGender.setOnClickListener(this);
        fragmentSelectSizeBinding.tvProductCategory.setOnClickListener(this);
        fragmentSelectSizeBinding.tvSize.setOnClickListener(this);
        fragmentSelectSizeBinding.scrollView.post(new Runnable() {
            @Override
            public void run() {
                fragmentSelectSizeBinding.scrollView.fullScroll(View.FOCUS_RIGHT);
            }
        });
        fragmentSelectSizeBinding.btnContinue.setOnClickListener(this);

        try {
            dataBundle = getArguments();
            if (dataBundle == null)
                dataBundle = new Bundle();
        } catch (Exception e) {
            e.printStackTrace();
        }
        horizontalLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        fragmentSelectSizeBinding.recyclerViewSize.setLayoutManager(horizontalLayoutManager);
        try {
            sizeList = (ArrayList<String>) selectUserDetailsViewModel.getSizeList();
            sizeAdapter = new SelectGeneralSizeAdapter(getContext(), sizeList);
            sizeAdapter.setOnItemClickListener(this);
            fragmentSelectSizeBinding.recyclerViewSize.setAdapter(sizeAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageButtonNext:
                if (horizontalLayoutManager.findLastCompletelyVisibleItemPosition() < (sizeAdapter.getItemCount() - 1)) {
                    horizontalLayoutManager.scrollToPosition(horizontalLayoutManager.findLastCompletelyVisibleItemPosition() + 1);
                }
                break;
            case R.id.imageButtonBackSize:
                try {
                    horizontalLayoutManager.scrollToPosition(horizontalLayoutManager.findLastCompletelyVisibleItemPosition() - 1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btnContinue:
                dataBundle.putString("size", selectedSize);
                Intent intent = new Intent(selectUserDetailsActivity, DashboardActivity.class);
                intent.putExtras(dataBundle);
                startActivity(intent);
                selectUserDetailsActivity.finish();
                break;
            case R.id.tvLanguage:
                selectUserDetailsActivity.replaceFragment(new SelectLanguageFragment(), null);
                break;
            case R.id.tvGender:
                selectUserDetailsActivity.replaceFragment(new SelectGenderFragment(), null);
                break;
            case R.id.tv_product_category:
                selectUserDetailsActivity.replaceFragment(new SelectCategoryFragment(), null);
                break;
            case R.id.tv_size:
                selectUserDetailsActivity.replaceFragment(new SelectSizeFragment(), null);
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClickListener(int position, View view) {
        sizeAdapter.selectedItem();
        try {
            selectedSize = sizeList.get(position);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        selectTextView();
    }

    public void selectTextView() {
        fragmentSelectSizeBinding.tvSize.setBackground(getResources().getDrawable(R.drawable.blue_rounded_rectangle_8dp));
        fragmentSelectSizeBinding.tvSize.setTextColor(getResources().getColor(R.color.white));
        fragmentSelectSizeBinding.tvSize.setCompoundDrawablesWithIntrinsicBounds(R.drawable.translation_white, 0, 0, 0);
    }
}
