package com.hasawi.sears.ui.main.view.dashboard.product;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.gson.Gson;
import com.hasawi.sears.R;
import com.hasawi.sears.data.api.Resource;
import com.hasawi.sears.data.api.model.pojo.Product;
import com.hasawi.sears.data.api.model.pojo.ProductAttribute;
import com.hasawi.sears.data.api.model.pojo.ProductConfigurable;
import com.hasawi.sears.data.api.model.pojo.ShoppingCartItem;
import com.hasawi.sears.data.api.response.CartResponse;
import com.hasawi.sears.databinding.ActivityImageShowingBinding;
import com.hasawi.sears.databinding.FragmentSelectedProductDetailsBinding;
import com.hasawi.sears.ui.base.BaseActivity;
import com.hasawi.sears.ui.base.BaseFragment;
import com.hasawi.sears.ui.main.adapters.OffersAdapter;
import com.hasawi.sears.ui.main.adapters.ProductAttributesAdapter;
import com.hasawi.sears.ui.main.adapters.ProductColorAdapter;
import com.hasawi.sears.ui.main.adapters.ProductSizeAdapter;
import com.hasawi.sears.ui.main.adapters.RelatedProductsRecyclerviewAdapter;
import com.hasawi.sears.ui.main.listeners.RecyclerviewSingleChoiceClickListener;
import com.hasawi.sears.ui.main.view.DashboardActivity;
import com.hasawi.sears.ui.main.view.checkout.MyCartFragment;
import com.hasawi.sears.ui.main.view.dashboard.user_account.WishListFragment;
import com.hasawi.sears.ui.main.viewmodel.ProductDetailViewModel;
import com.hasawi.sears.utils.PreferenceHandler;
import com.hasawi.sears.utils.dialogs.CartDialog;
import com.hasawi.sears.utils.dialogs.ChooseSizeDialog;
import com.hasawi.sears.utils.dialogs.DisabledCartDialog;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelectedProductDetailsFragment extends BaseFragment implements RecyclerviewSingleChoiceClickListener {

    ProductDetailViewModel productDetailViewModel;
    FragmentSelectedProductDetailsBinding fragmentSelectedProductDetailsBinding;
    Product currentSelectedProduct;
    DashboardActivity dashboardActivity;
    ArrayList<String> productAvailableSizes = new ArrayList<>();
    ProductColorAdapter productColorAdapter;
    private ArrayList<ProductAttribute> productAttributeArrayList = new ArrayList<>();
    private ProductAttributesAdapter productAttributesAdapter;
    private ProductSizeAdapter sizeAdapter;
    private ArrayList<String> offersList = new ArrayList<>();
    //    private ArrayList<String> services = new ArrayList<>();
    private ArrayList<Product> recommendedProductList = new ArrayList<>();
    private boolean isSearch = false;
    private String selectedObjectID = "";
    private String sessionToken, userID;
    private HashMap<String, List<ProductConfigurable>> sizeColorHashMap;
    ProductConfigurable selectedColor;
    private ArrayList<ProductConfigurable> currentColorsList = new ArrayList<>();
    private boolean isbuyNow = false;
    private boolean disableAddToCart = false;
    private boolean isRedirectedFromProductListing = false;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_selected_product_details;
    }

    @Override
    protected void setup() {
        fragmentSelectedProductDetailsBinding = (FragmentSelectedProductDetailsBinding) viewDataBinding;
        productDetailViewModel = new ViewModelProvider(getActivity()).get(ProductDetailViewModel.class);
        dashboardActivity = (DashboardActivity) getActivity();
        dashboardActivity.hideSearchPage();
        dashboardActivity.handleActionBarIcons(false);
        dashboardActivity.handleSocialShare(true);
        PreferenceHandler preferenceHandler = new PreferenceHandler(getContext(), PreferenceHandler.TOKEN_LOGIN);
        userID = preferenceHandler.getData(PreferenceHandler.LOGIN_USER_ID, "");
        sessionToken = preferenceHandler.getData(PreferenceHandler.LOGIN_TOKEN, "");
        setUpProductDescriptionRecyclerview();
//        setOffersAdapter();
        Bundle bundle = getArguments();
        try {

            selectedObjectID = bundle.getString("product_object_id");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            String selectedProductObj = bundle.getString("selected_product_object");
            isRedirectedFromProductListing = bundle.getBoolean("from_product_list");
            Gson gson = new Gson();
            currentSelectedProduct = gson.fromJson(selectedProductObj, Product.class);
            selectedObjectID = currentSelectedProduct.getProductId();
        } catch (Exception e) {
            e.printStackTrace();
        }

        fragmentSelectedProductDetailsBinding.progressBar.setVisibility(View.VISIBLE);
        productDetailViewModel.getSearchedProductDetails(selectedObjectID).observe(getActivity(), searchedProductDetailsResponse -> {
            switch (searchedProductDetailsResponse.status) {
                case SUCCESS:
                    try {
                        currentSelectedProduct = searchedProductDetailsResponse.data.getData().getProduct();
                        if (currentSelectedProduct.getDescriptions() != null)
                            dashboardActivity.handleActionMenuBar(true, false, currentSelectedProduct.getDescriptions().get(0).getProductName());
                        recommendedProductList = (ArrayList<Product>) searchedProductDetailsResponse.data.getData().getRecommendedProductList();
                        setRelatedProducts();
                        setUIValues(currentSelectedProduct);
                        setColorSizeAdapterList(currentSelectedProduct.getProductConfigurables());
                        logProductViewEvent(currentSelectedProduct);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case LOADING:
                    break;
                case ERROR:
                    Toast.makeText(dashboardActivity, searchedProductDetailsResponse.message, Toast.LENGTH_SHORT).show();
                    break;
            }

            fragmentSelectedProductDetailsBinding.progressBar.setVisibility(View.GONE);
        });

        fragmentSelectedProductDetailsBinding.imageViewSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(dashboardActivity, ProductImageActivity.class);
                i.putExtra("image_url", currentSelectedProduct.getProductImages().get(0).getImageUrl());
                startActivity(i);
            }
        });

        fragmentSelectedProductDetailsBinding.checkboxWishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferenceHandler preferenceHandler = new PreferenceHandler(dashboardActivity, PreferenceHandler.TOKEN_LOGIN);
                boolean isAlreadyLoggedIn = preferenceHandler.getData(PreferenceHandler.LOGIN_STATUS, false);
                if (isAlreadyLoggedIn)
                    callWishlistApi(currentSelectedProduct, fragmentSelectedProductDetailsBinding.checkboxWishlist.isChecked());
                else {
                    preferenceHandler.saveData(PreferenceHandler.LOGIN_ITEM_TO_BE_WISHLISTED, currentSelectedProduct.getProductId());
                    dashboardActivity.setTitle("Wishlist");
                    dashboardActivity.replaceFragment(R.id.fragment_replacer, new WishListFragment(), null, true, false);
                }
            }
        });

        fragmentSelectedProductDetailsBinding.lvAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (disableAddToCart) {
                    DisabledCartDialog disabledCartDialog = new DisabledCartDialog(dashboardActivity);
                    disabledCartDialog.show(getParentFragmentManager(), "DISABLED_CART_DIALOG");
                } else {
                    addingProductToCart();
                    isbuyNow = false;
                }

            }
        });
        fragmentSelectedProductDetailsBinding.lvBuyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (disableAddToCart) {
                    DisabledCartDialog disabledCartDialog = new DisabledCartDialog(dashboardActivity);
                    disabledCartDialog.show(getParentFragmentManager(), "DISABLED_CART_DIALOG");
                } else {
                    addingProductToCart();
                    isbuyNow = true;
                }
            }
        });
    }

    private void logProductViewEvent(Product currentSelectedProduct) {
        try {
            Bundle itemBundle = new Bundle();
            itemBundle.putString(FirebaseAnalytics.Param.ITEM_NAME, currentSelectedProduct.getDescriptions().get(0).getProductName());
            itemBundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, currentSelectedProduct.getCategories().get(0).getDescriptions().get(0).getCategoryName());
            itemBundle.putString(FirebaseAnalytics.Param.ITEM_BRAND, currentSelectedProduct.getManufature());
            itemBundle.putString("category_id", currentSelectedProduct.getCategories().get(0).getCategoryId());
            itemBundle.putString("product_id", currentSelectedProduct.getProductId());
            itemBundle.putString("product_type", currentSelectedProduct.getProductType());
            ArrayList<Bundle> itemParcelableList = new ArrayList<>();
            itemParcelableList.add(itemBundle);

            Bundle analyticsBundle = new Bundle();
            analyticsBundle.putString(FirebaseAnalytics.Param.CURRENCY, "KWD");
            analyticsBundle.putDouble(FirebaseAnalytics.Param.VALUE, currentSelectedProduct.getOriginalPrice());
            analyticsBundle.putParcelableArrayList(FirebaseAnalytics.Param.ITEMS, itemParcelableList);
            dashboardActivity.getmFirebaseAnalytics().logEvent(FirebaseAnalytics.Event.VIEW_ITEM, analyticsBundle);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addingProductToCart() {
        PreferenceHandler preferenceHandler = new PreferenceHandler(dashboardActivity, PreferenceHandler.TOKEN_LOGIN);
        boolean isAlreadyLoggedIn = preferenceHandler.getData(PreferenceHandler.LOGIN_STATUS, false);
        Map<String, Object> jsonParams = new ArrayMap<>();
        jsonParams.put("productId", currentSelectedProduct.getProductId());
        if (selectedColor != null)
            jsonParams.put("refSku", selectedColor.getRefSku());
        else
            jsonParams.put("refSku", currentSelectedProduct.getSku());
        jsonParams.put("quantity", 1 + "");
        String jsonParamString = (new JSONObject(jsonParams)).toString();
        if (isAlreadyLoggedIn)
            callAddToCartApi(jsonParamString);
        else {
            preferenceHandler.saveData(PreferenceHandler.LOGIN_ITEM_TO_BE_CARTED, jsonParamString);
            dashboardActivity.replaceFragment(R.id.fragment_replacer, new MyCartFragment(), null, true, false);
            dashboardActivity.setTitle("My Cart");
        }
    }

    private void setColorAdapter(List<ProductConfigurable> colors) {
        fragmentSelectedProductDetailsBinding.listviewColorVariants.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        productColorAdapter = new ProductColorAdapter(getContext(), (ArrayList<ProductConfigurable>) colors);
        productColorAdapter.setOnItemClickListener(this);
        fragmentSelectedProductDetailsBinding.listviewColorVariants.setAdapter(productColorAdapter);
    }

    private void setUIValues(Product currentSelectedProduct) {
        try {
            try {
                Glide.with(this)
                        .load(currentSelectedProduct.getProductImages().get(0).getImageUrl())
                        .centerCrop()
                        .into(fragmentSelectedProductDetailsBinding.imageViewSelected);
            } catch (Exception e) {
                e.printStackTrace();
                FirebaseCrashlytics.getInstance().log(e.getMessage());
            }
            fragmentSelectedProductDetailsBinding.tvProductName.setText(currentSelectedProduct.getDescriptions().get(0).getProductName());
            String description = currentSelectedProduct.getDescriptions().get(0).getProductDescription();
            if (description == null || description.equals("")) {
                fragmentSelectedProductDetailsBinding.tvProductDescription.setVisibility(View.GONE);
            } else if (description != null) {

                fragmentSelectedProductDetailsBinding.tvProductDescription.setVisibility(View.VISIBLE);
                fragmentSelectedProductDetailsBinding.tvProductDescription.setText(currentSelectedProduct.getDescriptions().get(0).getProductDescription());
            }
            if (currentSelectedProduct.getWishlist())
                fragmentSelectedProductDetailsBinding.checkboxWishlist.setChecked(true);
            else
                fragmentSelectedProductDetailsBinding.checkboxWishlist.setChecked(false);
            fragmentSelectedProductDetailsBinding.tvOriginalPrice.setText("KWD " + currentSelectedProduct.getDiscountPrice());
            fragmentSelectedProductDetailsBinding.tvOurPrice.setText("KWD " + currentSelectedProduct.getOriginalPrice());
            if (currentSelectedProduct.getSku() != null)
                fragmentSelectedProductDetailsBinding.tvSku.setText(currentSelectedProduct.getSku());
            try {
                Glide.with(getContext())
                        .load(currentSelectedProduct.getBrandLogoUrl())
                        .centerCrop()
                        .into(fragmentSelectedProductDetailsBinding.imageViewBrandLogo);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (currentSelectedProduct.getDiscountPercentage() == null || currentSelectedProduct.getDiscountPercentage() == 0)
                fragmentSelectedProductDetailsBinding.tvOfferPercent.setVisibility(View.GONE);
            else
                fragmentSelectedProductDetailsBinding.tvOfferPercent.setText(currentSelectedProduct.getDiscountPercentage() + "% OFF");
            fragmentSelectedProductDetailsBinding.tvOriginalPrice.setPaintFlags(fragmentSelectedProductDetailsBinding.tvOriginalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            productAttributeArrayList = (ArrayList<ProductAttribute>) currentSelectedProduct.getProductAttributes();
            productAttributesAdapter = new ProductAttributesAdapter(getActivity(), productAttributeArrayList);
            fragmentSelectedProductDetailsBinding.productDetails.recyclerviewProductDetails.setAdapter(productAttributesAdapter);
            if (!currentSelectedProduct.isAvailable()) {
                fragmentSelectedProductDetailsBinding.tvOutOfStock.setVisibility(View.VISIBLE);
                fragmentSelectedProductDetailsBinding.tvOfferPercent.setVisibility(View.GONE);
                disableAddToCart = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        dashboardActivity.handleSocialShare(false);
    }

    public void setUpProductDescriptionRecyclerview() {
        productAttributesAdapter = new ProductAttributesAdapter(getContext(), productAttributeArrayList);
        fragmentSelectedProductDetailsBinding.productDetails.recyclerviewProductDetails.setLayoutManager(new LinearLayoutManager(getContext()));
        fragmentSelectedProductDetailsBinding.productDetails.recyclerviewProductDetails.setAdapter(productAttributesAdapter);
    }

    public void callProductDetailApi(String productName) {
        productDetailViewModel.getProductDetails(productName).observe(getActivity(), productDetailsResponse -> {
            switch (productDetailsResponse.status) {
                case SUCCESS:
                    currentSelectedProduct = productDetailsResponse.data.getData();
                    setUIValues(currentSelectedProduct);
                    break;
                case LOADING:
                    break;
                case ERROR:
                    Toast.makeText(dashboardActivity, productDetailsResponse.message, Toast.LENGTH_SHORT).show();
                    break;
            }

            fragmentSelectedProductDetailsBinding.progressBar.setVisibility(View.GONE);

        });
    }

    private void setColorSizeAdapterList(List<ProductConfigurable> productConfigurableList) {
        sizeColorHashMap = new HashMap<>();
        List<ProductConfigurable> colorList = new ArrayList<>();
        for (int i = 0; i < productConfigurableList.size(); i++) {
            ProductConfigurable productConfigurableItem = productConfigurableList.get(i);
            String key = productConfigurableItem.getSize();
            try {
                if (sizeColorHashMap.containsKey(key)) {
                    colorList.add(productConfigurableItem);
                    List<ProductConfigurable> existingList = new ArrayList<>();
                    if (sizeColorHashMap != null && sizeColorHashMap.size() > 0)
                        existingList = sizeColorHashMap.get(key);
                    existingList.addAll(colorList);
                    sizeColorHashMap.remove(key);
                    sizeColorHashMap.put(key, existingList);
                    colorList = new ArrayList<>();
                } else {
                    colorList = new ArrayList<>();
                    colorList.add(productConfigurableItem);
                    sizeColorHashMap.put(key, colorList);
                    colorList = new ArrayList<>();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            ArrayList<String> sizeList = new ArrayList<>(sizeColorHashMap.keySet());
            setProductSizeRecyclerview(sizeList);
            if (sizeColorHashMap.size() > 0) {
                currentColorsList = (ArrayList<ProductConfigurable>) sizeColorHashMap.get(sizeList.get(0));
                //Hiding colors if color code is null
                for (int i = 0; i < currentColorsList.size(); i++) {
                    if (currentColorsList.get(i).getColorCode() == null)
                        currentColorsList.remove(i);
                }
                // hiding views
                if (currentColorsList.size() == 0) {
                    fragmentSelectedProductDetailsBinding.txtColorVariant.setVisibility(View.GONE);
                    fragmentSelectedProductDetailsBinding.listviewColorVariants.setVisibility(View.GONE);
                } else if (currentColorsList.size() > 0)
                    selectedColor = currentColorsList.get(0);

                setColorAdapter(currentColorsList);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setProductSizeRecyclerview(List<String> sizes) {
        productAvailableSizes = new ArrayList<>();
        productAvailableSizes.addAll(sizes);
        sizeAdapter = new ProductSizeAdapter(getContext(), productAvailableSizes);
        sizeAdapter.setOnItemClickListener(this);
        LinearLayoutManager horizontalLayoutManagersize = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        fragmentSelectedProductDetailsBinding.listviewProductSize.setLayoutManager(horizontalLayoutManagersize);
        fragmentSelectedProductDetailsBinding.listviewProductSize.setAdapter(sizeAdapter);
    }

    private void setOffersAdapter() {
        offersList.add("Get 10% off on this product while using NBK Credit Card ");
//        offersList.add("Bank offer 10% instant savings on ICIC Credit and Debit cards T&C");
//        offersList.add("Bank offer 5% unlimited cashback on Axisbank Credit and Debit cards T&C");

//        services.add("14 Days Return Policy");
//        services.add("Cash on Delivery Available");

        OffersAdapter offersAdapter = new OffersAdapter(getContext(), R.layout.layout_offer_item, offersList);
        fragmentSelectedProductDetailsBinding.productDetails.listviewOffers.setAdapter(offersAdapter);

//        ServicesAdapter servicesAdapter = new ServicesAdapter(getContext(), R.layout.layout_offer_item, services);
//        fragmentSelectedProductDetailsBinding.productDetails.recyclerViewServices.setAdapter(servicesAdapter);
    }

    @Override
    public void onItemClickListener(int position, View view) {

        try {
            if (view.getId() == R.id.cv_background_size) {
                sizeAdapter.selectedItem();
                String selectedSize = productAvailableSizes.get(position);
                Bundle bundle = new Bundle();
                bundle.putString("selected_size", selectedSize);
                dashboardActivity.getmFirebaseAnalytics().logEvent("SIZE_SELECTED", bundle);
                ProductColorAdapter.setsSelected(-1);
                currentColorsList = (ArrayList<ProductConfigurable>) sizeColorHashMap.get(productAvailableSizes.get(position));
                //Hiding colors if color code is null
                for (int i = 0; i < currentColorsList.size(); i++) {
                    if (currentColorsList.get(i).getColorCode() == null)
                        currentColorsList.remove(i);
                }
                // hiding views
                if (currentColorsList.size() == 0) {
                    fragmentSelectedProductDetailsBinding.txtColorVariant.setVisibility(View.GONE);
                    fragmentSelectedProductDetailsBinding.listviewColorVariants.setVisibility(View.GONE);
                } else if (currentColorsList.size() > 0)
                    selectedColor = currentColorsList.get(0);
                setColorAdapter(currentColorsList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (view.getId() == R.id.tvColorVariant) {
                productColorAdapter.selectedItem();
                if (ProductSizeAdapter.sSelected == -1) {
                    ChooseSizeDialog chooseSizeDialog = new ChooseSizeDialog(dashboardActivity);
                    chooseSizeDialog.show(getParentFragmentManager(), "CHOOSE_SIZE_DIALOG");
                } else
                    selectedColor = currentColorsList.get(position);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        dashboardActivity.handleActionMenuBar(false, true, "");
        dashboardActivity.hideSearchPage();
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            dashboardActivity.hideSearchPage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void callAddToCartApi(String jsonParamString) {

        fragmentSelectedProductDetailsBinding.progressBar.setVisibility(View.VISIBLE);
        PreferenceHandler preferenceHandler = new PreferenceHandler(getContext(), PreferenceHandler.TOKEN_LOGIN);
        userID = preferenceHandler.getData(PreferenceHandler.LOGIN_USER_ID, "");
        sessionToken = preferenceHandler.getData(PreferenceHandler.LOGIN_TOKEN, "");
        productDetailViewModel.addToCart(userID, jsonParamString, sessionToken).observe(getActivity(), addToCartResponse -> {
            fragmentSelectedProductDetailsBinding.progressBar.setVisibility(View.GONE);
            switch (addToCartResponse.status) {
                case SUCCESS:
                    if (isbuyNow) {
                        dashboardActivity.replaceFragment(R.id.fragment_replacer, new MyCartFragment(), null, true, false);
                        dashboardActivity.handleActionMenuBar(true, false, "My Cart");
                        dashboardActivity.setTitle("My Cart");
                    } else {
                        CartDialog cartDialog = new CartDialog(dashboardActivity);
                        cartDialog.show(getParentFragmentManager(), "CART_DIALOG");
                    }

                    logAddToCartEvent(addToCartResponse);
                    break;
                case LOADING:
                    break;
                case ERROR:
                    Toast.makeText(dashboardActivity, addToCartResponse.message, Toast.LENGTH_SHORT).show();
                    break;
            }
        });

    }

    private void logAddToCartEvent(Resource<CartResponse> addToCartResponse) {
        try {
            Bundle analyticsBundle = new Bundle();
            analyticsBundle.putString(FirebaseAnalytics.Param.CURRENCY, "KWD");
            analyticsBundle.putDouble(FirebaseAnalytics.Param.VALUE, addToCartResponse.data.getCartData().getSubTotal());
            List<ShoppingCartItem> shoppingCartItemList = addToCartResponse.data.getCartData().getShoppingCartItems();
            ArrayList<Bundle> cartedItemAnalyticBundles = new ArrayList<>();
            for (int i = 0; i < shoppingCartItemList.size(); i++) {
                Bundle itemBundle = new Bundle();
                itemBundle.putString(FirebaseAnalytics.Param.ITEM_NAME, shoppingCartItemList.get(i).getProduct().getDescriptions().get(0).getProductName());
                itemBundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, shoppingCartItemList.get(i).getProduct().getCategories().get(0).getDescriptions().get(0).getCategoryName());
                itemBundle.putString("product_id", shoppingCartItemList.get(i).getProductId());
                cartedItemAnalyticBundles.add(itemBundle);
            }
            analyticsBundle.putParcelableArrayList(FirebaseAnalytics.Param.ITEMS, cartedItemAnalyticBundles);
            dashboardActivity.getmFirebaseAnalytics().logEvent(FirebaseAnalytics.Event.ADD_TO_CART, analyticsBundle);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callWishlistApi(Product product, boolean isWishlisted) {
        PreferenceHandler preferenceHandler = new PreferenceHandler(getContext(), PreferenceHandler.TOKEN_LOGIN);
        String userID = preferenceHandler.getData(PreferenceHandler.LOGIN_USER_ID, "");
        productDetailViewModel.addToWishlist(product.getProductId(), userID, sessionToken).observe(getActivity(), wishlistResponse -> {
            switch (wishlistResponse.status) {
                case SUCCESS:
                    if (isWishlisted)
                        fragmentSelectedProductDetailsBinding.checkboxWishlist.setChecked(true);
                    else
                        fragmentSelectedProductDetailsBinding.checkboxWishlist.setChecked(false);
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

    private void setRelatedProducts() {
        if (recommendedProductList != null && recommendedProductList.size() > 0) {
            fragmentSelectedProductDetailsBinding.tvRecommendedProducts.setVisibility(View.VISIBLE);
            fragmentSelectedProductDetailsBinding.recyclerviewRelatedProducts.setVisibility(View.VISIBLE);
            RelatedProductsRecyclerviewAdapter relatedProductsRecyclerviewAdapter = new RelatedProductsRecyclerviewAdapter(getContext(), (ArrayList<Product>) recommendedProductList) {
                @Override
                public void onLikeClicked(Product product, boolean isWishlisted) {
                    callWishlistApi(product, isWishlisted);
                }

                @Override
                public void onItemClicked(Product productContent) {
                    Product selectedProduct = productContent;
//                    productListingViewModel.select(selectedProduct);
                    Gson gson = new Gson();
                    String objectString = gson.toJson(selectedProduct);
                    Bundle bundle = new Bundle();
                    bundle.putString("selected_product_object", objectString);

                    dashboardActivity.setTitle(selectedProduct.getDescriptions().get(0).getProductName());
                    if (isRedirectedFromProductListing)
                        dashboardActivity.replaceFragment(R.id.fragment_replacer_product, new SelectedProductDetailsFragment(), bundle, true, false);
                    else
                        dashboardActivity.replaceFragment(R.id.fragment_replacer, new SelectedProductDetailsFragment(), bundle, true, false);


                }
            };

            LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
            fragmentSelectedProductDetailsBinding.recyclerviewRelatedProducts.setLayoutManager(horizontalLayoutManager);
            fragmentSelectedProductDetailsBinding.recyclerviewRelatedProducts.setAdapter(relatedProductsRecyclerviewAdapter);
        } else {
            fragmentSelectedProductDetailsBinding.tvRecommendedProducts.setVisibility(View.GONE);
            fragmentSelectedProductDetailsBinding.recyclerviewRelatedProducts.setVisibility(View.GONE);

        }

    }


    public static class ProductImageActivity extends BaseActivity {

        ActivityImageShowingBinding imageShowingBinding;
        private ScaleGestureDetector mScaleGestureDetector;
        private float mScaleFactor = 1.0f;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_image_showing);
            imageShowingBinding = DataBindingUtil.setContentView(this, R.layout.activity_image_showing);
            try {
                String imageUrl = getIntent().getStringExtra("image_url");
                Glide.with(this)
                        .load(imageUrl)
                        .centerCrop()
                        .into(imageShowingBinding.imageViewSelected);
            } catch (Exception e) {
                e.printStackTrace();
            }
            mScaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());
        }

        @Override
        public void onBackPressed() {
            super.onBackPressed();
            finish();
        }

        public boolean onTouchEvent(MotionEvent motionEvent) {
            mScaleGestureDetector.onTouchEvent(motionEvent);
            return true;
        }

        private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
            @Override
            public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
                mScaleFactor *= scaleGestureDetector.getScaleFactor();
                mScaleFactor = Math.max(0.1f,
                        Math.min(mScaleFactor, 10.0f));
                imageShowingBinding.imageViewSelected.setScaleX(mScaleFactor);
                imageShowingBinding.imageViewSelected.setScaleY(mScaleFactor);
                return true;
            }
        }
    }
}