package com.hasawi.sears.ui.main.view.checkout;

import android.os.Bundle;
import android.util.ArrayMap;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;

import com.facebook.appevents.AppEventsConstants;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.hasawi.sears.R;
import com.hasawi.sears.data.api.model.pojo.Address;
import com.hasawi.sears.data.api.model.pojo.OrderProduct;
import com.hasawi.sears.data.api.model.pojo.OrderTrack;
import com.hasawi.sears.data.api.model.pojo.PaymentMode;
import com.hasawi.sears.data.api.response.OrderResponse;
import com.hasawi.sears.databinding.FragmentOrderReviewBinding;
import com.hasawi.sears.ui.base.BaseFragment;
import com.hasawi.sears.ui.main.view.DashboardActivity;
import com.hasawi.sears.ui.main.viewmodel.OrderViewModel;
import com.hasawi.sears.utils.DateTimeUtils;
import com.hasawi.sears.utils.PreferenceHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrderFragment extends BaseFragment {
    FragmentOrderReviewBinding fragmentOrderReviewBinding;
    OrderViewModel orderViewModel;
    DashboardActivity dashboardActivity;
    OrderResponse orderConfirmedResponse;
    String orderedDate = "";
    String paymentId = null, paymentModeString = "";
    PaymentMode selectedPaymentMode;


    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_order_review;
    }

    @Override
    protected void setup() {
        fragmentOrderReviewBinding = (FragmentOrderReviewBinding) viewDataBinding;
        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);
        dashboardActivity = (DashboardActivity) getActivity();
        dashboardActivity.handleActionMenuBar(false, true, "Order Confirmation");
        dashboardActivity.handleActionBarIcons(false);
        try {
            Bundle bundle = getArguments();
            paymentModeString = bundle.getString("payment");
            paymentId = bundle.getString("payment_id");
            String paymentUrl = bundle.getString("payment_url");
            Gson gson = new Gson();
            selectedPaymentMode = gson.fromJson(paymentModeString, PaymentMode.class);
            if (selectedPaymentMode != null) {
                if (selectedPaymentMode.isPostPay()) {
                    callReviewOrderApi();
                } else {
                    callPaymentSuccessApi(paymentUrl);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setUiValues() {
        try {
            fragmentOrderReviewBinding.layoutOrderConfirmation.tvOrderIdTop.setText(orderConfirmedResponse.getOrderData().getOrderId());
            fragmentOrderReviewBinding.layoutOrderConfirmation.tvOrderId.setText(orderConfirmedResponse.getOrderData().getOrderId());
            fragmentOrderReviewBinding.layoutOrderConfirmation.tvPaymentMode.setText(orderConfirmedResponse.getOrderData().getPayment().getName());
            try {
                orderedDate = DateTimeUtils.changeDateFormatFromAnother(orderConfirmedResponse.getOrderData().getDateOfPurchase());
            } catch (Exception e) {
                e.printStackTrace();
            }

            fragmentOrderReviewBinding.layoutOrderConfirmation.tvOrderedDate.setText(orderedDate);
            fragmentOrderReviewBinding.layoutOrderConfirmation.tvPurchaseDate.setText(orderedDate);
            fragmentOrderReviewBinding.layoutOrderConfirmation.tvName.setText(orderConfirmedResponse.getOrderData().getCustomerName());
            Address shippingAddress = orderConfirmedResponse.getOrderData().getAddress();
            fragmentOrderReviewBinding.layoutOrderConfirmation.tvStreetAddress.setText(shippingAddress.getStreet());
            fragmentOrderReviewBinding.layoutOrderConfirmation.tvRuralAddress.setText(shippingAddress.getFlat() + " " + shippingAddress.getBlock());
            fragmentOrderReviewBinding.layoutOrderConfirmation.tvLandmark.setText(shippingAddress.getArea());

            List<OrderTrack> orderTrackList = orderConfirmedResponse.getOrderData().getOrderTrackList();
            for (int i = 0; i < orderTrackList.size(); i++) {
                OrderTrack orderTrackItem = orderTrackList.get(i);
                if (orderTrackItem.getSortOrder() == 1) {
                    fragmentOrderReviewBinding.layoutOrderConfirmation.radioButtonOrdered.setChecked(true);
                    fragmentOrderReviewBinding.layoutOrderConfirmation.tvOrderedDate.setText(orderedDate);
                }
            }
            fragmentOrderReviewBinding.layoutOrderConfirmation.tvPaymentDate.setText(orderConfirmedResponse.getOrderData().getGetPaymentStatusResponse().getInvoiceTransactions().get(0).getTransactionDate());
            fragmentOrderReviewBinding.layoutOrderConfirmation.tvPaymentAmount.setText("KWD " + orderConfirmedResponse.getOrderData().getTotal());
            fragmentOrderReviewBinding.layoutOrderConfirmation.tvReferenceNo.setText(orderConfirmedResponse.getOrderData().getGetPaymentStatusResponse().getInvoiceTransactions().get(0).getReferenceId());
            fragmentOrderReviewBinding.layoutOrderConfirmation.tvTransactionId.setText(orderConfirmedResponse.getOrderData().getGetPaymentStatusResponse().getInvoiceTransactions().get(0).getTransactionId());
            logPurchaseEvent();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void logPurchaseEvent() throws JSONException {
        ArrayList<Bundle> itemParcelableList = new ArrayList<>();
        JSONArray productJsonArray = new JSONArray();
        List<OrderProduct> orderProductList = orderConfirmedResponse.getOrderData().getOrderProducts();
        for (int i = 0; i < orderProductList.size(); i++) {
            Bundle itemBundle = new Bundle();
            itemBundle.putString(FirebaseAnalytics.Param.ITEM_NAME, orderProductList.get(i).getProductName());
            itemBundle.putLong(FirebaseAnalytics.Param.QUANTITY, orderProductList.get(i).getQuantity());
            itemBundle.putDouble(FirebaseAnalytics.Param.VALUE, orderProductList.get(i).getOneTimePrice());
            itemBundle.putString(FirebaseAnalytics.Param.CURRENCY, "KWD");
            itemParcelableList.add(itemBundle);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("product_name", orderProductList.get(i).getProductName());
            jsonObject.put("product_quantity", orderProductList.get(i).getQuantity());
            jsonObject.put("product_id", orderProductList.get(i).getOrderProductId());
            jsonObject.put("product_price", orderProductList.get(i).getAmount());
            productJsonArray.put(jsonObject);

        }

        Bundle analyticsBundle = new Bundle();
        analyticsBundle.putString(FirebaseAnalytics.Param.CURRENCY, "KWD");
        analyticsBundle.putDouble(FirebaseAnalytics.Param.VALUE, orderConfirmedResponse.getOrderData().getTotal());
        analyticsBundle.putString(FirebaseAnalytics.Param.COUPON, orderConfirmedResponse.getOrderData().getCouponCode());
        analyticsBundle.putString(FirebaseAnalytics.Param.SHIPPING, orderConfirmedResponse.getOrderData().getShippingId());
        analyticsBundle.putParcelableArrayList(FirebaseAnalytics.Param.ITEMS, itemParcelableList);
        analyticsBundle.putString("date_of_purchase", orderConfirmedResponse.getOrderData().getDateOfPurchase());
        analyticsBundle.putString("order_id", orderConfirmedResponse.getOrderData().getOrderId());
        dashboardActivity.getmFirebaseAnalytics().logEvent(FirebaseAnalytics.Event.PURCHASE, analyticsBundle);

        // Logging events to facebook analytics
        Gson gson = new Gson();
        String productString = gson.toJson(productJsonArray);
        Bundle facebookParams = new Bundle();
        facebookParams.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, "product");
        facebookParams.putString(AppEventsConstants.EVENT_PARAM_CONTENT_ID, orderConfirmedResponse.getOrderData().getOrderId());
        facebookParams.putString(AppEventsConstants.EVENT_PARAM_CONTENT, productString);
        facebookParams.putString("coupon", orderConfirmedResponse.getOrderData().getCouponCode());
        facebookParams.putString("shipping", orderConfirmedResponse.getOrderData().getShippingId());
        facebookParams.putString(AppEventsConstants.EVENT_PARAM_CURRENCY, "KWD");
        facebookParams.putDouble("purchase_amount", orderConfirmedResponse.getOrderData().getTotal());
        facebookParams.putString("date_of_purchase", orderConfirmedResponse.getOrderData().getDateOfPurchase());
        dashboardActivity.getFacebookEventsLogger().logEvent("PURCHASE", facebookParams);
    }

    private void callPaymentSuccessApi(String url) {
        fragmentOrderReviewBinding.progressBar.setVisibility(View.VISIBLE);
        orderViewModel.getPaymentSuccess(url).observe(getActivity(), paymentResponseResource -> {
            fragmentOrderReviewBinding.progressBar.setVisibility(View.GONE);
            switch (paymentResponseResource.status) {
                case SUCCESS:
                    paymentId = paymentResponseResource.data.getData().getInvoiceTransactions().get(0).getPaymentId();
                    try {
                        callReviewOrderApi();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case LOADING:
                    break;
                case ERROR:
                    try {
                        Toast.makeText(dashboardActivity, paymentResponseResource.message, Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        });
    }

    private void callReviewOrderApi() throws JSONException {
        JSONObject paymentJson = new JSONObject(paymentModeString);
        Map<String, Object> jsonParams = new ArrayMap<>();
        jsonParams.put("payment", paymentJson);
        jsonParams.put("paymentId", paymentId);
        fragmentOrderReviewBinding.progressBar.setVisibility(View.VISIBLE);
        PreferenceHandler preferenceHandler = new PreferenceHandler(getContext(), PreferenceHandler.TOKEN_LOGIN);
        String customerId = preferenceHandler.getData(PreferenceHandler.LOGIN_USER_ID, "");
        String addressId = preferenceHandler.getData(PreferenceHandler.LOGIN_USER_SELECTED_ADDRESS_ID, "");
        String shippingModeId = preferenceHandler.getData(PreferenceHandler.LOGIN_USER_SELECTED_SHIPPING_MODE_ID, "");
        jsonParams.put("shippingId", shippingModeId);
        String cartId = preferenceHandler.getData(PreferenceHandler.LOGIN_USER_CART_ID, "");
        String sessionToken = preferenceHandler.getData(PreferenceHandler.LOGIN_TOKEN, "");

        orderViewModel.orderConfirmation(customerId, addressId, cartId, sessionToken, jsonParams).observe(getActivity(), orderResponseResource -> {
            fragmentOrderReviewBinding.progressBar.setVisibility(View.GONE);
            switch (orderResponseResource.status) {
                case SUCCESS:
                    orderConfirmedResponse = orderResponseResource.data;
                    setUiValues();
                    break;
                case LOADING:
                    break;
                case ERROR:
                    try {
                        Toast.makeText(dashboardActivity, orderResponseResource.message, Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        });

    }
}
