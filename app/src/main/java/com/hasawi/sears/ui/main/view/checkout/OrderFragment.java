package com.hasawi.sears.ui.main.view.checkout;

import android.os.Bundle;

import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.hasawi.sears.R;
import com.hasawi.sears.data.api.model.pojo.Address;
import com.hasawi.sears.data.api.model.pojo.OrderProduct;
import com.hasawi.sears.data.api.model.pojo.OrderTrack;
import com.hasawi.sears.data.api.response.OrderResponse;
import com.hasawi.sears.databinding.FragmentOrderReviewBinding;
import com.hasawi.sears.ui.base.BaseFragment;
import com.hasawi.sears.ui.main.view.DashboardActivity;
import com.hasawi.sears.ui.main.viewmodel.OrderViewModel;
import com.hasawi.sears.utils.DateTimeUtils;

import java.util.ArrayList;
import java.util.List;

public class OrderFragment extends BaseFragment {
    FragmentOrderReviewBinding fragmentOrderReviewBinding;
    OrderViewModel orderViewModel;
    DashboardActivity dashboardActivity;
    OrderResponse orderConfirmedResponse;
    String orderedDate = "";

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_order_review;
    }

    @Override
    protected void setup() {
        fragmentOrderReviewBinding = (FragmentOrderReviewBinding) viewDataBinding;
        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);
        dashboardActivity = (DashboardActivity) getActivity();
        try {
            dashboardActivity.setTitle("Order Confirmation");
            Bundle bundle = getArguments();
            String orderResponse = bundle.getString("order_confirmed_response");
            Gson gson = new Gson();
            orderConfirmedResponse = gson.fromJson(orderResponse, OrderResponse.class);
            try {
                fragmentOrderReviewBinding.layoutOrderConfirmation.tvOrderIdTop.setText(orderConfirmedResponse.getOrderData().getOrderId());
                fragmentOrderReviewBinding.layoutOrderConfirmation.tvOrderId.setText(orderConfirmedResponse.getOrderData().getOrderId());
//                fragmentOrderReviewBinding.layoutOrderConfirmation.tvName.setText(orderConfirmedResponse.getOrderData().get().getCustomerName());
                fragmentOrderReviewBinding.layoutOrderConfirmation.tvPaymentMode.setText(orderConfirmedResponse.getOrderData().getPayment().getName());
                try {
                    orderedDate = DateTimeUtils.changeDateFormatFromAnother(orderConfirmedResponse.getOrderData().getDateOfPurchase());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                fragmentOrderReviewBinding.layoutOrderConfirmation.tvOrderedDate.setText(orderedDate);
                fragmentOrderReviewBinding.layoutOrderConfirmation.tvPurchaseDate.setText(orderedDate);
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
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        dashboardActivity.handleActionMenuBar(false, true, "");
        int fragmentCount = getParentFragmentManager().getBackStackEntryCount();
        for (int i = 0; i < fragmentCount; i++) {
            getParentFragmentManager().popBackStackImmediate();
        }
    }

    private void logPurchaseEvent() {
        ArrayList<Bundle> itemParcelableList = new ArrayList<>();
        List<OrderProduct> orderProductList = orderConfirmedResponse.getOrderData().getOrderProducts();
        for (int i = 0; i < orderProductList.size(); i++) {
            Bundle itemBundle = new Bundle();
            itemBundle.putString(FirebaseAnalytics.Param.ITEM_NAME, orderProductList.get(i).getProductName());
            itemBundle.putLong(FirebaseAnalytics.Param.QUANTITY, orderProductList.get(i).getQuantity());
            itemBundle.putDouble(FirebaseAnalytics.Param.VALUE, orderProductList.get(i).getOneTimePrice());
            itemBundle.putString(FirebaseAnalytics.Param.CURRENCY, "KWD");
            itemParcelableList.add(itemBundle);
        }

        Bundle analyticsBundle = new Bundle();
        analyticsBundle.putString(FirebaseAnalytics.Param.CURRENCY, "KWD");
        analyticsBundle.putDouble(FirebaseAnalytics.Param.VALUE, orderConfirmedResponse.getOrderData().getTotal());
        analyticsBundle.putString(FirebaseAnalytics.Param.COUPON, orderConfirmedResponse.getOrderData().getCouponCode());
        analyticsBundle.putString(FirebaseAnalytics.Param.SHIPPING, orderConfirmedResponse.getOrderData().getShippingId());
        analyticsBundle.putParcelableArrayList(FirebaseAnalytics.Param.ITEMS, itemParcelableList);
        dashboardActivity.getmFirebaseAnalytics().logEvent(FirebaseAnalytics.Event.PURCHASE, analyticsBundle);
    }
}
