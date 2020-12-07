package com.hasawi.sears.ui.main.view.checkout;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.hasawi.sears.R;
import com.hasawi.sears.data.api.model.pojo.Address;
import com.hasawi.sears.data.api.model.pojo.PaymentMode;
import com.hasawi.sears.data.api.model.pojo.ShoppingCartItem;
import com.hasawi.sears.databinding.FragmentCheckoutNewBinding;
import com.hasawi.sears.ui.base.BaseFragment;
import com.hasawi.sears.ui.main.adapters.CheckoutProductListAdapter;
import com.hasawi.sears.ui.main.adapters.PaymentModeAdapter;
import com.hasawi.sears.ui.main.adapters.ShippingAddressAdapter;
import com.hasawi.sears.ui.main.listeners.RecyclerviewSingleChoiceClickListener;
import com.hasawi.sears.ui.main.view.DashboardActivity;
import com.hasawi.sears.ui.main.viewmodel.CheckoutViewModel;
import com.hasawi.sears.utils.PreferenceHandler;

import java.util.ArrayList;
import java.util.List;

public class CheckoutFragment extends BaseFragment implements View.OnClickListener, RecyclerviewSingleChoiceClickListener {

    FragmentCheckoutNewBinding fragmentCheckoutNewBinding;
    CheckoutViewModel checkoutViewModel;
    String userId, cartId, sessionToken;
    PaymentModeAdapter paymentModeAdapter;
    ShippingAddressAdapter shippingAddressAdapter;
    CheckoutProductListAdapter checkoutProductListAdapter;
    List<PaymentMode> paymentModeList = new ArrayList<>();
    List<Address> addressList = new ArrayList<>();
    DashboardActivity dashboardActivity;
    private LinearLayoutManager horizontalLayoutManager;
    private PaymentMode selectedPaymentMode;
    private LinearLayoutManager horizontalLayoutManagerAddress;
    private Address selectedAddress;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_checkout_new;
    }

    @Override
    protected void setup() {
        fragmentCheckoutNewBinding = (FragmentCheckoutNewBinding) viewDataBinding;
        checkoutViewModel = new ViewModelProvider(this).get(CheckoutViewModel.class);
        fragmentCheckoutNewBinding.layoutCheckout.tvAddAddress.setOnClickListener(this);
        fragmentCheckoutNewBinding.layoutCheckout.tvPurchaseMore.setOnClickListener(this);
        fragmentCheckoutNewBinding.tvMakeYourpayment.setOnClickListener(this);
        fragmentCheckoutNewBinding.layoutCheckout.tvApplyCoupon.setOnClickListener(this);
        dashboardActivity = (DashboardActivity) getActivity();
        try {
            PreferenceHandler preferenceHandler = new PreferenceHandler(getActivity(), PreferenceHandler.TOKEN_LOGIN);
            userId = preferenceHandler.getData(PreferenceHandler.LOGIN_USER_ID, "");
            sessionToken = preferenceHandler.getData(PreferenceHandler.LOGIN_TOKEN, "");
            cartId = preferenceHandler.getData(PreferenceHandler.LOGIN_USER_CART_ID, "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        callCheckoutApi(userId, cartId, sessionToken);
        horizontalLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        fragmentCheckoutNewBinding.layoutCheckout.recyclerviewPaymentmode.setLayoutManager(horizontalLayoutManager);
        horizontalLayoutManagerAddress = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        fragmentCheckoutNewBinding.layoutCheckout.recyclerViewAddress.setLayoutManager(horizontalLayoutManagerAddress);
        fragmentCheckoutNewBinding.layoutCheckout.recyclerViewProducts.setLayoutManager(new LinearLayoutManager(getContext()));
        callGetAddressApi(userId, sessionToken);

    }

    private void callGetAddressApi(String userId, String sessionToken) {
        fragmentCheckoutNewBinding.progressBar.setVisibility(View.VISIBLE);
        checkoutViewModel.getAddresses(userId, sessionToken).observe(getActivity(), getAllAddressResponseResource -> {
            switch (getAllAddressResponseResource.status) {
                case SUCCESS:
                    try {
                        addressList = getAllAddressResponseResource.data.getData().getAddresses();
                        shippingAddressAdapter = new ShippingAddressAdapter(getContext(), (ArrayList<Address>) addressList) {
                            @Override
                            public void onEditClicked(Address address) {
                                dashboardActivity.showBackButton(true);
                                dashboardActivity.setTitle("");
                                Gson gson = new Gson();
                                Bundle bundle = new Bundle();
                                bundle.putString("address", gson.toJson(address));
                                dashboardActivity.replaceFragment(R.id.fragment_replacer, new AddShippingAddressFragment(), bundle, true, false);
                            }
                        };
                        shippingAddressAdapter.setOnItemClickListener(this);
                        fragmentCheckoutNewBinding.layoutCheckout.recyclerViewAddress.setAdapter(shippingAddressAdapter);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case LOADING:
                    break;
                case ERROR:
                    Toast.makeText(getActivity(), getAllAddressResponseResource.message, Toast.LENGTH_SHORT).show();
                    break;
            }
            fragmentCheckoutNewBinding.progressBar.setVisibility(View.GONE);
        });
    }


    public void callCheckoutApi(String userId, String cartId, String sessionToken) {
        checkoutViewModel.checkout(userId, cartId, sessionToken).observe(this, checkoutResponseResource -> {
            switch (checkoutResponseResource.status) {
                case SUCCESS:
                    paymentModeList = new ArrayList<>();
                    paymentModeList = checkoutResponseResource.data.getCheckoutData().getPaymentModes();
                    paymentModeAdapter = new PaymentModeAdapter(getContext(), (ArrayList<PaymentMode>) paymentModeList);
                    paymentModeAdapter.setOnItemClickListener(this);
                    fragmentCheckoutNewBinding.layoutCheckout.recyclerviewPaymentmode.setAdapter(paymentModeAdapter);
                    fragmentCheckoutNewBinding.layoutCheckout.tvTotalAmount.setText(checkoutResponseResource.data.getCheckoutData().getShoppingCart().getTotal() + "");
                    fragmentCheckoutNewBinding.layoutCheckout.tvTotalPrice.setText("KWD " + checkoutResponseResource.data.getCheckoutData().getShoppingCart().getTotal());
                    checkoutProductListAdapter = new CheckoutProductListAdapter((ArrayList<ShoppingCartItem>) checkoutResponseResource.data.getCheckoutData().getShoppingCart().getShoppingCartItems(), getContext());
                    fragmentCheckoutNewBinding.layoutCheckout.recyclerViewProducts.setAdapter(checkoutProductListAdapter);
                    break;
                case LOADING:
                    break;
                case ERROR:
                    Toast.makeText(getActivity(), checkoutResponseResource.message, Toast.LENGTH_SHORT).show();
                    break;
            }
        });
        fragmentCheckoutNewBinding.progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvAddAddress:
                dashboardActivity.showBackButton(true);
                dashboardActivity.setTitle("");
                dashboardActivity.replaceFragment(R.id.fragment_replacer, new AddShippingAddressFragment(), null, true, false);
                break;
            case R.id.tvPurchaseMore:
                int fragmentCount = getParentFragmentManager().getBackStackEntryCount();
                for (int i = 0; i < fragmentCount; i++) {
                    getParentFragmentManager().popBackStackImmediate();
                }
                break;
            case R.id.tvApplyCoupon:
                break;
            case R.id.tvMakeYourpayment:
                if (selectedPaymentMode == null) {
                    Toast.makeText(dashboardActivity, "Please select any payment mode to continue.", Toast.LENGTH_SHORT).show();
                } else {
                    Gson gson = new Gson();
                    String paymentModeString = gson.toJson(selectedPaymentMode);
                    Bundle bundle = new Bundle();
                    bundle.putString("payment_mode", paymentModeString);
                    dashboardActivity.showBackButton(true);
                    dashboardActivity.setTitle("Payment");
                    dashboardActivity.replaceFragment(R.id.fragment_replacer, new PaymentFragment(), bundle, true, false);

                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClickListener(int position, View view) {
        try {
            selectedPaymentMode = paymentModeList.get(position);
            paymentModeAdapter.selectedItem();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            selectedAddress = addressList.get(position);
            shippingAddressAdapter.selectedItem();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        callGetAddressApi(userId, sessionToken);
        dashboardActivity.setTitle("My Cart");
    }
}
