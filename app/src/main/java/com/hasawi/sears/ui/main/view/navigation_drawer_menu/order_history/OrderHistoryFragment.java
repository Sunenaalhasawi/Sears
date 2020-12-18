package com.hasawi.sears.ui.main.view.navigation_drawer_menu.order_history;

import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.hasawi.sears.R;
import com.hasawi.sears.data.api.model.pojo.Order;
import com.hasawi.sears.databinding.FragmentOrderHistoryBinding;
import com.hasawi.sears.ui.base.BaseFragment;
import com.hasawi.sears.ui.main.adapters.OrderHistoryAdapter;
import com.hasawi.sears.ui.main.viewmodel.OrderHistoryViewModel;
import com.hasawi.sears.utils.PreferenceHandler;

import java.util.ArrayList;

public class OrderHistoryFragment extends BaseFragment {
    FragmentOrderHistoryBinding fragmentOrderHistoryBinding;
    OrderHistoryViewModel orderHistoryViewModel;
    ArrayList<Order> orderArrayList = new ArrayList<>();
    OrderHistoryAdapter orderHistoryAdapter;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_order_history;
    }

    @Override
    protected void setup() {
        fragmentOrderHistoryBinding = (FragmentOrderHistoryBinding) viewDataBinding;
        orderHistoryViewModel = new ViewModelProvider(this).get(OrderHistoryViewModel.class);
        PreferenceHandler preferenceHandler = new PreferenceHandler(getActivity(), PreferenceHandler.TOKEN_LOGIN);
        String userId = preferenceHandler.getData(PreferenceHandler.LOGIN_USER_ID, "");
        String sessionToken = preferenceHandler.getData(PreferenceHandler.LOGIN_TOKEN, "");
        fragmentOrderHistoryBinding.recyclerviewOrders.setLayoutManager(new LinearLayoutManager(getActivity()));
        callOrderHistoryApi(userId, sessionToken);
    }

    private void callOrderHistoryApi(String userId, String sessionToken) {
        fragmentOrderHistoryBinding.progressBar.setVisibility(View.VISIBLE);
        orderHistoryViewModel.getOrderHistory(userId, sessionToken).observe(this, orderHistoryResponseResource -> {
            switch (orderHistoryResponseResource.status) {
                case SUCCESS:
                    if (orderHistoryResponseResource.data != null) {
                        orderArrayList = (ArrayList<Order>) orderHistoryResponseResource.data.getOrders();
                        orderHistoryAdapter = new OrderHistoryAdapter(orderArrayList, getActivity());
                        fragmentOrderHistoryBinding.recyclerviewOrders.setAdapter(orderHistoryAdapter);
                    }
                    break;
                case LOADING:
                    break;
                case ERROR:
                    Toast.makeText(getActivity(), orderHistoryResponseResource.message, Toast.LENGTH_SHORT).show();
                    break;
            }
            fragmentOrderHistoryBinding.progressBar.setVisibility(View.GONE);
        });

    }
}
