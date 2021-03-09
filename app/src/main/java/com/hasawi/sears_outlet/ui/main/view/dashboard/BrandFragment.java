package com.hasawi.sears_outlet.ui.main.view.dashboard;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.hasawi.sears_outlet.R;
import com.hasawi.sears_outlet.data.api.model.BrandData;
import com.hasawi.sears_outlet.data.api.model.pojo.Manufacture;
import com.hasawi.sears_outlet.databinding.FragmentBrandBinding;
import com.hasawi.sears_outlet.ui.base.BaseFragment;
import com.hasawi.sears_outlet.ui.main.adapters.AlphabeticBrandAdapter;
import com.hasawi.sears_outlet.ui.main.adapters.IndexAdapter;
import com.hasawi.sears_outlet.ui.main.view.DashboardActivity;
import com.hasawi.sears_outlet.ui.main.view.dashboard.product.paging.ProductListingFragment;
import com.hasawi.sears_outlet.ui.main.viewmodel.BrandViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

public class BrandFragment extends BaseFragment {
    FragmentBrandBinding fragmentBrandBinding;
    DashboardActivity dashboardActivity;
    BrandViewModel brandViewModel;
    IndexAdapter indexAdapter;
    AlphabeticBrandAdapter dataAdapter;
    String[] alphabets = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    private HashMap<String, Integer> mapIndex;
    private List<BrandData> dataList;
    private ArrayList<Manufacture> brandList = new ArrayList<>();
    private LinearLayoutManager layoutManager;

    @Override
    protected void setup() {
        fragmentBrandBinding = (FragmentBrandBinding) viewDataBinding;
        dashboardActivity = (DashboardActivity) getActivity();
        brandViewModel = new ViewModelProvider(requireActivity(), getDefaultViewModelProviderFactory()).get(BrandViewModel.class);
        dataList = new ArrayList<>();
        mapIndex = new LinkedHashMap<String, Integer>();
        callBrandApi();
        layoutManager = new LinearLayoutManager(getActivity());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(fragmentBrandBinding.recyclerviewBrandsList.getContext(), layoutManager.getOrientation());

        List<String> alphabetsList = Arrays.asList(alphabets);
        dataAdapter = new AlphabeticBrandAdapter(dataList) {
            @Override
            public void onBrandSelected(BrandData brandData) {
                Bundle bundle = new Bundle();
                bundle.putString("SELECTED_BRAND", brandData.getManufacture().getManufactureId());
                dashboardActivity.replaceFragment(R.id.fragment_replacer, new ProductListingFragment(), bundle, true, false);
            }
        };
        indexAdapter = new IndexAdapter(new ArrayList<String>(mapIndex.keySet())
                , getContext()) {
            @Override
            public void onSelectedIndex(String index, int position) {
                layoutManager.scrollToPositionWithOffset(mapIndex.get(index), 0);
            }
        };
        fragmentBrandBinding.recyclerviewBrandsList.setLayoutManager(layoutManager);
        fragmentBrandBinding.recyclerviewBrandsList.addItemDecoration(dividerItemDecoration);
        fragmentBrandBinding.recyclerviewBrandsList.setAdapter(dataAdapter);

        fragmentBrandBinding.recyclerviewIndex.setLayoutManager(new LinearLayoutManager(getActivity()));
        fragmentBrandBinding.recyclerviewIndex.setAdapter(indexAdapter);
    }

    private void callBrandApi() {
        brandViewModel.getBrands().observe(this, brandsResponseResource -> {
            switch (brandsResponseResource.status) {
                case SUCCESS:
                    brandList = (ArrayList<Manufacture>) brandsResponseResource.data.getManufactureList();
                    getData(brandList);
                    break;
                case LOADING:
                    break;
                case ERROR:
                    Toast.makeText(getActivity(), brandsResponseResource.message, Toast.LENGTH_SHORT).show();
                    break;
            }
            fragmentBrandBinding.progressBar.setVisibility(View.GONE);
        });
    }

    private void getData(ArrayList<Manufacture> brandList) {

        for (int i = 0; i < brandList.size(); i++) {
            dataList.add(new BrandData(brandList.get(i).getManufactureDescriptions().get(0).getName(), brandList.get(i)));
        }
        Set<String> temp = new HashSet<>();
        for (int i = 0; i < dataList.size(); i++) {
            String obj1 = Character.toString(dataList.get(i).getName().charAt(0)).toUpperCase();
            String obj2;
            try {
                obj2 = Character.toString(dataList.get(i + 1).getName().charAt(0)).toUpperCase();
            } catch (IndexOutOfBoundsException e) {
                obj2 = "#";
            }

            if (!obj1.equalsIgnoreCase(obj2)) {
                temp.add(obj1);
            }
        }
        for (String title : temp) {
            BrandData member = new BrandData();
            member.setIndex(title);
            member.setName(title);
            dataList.add(member);
        }

        Collections.sort(dataList, new Comparator<BrandData>() {
            public int compare(BrandData obj1, BrandData obj2) {
                return obj1.getName().compareToIgnoreCase(obj2.getName());
            }
        });

        getIndexList(dataList);
        indexAdapter.addAll(new ArrayList<String>(mapIndex.keySet()));
        dataAdapter.addAll(dataList);

    }

    private void getIndexList(List<BrandData> memberList) {
        for (int i = 0; i < memberList.size(); i++) {
            if (memberList.get(i).getIndex() != null) {
                String index = memberList.get(i).getIndex();
                if (mapIndex.get(index) == null)
                    mapIndex.put(index, i);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        dashboardActivity.handleActionMenuBar(true, true, "Brands");
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_brand;
    }


}
