package com.hasawi.sears.ui.main.view.paging_lib;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.hasawi.sears.R;
import com.hasawi.sears.data.api.model.pojo.Category;
import com.hasawi.sears.data.api.model.pojo.Content;
import com.hasawi.sears.databinding.FragmentHomeBinding;
import com.hasawi.sears.ui.base.BaseFragment;
import com.hasawi.sears.ui.main.adapters.CategoryRecyclerviewAdapter;
import com.hasawi.sears.ui.main.listeners.RecyclerviewSingleChoiceClickListener;
import com.hasawi.sears.ui.main.view.DashboardActivity;
import com.hasawi.sears.ui.main.view.home.FilterFragment;
import com.hasawi.sears.ui.main.view.home.SelectedProductDetailsFragment;
import com.hasawi.sears.ui.main.view.home.SortFragment;
import com.hasawi.sears.ui.main.view.home.WishListFragment;
import com.hasawi.sears.ui.main.viewmodel.SharedHomeViewModel;
import com.hasawi.sears.utils.CartDialog;
import com.hasawi.sears.utils.Connectivity;
import com.hasawi.sears.utils.DialogGeneral;
import com.hasawi.sears.utils.LoadingIndicator;
import com.hasawi.sears.utils.PreferenceHandler;
import com.hasawi.sears.utils.ProgressBarDialog;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends BaseFragment implements RecyclerviewSingleChoiceClickListener, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private static final int SET_SORT_REQUEST_CODE = 200;
    private static final int SET_FILTER_REQUEST_CODE = 201;
    CategoryRecyclerviewAdapter categoryRecyclerviewAdapter;
    DashboardActivity dashboardActivity;
    String currentSelectedCategory = "";
    JSONArray selectedFilterData;
    String selectedSortString;
    JSONArray filterArray;
    List<String> sortStrings = new ArrayList<>();
    DialogGeneral noInternetDialog;
    String sessionToken;
    boolean shouldRefreshTheList = false;
    boolean isUserLoggedIn = false;
    ProgressBarDialog progressBarDialog;
    private ProductPagedListAdapter productPagedListAdapter;
    private SharedHomeViewModel sharedHomeViewModel;
    private FragmentHomeBinding fragmentHomeBinding;
    private List<Category> categoryArrayList = new ArrayList<>();
    private int currentPage = 0;
    private LoadingIndicator loadingIndicator;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void setup() {
        fragmentHomeBinding = (FragmentHomeBinding) viewDataBinding;
        dashboardActivity = (DashboardActivity) getBaseActivity();
        sharedHomeViewModel = new ViewModelProvider(getBaseActivity()).get(SharedHomeViewModel.class);
        fetchDataFromSharedPref();
//        fragmentHomeBinding.swipeRefreshProducts.setOnRefreshListener(this);
        fragmentHomeBinding.lvSort.setOnClickListener(this);
        fragmentHomeBinding.lvFilter.setOnClickListener(this);
        if (!Connectivity.isConnected(dashboardActivity)) {
            showNoInternetDialog();
        } else {
            setupProductRecyclerview();
            setupCategories();
            loadFilterData(filterArray);
        }

        handleWishlistCartApiBeforeLoggedIn();
    }

    private void handleWishlistCartApiBeforeLoggedIn() {
        PreferenceHandler preferenceHandler = new PreferenceHandler(getContext(), PreferenceHandler.TOKEN_LOGIN);
        sessionToken = preferenceHandler.getData(PreferenceHandler.LOGIN_TOKEN, "");
        isUserLoggedIn = preferenceHandler.getData(PreferenceHandler.LOGIN_STATUS, false);

        if (!preferenceHandler.getData(PreferenceHandler.LOGIN_ITEM_TO_BE_WISHLISTED, "").equalsIgnoreCase("") && isUserLoggedIn) {
            dashboardActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    shouldRefreshTheList = true;
                    String productId = preferenceHandler.getData(PreferenceHandler.LOGIN_ITEM_TO_BE_WISHLISTED, "");
                    callWishlistApi(productId, null, true);
                }
            });

        }
