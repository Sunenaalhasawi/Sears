package com.hasawi.sears.ui.main.view.checkout;

import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.hasawi.sears.R;
import com.hasawi.sears.data.api.model.pojo.ShoppingCartItem;
import com.hasawi.sears.databinding.FragmentCartBinding;
import com.hasawi.sears.ui.base.BaseFragment;
import com.hasawi.sears.ui.main.adapters.CartAdapter;
import com.hasawi.sears.ui.main.view.DashboardActivity;
import com.hasawi.sears.ui.main.view.signin.SigninActivity;
import com.hasawi.sears.ui.main.viewmodel.CartViewModel;
import com.hasawi.sears.utils.PreferenceHandler;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_cart;
    }

    @Override
    protected void setup() {
        fragmentCartBinding = (FragmentCartBinding) viewDataBinding;
        dashboardActivity = (DashboardActivity) getActivity();
        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);
        fragmentCartBinding.recyclerviewCart.setLayoutManager(new LinearLayoutManager(getActivity()));
        cartAdapter = new CartAdapter() {
            @Override
            public void onItemDeleteClicked(ShoppingCartItem cartItem) {
                fragmentCartBinding.progressBar.setVisibility(View.VISIBLE);
                removeItemFromCartApi(cartItem);
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvContinueOrder:
                dashboardActivity.replaceFragment(R.id.fragment_replacer, new CheckoutFragment(), null, true, false);
                break;
            case R.id.btnShopNow:
                int fragmentCount = getParentFragmentManager().getBackStackEntryCount();
                for (int i = 0; i < fragmentCount; i++) {
                    getParentFragmentManager().popBackStackImmediate();
                }
                dashboardActivity.showBackButton(false);
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
                        cartItemsList = cartResponse.data.getCartData().getShoppingCartItems();
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
                            totalPrice = cartResponse.data.getCartData().getSubTotal() + "";
                            cartCount = cartResponse.data.getCartData().getShoppingCartItems().size();
                            cartAdapter.addAll(cartItemsList);
                            setUiValues();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
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
                    cartItemsList = cartResponse.data.getCartData().getShoppingCartItems();
                    if (cartItemsList.size() == 0) {
                        fragmentCartBinding.cvLoggedUserEmptyCart.setVisibility(View.VISIBLE);
                    } else {
                        totalPrice = cartResponse.data.getCartData().getSubTotal() + "";
                        cartCount = cartResponse.data.getCartData().getShoppingCartItems().size();
                        cartAdapter.addAll(cartItemsList);
                        setUiValues();
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
}
