package com.hasawi.sears.ui.main.view.checkout;

import android.content.Intent;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.facebook.appevents.AppEventsConstants;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.hasawi.sears.R;
import com.hasawi.sears.data.api.model.pojo.ShoppingCartItem;
import com.hasawi.sears.databinding.FragmentCartBinding;
import com.hasawi.sears.ui.base.BaseFragment;
import com.hasawi.sears.ui.main.adapters.CartAdapter;
import com.hasawi.sears.ui.main.view.DashboardActivity;
import com.hasawi.sears.ui.main.view.signin.SigninActivity;
import com.hasawi.sears.ui.main.viewmodel.CartViewModel;
import com.hasawi.sears.utils.PreferenceHandler;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.facebook.appevents.AppEventsConstants.EVENT_PARAM_CURRENCY;

public class MyCartFragment extends BaseFragment implements View.OnClickListener {
    FragmentCartBinding fragmentCartBinding;
    CartAdapter cartAdapter;
    CartViewModel cartViewModel;
    List<ShoppingCartItem> cartItemsList = new ArrayList<>();
    int cartCount = 0;
    String totalPrice = "", userID = "", username = "";
    String sessionToken;
    String cartId;
    boolean isUserLoggedIn = false;
    DashboardActivity dashboardActivity;
    boolean isQuantityIncreased;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_cart;
    }

    @Override
    protected void setup() {
        fragmentCartBinding = (FragmentCartBinding) viewDataBinding;
        dashboardActivity = (DashboardActivity) getActivity();
        dashboardActivity.handleActionBarIcons(false);
        Boolean isLoggedIn = new PreferenceHandler(getContext(), PreferenceHandler.TOKEN_LOGIN).getData(PreferenceHandler.LOGIN_STATUS, false);
        if (isLoggedIn)
            dashboardActivity.handleActionMenuBar(true, false, "My Cart");
        else
            dashboardActivity.handleActionMenuBar(true, true, "My Cart");
        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);
        fragmentCartBinding.recyclerviewCart.setLayoutManager(new LinearLayoutManager(getActivity()));
        cartAdapter = new CartAdapter(dashboardActivity) {
            @Override
            public void onItemDeleteClicked(ShoppingCartItem cartItem) {
                fragmentCartBinding.progressBar.setVisibility(View.VISIBLE);
                removeItemFromCartApi(cartItem);
            }

            @Override
            public void cartItemsUpdated(ShoppingCartItem cartItem, int quantity, boolean isIncreased) {
                updateCartItemsApi(cartItem.getProductId(), cartItem.getProductConfigurable().getRefSku(), quantity + "");
                isQuantityIncreased = isIncreased;
            }

        };
        fragmentCartBinding.recyclerviewCart.setAdapter(cartAdapter);
        fragmentCartBinding.tvContinueOrder.setOnClickListener(this);
        try {
            PreferenceHandler preferenceHandler = new PreferenceHandler(getActivity(), PreferenceHandler.TOKEN_LOGIN);
            userID = preferenceHandler.getData(PreferenceHandler.LOGIN_USER_ID, "");
            sessionToken = preferenceHandler.getData(PreferenceHandler.LOGIN_TOKEN, "");
            username = preferenceHandler.getData(PreferenceHandler.LOGIN_USERNAME, "");
            isUserLoggedIn = preferenceHandler.getData(PreferenceHandler.LOGIN_STATUS, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (isUserLoggedIn)
            callMyCartApi();
        else {
            fragmentCartBinding.cvGuestUserEmptyCart.setVisibility(View.VISIBLE);
        }
        fragmentCartBinding.layoutNoItemsCart.btnShopNow.setOnClickListener(this);
        fragmentCartBinding.layoutGuestNoItemsCart.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(dashboardActivity, SigninActivity.class);
                startActivity(intent);
                dashboardActivity.finish();
            }
        });
    }

    private void setUiValues() {
        fragmentCartBinding.tvItemCount.setText("Bag " + cartCount);
        fragmentCartBinding.tvTotalPrice.setText("KWD " + totalPrice);
        fragmentCartBinding.tvTotalAmount.setText("KWD " + totalPrice);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvContinueOrder:
                dashboardActivity.handleActionMenuBar(true, false, "Checkout");
                dashboardActivity.replaceFragment(R.id.fragment_replacer, new CheckoutFragment(), null, true, false);
                break;
            case R.id.btnShopNow:
                int fragmentCount = getParentFragmentManager().getBackStackEntryCount();
                for (int i = 0; i < fragmentCount; i++) {
                    getParentFragmentManager().popBackStackImmediate();
                }
                dashboardActivity.handleActionMenuBar(false, true, "");
                break;
            default:
                break;
        }
    }

    public void callMyCartApi() {

        cartViewModel.getCartItems(userID, sessionToken).observe(this, cartResponse -> {
            switch (cartResponse.status) {
                case SUCCESS:
                    try {
                        cartItemsList = new ArrayList<>();
                        List<ShoppingCartItem> availableItems = cartResponse.data.getCartData().getAvailable();
                        for (int i = 0; i < availableItems.size(); i++) {
                            availableItems.get(i).setOutOfStock(false);
                            cartItemsList.add(availableItems.get(i));
                        }
                        List<ShoppingCartItem> outofStockItems = cartResponse.data.getCartData().getOutOfStock();
                        for (int i = 0; i < outofStockItems.size(); i++) {
                            outofStockItems.get(i).setOutOfStock(true);
                            cartItemsList.add(outofStockItems.get(i));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (cartItemsList.size() == 0) {
                        fragmentCartBinding.cvLoggedUserEmptyCart.setVisibility(View.VISIBLE);
                    } else {
                        try {
                            cartId = cartResponse.data.getCartData().getShoppingCartId();
                            PreferenceHandler preferenceHandler = new PreferenceHandler(dashboardActivity, PreferenceHandler.TOKEN_LOGIN);
                            preferenceHandler.saveData(PreferenceHandler.LOGIN_USER_CART_ID, cartId);
                            totalPrice = cartResponse.data.getCartData().getTotal() + "";
                            cartCount = cartResponse.data.getCartData().getShoppingCartItems().size();
                            cartAdapter.addAll(cartItemsList);
                            setUiValues();
                            dashboardActivity.setCartBadgeNumber(cartCount);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    try {
                        logViewCartEvent((ArrayList<ShoppingCartItem>) cartResponse.data.getCartData().getShoppingCartItems(), cartResponse.data.getCartData().getTotal());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case LOADING:
                    break;
                case ERROR:
                    Toast.makeText(dashboardActivity, cartResponse.message, Toast.LENGTH_SHORT).show();
                    break;
            }
            fragmentCartBinding.progressBar.setVisibility(View.GONE);
        });
    }

    public void removeItemFromCartApi(ShoppingCartItem shoppingCartItem) {

        cartViewModel.removeItemFromCart(userID, shoppingCartItem.getShoppingCartItemId(), sessionToken).observe(this, cartResponse -> {
            switch (cartResponse.status) {
                case SUCCESS:
                    Toast.makeText(dashboardActivity, "Removed item from cart successfully", Toast.LENGTH_SHORT).show();
//                    cartItemsList = cartResponse.data.getCartData().getShoppingCartItems();
//                    if (cartItemsList.size() == 0) {
//                        fragmentCartBinding.cvLoggedUserEmptyCart.setVisibility(View.VISIBLE);
//                    } else {
//                        totalPrice = cartResponse.data.getCartData().getSubTotal() + "";
//                        cartCount = cartResponse.data.getCartData().getShoppingCartItems().size();
//                        cartAdapter.addAll(cartItemsList);
//                        setUiValues();
//                    }
                    callMyCartApi();
                    break;
                case LOADING:
                    break;
                case ERROR:
                    Toast.makeText(dashboardActivity, cartResponse.message, Toast.LENGTH_SHORT).show();
                    break;
            }
            fragmentCartBinding.progressBar.setVisibility(View.GONE);
        });
    }

    private void updateCartItemsApi(String productId, String refSku, String quantity) {
        fragmentCartBinding.progressBar.setVisibility(View.VISIBLE);
        Map<String, Object> jsonParams = new ArrayMap<>();
        jsonParams.put("productId", productId);
        jsonParams.put("refSku", refSku);
        jsonParams.put("quantity", quantity);

        String jsonParamString = (new JSONObject(jsonParams)).toString();
        cartViewModel.updateCartItems(userID, jsonParamString, sessionToken).observe(getActivity(), addToCartResponse -> {
            fragmentCartBinding.progressBar.setVisibility(View.GONE);
            switch (addToCartResponse.status) {
                case SUCCESS:
                    totalPrice = addToCartResponse.data.getCartData().getTotal() + "";
                    cartCount = addToCartResponse.data.getCartData().getShoppingCartItems().size();
                    setUiValues();
                    Toast.makeText(dashboardActivity, "Cart Updated Successfully", Toast.LENGTH_SHORT).show();
                    break;
                case LOADING:
                    break;
                case ERROR:
                    cartAdapter.addAll(cartItemsList);
                    Toast.makeText(dashboardActivity, addToCartResponse.message, Toast.LENGTH_SHORT).show();
                    break;
            }
        });
    }

    public void showProgressbar(boolean shouldShowProgressbar) {
        if (shouldShowProgressbar)
            fragmentCartBinding.progressBar.setVisibility(View.VISIBLE);
        else
            fragmentCartBinding.progressBar.setVisibility(View.GONE);
    }

    private void logViewCartEvent(ArrayList<ShoppingCartItem> cartItemsList, Double cartValue) {
        ArrayList<Bundle> itemParcelableList = new ArrayList<>();
        int cartCount = cartItemsList.size();
        for (int i = 0; i < cartItemsList.size(); i++) {
            Bundle itemBundle = new Bundle();
            itemBundle.putString(FirebaseAnalytics.Param.ITEM_NAME, cartItemsList.get(i).getProduct().getDescriptions().get(0).getProductName());
            itemBundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, cartItemsList.get(i).getProduct().getCategories().get(0).getDescriptions().get(0).getCategoryName());
            itemBundle.putString(FirebaseAnalytics.Param.ITEM_BRAND, cartItemsList.get(i).getProduct().getManufature().getManufactureDescriptions().get(0).getName());
            itemBundle.putString("category_id", cartItemsList.get(i).getProduct().getCategories().get(0).getCategoryId());
            itemBundle.putString(FirebaseAnalytics.Param.ITEM_ID, cartItemsList.get(i).getProduct().getProductId());
            itemParcelableList.add(itemBundle);
        }


        Bundle analyticsBundle = new Bundle();
        analyticsBundle.putString(FirebaseAnalytics.Param.CURRENCY, "KWD");
        analyticsBundle.putDouble(FirebaseAnalytics.Param.VALUE, cartValue);
        analyticsBundle.putLong("item_count", cartCount);
        analyticsBundle.putParcelableArrayList(FirebaseAnalytics.Param.ITEMS, itemParcelableList);
        dashboardActivity.getmFirebaseAnalytics().logEvent(FirebaseAnalytics.Event.VIEW_CART, analyticsBundle);
    }

    private void logViewCartFacebookEvent(ArrayList<ShoppingCartItem> cartItemsList, Double cartValue) {
        ArrayList<Bundle> itemParcelableList = new ArrayList<>();
        int cartCount = cartItemsList.size();
        for (int i = 0; i < cartItemsList.size(); i++) {
            Bundle itemBundle = new Bundle();
            itemBundle.putString(AppEventsConstants.EVENT_PARAM_CONTENT, cartItemsList.get(i).getProduct().getDescriptions().get(0).getProductName());
            itemBundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, cartItemsList.get(i).getProduct().getCategories().get(0).getDescriptions().get(0).getCategoryName());
            itemBundle.putString(FirebaseAnalytics.Param.ITEM_BRAND, cartItemsList.get(i).getProduct().getManufature().getManufactureDescriptions().get(0).getName());
            itemBundle.putString("category_id", cartItemsList.get(i).getProduct().getCategories().get(0).getCategoryId());
            itemBundle.putString(FirebaseAnalytics.Param.ITEM_ID, cartItemsList.get(i).getProduct().getProductId());
            itemParcelableList.add(itemBundle);
        }


        Bundle analyticsBundle = new Bundle();
        analyticsBundle.putString(EVENT_PARAM_CURRENCY, "KWD");
        analyticsBundle.putDouble(FirebaseAnalytics.Param.VALUE, cartValue);
        analyticsBundle.putLong("item_count", cartCount);
        analyticsBundle.putParcelableArrayList(FirebaseAnalytics.Param.ITEMS, itemParcelableList);
        dashboardActivity.getmFirebaseAnalytics().logEvent(FirebaseAnalytics.Event.VIEW_CART, analyticsBundle);
    }
}
