package com.hasawi.sears_outlet.ui.main.view.onboard;

import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.hasawi.sears_outlet.R;
import com.hasawi.sears_outlet.databinding.FragmentOnboardBrandBinding;
import com.hasawi.sears_outlet.ui.base.BaseFragment;
import com.hasawi.sears_outlet.ui.main.adapters.OnBoardBrandAdapter;

import java.util.ArrayList;

public class SelectBrandFragment extends BaseFragment {
    FragmentOnboardBrandBinding fragmentOnboardBrandBinding;
    OnBoardActivity onBoardActivity;
    OnBoardBrandAdapter onBoardBrandAdapter;
    ArrayList<String> brandList = new ArrayList<>();

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_onboard_brand;
    }

    @Override
    protected void setup() {
        fragmentOnboardBrandBinding = (FragmentOnboardBrandBinding) viewDataBinding;
        onBoardActivity = (OnBoardActivity) getActivity();
        brandList.add("Calvin Klein");
        brandList.add("Boss");
        brandList.add("Dkny");
        brandList.add("Sketcher");
        brandList.add("Adidas");
        fragmentOnboardBrandBinding.recyclerviewBrands.setLayoutManager(new LinearLayoutManager(getActivity()));
        onBoardBrandAdapter = new OnBoardBrandAdapter(getActivity(), brandList);
        fragmentOnboardBrandBinding.recyclerviewBrands.setAdapter(onBoardBrandAdapter);
        fragmentOnboardBrandBinding.tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBoardActivity.redirectToHomePage();
            }
        });

        fragmentOnboardBrandBinding.tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBoardActivity.replaceFragment(new EndFragment(), null);
            }
        });
    }
}