//        else {
//            dashboardActivity.setTitle("Wishlist");
//            dashboardActivity.replaceFragment(R.id.fragment_replacer, new WishListFragment(), null, true, false);
//            dashboardActivity.showBackButton(true);
//        }

        if (!preferenceHandler.getData(PreferenceHandler.LOGIN_ITEM_TO_BE_CARTED, "").equalsIgnoreCase("") && isUserLoggedIn) {
            dashboardActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String jsonParmsString = preferenceHandler.getData(PreferenceHandler.LOGIN_ITEM_TO_BE_CARTED, "");
                    callAddToCartApi(jsonParmsString);
                }
            });
        }
//        else {
//            dashboardActivity.setTitle("My Cart");
//            dashboardActivity.replaceFragment(R.id.fragment_replacer, new MyCartFragment(), null, true, false);
//            dashboardActivity.showBackButton(true);
//        }

    }

    private void fetchDataFromSharedPref() {
        try {
            PreferenceHandler preferenceHandler = new PreferenceHandler(getContext(), PreferenceHandler.TOKEN_LOGIN);
            currentSelectedCategory = preferenceHandler.getData(PreferenceHandler.LOGIN_CATEGORY_ID, "");
            String currentCategoryName = preferenceHandler.getData(PreferenceHandler.LOGIN_CATEGORY_NAME, "");
            fragmentHomeBinding.tvTopDealsHeading.setText(currentCategoryName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupCategories() {
        showProgressBarDialog(true);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        fragmentHomeBinding.recyclerViewCategories.setLayoutManager(horizontalLayoutManager);
        categoryRecyclerviewAdapter = new CategoryRecyclerviewAdapter(getContext(), (ArrayList<Category>) categoryArrayList);
        categoryRecyclerviewAdapter.setOnItemClickListener(this);
        fragmentHomeBinding.recyclerViewCategories.setAdapter(categoryRecyclerviewAdapter);
        sharedHomeViewModel.callProductDataApi(currentSelectedCategory, currentPage + "").observe(getActivity(), productResponseResource -> {
            switch (productResponseResource.status) {
                case SUCCESS:
                    categoryArrayList = productResponseResource.data.getData().getCategories();
                    sortStrings = productResponseResource.data.getData().getSortStrings();
                    categoryRecyclerviewAdapter = new CategoryRecyclerviewAdapter(getContext(), (ArrayList<Category>) categoryArrayList);
                    fragmentHomeBinding.recyclerViewCategories.setAdapter(categoryRecyclerviewAdapter);
                    break;
                case LOADING:
                    break;
                case ERROR:
                    Toast.makeText(dashboardActivity, productResponseResource.message, Toast.LENGTH_SHORT).show();
                    break;
            }
            showProgressBarDialog(false);
        });

    }

    public void loadFilterData(JSONArray filterArray) {
        showProgressBarDialog(true);
        sharedHomeViewModel.getProductsInfo(currentSelectedCategory, "0" +
                "", filterArray).observe(dashboardActivity, productResponse -> {
            switch (productResponse.status) {
                case SUCCESS:
                    sharedHomeViewModel.setFilterData(productResponse.data.getData().getFilterAttributes());
                    break;
                case LOADING:
                    break;
                case ERROR:
                    Toast.makeText(dashboardActivity, productResponse.message, Toast.LENGTH_SHORT).show();
                    break;
            }

            showProgressBarDialog(false);
        });
    }

    private void setupProductRecyclerview() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        fragmentHomeBinding.recyclerviewProducts.setLayoutManager(gridLayoutManager);
        loadProductsFromApi(currentSelectedCategory, null, currentPage, "");
    }

    private void loadProductsFromApi(String category, JSONArray jsonArray, int page, String selectedSortString) {
        showProgressBarDialog(true);
        initializeProductAdapter();
        sharedHomeViewModel.fetchProductData(category, jsonArray, page + "", selectedSortString);
        sharedHomeViewModel.getProductPaginationLiveData().observe(this, pagedList -> {
            productPagedListAdapter.submitList(pagedList);
            shouldRefreshTheList = false;
        });

        /*
         * Step 5: When a new page is available, we call submitList() method
         * of the PagedListAdapter class
         *
         * */
        sharedHomeViewModel.getNetworkState().observe(this, networkState -> {
            productPagedListAdapter.setNetworkState(networkState);
        });

        fragmentHomeBinding.recyclerviewProducts.setAdapter(productPagedListAdapter);
    }

    private void initializeProductAdapter() {
        productPagedListAdapter = new ProductPagedListAdapter(getContext()) {
            @Override
            public void onLikeClicked(Content product, boolean checked) {
                PreferenceHandler preferenceHandler = new PreferenceHandler(dashboardActivity, PreferenceHandler.TOKEN_LOGIN);
                boolean isAlreadyLoggedIn = preferenceHandler.getData(PreferenceHandler.LOGIN_STATUS, false);
                if (isAlreadyLoggedIn) {
                    callWishlistApi(product.getProductId(), product, checked);
                } else {
                    preferenceHandler.saveData(PreferenceHandler.LOGIN_ITEM_TO_BE_WISHLISTED, product.getProductId());
                    dashboardActivity.showBackButton(true, true);
                    dashboardActivity.replaceFragment(R.id.fragment_replacer, new WishListFragment(), null, true, false);
                }
            }

            @Override
            public void onItemClicked(Content productContent) {
                try {

                    Content selectedProduct = productContent;
                    sharedHomeViewModel.select(selectedProduct);
                    Gson gson = new Gson();
                    String objectString = gson.toJson(selectedProduct);
                    Bundle bundle = new Bundle();
                    bundle.putString("selected_product_object", objectString);

                    dashboardActivity.showBackButton(true, false);
                    dashboardActivity.setTitle(selectedProduct.getDescriptions().get(0).getProductName());
                    dashboardActivity.replaceFragment(R.id.fragment_replacer, new SelectedProductDetailsFragment(), bundle, true, false);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onLoadedProducts() {
                showProgressBarDialog(false);
            }

        };
    }

    private void callWishlistApi(String productId, Content product, boolean isWishlisted) {
        PreferenceHandler preferenceHandler = new PreferenceHandler(getContext(), PreferenceHandler.TOKEN_LOGIN);
        String userID = preferenceHandler.getData(PreferenceHandler.LOGIN_USER_ID, "");
        sharedHomeViewModel.addToWishlist(productId, userID, sessionToken).observe(getActivity(), wishlistResponse -> {
            switch (wishlistResponse.status) {
                case SUCCESS:
                    if (product != null)
                        if (isWishlisted)
                            product.setWishlist(true);
                        else
                            product.setWishlist(false);
                    preferenceHandler.saveData(PreferenceHandler.LOGIN_ITEM_TO_BE_WISHLISTED, "");
                    if (shouldRefreshTheList)
                        refreshProductList(currentSelectedCategory, filterArray, selectedSortString);
                    dashboardActivity.showCustomWislistToast(isWishlisted);
                    break;
                case LOADING:
                    break;
                case ERROR:
                    Toast.makeText(dashboardActivity, wishlistResponse.message, Toast.LENGTH_SHORT).show();
                    break;
            }
        });
    }

    public void callAddToCartApi(String jsonParamString) {
        PreferenceHandler preferenceHandler = new PreferenceHandler(getContext(), PreferenceHandler.TOKEN_LOGIN);
        String userID = preferenceHandler.getData(PreferenceHandler.LOGIN_USER_ID, "");
        fragmentHomeBinding.progressBar.setVisibility(View.VISIBLE);

        sharedHomeViewModel.addToCart(userID, jsonParamString, sessionToken).observe(getActivity(), addToCartResponse -> {
            fragmentHomeBinding.progressBar.setVisibility(View.GONE);
            switch (addToCartResponse.status) {
                case SUCCESS:
                    dashboardActivity.setCartCount(1);
                    preferenceHandler.saveData(PreferenceHandler.LOGIN_ITEM_TO_BE_CARTED, "");
                    CartDialog cartDialog = new CartDialog(dashboardActivity);
                    cartDialog.show(getParentFragmentManager(), "CART_DIALOG");
                    break;
                case LOADING:
                    break;
                case ERROR:
                    Toast.makeText(dashboardActivity, addToCartResponse.message, Toast.LENGTH_SHORT).show();
                    break;
            }
        });

    }

    private void launchSortFragment() {
        SortFragment sortFragment = new SortFragment();
        sortFragment.setTargetFragment(this, SET_SORT_REQUEST_CODE);
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("sort_string", (ArrayList<String>) sortStrings);
        dashboardActivity.replaceFragment(R.id.fragment_replacer, sortFragment, bundle, true, false);

    }

    private void launchFilterFragment() {
        FilterFragment filterFragment = new FilterFragment();
        filterFragment.setTargetFragment(this, SET_FILTER_REQUEST_CODE);
        Bundle bundle = new Bundle();
        bundle.putString("category_id", currentSelectedCategory);
        dashboardActivity.replaceFragment(R.id.fragment_replacer, filterFragment, bundle, true, false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lv_sort:
                launchSortFragment();
                break;
            case R.id.lv_filter:
                launchFilterFragment();
                break;
            default:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        CategoryRecyclerviewAdapter.setsSelected(-1);
        dashboardActivity.showBackButton(false, true);
        dashboardActivity.setTitle("");
    }

    @Override
    public void onRefresh() {
//        itemCount = 0;
//        currentPage = PAGE_START;
//        isLastPage = false;
//        productRecyclerviewAdapter.clear();
//        currentProductList.clear();
//        Toast.makeText(dashboardActivity, "Call Api on Refresh", Toast.LENGTH_SHORT).show();
//        loadFromApi(currentPage, currentSelectedCategory);
    }


    @Override
    public void onItemClickListener(int position, View view) {

        categoryRecyclerviewAdapter.selectedItem();
        currentSelectedCategory = categoryArrayList.get(position).getCategoryId();
        refreshProductList(currentSelectedCategory, null, "");
        loadFilterData(filterArray);


    }

    private void refreshProductList(String currentSelectedCategory, JSONArray filterArray, String sort) {
        sharedHomeViewModel.invalidateProductLiveData();
        loadProductsFromApi(currentSelectedCategory, filterArray, 0, sort);
    }

    public void setFilterData(JSONArray filterArray) {
        this.selectedFilterData = filterArray;
        refreshProductList(currentSelectedCategory, selectedFilterData, selectedSortString);
        loadFilterData(filterArray);
    }

    public void setSortData(String sort) {
        this.selectedSortString = sort;
        refreshProductList(currentSelectedCategory, selectedFilterData, selectedSortString);
    }

    public void showNoInternetDialog() {
        noInternetDialog = new DialogGeneral();
//        noInternetDialog.setTexts(getString(R.string.notification), jsonObject.getString("e"), getString(R.string.ok), null, null);
        noInternetDialog.setClickListener(new DialogGeneral.DialogGeneralInterface() {
            @Override
            public void positiveClick() {
                loadProductsFromApi(currentSelectedCategory, null, currentPage, "");
                setupCategories();
                loadFilterData(filterArray);

            }

            @Override
            public void negativeClick() {

            }
        });
        noInternetDialog.showDialog(dashboardActivity);
    }

    private void showProgressBarDialog(boolean shouldShow) {
        if (shouldShow)
            fragmentHomeBinding.progressBar.setVisibility(View.VISIBLE);
        else
            fragmentHomeBinding.progressBar.setVisibility(View.GONE);

    }
}



