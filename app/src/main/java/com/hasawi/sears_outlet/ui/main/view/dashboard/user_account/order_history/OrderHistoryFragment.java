package com.hasawi.sears_outlet.ui.main.view.dashboard.user_account.order_history;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.hasawi.sears_outlet.R;
import com.hasawi.sears_outlet.data.api.model.pojo.Order;
import com.hasawi.sears_outlet.databinding.FragmentOrderHistoryBinding;
import com.hasawi.sears_outlet.ui.base.BaseFragment;
import com.hasawi.sears_outlet.ui.main.adapters.OrderHistoryAdapter;
import com.hasawi.sears_outlet.ui.main.listeners.RecyclerItemClickListener;
import com.hasawi.sears_outlet.ui.main.view.DashboardActivity;
import com.hasawi.sears_outlet.ui.main.viewmodel.OrderHistoryViewModel;
import com.hasawi.sears_outlet.utils.PreferenceHandler;

import java.util.ArrayList;

public class OrderHistoryFragment extends BaseFragment {
    FragmentOrderHistoryBinding fragmentOrderHistoryBinding;
    OrderHistoryViewModel orderHistoryViewModel;
    ArrayList<Order> orderArrayList = new ArrayList<>();
    OrderHistoryAdapter orderHistoryAdapter;
    DashboardActivity dashboardActivity;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_order_history;
    }

    @Override
    protected void setup() {
        fragmentOrderHistoryBinding = (FragmentOrderHistoryBinding) viewDataBinding;
        dashboardActivity = (DashboardActivity) getActivity();
        dashboardActivity.handleActionBarIcons(false);
        orderHistoryViewModel = new ViewModelProvider(this).get(OrderHistoryViewModel.class);
        PreferenceHandler preferenceHandler = new PreferenceHandler(getActivity(), PreferenceHandler.TOKEN_LOGIN);
        String userId = preferenceHandler.getData(PreferenceHandler.LOGIN_USER_ID, "");
        String sessionToken = preferenceHandler.getData(PreferenceHandler.LOGIN_TOKEN, "");
        fragmentOrderHistoryBinding.recyclerviewOrders.setLayoutManager(new LinearLayoutManager(getActivity()));
        callOrderHistoryApi(userId, sessionToken);

        fragmentOrderHistoryBinding.recyclerviewOrders.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                try {

                    Order selectedOrder = orderArrayList.get(position);
                    Bundle bundle = new Bundle();
                    Gson gson = new Gson();
                    String orderObject = gson.toJson(selectedOrder);
                    bundle.putString("order_object", orderObject);
                    dashboardActivity.handleActionMenuBar(true, false, "Order Details");
                    dashboardActivity.replaceFragment(R.id.fragment_replacer, new OrderHistoryDetailFragment(), bundle, true, false);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }));
    }

    private void callOrderHistoryApi(String userId, String sessionToken) {
        fragmentOrderHistoryBinding.progressBar.setVisibility(View.VISIBLE);
        orderHistoryViewModel.getOrderHistory(userId, sessionToken).observe(this, orderHistoryResponseResource -> {
            switch (orderHistoryResponseResource.status) {
                case SUCCESS:
                    if (orderHistoryResponseResource.data != null) {
                        orderArrayList = (ArrayList<Order>) orderHistoryResponseResource.data.getOrders();
                        if (orderArrayList == null || orderArrayList.size() == 0) {
                            fragmentOrderHistoryBinding.lvNoOrders.setVisibility(View.VISIBLE);
                        } else {
                            fragmentOrderHistoryBinding.lvNoOrders.setVisibility(View.GONE);
                            orderHistoryAdapter = new OrderHistoryAdapter(orderArrayList, getActivity());
                            fragmentOrderHistoryBinding.recyclerviewOrders.setAdapter(orderHistoryAdapter);
                        }

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
