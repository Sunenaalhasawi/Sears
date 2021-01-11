package com.hasawi.sears.ui.main.view.checkout;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.hasawi.sears.R;
import com.hasawi.sears.data.api.Resource;
import com.hasawi.sears.data.api.model.pojo.Address;
import com.hasawi.sears.data.api.model.pojo.PaymentMode;
import com.hasawi.sears.data.api.model.pojo.ShippingMode;
import com.hasawi.sears.data.api.model.pojo.ShoppingCartItem;
import com.hasawi.sears.data.api.response.CheckoutResponse;
import com.hasawi.sears.databinding.FragmentCheckoutNewBinding;
import com.hasawi.sears.ui.base.BaseFragment;
import com.hasawi.sears.ui.main.adapters.CheckoutProductListAdapter;
import com.hasawi.sears.ui.main.adapters.PaymentModeAdapter;
import com.hasawi.sears.ui.main.adapters.ShippingAddressAdapter;
import com.hasawi.sears.ui.main.adapters.ShippingModeAdapter;
import com.hasawi.sears.ui.main.listeners.RecyclerviewSingleChoiceClickListener;
import com.hasawi.sears.ui.main.view.DashboardActivity;
import com.hasawi.sears.ui.main.viewmodel.CheckoutViewModel;
import com.hasawi.sears.utils.AppConstants;
import com.hasawi.sears.utils.PreferenceHandler;

import java.util.ArrayList;
import java.util.List;

public class CheckoutFragment extends BaseFragment implements View.OnClickListener, RecyclerviewSingleChoiceClickListener {

