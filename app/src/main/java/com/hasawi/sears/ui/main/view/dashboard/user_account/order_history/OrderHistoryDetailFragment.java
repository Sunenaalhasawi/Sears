package com.hasawi.sears.ui.main.view.dashboard.user_account.order_history;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.hasawi.sears.R;
import com.hasawi.sears.data.api.model.pojo.Order;
import com.hasawi.sears.databinding.FragmentOrderDetailsBinding;
import com.hasawi.sears.ui.base.BaseFragment;
import com.hasawi.sears.ui.main.adapters.OrderedProductsAdapter;
import com.hasawi.sears.ui.main.view.DashboardActivity;
import com.hasawi.sears.utils.PreferenceHandler;

public class OrderHistoryDetailFragment extends BaseFragment {
    FragmentOrderDetailsBinding fragmentOrderDetailsBinding;
    Order selectedOrder;
    OrderedProductsAdapter orderedProductsAdapter;
    DashboardActivity dashboardActivity;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_order_details;
    }

    @Override
    protected void setup() {
        fragmentOrderDetailsBinding = (FragmentOrderDetailsBinding) viewDataBinding;
        dashboardActivity = (DashboardActivity) getActivity();
        dashboardActivity.handleActionBarIcons(false);
        try {
            Bundle bundle = getArguments();
            String orderObject = bundle.getString("order_object");
            Gson gson = new Gson();
            selectedOrder = gson.fromJson(orderObject, Order.class);
            if (selectedOrder != null) {
                if (selectedOrder.getOrderId() != null)
                    fragmentOrderDetailsBinding.tvOrderId.setText(selectedOrder.getOrderId());
                fragmentOrderDetailsBinding.tvTrackOrder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle1 = new Bundle();
                        bundle1.putString("order_object", orderObject);
                        dashboardActivity.replaceFragment(R.id.fragment_replacer, new TrackOrderFragment(), bundle1, true, false);
                    }
                });
//                fragmentOrderDetailsBinding.tvStreetAddress.setText(selectedOrder.getAddress().getStreet());
//                fragmentOrderDetailsBinding.tvRuralAddress.setText(selectedOrder.getAddress().getFlat() + " " + selectedOrder.getAddress().getBlock());
//                fragmentOrderDetailsBinding.tvLandmark.setText(selectedOrder.getAddress().getArea());
                if (selectedOrder.getPayment() != null)
                    fragmentOrderDetailsBinding.tvPaymentMode.setText(selectedOrder.getPayment().getName());
                if (selectedOrder.getTotal() != null)
                    fragmentOrderDetailsBinding.tvOrderPrice.setText("KWD " + selectedOrder.getTotal());
                if (selectedOrder.getDiscounted() != null)
                    fragmentOrderDetailsBinding.tvSavedAmount.setText("KWD " + selectedOrder.getDiscounted());
                PreferenceHandler preferenceHandler = new PreferenceHandler(getContext(), PreferenceHandler.TOKEN_LOGIN);
                String name = preferenceHandler.getData(PreferenceHandler.LOGIN_USERNAME, "");
                String mobile = preferenceHandler.getData(PreferenceHandler.LOGIN_PHONENUMBER, "");
                String email = preferenceHandler.getData(PreferenceHandler.LOGIN_EMAIL, "");
                fragmentOrderDetailsBinding.tvName.setText(name);
                fragmentOrderDetailsBinding.tvMailId.setText(email);
                fragmentOrderDetailsBinding.tvMobile.setText(mobile);

                fragmentOrderDetailsBinding.recyclerviewProducts.setLayoutManager(new LinearLayoutManager(getActivity()));
                orderedProductsAdapter = new OrderedProductsAdapter(selectedOrder, getActivity());
                fragmentOrderDetailsBinding.recyclerviewProducts.setAdapter(orderedProductsAdapter);

                fragmentOrderDetailsBinding.progressBar.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
