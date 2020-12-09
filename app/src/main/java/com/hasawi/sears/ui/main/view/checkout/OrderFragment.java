package com.hasawi.sears.ui.main.view.checkout;

import android.os.Bundle;

import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;
import com.hasawi.sears.R;
import com.hasawi.sears.data.api.response.OrderResponse;
import com.hasawi.sears.databinding.FragmentOrderReviewBinding;
import com.hasawi.sears.ui.base.BaseFragment;
import com.hasawi.sears.ui.main.view.DashboardActivity;
import com.hasawi.sears.ui.main.viewmodel.OrderViewModel;
import com.hasawi.sears.utils.DateTimeUtils;

public class OrderFragment extends BaseFragment {
    FragmentOrderReviewBinding fragmentOrderReviewBinding;
    OrderViewModel orderViewModel;
    DashboardActivity dashboardActivity;
    OrderResponse orderConfirmedResponse;

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
                fragmentOrderReviewBinding.layoutOrderConfirmation.tvOrderIdTop.setText(orderConfirmedResponse.getData().getOrderId());
                fragmentOrderReviewBinding.layoutOrderConfirmation.tvOrderId.setText(orderConfirmedResponse.getData().getOrderId());
                fragmentOrderReviewBinding.layoutOrderConfirmation.tvName.setText(orderConfirmedResponse.getData().getInvoiceData().getCustomerName());
                fragmentOrderReviewBinding.layoutOrderConfirmation.tvPaymentMode.setText(orderConfirmedResponse.getData().getPayment().getName());
                String orderedDate = DateTimeUtils.changeDateFormat(DateTimeUtils.FORMAT_D_M_Y, DateTimeUtils.FORMAT_ORDER_STATUS, orderConfirmedResponse.getData().getDateOfPurchase());
                fragmentOrderReviewBinding.layoutOrderConfirmation.tvOrderedDate.setText(orderedDate);
                fragmentOrderReviewBinding.layoutOrderConfirmation.tvPurchaseDate.setText(orderConfirmedResponse
                        .getData().getDateOfPurchase());
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
        dashboardActivity.showBackButton(false, true);
        int fragmentCount = getParentFragmentManager().getBackStackEntryCount();
        for (int i = 0; i < fragmentCount; i++) {
            getParentFragmentManager().popBackStackImmediate();
        }
    }
}
