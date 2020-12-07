package com.hasawi.sears.ui.main.view.home;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.hasawi.sears.R;
import com.hasawi.sears.data.api.model.pojo.ProductSearch;
import com.hasawi.sears.data.api.model.pojo.SearchProductListResponse;
import com.hasawi.sears.databinding.FragmentSearchBinding;
import com.hasawi.sears.ui.base.BaseFragment;
import com.hasawi.sears.ui.main.adapters.SearchProductAdapter;
import com.hasawi.sears.ui.main.listeners.RecyclerItemClickListener;
import com.hasawi.sears.ui.main.view.DashboardActivity;
import com.hasawi.sears.ui.main.viewmodel.SearchViewModel;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends BaseFragment {
    FragmentSearchBinding fragmentSearchBinding;
    DashboardActivity dashboardActivity;
    SearchViewModel searchViewModel;
    List<ProductSearch> productSearchList = new ArrayList<>();
    SearchProductAdapter searchProductAdapter;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_search;
    }

    @Override
    protected void setup() {
        fragmentSearchBinding = DataBindingUtil.setContentView(getActivity(), R.layout.fragment_search);
        dashboardActivity = (DashboardActivity) getActivity();
        searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        fragmentSearchBinding.recyclerSearchProducts.setLayoutManager(new LinearLayoutManager(getActivity()));
        try {
            String query = getArguments().getString("search_query");
            searchViewModel.searchProducts(query).observe(this, searchProductListResponse -> {
                switch (searchProductListResponse.status) {
                    case SUCCESS:
                        if (searchProductListResponse != null) {
                            SearchProductListResponse productListResponse = searchProductListResponse.data;
                            productSearchList = productListResponse.getData().getProductSearchs();
                            searchProductAdapter = new SearchProductAdapter(productSearchList);
                            fragmentSearchBinding.recyclerSearchProducts.setAdapter(searchProductAdapter);
                        }
                        break;
                    case LOADING:
                        break;
                    case ERROR:
                        Toast.makeText(dashboardActivity, searchProductListResponse.message, Toast.LENGTH_SHORT).show();
                        break;
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        fragmentSearchBinding.recyclerSearchProducts.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                try {

                    ProductSearch selectedProduct = productSearchList.get(position);
                    Bundle bundle = new Bundle();
                    bundle.putString("product_object_id", selectedProduct.getObjectID());
                    bundle.putBoolean("is_search", true);
                    dashboardActivity.replaceFragment(R.id.fragment_replace_search_view, new SelectedProductDetailsFragment(), bundle, true, false);
//                    getParentFragmentManager().popBackStackImmediate();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }));
    }
}
