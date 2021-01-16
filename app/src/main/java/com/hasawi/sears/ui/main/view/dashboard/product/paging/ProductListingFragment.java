package com.hasawi.sears.ui.main.view.dashboard.product.paging;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.hasawi.sears.R;
import com.hasawi.sears.data.api.model.pojo.Category;
import com.hasawi.sears.data.api.model.pojo.Product;
import com.hasawi.sears.databinding.FragmentProductListingBinding;
import com.hasawi.sears.ui.base.BaseFragment;
import com.hasawi.sears.ui.main.adapters.CategoryRecyclerviewAdapter;
import com.hasawi.sears.ui.main.listeners.RecyclerviewSingleChoiceClickListener;
import com.hasawi.sears.ui.main.view.DashboardActivity;
import com.hasawi.sears.ui.main.view.dashboard.product.FilterFragment;
import com.hasawi.sears.ui.main.view.dashboard.product.SelectedProductDetailsFragment;
import com.hasawi.sears.ui.main.view.dashboard.product.SortFragment;
import com.hasawi.sears.ui.main.view.dashboard.user_account.WishListFragment;
import com.hasawi.sears.ui.main.viewmodel.ProductListingViewModel;
import com.hasawi.sears.utils.Connectivity;
import com.hasawi.sears.utils.LoadingIndicator;
import com.hasawi.sears.utils.PreferenceHandler;
import com.hasawi.sears.utils.dialogs.DialogGeneral;
import com.hasawi.sears.utils.dialogs.ProgressBarDialog;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class ProductListingFragment extends BaseFragment implements RecyclerviewSingleChoiceClickListener, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private static final int SET_SORT_REQUEST_CODE = 200;
    private static final int SET_FILTER_REQUEST_CODE = 201;
    //    CategoryRecyclerviewAdapter categoryRecyclerviewAdapter;
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
    private ProductListingViewModel productListingViewModel;
    private FragmentProductListingBinding fragmentProductListingBinding;
    private List<Category> categoryArrayList = new ArrayList<>();
    private int currentPage = 0;
    private LoadingIndicator loadingIndicator;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_product_listing;
    }

    @Override
    protected void setup() {
        fragmentProductListingBinding = (FragmentProductListingBinding) viewDataBinding;
        dashboardActivity = (DashboardActivity) getBaseActivity();
        dashboardActivity.handleActionBarIcons(true);
        productListingViewModel = new ViewModelProvider(getBaseActivity()).get(ProductListingViewModel.class);
        fetchDataFromSharedPref();
//        fragmentProductListingBinding.swipeRefreshProducts.setOnRefreshListener(this);
        fragmentProductListingBinding.lvSort.setOnClickListener(this);
        fragmentProductListingBinding.lvFilter.setOnClickListener(this);
        fragmentProductListingBinding.layoutNoProducts.tvContinueShopping.setOnClickListener(this);
        // Redirecting from home sub category
        try {
            Bundle bundle = getArguments();
            currentSelectedCategory = bundle.getString("category_id");
            String currentCategoryName = bundle.getString("category_name");
            fragmentProductListingBinding.tvTopDealsHeading.setText(currentCategoryName);
//            PreferenceHandler preferenceHandler = new PreferenceHandler(getContext(), PreferenceHandler.TOKEN_LOGIN);
//            preferenceHandler.saveData(PreferenceHandler.LOGIN_CATEGORY_ID, currentSelectedCategory);
//            preferenceHandler.saveData(PreferenceHandler.LOGIN_CATEGORY_NAME, currentCategoryName);
            fragmentProductListingBinding.tvTopDealsHeading.setText(currentCategoryName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!Connectivity.isConnected(dashboardActivity)) {
            showNoInternetDialog();
        } else {
            setupProductRecyclerview();
            getSortStrings();
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

//        if (!preferenceHandler.getData(PreferenceHandler.LOGIN_ITEM_TO_BE_CARTED, "").equalsIgnoreCase("") && isUserLoggedIn) {
//            dashboardActivity.runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    String jsonParmsString = preferenceHandler.getData(PreferenceHandler.LOGIN_ITEM_TO_BE_CARTED, "");
//                    callAddToCartApi(jsonParmsString);
//                }
//            });
    }
//        else {
//            dashboardActivity.setTitle("My Cart");
//            dashboardActivity.replaceFragment(R.id.fragment_replacer, new MyCartFragment(), null, true, false);
//            dashboardActivity.showBackButton(true);
//        }

//    }

    private void fetchDataFromSharedPref() {
        try {
            PreferenceHandler preferenceHandler = new PreferenceHandler(getContext(), PreferenceHandler.TOKEN_LOGIN);
            currentSelectedCategory = preferenceHandler.getData(PreferenceHandler.LOGIN_CATEGORY_ID, "");
            String currentCategoryName = preferenceHandler.getData(PreferenceHandler.LOGIN_CATEGORY_NAME, "");
            fragmentProductListingBinding.tvTopDealsHeading.setText(currentCategoryName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getSortStrings() {
        showProgressBarDialog(true);
//        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
//        fragmentProductListingBinding.recyclerViewCategories.setLayoutManager(horizontalLayoutManager);
//        categoryRecyclerviewAdapter = new CategoryRecyclerviewAdapter(getContext(), (ArrayList<Category>) categoryArrayList);
//        categoryRecyclerviewAdapter.setOnItemClickListener(this);
//        fragmentProductListingBinding.recyclerViewCategories.setAdapter(categoryRecyclerviewAdapter);
        productListingViewModel.callProductDataApi(currentSelectedCategory, currentPage + "").observe(getActivity(), productResponseResource -> {
            switch (productResponseResource.status) {
                case SUCCESS:
//                    categoryArrayList = productResponseResource.data.getData().getCategories();
                    sortStrings = productResponseResource.data.getData().getSortStrings();
//                    categoryRecyclerviewAdapter = new CategoryRecyclerviewAdapter(getContext(), (ArrayList<Category>) categoryArrayList);
//                    fragmentProductListingBinding.recyclerViewCategories.setAdapter(categoryRecyclerviewAdapter);
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
        productListingViewModel.getProductsInfo(currentSelectedCategory, "0" +
                "", filterArray).observe(dashboardActivity, productResponse -> {
            switch (productResponse.status) {
                case SUCCESS:
                    productListingViewModel.setFilterData(productResponse.data.getData().getFilterAttributes());
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
        fragmentProductListingBinding.recyclerviewProducts.setLayoutManager(gridLayoutManager);
        loadProductsFromApi(currentSelectedCategory, null, currentPage, "");
    }

    private void loadProductsFromApi(String category, JSONArray jsonArray, int page, String selectedSortString) {
        showProgressBarDialog(true);
        initializeProductAdapter();
        productListingViewModel.fetchProductData(category, jsonArray, page + "", selectedSortString);
        productListingViewModel.getProductPaginationLiveData().observe(this, pagedList -> {
            fragmentProductListingBinding.lvNoProductsFound.setVisibility(View.GONE);
            productPagedListAdapter.submitList(pagedList);
            shouldRefreshTheList = false;

        });

        /*
         * Step 5: When a new page is available, we call submitList() method
         * of the PagedListAdapter class
         *
         * */
        productListingViewModel.getNetworkState().observe(this, networkState -> {
            productPagedListAdapter.setNetworkState(networkState);
        });

        fragmentProductListingBinding.recyclerviewProducts.setAdapter(productPagedListAdapter);
    }

    private void initializeProductAdapter() {
        productPagedListAdapter = new ProductPagedListAdapter(getContext()) {
            @Override
            public void onLikeClicked(Product product, boolean checked) {
                PreferenceHandler preferenceHandler = new PreferenceHandler(dashboardActivity, PreferenceHandler.TOKEN_LOGIN);
                boolean isAlreadyLoggedIn = preferenceHandler.getData(PreferenceHandler.LOGIN_STATUS, false);
                if (isAlreadyLoggedIn) {
                    callWishlistApi(product.getProductId(), product, checked);
                } else {
                    preferenceHandler.saveData(PreferenceHandler.LOGIN_ITEM_TO_BE_WISHLISTED, product.getProductId());
                    dashboardActivity.handleActionMenuBar(true, true, "Wishlist");
                    dashboardActivity.replaceFragment(R.id.fragment_replacer, new WishListFragment(), null, true, false);
                }
            }

            @Override
            public void onItemClicked(Product productContent) {
                try {

                    Product selectedProduct = productContent;
//                    productListingViewModel.select(selectedProduct);
                    Gson gson = new Gson();
                    String objectString = gson.toJson(selectedProduct);
                    Bundle bundle = new Bundle();
                    bundle.putString("selected_product_object", objectString);
                    bundle.putBoolean("from_product_list", true);

                    dashboardActivity.handleActionMenuBar(true, false, selectedProduct.getDescriptions().get(0).getProductName());
                    dashboardActivity.replaceFragment(R.id.fragment_replacer_product, new SelectedProductDetailsFragment(), bundle, true, false);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onLoadedProducts() {
                showProgressBarDialog(false);
            }

            @Override
            public void onItemCountChanged(int itemCount) {
                if (itemCount == 0) {
                    fragmentProductListingBinding.lvNoProductsFound.setVisibility(View.VISIBLE);
                } else {
                    fragmentProductListingBinding.lvNoProductsFound.setVisibility(View.GONE);
                }
            }

        };
    }

    private void callWishlistApi(String productId, Product product, boolean isWishlisted) {
        logAddToWishlistEvent(product);
        PreferenceHandler preferenceHandler = new PreferenceHandler(getContext(), PreferenceHandler.TOKEN_LOGIN);
        String userID = preferenceHandler.getData(PreferenceHandler.LOGIN_USER_ID, "");
        productListingViewModel.addToWishlist(productId, userID, sessionToken).observe(getActivity(), wishlistResponse -> {
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
        fragmentProductListingBinding.progressBar.setVisibility(View.VISIBLE);

//        productListingViewModel.addToCart(userID, jsonParamString, sessionToken).observe(getActivity(), addToCartResponse -> {
//            fragmentProductListingBinding.progressBar.setVisibility(View.GONE);
//            switch (addToCartResponse.status) {
//                case SUCCESS:
//                    preferenceHandler.saveData(PreferenceHandler.LOGIN_ITEM_TO_BE_CARTED, "");
//                    CartDialog cartDialog = new CartDialog(dashboardActivity);
//                    cartDialog.show(getParentFragmentManager(), "CART_DIALOG");
//                    break;
//                case LOADING:
//                    break;
//                case ERROR:
//                    Toast.makeText(dashboardActivity, addToCartResponse.message, Toast.LENGTH_SHORT).show();
//                    break;
//            }
//        });

    }

    private void launchSortFragment() {
        SortFragment sortFragment = new SortFragment();
        sortFragment.setTargetFragment(this, SET_SORT_REQUEST_CODE);
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("sort_string", (ArrayList<String>) sortStrings);
        dashboardActivity.replaceFragment(R.id.fragment_replacer_product, sortFragment, bundle, true, false);

    }

    private void launchFilterFragment() {
        FilterFragment filterFragment = new FilterFragment();
        filterFragment.setTargetFragment(this, SET_FILTER_REQUEST_CODE);
        Bundle bundle = new Bundle();
        bundle.putString("category_id", currentSelectedCategory);
        dashboardActivity.handleActionMenuBar(true, true, "");
        dashboardActivity.handleActionBarIcons(false);
        dashboardActivity.replaceFragment(R.id.fragment_replacer_product, filterFragment, bundle, true, false);
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
            case R.id.tvContinueShopping:
                dashboardActivity.onBackPressed();
            default:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        CategoryRecyclerviewAdapter.setsSelected(-1);
        dashboardActivity.handleActionMenuBar(false, true, "");
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

//        categoryRecyclerviewAdapter.selectedItem();
//        currentSelectedCategory = categoryArrayList.get(position).getCategoryId();
//        refreshProductList(currentSelectedCategory, null, "");
//        loadFilterData(filterArray);
        try {
            Bundle analyticsBundle = new Bundle();
            analyticsBundle.putString("category_id", categoryArrayList.get(position).getCategoryId());
            analyticsBundle.putString("category_name", categoryArrayList.get(position).getDescriptions().get(0).getCategoryName());
            analyticsBundle.putString("category_code", categoryArrayList.get(position).getCategoryCode());
            analyticsBundle.putString("parent_category_id", categoryArrayList.get(position).getParentId());
            dashboardActivity.getmFirebaseAnalytics().logEvent("OUTFIT_SELECTED", analyticsBundle);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void refreshProductList(String currentSelectedCategory, JSONArray filterArray, String sort) {
        productListingViewModel.invalidateProductLiveData();
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
//                setupCategories();
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
            fragmentProductListingBinding.progressBar.setVisibility(View.VISIBLE);
        else
            fragmentProductListingBinding.progressBar.setVisibility(View.GONE);

    }

    private void logAddToWishlistEvent(Product product) {
        Bundle analyticsBundle = new Bundle();
        try {
            analyticsBundle.putString(FirebaseAnalytics.Param.CURRENCY, "KWD");
            analyticsBundle.putDouble(FirebaseAnalytics.Param.VALUE, product.getOriginalPrice());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Bundle itemBundle = new Bundle();
        try {
            itemBundle.putString(FirebaseAnalytics.Param.ITEM_NAME, product.getDescriptions().get(0).getProductName());
            itemBundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, product.getCategories().get(0).getDescriptions().get(0).getCategoryName());
            itemBundle.putString("product_id", product.getProductId());
            ArrayList<Bundle> parcelabeList = new ArrayList<>();
            parcelabeList.add(itemBundle);
            analyticsBundle.putParcelableArrayList(FirebaseAnalytics.Param.ITEMS, parcelabeList);
            dashboardActivity.getmFirebaseAnalytics().logEvent(FirebaseAnalytics.Event.ADD_TO_CART, analyticsBundle);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}



