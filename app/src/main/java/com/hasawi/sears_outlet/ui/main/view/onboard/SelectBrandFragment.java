package com.hasawi.sears_outlet.ui.main.view.onboard;

import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.hasawi.sears_outlet.R;
import com.hasawi.sears_outlet.data.api.model.pojo.Manufacture;
import com.hasawi.sears_outlet.databinding.FragmentOnboardBrandBinding;
import com.hasawi.sears_outlet.ui.base.BaseFragment;
import com.hasawi.sears_outlet.ui.main.adapters.OnBoardBrandAdapter;
import com.hasawi.sears_outlet.ui.main.viewmodel.OnboardViewModel;
import com.hasawi.sears_outlet.utils.PreferenceHandler;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SelectBrandFragment extends BaseFragment {
    FragmentOnboardBrandBinding fragmentOnboardBrandBinding;
    OnBoardActivity onBoardActivity;
    OnBoardBrandAdapter onBoardBrandAdapter;
    OnboardViewModel onboardViewModel;
    JSONArray brandArray;
    ArrayList<Manufacture> brandList = new ArrayList<>();
    ArrayList<Manufacture> selectedBrandList = new ArrayList<>();

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_onboard_brand;
    }

    @Override
    protected void setup() {
        fragmentOnboardBrandBinding = (FragmentOnboardBrandBinding) viewDataBinding;
        onBoardActivity = (OnBoardActivity) getActivity();
        onboardViewModel = new ViewModelProvider(this).get(OnboardViewModel.class);
//        brandList.add("Calvin Klein");
//        brandList.add("Boss");
//        brandList.add("Dkny");
//        brandList.add("Sketcher");
//        brandList.add("Adidas");
        fragmentOnboardBrandBinding.recyclerviewBrands.setLayoutManager(new LinearLayoutManager(getActivity()));

        fragmentOnboardBrandBinding.tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBoardActivity.redirectToHomePage();
            }
        });

        fragmentOnboardBrandBinding.tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//              savePreference();
                onBoardActivity.replaceFragment(new EndFragment(), null);
            }
        });

        fragmentOnboardBrandBinding.btnSelectSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentOnboardBrandBinding.constrainLayoutSelect.setVisibility(View.VISIBLE);
                fragmentOnboardBrandBinding.recyclerviewBrands.setVisibility(View.VISIBLE);
            }
        });


        onBoardActivity.showProgressBar(true);
        onboardViewModel.getBrands().observe(this, brandsResponseResource -> {
            switch (brandsResponseResource.status) {
                case SUCCESS:
                    brandList = (ArrayList<Manufacture>) brandsResponseResource.data.getManufactureList();
                    onBoardBrandAdapter = new OnBoardBrandAdapter(getActivity(), brandList) {
                        @Override
                        public void onBrandSelected(ArrayList<Manufacture> brandList) {
                            selectedBrandList = brandList;
                        }
                    };
                    fragmentOnboardBrandBinding.recyclerviewBrands.setAdapter(onBoardBrandAdapter);
                    break;
                case LOADING:
                    break;
                case ERROR:
                    Toast.makeText(getActivity(), brandsResponseResource.message, Toast.LENGTH_SHORT).show();
                    break;
            }
            onBoardActivity.showProgressBar(false);
        });
    }

    public void savePreference() {
        onBoardActivity.showProgressBar(true);
        PreferenceHandler preferenceHandler = new PreferenceHandler(getContext(), PreferenceHandler.TOKEN_LOGIN);
        Map<String, Object> jsonParams = new HashMap<>();
        String size = preferenceHandler.getData(PreferenceHandler.SELECTED_SIZE, "");
        String ANDROID_UDID = Settings.Secure.getString(getContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);

        brandArray = new JSONArray();
        if (selectedBrandList != null)
            for (int i = 0; i < selectedBrandList.size(); i++) {
                brandArray.put(selectedBrandList.get(i).getManufactureId());
            }
        else
            brandArray = null;

        jsonParams.put("size", size);
        jsonParams.put("brandId", brandArray);

        onboardViewModel.savePreference(jsonParams, ANDROID_UDID).observe(this, preferenceResponseResource -> {
            switch (preferenceResponseResource.status) {
                case SUCCESS:
                    onBoardActivity.replaceFragment(new EndFragment(), null);
                    break;
                case LOADING:
                    break;
                case ERROR:
                    Toast.makeText(getActivity(), preferenceResponseResource.message, Toast.LENGTH_SHORT).show();
                    break;
            }
        });

    }
}
