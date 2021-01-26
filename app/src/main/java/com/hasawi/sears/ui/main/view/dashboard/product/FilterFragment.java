package com.hasawi.sears.ui.main.view.dashboard.product;

import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.hasawi.sears.R;
import com.hasawi.sears.data.api.model.pojo.FilterAttributeValues;
import com.hasawi.sears.databinding.FragmentFilterBinding;
import com.hasawi.sears.ui.base.BaseFragment;
import com.hasawi.sears.ui.main.adapters.FilterOptionAdapter;
import com.hasawi.sears.ui.main.listeners.RecyclerviewSingleChoiceClickListener;
import com.hasawi.sears.ui.main.view.DashboardActivity;
import com.hasawi.sears.ui.main.view.dashboard.product.paging.ProductListingFragment;
import com.hasawi.sears.ui.main.viewmodel.ProductListingViewModel;
import com.hasawi.sears.utils.dialogs.ProgressBarDialog;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FilterFragment extends BaseFragment implements RecyclerviewSingleChoiceClickListener {
    public List<FilterAttributeValues> selectedFilters = new ArrayList<>();
    public List<FilterAttributeValues> selectedBrandFilters = new ArrayList<>();
    public List<FilterAttributeValues> selectedColorFilters = new ArrayList<>();
    public List<FilterAttributeValues> selectedSizeFilters = new ArrayList<>();
    DashboardActivity dashboardActivity;
    List<FilterAttributeValues> filterAttributeValuesList = new ArrayList<>();
    ProductListingViewModel sharedHomeViewModel;
    FragmentFilterBinding fragmentFilterBinding;
    FilterOptionAdapter filterOptionAdapter;
    //    FilterValueAdapter filterValueAdapter;
    ArrayList<String> filterKeysList = new ArrayList<>();
    Map<String, List<FilterAttributeValues>> filterAttributeMap;
    ProgressBarDialog progressBarDialog;
    JSONArray filterArray = null, brandArray = null, colorArray = null, sizeArray = null;
    private String selectedCategoryId = "";
    Bundle filterAnalyticsBundle = new Bundle();

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_filter;
    }

    @Override
    protected void setup() {
        fragmentFilterBinding = (FragmentFilterBinding) viewDataBinding;
        dashboardActivity = (DashboardActivity) getActivity();
        dashboardActivity.handleActionBarIcons(true);
        sharedHomeViewModel = new ViewModelProvider(dashboardActivity).get(ProductListingViewModel.class);
        fragmentFilterBinding.recyclerFilterOptions.setLayoutManager(new LinearLayoutManager(getActivity()));
        fragmentFilterBinding.recyclerFilterValues.setLayoutManager(new LinearLayoutManager(getActivity()));

        intitializeFilterAdapters();

        try {
            Bundle bundle = getArguments();
            selectedCategoryId = bundle.getString("category_id");
            loadFilterData(filterArray);
        } catch (Exception e) {
            e.printStackTrace();
        }

        fragmentFilterBinding.btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterArray = new JSONArray();
                for (int i = 0; i < selectedFilters.size(); i++) {
                    filterArray.put(selectedFilters.get(i).getId());

                }
                brandArray = new JSONArray();
                if (selectedBrandFilters != null)
                    for (int i = 0; i < selectedBrandFilters.size(); i++) {
                        brandArray.put(selectedBrandFilters.get(i).getId());
                    }
                else
                    brandArray = null;
                colorArray = new JSONArray();
                if (selectedColorFilters != null)
                    for (int i = 0; i < selectedColorFilters.size(); i++) {
                        colorArray.put(selectedColorFilters.get(i).getId());
                    }
                else
                    colorArray = null;
                sizeArray = new JSONArray();
                if (selectedSizeFilters != null)
                    for (int i = 0; i < selectedSizeFilters.size(); i++) {
                        sizeArray.put(selectedSizeFilters.get(i).getId());
                    }
                else
                    sizeArray = null;
                Gson gson = new Gson();
                String appliedFilterValues = gson.toJson(filterArray);
                filterAnalyticsBundle.putString("filter_values", appliedFilterValues);
                try {
                    filterAnalyticsBundle.putString("selected_brands", gson.toJson(brandArray));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dashboardActivity.getmFirebaseAnalytics().logEvent("FILTER_BY", filterAnalyticsBundle);
                dashboardActivity.getFacebookEventsLogger().logEvent("FILTER_BY", filterAnalyticsBundle);
                returnFilterValues(filterArray, brandArray);
            }
        });

        fragmentFilterBinding.btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                for (int i = 0; i < selectedFilters.size(); i++) {
//                    selectedFilters.get(i).setSelected(false);
//                }
//                filterOptionAdapter.addAll(filterAttributeMap, filterKeysList);
//                loadFilterData(null);
                resetilterData();
            }
        });

    }

    private void intitializeFilterAdapters() {
        filterOptionAdapter = new FilterOptionAdapter(getActivity(), fragmentFilterBinding.recyclerFilterValues) {
            @Override
            public void onFilterSelected(List<FilterAttributeValues> filterAttributeValues, List<FilterAttributeValues> selectedBrands, List<FilterAttributeValues> selectedColors, List<FilterAttributeValues> selectedSizes) {
                selectedFilters = filterAttributeValues;
                selectedBrandFilters = selectedBrands;
                selectedColorFilters = selectedColors;
                selectedSizeFilters = selectedSizes;
            }
        };
        filterOptionAdapter.setOnItemClickListener(this);
        fragmentFilterBinding.recyclerFilterOptions.setAdapter(filterOptionAdapter);
    }

    public void loadFilterData(JSONArray filterArray) {
        fragmentFilterBinding.progressBar.setVisibility(View.VISIBLE);
//        sharedHomeViewModel.getProductsInfo(selectedCategoryId, "0" +
//                "", filterArray).observe(dashboardActivity, productResponse -> {
//            switch (productResponse.status) {
//                case SUCCESS:
//                    filterKeysList.clear();
//                    filterAttributeMap = productResponse.data.getData().getFilterAttributes();
//                    filterKeysList = new ArrayList<>(filterAttributeMap.keySet());
//                    filterOptionAdapter.addAll(filterAttributeMap, filterKeysList);
//                    break;
//                case LOADING:
//                    break;
//                case ERROR:
//                    Toast.makeText(dashboardActivity, productResponse.message, Toast.LENGTH_SHORT).show();
//                    break;
//            }
//
//            fragmentFilterBinding.progressBar.setVisibility(View.GONE);
//        });

        sharedHomeViewModel.getFilterAttributeMap().observe(getActivity(), stringListMap -> {
            filterKeysList.clear();
            filterAttributeMap = stringListMap;
            filterKeysList = new ArrayList<>(filterAttributeMap.keySet());
            filterOptionAdapter.addAll(filterAttributeMap, filterKeysList);
            fragmentFilterBinding.progressBar.setVisibility(View.GONE);
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        dashboardActivity.handleActionMenuBar(false, true, "");
        dashboardActivity.handleActionBarIcons(true);
    }

    public void resetilterData() {
//        fragmentFilterBinding.progressBar.setVisibility(View.VISIBLE);
//        sharedHomeViewModel.ge(selectedCategoryId, "0" +
//                "", null).observe(dashboardActivity, productResponse -> {
//            switch (productResponse.status) {
//                case SUCCESS:
//                    sharedHomeViewModel.setFilterData(productResponse.data.getData().getFilterAttributes());
//                    break;
//                case LOADING:
//                    break;
//                case ERROR:
//                    Toast.makeText(dashboardActivity, productResponse.message, Toast.LENGTH_SHORT).show();
//                    break;
//            }
//
//            fragmentFilterBinding.progressBar.setVisibility(View.GONE);
//        });
        filterOptionAdapter.clear();
        // using for-each loop for iteration over Map.entrySet()
        for (Map.Entry<String, List<FilterAttributeValues>> entry : filterAttributeMap.entrySet()) {
            for (int i = 0; i < entry.getValue().size(); i++) {
                entry.getValue().get(i).setChecked(false);
            }
        }
        filterOptionAdapter.addAll(filterAttributeMap, filterKeysList);
    }

    private void returnFilterValues(JSONArray filterArray, JSONArray brandArray) {
        ProductListingFragment productListingFragment = (ProductListingFragment) getTargetFragment();
        productListingFragment.setFilterData(filterArray, brandArray, colorArray, sizeArray);

        dashboardActivity.getSupportFragmentManager().popBackStackImmediate();

    }

    @Override
    public void onItemClickListener(int position, View view) {
        filterOptionAdapter.selectedItem();
        String selectedOption = filterKeysList.get(position);
        filterAnalyticsBundle.putString("filter_key", filterKeysList.get(position));
//        filterAttributeValuesList = filterAttributeMap.get(selectedOption);
//        filterValueAdapter.addAll(filterAttributeValuesList);
    }

}
