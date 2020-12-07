package com.hasawi.sears.ui.main.view.home;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.hasawi.sears.R;
import com.hasawi.sears.data.api.model.pojo.Content;
import com.hasawi.sears.data.api.model.pojo.ProductAttribute;
import com.hasawi.sears.data.api.model.pojo.ProductConfigurable;
import com.hasawi.sears.databinding.FragmentSelectedProductDetailsBinding;
import com.hasawi.sears.ui.base.BaseFragment;
import com.hasawi.sears.ui.main.adapters.OffersAdapter;
import com.hasawi.sears.ui.main.adapters.ProductAttributesAdapter;
import com.hasawi.sears.ui.main.adapters.ProductColorAdapter;
import com.hasawi.sears.ui.main.adapters.ProductSizeAdapter;
import com.hasawi.sears.ui.main.adapters.RelatedProductsRecyclerviewAdapter;
import com.hasawi.sears.ui.main.adapters.ServicesAdapter;
import com.hasawi.sears.ui.main.listeners.RecyclerviewSingleChoiceClickListener;
import com.hasawi.sears.ui.main.view.DashboardActivity;
import com.hasawi.sears.ui.main.view.checkout.MyCartFragment;
import com.hasawi.sears.ui.main.viewmodel.SharedHomeViewModel;
import com.hasawi.sears.utils.CartDialog;
import com.hasawi.sears.utils.PreferenceHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelectedProductDetailsFragment extends BaseFragment implements RecyclerviewSingleChoiceClickListener {

    SharedHomeViewModel sharedHomeViewModel;
    FragmentSelectedProductDetailsBinding fragmentSelectedProductDetailsBinding;
    Content currentSelectedProduct;
    DashboardActivity dashboardActivity;
    ArrayList<String> productAvailableSizes = new ArrayList<>();
    ProductColorAdapter productColorAdapter;
    private ArrayList<ProductAttribute> productAttributeArrayList = new ArrayList<>();
    private ProductAttributesAdapter productAttributesAdapter;
    private ProductSizeAdapter sizeAdapter;
    private ArrayList<String> offersList = new ArrayList<>();
    private ArrayList<String> services = new ArrayList<>();
    private ArrayList<String> colorsList = new ArrayList<>();
    private boolean isSearch = false;
    private String selectedObjectID = "";
    private String sessionToken, userID;
    private HashMap<String, List<ProductConfigurable>> sizeColorHashMap;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_selected_product_details;
    }

    @Override
    protected void setup() {
        fragmentSelectedProductDetailsBinding = (FragmentSelectedProductDetailsBinding) viewDataBinding;
        sharedHomeViewModel = new ViewModelProvider(getActivity()).get(SharedHomeViewModel.class);
        dashboardActivity = (DashboardActivity) getActivity();
        PreferenceHandler preferenceHandler = new PreferenceHandler(getContext(), PreferenceHandler.TOKEN_LOGIN);
        userID = preferenceHandler.getData(PreferenceHandler.LOGIN_USER_ID, "");
        sessionToken = preferenceHandler.getData(PreferenceHandler.LOGIN_TOKEN, "");
        setUpProductDescriptionRecyclerview();
//        setProductSizeRecyclerview();
        setOffersAdapter();
//        setColorAdapter();
        Bundle bundle = getArguments();
        try {

            selectedObjectID = bundle.getString("product_object_id");
            isSearch = bundle.getBoolean("is_search");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            String selectedProductObj = bundle.getString("selected_product_object");
            Gson gson = new Gson();
            currentSelectedProduct = gson.fromJson(selectedProductObj, Content.class);
            selectedObjectID = currentSelectedProduct.getProductId();
        } catch (Exception e) {
            e.printStackTrace();
        }

//        if (isSearch) {
        fragmentSelectedProductDetailsBinding.progressBar.setVisibility(View.VISIBLE);
        sharedHomeViewModel.getSearchedProductDetails(selectedObjectID).observe(getActivity(), searchedProductDetailsResponse -> {
            switch (searchedProductDetailsResponse.status) {
                case SUCCESS:
                    currentSelectedProduct = searchedProductDetailsResponse.data.getSingleProductData();
                    setUIValues(currentSelectedProduct);
                    setColorSizeAdapterList(currentSelectedProduct.getProductConfigurables());
                    break;
                case LOADING:
                    break;
                case ERROR:
                    Toast.makeText(dashboardActivity, searchedProductDetailsResponse.message, Toast.LENGTH_SHORT).show();
                    break;
            }

            fragmentSelectedProductDetailsBinding.progressBar.setVisibility(View.GONE);
        });
//        }
//        else {
//            fragmentSelectedProductDetailsBinding.progressBar.setVisibility(View.VISIBLE);
//            callProductDetailApi(currentSelectedProduct.getDescriptions().get(0).getSlug());
//            sharedHomeViewModel.getSelected().observe(getViewLifecycleOwner(), item -> {
//                // Update the UI.Todo update all views here
//                currentSelectedProduct = item;
//                if (currentSelectedProduct != null) {
//                    setUIValues(currentSelectedProduct);
//                    callProductDetailApi(currentSelectedProduct.getDescriptions().get(0).getSlug());
//                }
//
//
//            });
//        }


        fragmentSelectedProductDetailsBinding.imageViewSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(dashboardActivity, ProductImageActivity.class);
                i.putExtra("image_url", currentSelectedProduct.getProductImages().get(0).getImageName());
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
                PreferenceHandler preferenceHandler = new PreferenceHandler(dashboardActivity, PreferenceHandler.TOKEN_LOGIN);
                boolean isAlreadyLoggedIn = preferenceHandler.getData(PreferenceHandler.LOGIN_STATUS, false);
                Map<String, Object> jsonParams = new ArrayMap<>();
                jsonParams.put("productId", currentSelectedProduct.getProductId());
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
        });

        setRelatedProducts();
    }

    private void setColorAdapter(List<ProductConfigurable> colors) {
//        colorsList.add("#377AD3");
//        colorsList.add("#61BF22");
//        colorsList.add("#BF118F");
//        colorsList.add("#43BFA8");
//        colorsList.add("#7178BF");
        fragmentSelectedProductDetailsBinding.listviewColorVariants.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        productColorAdapter = new ProductColorAdapter(getContext(), (ArrayList<ProductConfigurable>) colors);
        fragmentSelectedProductDetailsBinding.listviewColorVariants.setAdapter(productColorAdapter);
    }

    private void setUIValues(Content currentSelectedProduct) {
        Picasso.get().load(currentSelectedProduct.getProductImages().get(0).getImageName()).into(fragmentSelectedProductDetailsBinding.imageViewSelected);
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
        fragmentSelectedProductDetailsBinding.tvSku.setText(currentSelectedProduct.getSku());
        Picasso.get().load(currentSelectedProduct.getBrandLogoUrl()).into(fragmentSelectedProductDetailsBinding.imageViewBrandLogo);
        fragmentSelectedProductDetailsBinding.tvOfferPercent.setText(currentSelectedProduct.getDiscountPercentage() + "% OFF");
        fragmentSelectedProductDetailsBinding.tvOriginalPrice.setPaintFlags(fragmentSelectedProductDetailsBinding.tvOriginalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        productAttributeArrayList = (ArrayList<ProductAttribute>) currentSelectedProduct.getProductAttributes();
        productAttributesAdapter = new ProductAttributesAdapter(getActivity(), productAttributeArrayList);
        fragmentSelectedProductDetailsBinding.productDetails.recyclerviewProductDetails.setAdapter(productAttributesAdapter);

    }

    public void setUpProductDescriptionRecyclerview() {
        productAttributesAdapter = new ProductAttributesAdapter(getContext(), productAttributeArrayList);
        fragmentSelectedProductDetailsBinding.productDetails.recyclerviewProductDetails.setLayoutManager(new LinearLayoutManager(getContext()));
        fragmentSelectedProductDetailsBinding.productDetails.recyclerviewProductDetails.setAdapter(productAttributesAdapter);
    }

    public void callProductDetailApi(String productName) {
        sharedHomeViewModel.getProductDetails(productName).observe(getActivity(), productDetailsResponse -> {
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
            if (sizeColorHashMap.size() > 0)
                setColorAdapter(sizeColorHashMap.get(sizeList.get(0)));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setProductSizeRecyclerview(List<String> sizes) {
//        productAvailableSizes.clear();
//        productAvailableSizes.add("S");
//        productAvailableSizes.add("M");
//        productAvailableSizes.add("L");
//        productAvailableSizes.add("XL");
//        productAvailableSizes.add("XXL");
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

        services.add("14 Days Return Policy");
        services.add("Cash on Delivery Available");

        OffersAdapter offersAdapter = new OffersAdapter(getContext(), R.layout.layout_offer_item, offersList);
        fragmentSelectedProductDetailsBinding.productDetails.listviewOffers.setAdapter(offersAdapter);

        ServicesAdapter servicesAdapter = new ServicesAdapter(getContext(), R.layout.layout_offer_item, services);
        fragmentSelectedProductDetailsBinding.productDetails.recyclerViewServices.setAdapter(servicesAdapter);
    }

    @Override
    public void onItemClickListener(int position, View view) {
        sizeAdapter.selectedItem();
        try {
            setColorAdapter(sizeColorHashMap.get(productAvailableSizes.get(position)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        dashboardActivity.showBottomNavigationBar();
        dashboardActivity.hideSearchPage();
    }

    public void callAddToCartApi(String jsonParamString) {

        fragmentSelectedProductDetailsBinding.progressBar.setVisibility(View.VISIBLE);

        sharedHomeViewModel.addToCart(userID, jsonParamString, sessionToken).observe(getActivity(), addToCartResponse -> {
            fragmentSelectedProductDetailsBinding.progressBar.setVisibility(View.GONE);
            switch (addToCartResponse.status) {
                case SUCCESS:
                    dashboardActivity.setCartCount(1);
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

    private void callWishlistApi(Content product, boolean isWishlisted) {
        PreferenceHandler preferenceHandler = new PreferenceHandler(getContext(), PreferenceHandler.TOKEN_LOGIN);
        String userID = preferenceHandler.getData(PreferenceHandler.LOGIN_USER_ID, "");
        sharedHomeViewModel.addToWishlist(product.getProductId(), userID, sessionToken).observe(getActivity(), wishlistResponse -> {
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
        List<Content> productList = new ArrayList<>();
        productList.add(new Content());
        productList.add(new Content());
        productList.add(new Content());
        productList.add(new Content());
        productList.add(new Content());
        productList.add(new Content());
        productList.add(new Content());
        productList.add(new Content());
        productList.add(new Content());
        productList.add(new Content());

        RelatedProductsRecyclerviewAdapter relatedProductsRecyclerviewAdapter = new RelatedProductsRecyclerviewAdapter((ArrayList<Content>) productList) {
            @Override
            public void onLikeClicked(Content product) {

            }

            @Override
            public void onItemClicked(Content productContent) {

            }
        };

        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        fragmentSelectedProductDetailsBinding.recyclerviewRelatedProducts.setLayoutManager(horizontalLayoutManager);
        fragmentSelectedProductDetailsBinding.recyclerviewRelatedProducts.setAdapter(relatedProductsRecyclerviewAdapter);


    }
}
