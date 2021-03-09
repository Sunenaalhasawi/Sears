package com.hasawi.sears_outlet.ui.main.view.dashboard.product.paging;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.hasawi.sears_outlet.R;
import com.hasawi.sears_outlet.data.api.model.pojo.Category;
import com.hasawi.sears_outlet.data.api.model.pojo.Product;
import com.hasawi.sears_outlet.databinding.FragmentProductListingBinding;
import com.hasawi.sears_outlet.ui.base.BaseFragment;
import com.hasawi.sears_outlet.ui.main.adapters.CategoryRecyclerviewAdapter;
import com.hasawi.sears_outlet.ui.main.listeners.RecyclerviewSingleChoiceClickListener;
import com.hasawi.sears_outlet.ui.main.view.DashboardActivity;
import com.hasawi.sears_outlet.ui.main.view.dashboard.product.FilterFragment;
import com.hasawi.sears_outlet.ui.main.view.dashboard.product.SelectedProductDetailsFragment;
import com.hasawi.sears_outlet.ui.main.view.dashboard.product.SortFragment;
import com.hasawi.sears_outlet.ui.main.view.dashboard.user_account.WishListFragment;
import com.hasawi.sears_outlet.ui.main.viewmodel.ProductListingViewModel;
import com.hasawi.sears_outlet.utils.Connectivity;
import com.hasawi.sears_outlet.utils.LoadingIndicator;
import com.hasawi.sears_outlet.utils.PreferenceHandler;
import com.hasawi.sears_outlet.utils.dialogs.DialogGeneral;
import com.hasawi.sears_outlet.utils.dialogs.ProgressBarDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProductListingFragment extends BaseFragment implements RecyclerviewSingleChoiceClickListener, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private static final int SET_SORT_REQUEST_CODE = 200;
    private static final int SET_FILTER_REQUEST_CODE = 201;
    CategoryRecyclerviewAdapter categoryRecyclerviewAdapter;
    DashboardActivity dashboardActivity;
    String currentSelectedCategory = "";
    JSONArray selectedFilterData, selectedBrandData, selectedColorData, selectedSizeData;
    String selectedSortString = "";
    JSONArray filterArray;
    String attributeIds = null;
    List<String> sortStrings = new ArrayList<>();
    DialogGeneral noInternetDialog;
    String sessionToken;
    boolean shouldRefreshTheList = false;
    boolean isUserLoggedIn = false;
    String userId = "";
    ProgressBarDialog progressBarDialog;
    private ProductPagedListAdapter productPagedListAdapter;
    private ProductListingViewModel productListingViewModel;
    private FragmentProductListingBinding fragmentProductListingBinding;
    private List<Category> categoryArrayList = new ArrayList<>();
    private int currentPage = 0;
    JSONObject productPayload = null;
    private LoadingIndicator loadingIndicator;
    PreferenceHandler preferenceHandler;

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
        preferenceHandler = new PreferenceHandler(getContext(), PreferenceHandler.TOKEN_LOGIN);
        sessionToken = preferenceHandler.getData(PreferenceHandler.LOGIN_TOKEN, "");
        isUserLoggedIn = preferenceHandler.getData(PreferenceHandler.LOGIN_STATUS, false);
        userId = preferenceHandler.getData(PreferenceHandler.LOGIN_USER_ID, "");