    FragmentCheckoutNewBinding fragmentCheckoutNewBinding;
    CheckoutViewModel checkoutViewModel;
    String userId, cartId, sessionToken;
    PaymentModeAdapter paymentModeAdapter;
    ShippingAddressAdapter shippingAddressAdapter;
    ShippingModeAdapter shippingModeAdapter;
    CheckoutProductListAdapter checkoutProductListAdapter;
    List<PaymentMode> paymentModeList = new ArrayList<>();
    List<Address> addressList = new ArrayList<>();
    DashboardActivity dashboardActivity;
    private LinearLayoutManager horizontalLayoutManager;
    private PaymentMode selectedPaymentMode;
    private LinearLayoutManager horizontalLayoutManagerAddress;
    private Address selectedAddress;
    List<ShoppingCartItem> shoppingCartItemList = new ArrayList<>();
    List<ShippingMode> shippingModeList = new ArrayList<>();
    ShippingMode selectedShippingMode;
    private boolean hasCouponApplied = false;
    Bundle analyticsBundle = new Bundle();

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
        fragmentCheckoutNewBinding.layoutCheckout.tvRemoveCoupon.setOnClickListener(this);
        dashboardActivity = (DashboardActivity) getActivity();
        dashboardActivity.handleActionMenuBar(true, false, "Checkout");
        dashboardActivity.handleActionBarIcons(false);
        try {
            PreferenceHandler preferenceHandler = new PreferenceHandler(getActivity(), PreferenceHandler.TOKEN_LOGIN);
            userId = preferenceHandler.getData(PreferenceHandler.LOGIN_USER_ID, "");
            sessionToken = preferenceHandler.getData(PreferenceHandler.LOGIN_TOKEN, "");
            cartId = preferenceHandler.getData(PreferenceHandler.LOGIN_USER_CART_ID, "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        callCheckoutApi(userId, cartId, "", sessionToken);
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
                        shippingAddressAdapter = new ShippingAddressAdapter(getContext(), (ArrayList<Address>) addressList, AppConstants.ADDRESS_VIEW_TYPE_CHECKOUT) {
                            @Override
                            public void onEditClicked(Address address) {
                                dashboardActivity.handleActionMenuBar(true, false, "");
                                Gson gson = new Gson();
                                Bundle bundle = new Bundle();
                                bundle.putString("address", gson.toJson(address));
                                dashboardActivity.replaceFragment(R.id.fragment_replacer, new AddShippingAddressFragment(), bundle, true, false);
                            }

                            @Override
                            public void onDeleteClicked(Address address) {
                                callDeleteAddressApi(address);
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

    private void callDeleteAddressApi(Address address) {
        fragmentCheckoutNewBinding.progressBar.setVisibility(View.VISIBLE);
        checkoutViewModel.deleteAddress(address.getAddressId(), sessionToken).observe(this, deleteAddressResponseResource -> {
            switch (deleteAddressResponseResource.status) {
                case SUCCESS:
                    Toast.makeText(dashboardActivity, "Deleted Address Successfully", Toast.LENGTH_SHORT).show();
                    break;
                case LOADING:
                    break;
                case ERROR:
                    Toast.makeText(getActivity(), deleteAddressResponseResource.message, Toast.LENGTH_SHORT).show();
                    break;
            }
            fragmentCheckoutNewBinding.progressBar.setVisibility(View.GONE);
        });

    }


    public void callCheckoutApi(String userId, String cartId, String couponCode, String sessionToken) {
        fragmentCheckoutNewBinding.progressBar.setVisibility(View.VISIBLE);
        checkoutViewModel.checkout(userId, cartId, couponCode, sessionToken).observe(this, checkoutResponseResource -> {
            switch (checkoutResponseResource.status) {
                case SUCCESS:
                    try {
                        if (checkoutResponseResource.data.getCheckoutData().getShoppingCart().getCouponCode() == null) {
                            hasCouponApplied = false;
                            fragmentCheckoutNewBinding.layoutCheckout.tvRemoveCoupon.setVisibility(View.GONE);
                            fragmentCheckoutNewBinding.layoutCheckout.edtCouponCode.setText("");
                            fragmentCheckoutNewBinding.layoutCheckout.tvApplyCoupon.setVisibility(View.VISIBLE);

                        } else {
                            hasCouponApplied = true;
                            fragmentCheckoutNewBinding.layoutCheckout.tvRemoveCoupon.setVisibility(View.VISIBLE);
                            fragmentCheckoutNewBinding.layoutCheckout.tvApplyCoupon.setVisibility(View.GONE);
                            dashboardActivity.showMessageToast("Coupon Applied", Toast.LENGTH_SHORT);
                            logCouponAppliedEvent(checkoutResponseResource);

                        }
                        fragmentCheckoutNewBinding.layoutCheckout.edtCouponCode.clearFocus();

                        shoppingCartItemList = checkoutResponseResource.data.getCheckoutData().getShoppingCart().getShoppingCartItems();
                        paymentModeList = new ArrayList<>();
                        paymentModeList = checkoutResponseResource.data.getCheckoutData().getPaymentModes();
                        paymentModeAdapter = new PaymentModeAdapter(getContext(), (ArrayList<PaymentMode>) paymentModeList);
                        paymentModeAdapter.setOnItemClickListener(this);
                        shippingModeList = checkoutResponseResource.data.getCheckoutData().getShippingModes();
                        setShippingModeAdapter(shippingModeList);
                        fragmentCheckoutNewBinding.layoutCheckout.recyclerviewPaymentmode.setAdapter(paymentModeAdapter);
                        fragmentCheckoutNewBinding.layoutCheckout.tvDiscountAmount.setText("KWD " + checkoutResponseResource.data.getCheckoutData().getShoppingCart().getDiscountedAmount());
                        fragmentCheckoutNewBinding.layoutCheckout.tvItemCount.setText("Bag " + checkoutResponseResource.data.getCheckoutData().getShoppingCart().getAvailable().size());
                        fragmentCheckoutNewBinding.layoutCheckout.tvtotal.setText(checkoutResponseResource.data.getCheckoutData().getShoppingCart().getTotal() + "");
                        fragmentCheckoutNewBinding.layoutCheckout.tvSubTotalAmount.setText("KWD " + checkoutResponseResource.data.getCheckoutData().getShoppingCart().getSubTotal());
                        checkoutProductListAdapter = new CheckoutProductListAdapter((ArrayList<ShoppingCartItem>) checkoutResponseResource.data.getCheckoutData().getShoppingCart().getShoppingCartItems(), getContext());
                        fragmentCheckoutNewBinding.layoutCheckout.recyclerViewProducts.setAdapter(checkoutProductListAdapter);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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

    private void logCouponAppliedEvent(Resource<CheckoutResponse> checkoutResponseResource) {
        analyticsBundle.putString(FirebaseAnalytics.Param.COUPON, checkoutResponseResource.data.getCheckoutData().getShoppingCart().getCouponCode());
        analyticsBundle.putString(FirebaseAnalytics.Param.CURRENCY, "KWD");
        analyticsBundle.putDouble(FirebaseAnalytics.Param.VALUE, checkoutResponseResource.data.getCheckoutData().getShoppingCart().getSubTotal());
        Bundle couponBundle = new Bundle();
        couponBundle.putString(FirebaseAnalytics.Param.COUPON, checkoutResponseResource.data.getCheckoutData().getShoppingCart().getCouponCode());
        dashboardActivity.getmFirebaseAnalytics().logEvent("COUPON_APPLIED", analyticsBundle);
    }

    private void setShippingModeAdapter(List<ShippingMode> shippingModeList) {
        LinearLayoutManager horizontalLayoutManagerShippingMode = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        fragmentCheckoutNewBinding.layoutCheckout.recyclerViewShippingMode.setLayoutManager(horizontalLayoutManagerShippingMode);
        shippingModeAdapter = new ShippingModeAdapter(getContext(), (ArrayList<ShippingMode>) shippingModeList);
        shippingModeAdapter.setOnItemClickListener(this);
        fragmentCheckoutNewBinding.layoutCheckout.recyclerViewShippingMode.setAdapter(shippingModeAdapter);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvAddAddress:
                dashboardActivity.handleActionMenuBar(true, false, "");
                dashboardActivity.replaceFragment(R.id.fragment_replacer, new AddShippingAddressFragment(), null, true, false);
                break;
            case R.id.tvPurchaseMore:
                dashboardActivity.handleActionMenuBar(false, true, "");
                int fragmentCount = getParentFragmentManager().getBackStackEntryCount();
                for (int i = 0; i < fragmentCount; i++) {
                    getParentFragmentManager().popBackStackImmediate();
                }
                break;
            case R.id.tvApplyCoupon:
                String couponCode = fragmentCheckoutNewBinding.layoutCheckout.edtCouponCode.getText().toString();
                if (couponCode.equals(""))
                    Toast.makeText(dashboardActivity, "Invalid Coupon code", Toast.LENGTH_SHORT).show();
                else
                    callCheckoutApi(userId, cartId, couponCode, sessionToken);
                break;
            case R.id.tvRemoveCoupon:
                dashboardActivity.showMessageToast("Coupon Removed", Toast.LENGTH_SHORT);
                callCheckoutApi(userId, cartId, "", sessionToken);
                break;
            case R.id.tvMakeYourpayment:
                logCheckoutEvent();
                if (selectedPaymentMode == null || selectedAddress == null || selectedShippingMode == null) {
                    if (selectedPaymentMode == null)
                        Toast.makeText(dashboardActivity, "Please select any payment mode to proceed.", Toast.LENGTH_SHORT).show();
                    if (selectedAddress == null) {
                        Toast.makeText(dashboardActivity, "Please select any address or add new address to proceed.", Toast.LENGTH_SHORT).show();
                    }
                    if (selectedShippingMode == null) {
                        Toast.makeText(dashboardActivity, "Please select any shipping mode to proceed", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    PreferenceHandler preferenceHandler = new PreferenceHandler(getContext(), PreferenceHandler.TOKEN_LOGIN);
                    preferenceHandler.saveData(PreferenceHandler.LOGIN_USER_SELECTED_ADDRESS_ID, selectedAddress.getAddressId());
                    preferenceHandler.saveData(PreferenceHandler.LOGIN_USER_SELECTED_SHIPPING_MODE_ID, selectedShippingMode.getShippingId());
                    Gson gson = new Gson();
                    String paymentModeString = gson.toJson(selectedPaymentMode);
                    Bundle bundle = new Bundle();
                    bundle.putString("payment_mode", paymentModeString);
                    dashboardActivity.handleActionMenuBar(true, false, "Payment");
                    dashboardActivity.replaceFragment(R.id.fragment_replacer, new PaymentFragment(), bundle, true, false);

                }
                break;
            default:
                break;
        }

    }

    private void logCheckoutEvent() {
        ArrayList<Bundle> itemBundles = new ArrayList<>();
        for (int i = 0; i < shoppingCartItemList.size(); i++) {
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, shoppingCartItemList.get(i).getProduct().getDescriptions().get(0).getProductName());
            try {
                bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, shoppingCartItemList.get(i).getProduct().getCategories().get(0).getDescriptions().get(0).getCategoryName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            itemBundles.add(bundle);

        }
        analyticsBundle.putParcelableArrayList(FirebaseAnalytics.Param.ITEMS, itemBundles);
        dashboardActivity.getmFirebaseAnalytics().logEvent(FirebaseAnalytics.Event.BEGIN_CHECKOUT, analyticsBundle);
    }

    @Override
    public void onItemClickListener(int position, View view) {
        switch (view.getId()) {
            case R.id.lv_payment_mode_cod:
                selectedPaymentMode = paymentModeList.get(position);
                paymentModeAdapter.selectedItem();
                break;
            case R.id.cv_background_address:
                selectedAddress = addressList.get(position);
                shippingAddressAdapter.selectedItem();
                break;
            case R.id.cv_background_shipping_mode:
                shippingModeAdapter.selectedItem();
                selectedShippingMode = shippingModeList.get(position);
                break;
            default:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        callGetAddressApi(userId, sessionToken);
        ShippingModeAdapter.setsSelected(-1);
        ShippingAddressAdapter.setsSelected(-1);
        PaymentModeAdapter.setsSelected(-1);
        dashboardActivity.setTitle("Checkout");
    }
}
