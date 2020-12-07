package com.hasawi.sears.ui.main.view.checkout;

import android.os.Bundle;
import android.util.ArrayMap;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;

import com.hasawi.sears.R;
import com.hasawi.sears.databinding.FragmentOrderReviewBinding;
import com.hasawi.sears.ui.base.BaseFragment;
import com.hasawi.sears.ui.main.viewmodel.OrderViewModel;
import com.hasawi.sears.utils.DateTimeUtils;
import com.hasawi.sears.utils.PreferenceHandler;

import org.json.JSONObject;

import java.util.Map;

public class OrderFragment extends BaseFragment {
    FragmentOrderReviewBinding fragmentOrderReviewBinding;
    OrderViewModel orderViewModel;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_order_review;
    }

    @Override
    protected void setup() {
        fragmentOrderReviewBinding = (FragmentOrderReviewBinding) viewDataBinding;
        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);
        try {
            Bundle bundle = getArguments();
            String payment = bundle.getString("payment");
            String getPaymentStatusResponse = bundle.getString("getPaymentStatusResponse");
            JSONObject invoiceData = new JSONObject(getPaymentStatusResponse);
            Map<String, Object> jsonParams = new ArrayMap<>();
            jsonParams.put("payment", payment);
            jsonParams.put("getPaymentStatusResponse", invoiceData);
            callReviewOrderApi(jsonParams);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void callReviewOrderApi(Map<String, Object> jsonParams) {
        PreferenceHandler preferenceHandler = new PreferenceHandler(getContext(), PreferenceHandler.TOKEN_LOGIN);
        String customerId = preferenceHandler.getData(PreferenceHandler.LOGIN_USER_ID, "");
        String addressId = "ADRE_00008";
        String cartId = preferenceHandler.getData(PreferenceHandler.LOGIN_USER_CART_ID, "");
        String sessionToken = preferenceHandler.getData(PreferenceHandler.LOGIN_TOKEN, "");

        orderViewModel.orderConfirmation(customerId, addressId, cartId, sessionToken, jsonParams).observe(getActivity(), orderResponseResource -> {
            fragmentOrderReviewBinding.progressBar.setVisibility(View.GONE);
            switch (orderResponseResource.status) {
                case SUCCESS:
                    try {
                        fragmentOrderReviewBinding.tvOrderId.setText(orderResponseResource.data.getData().getOrderId());
                        fragmentOrderReviewBinding.tvName.setText(orderResponseResource.data.getData().getInvoiceData().getCustomerName());
                        String purchaseDate = DateTimeUtils.changeDateFormat(DateTimeUtils.FORMAT_D_M_Y, DateTimeUtils.FORMAT_ORDER_STATUS, orderResponseResource.data.getData().getDateOfPurchase());
                        fragmentOrderReviewBinding.tvOrderedDate.setText(purchaseDate);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case LOADING:
                    break;
                case ERROR:
                    Toast.makeText(getActivity(), orderResponseResource.message, Toast.LENGTH_SHORT).show();
                    break;
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        int fragmentCount = getParentFragmentManager().getBackStackEntryCount();
        for (int i = 0; i < fragmentCount; i++) {
            getParentFragmentManager().popBackStackImmediate();
        }
    }
}