//        fragmentProductListingBinding.swipeRefreshProducts.setOnRefreshListener(this);
        fragmentProductListingBinding.lvSort.setOnClickListener(this);
        fragmentProductListingBinding.lvFilter.setOnClickListener(this);
        fragmentProductListingBinding.layoutNoProducts.tvContinueShopping.setOnClickListener(this);
        // Redirecting from home sub category
        try {
            Bundle bundle = getArguments();
            if (bundle.containsKey("SELECTED_BRAND")) {
                selectedBrandData = new JSONArray();
                selectedBrandData.put(bundle.getString("SELECTED_BRAND"));
                currentSelectedCategory = preferenceHandler.getData(PreferenceHandler.LOGIN_CATEGORY_ID, "");
            } else {
                currentSelectedCategory = bundle.getString("category_id");
                String currentCategoryName = bundle.getString("category_name");
                attributeIds = bundle.getString("attribute_ids");
                fragmentProductListingBinding.tvTopDealsHeading.setText(currentCategoryName);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!Connectivity.isConnected(dashboardActivity)) {
            showNoInternetDialog();
        } else {
            if (attributeIds == null || attributeIds.equalsIgnoreCase("")) {
                productPayload = null;
            } else {
                try {
                    productPayload = new JSONObject(attributeIds);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            setupProductRecyclerview();
            productListingViewModel.getSortStrings().observe(this, strings -> {
                sortStrings = strings;
            });
        }

        handleWishlistCartApiBeforeLoggedIn();
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        fragmentProductListingBinding.recyclerViewCategories.setLayoutManager(horizontalLayoutManager);
        productListingViewModel.getCategoryList().observe(this, categories -> {
            categoryArrayList = categories;
            if (categories != null && categories.size() > 0) {
                categoryRecyclerviewAdapter = new CategoryRecyclerviewAdapter(getContext(), categoryArrayList);
                categoryRecyclerviewAdapter.setOnItemClickListener(this);
                fragmentProductListingBinding.recyclerViewCategories.setAdapter(categoryRecyclerviewAdapter);
            }
        });
    }

    private void handleWishlistCartApiBeforeLoggedIn() {
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

    private void setupProductRecyclerview() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        fragmentProductListingBinding.recyclerviewProducts.setLayoutManager(gridLayoutManager);
        loadProductsFromApi(currentSelectedCategory, selectedFilterData, selectedBrandData, selectedColorData, selectedSizeData, currentPage, "", productPayload);
    }

    private void loadProductsFromApi(String category, JSONArray filterArray, JSONArray brandArray, JSONArray colorArray, JSONArray sizeArray, int page, String selectedSortString, JSONObject productPayload) {
        showProgressBarDialog(true);
        initializeProductAdapter();
        productListingViewModel.fetchProductData(category, filterArray, brandArray, colorArray, sizeArray, page + "", selectedSortString, userId, productPayload);
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
                    fragmentProductListingBinding.progressBar.setVisibility(View.GONE);
                    fragmentProductListingBinding.lvNoProductsFound.setVisibility(View.VISIBLE);
                } else {
                    fragmentProductListingBinding.lvNoProductsFound.setVisibility(View.GONE);
                }
            }

        };
    }

    private void callWishlistApi(String productId, Product product, boolean isWishlisted) {

        PreferenceHandler preferenceHandler = new PreferenceHandler(getContext(), PreferenceHandler.TOKEN_LOGIN);
        String userID = preferenceHandler.getData(PreferenceHandler.LOGIN_USER_ID, "");
        productListingViewModel.addToWishlist(productId, userID, sessionToken).observe(getActivity(), wishlistResponse -> {
            switch (wishlistResponse.status) {
                case SUCCESS:
                    try {
                        if (product != null)
                            if (isWishlisted)
                                product.setWishlist(true);
                            else
                                product.setWishlist(false);
                        preferenceHandler.saveData(PreferenceHandler.LOGIN_ITEM_TO_BE_WISHLISTED, "");
                        if (shouldRefreshTheList)
                            refreshProductList(currentSelectedCategory, filterArray, selectedBrandData, selectedColorData, selectedSizeData, selectedSortString);
                        dashboardActivity.showCustomWislistToast(isWishlisted);
                        if (isWishlisted) {
                            dashboardActivity.logAddToWishlistFirebaseEvent(product);
                            dashboardActivity.logAddToWishlistEvent(product);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case LOADING:
                    break;
                case ERROR:
                    Toast.makeText(dashboardActivity, wishlistResponse.message, Toast.LENGTH_SHORT).show();
                    break;
            }
        });
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

        categoryRecyclerviewAdapter.selectedItem();
        currentSelectedCategory = categoryArrayList.get(position).getCategoryId();
        refreshProductList(currentSelectedCategory, filterArray, selectedBrandData, selectedColorData, selectedSizeData, selectedSortString);
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

    private void refreshProductList(String currentSelectedCategory, JSONArray filterArray, JSONArray brandArray, JSONArray colorArray, JSONArray sizeArray, String sort) {
        productListingViewModel.invalidateProductLiveData();
        loadProductsFromApi(currentSelectedCategory, filterArray, brandArray, colorArray, sizeArray, 0, sort, productPayload);
    }

    public void setFilterData(JSONArray filterArray, JSONArray brandArray, JSONArray colorArray, JSONArray sizeArray) {
        this.selectedFilterData = filterArray;
        this.selectedBrandData = brandArray;
        this.selectedColorData = colorArray;
        this.selectedSizeData = sizeArray;
        refreshProductList(currentSelectedCategory, selectedFilterData, selectedBrandData, selectedColorData, selectedSizeData, selectedSortString);
//        loadFilterData(filterArray);
    }

    public void setSortData(String sort) {
        this.selectedSortString = sort;
        refreshProductList(currentSelectedCategory, selectedFilterData, selectedBrandData, selectedColorData, selectedSizeData, selectedSortString);
    }

    public void showNoInternetDialog() {
        noInternetDialog = new DialogGeneral();
//        noInternetDialog.setTexts(getString(R.string.notification), jsonObject.getString("e"), getString(R.string.ok), null, null);
        noInternetDialog.setClickListener(new DialogGeneral.DialogGeneralInterface() {
            @Override
            public void positiveClick() {
                loadProductsFromApi(currentSelectedCategory, selectedFilterData, selectedBrandData, selectedColorData, selectedSizeData, currentPage, "", productPayload);
//                setupCategories();
//                loadFilterData(filterArray);

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
}



