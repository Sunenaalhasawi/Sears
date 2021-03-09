package com.hasawi.sears_outlet.ui.main.view.onboard;

import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;

import com.hasawi.sears_outlet.R;
import com.hasawi.sears_outlet.data.api.model.pojo.Size;
import com.hasawi.sears_outlet.databinding.FragmentOnboardSizeBinding;
import com.hasawi.sears_outlet.ui.base.BaseFragment;
import com.hasawi.sears_outlet.ui.main.viewmodel.OnboardViewModel;
import com.hasawi.sears_outlet.utils.PreferenceHandler;

import java.util.ArrayList;
import java.util.List;

public class SelectSizeFragment extends BaseFragment {
    FragmentOnboardSizeBinding fragmentOnboardSizeBinding;
    OnBoardActivity onBoardActivity;
    OnboardViewModel onboardViewModel;
    ArrayList<String> sizeStringList = new ArrayList<>();
    List<Size> sizeArrayList = new ArrayList<>();
    private Size selectedSize;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_onboard_size;
    }

    @Override
    protected void setup() {

        fragmentOnboardSizeBinding = (FragmentOnboardSizeBinding) viewDataBinding;
        onBoardActivity = (OnBoardActivity) getActivity();
        onboardViewModel = new ViewModelProvider(this).get(OnboardViewModel.class);
        fragmentOnboardSizeBinding.tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBoardActivity.redirectToHomePage();
            }
        });
        fragmentOnboardSizeBinding.tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedSize = sizeArrayList.get(fragmentOnboardSizeBinding.numberPicker.getValue());
                PreferenceHandler preferenceHandler = new PreferenceHandler(getContext(), PreferenceHandler.TOKEN_LOGIN);
                preferenceHandler.saveData(PreferenceHandler.SELECTED_SIZE, selectedSize.getId());
                onBoardActivity.replaceFragment(new SelectBrandFragment(), null);
            }
        });
        fragmentOnboardSizeBinding.btnSelectSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentOnboardSizeBinding.constrainLayoutSelect.setVisibility(View.VISIBLE);
                fragmentOnboardSizeBinding.numberPicker.setVisibility(View.GONE);
            }
        });

        onBoardActivity.showProgressBar(true);
        onboardViewModel.getGenericSizes().observe(this, genericSizeResponseResource -> {
            switch (genericSizeResponseResource.status) {
                case SUCCESS:
                    sizeArrayList = (ArrayList) genericSizeResponseResource.data.getSizeList();
                    for (int i = 0; i < sizeArrayList.size(); i++) {
                        sizeStringList.add(sizeArrayList.get(i).getName());
                    }
                    String sizeStringArray[] = new String[sizeStringList.size()];
                    for (int j = 0; j < sizeStringList.size(); j++) {
                        sizeStringArray[j] = sizeStringList.get(j);
                    }
                    fragmentOnboardSizeBinding.numberPicker.setMinValue(0);
                    fragmentOnboardSizeBinding.numberPicker.setMaxValue(sizeStringArray.length - 1);
                    fragmentOnboardSizeBinding.numberPicker.setDisplayedValues(sizeStringArray);
                    break;
                case LOADING:
                    break;
                case ERROR:
                    Toast.makeText(getActivity(), genericSizeResponseResource.message, Toast.LENGTH_SHORT).show();
                    break;
            }
            onBoardActivity.showProgressBar(false);
        });
    }

    private void savePreference() {

    }

}
