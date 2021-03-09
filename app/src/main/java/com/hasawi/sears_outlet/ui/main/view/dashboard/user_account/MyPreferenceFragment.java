package com.hasawi.sears_outlet.ui.main.view.dashboard.user_account;

import android.provider.Settings;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.hasawi.sears_outlet.R;
import com.hasawi.sears_outlet.data.api.model.pojo.Manufacture;
import com.hasawi.sears_outlet.data.api.model.pojo.Size;
import com.hasawi.sears_outlet.databinding.FragmentEditPreferenceBinding;
import com.hasawi.sears_outlet.ui.base.BaseFragment;
import com.hasawi.sears_outlet.ui.main.adapters.OnBoardBrandAdapter;
import com.hasawi.sears_outlet.ui.main.view.DashboardActivity;
import com.hasawi.sears_outlet.ui.main.viewmodel.MyPreferenceViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyPreferenceFragment extends BaseFragment {
    FragmentEditPreferenceBinding fragmentEditPreferenceBinding;
    DashboardActivity dashboardActivity;
    MyPreferenceViewModel myPreferenceViewModel;
    ArrayList<Manufacture> brandList = new ArrayList<>();
    OnBoardBrandAdapter onBoardBrandAdapter;
    Map<String, String> sizeStringMap = new HashMap<>();
    private ArrayList<Size> sizeArrayList = new ArrayList<>();
    private ArrayList<String> sizeStringList = new ArrayList<>();
    private ArrayList<Manufacture> selectedBrandList = new ArrayList<>();
    private Size selectedSize;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_edit_preference;
    }

    @Override
    protected void setup() {
        fragmentEditPreferenceBinding = (FragmentEditPreferenceBinding) viewDataBinding;
        dashboardActivity = (DashboardActivity) getActivity();
        myPreferenceViewModel = new ViewModelProvider(this).get(MyPreferenceViewModel.class);
        fragmentEditPreferenceBinding.recyclerviewBrands.setLayoutManager(new LinearLayoutManager(dashboardActivity));
        getUserPreferences();
        getSizes();
        fragmentEditPreferenceBinding.checkBoxSize.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    fragmentEditPreferenceBinding.recyclerviewBrands.setVisibility(View.GONE);
                    fragmentEditPreferenceBinding.sizePicker.setVisibility(View.VISIBLE);
                    fragmentEditPreferenceBinding.checkBoxEditBrand.setChecked(false);
                    getBrands();
                } else {
                    fragmentEditPreferenceBinding.recyclerviewBrands.setVisibility(View.VISIBLE);
                    fragmentEditPreferenceBinding.sizePicker.setVisibility(View.GONE);
                }
            }
        });
        fragmentEditPreferenceBinding.checkBoxEditBrand.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    fragmentEditPreferenceBinding.recyclerviewBrands.setVisibility(View.VISIBLE);
                    fragmentEditPreferenceBinding.sizePicker.setVisibility(View.GONE);
                    fragmentEditPreferenceBinding.checkBoxSize.setChecked(false);
                    getBrands();
                } else {
                    fragmentEditPreferenceBinding.recyclerviewBrands.setVisibility(View.GONE);
                    fragmentEditPreferenceBinding.sizePicker.setVisibility(View.VISIBLE);
                }
            }
        });

        fragmentEditPreferenceBinding.sizePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                try {
                    selectedSize = sizeArrayList.get(i1);
                    fragmentEditPreferenceBinding.tvSelectedSize.setText(selectedSize.getName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        fragmentEditPreferenceBinding.btnSavePreference.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dashboardActivity.onBackPressed();
            }
        });
    }

    public void getUserPreferences() {
        fragmentEditPreferenceBinding.progressBar.setVisibility(View.VISIBLE);
        String ANDROID_UDID = Settings.Secure.getString(getContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        myPreferenceViewModel.getPreference(ANDROID_UDID).observe(this, preferenceResponseResource -> {
            switch (preferenceResponseResource.status) {
                case SUCCESS:
                    fragmentEditPreferenceBinding.tvSelectedSize.setText(preferenceResponseResource.data.getPreferenceData().getSize());
                    break;
                case LOADING:
                    break;
                case ERROR:
                    Toast.makeText(getActivity(), preferenceResponseResource.message, Toast.LENGTH_SHORT).show();
                    break;
            }
            fragmentEditPreferenceBinding.progressBar.setVisibility(View.GONE);
        });
    }

    public void getBrands() {
        fragmentEditPreferenceBinding.progressBar.setVisibility(View.VISIBLE);
        myPreferenceViewModel.getBrands().observe(this, brandsResponseResource -> {
            switch (brandsResponseResource.status) {
                case SUCCESS:
                    brandList = (ArrayList<Manufacture>) brandsResponseResource.data.getManufactureList();
                    onBoardBrandAdapter = new OnBoardBrandAdapter(getActivity(), brandList) {
                        @Override
                        public void onBrandSelected(ArrayList<Manufacture> brandList) {
                            selectedBrandList = brandList;
                        }
                    };
                    fragmentEditPreferenceBinding.recyclerviewBrands.setAdapter(onBoardBrandAdapter);
                    break;
                case LOADING:
                    break;
                case ERROR:
                    Toast.makeText(getActivity(), brandsResponseResource.message, Toast.LENGTH_SHORT).show();
                    break;
            }
            fragmentEditPreferenceBinding.progressBar.setVisibility(View.GONE);
        });
    }

    public void getSizes() {
        fragmentEditPreferenceBinding.progressBar.setVisibility(View.VISIBLE);
        myPreferenceViewModel.getGenericSizes().observe(this, genericSizeResponseResource -> {
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
                    fragmentEditPreferenceBinding.sizePicker.setMinValue(0);
                    fragmentEditPreferenceBinding.sizePicker.setMaxValue(sizeStringArray.length - 1);
                    fragmentEditPreferenceBinding.sizePicker.setDisplayedValues(sizeStringArray);
                    break;
                case LOADING:
                    break;
                case ERROR:
                    Toast.makeText(getActivity(), genericSizeResponseResource.message, Toast.LENGTH_SHORT).show();
                    break;
            }
            fragmentEditPreferenceBinding.progressBar.setVisibility(View.GONE);
        });
    }
}
