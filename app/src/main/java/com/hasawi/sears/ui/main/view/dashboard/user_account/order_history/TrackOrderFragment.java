package com.hasawi.sears.ui.main.view.dashboard.user_account.order_history;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.hasawi.sears.R;
import com.hasawi.sears.data.api.model.pojo.Order;
import com.hasawi.sears.data.api.model.pojo.OrderTrack;
import com.hasawi.sears.databinding.FragmentTrackOrderBinding;
import com.hasawi.sears.ui.base.BaseFragment;
import com.hasawi.sears.ui.main.adapters.TrackOrderAdapter;

import java.util.ArrayList;
import java.util.List;

public class TrackOrderFragment extends BaseFragment {
    FragmentTrackOrderBinding fragmentTrackOrderBinding;
    TrackOrderAdapter trackOrderAdapter;
    Order selectedOrder;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_track_order;
    }

    @Override
    protected void setup() {
        fragmentTrackOrderBinding = (FragmentTrackOrderBinding) viewDataBinding;
        try {
            Bundle bundle = getArguments();
            String orderObject = bundle.getString("order_object");
            Gson gson = new Gson();
            selectedOrder = gson.fromJson(orderObject, Order.class);
            List<OrderTrack> orderTrackList = selectedOrder.getOrderTrackList();
            fragmentTrackOrderBinding.recyclerviewOrderStatus.setLayoutManager(new LinearLayoutManager(getActivity()));
            trackOrderAdapter = new TrackOrderAdapter((ArrayList<OrderTrack>) orderTrackList, getActivity());
            fragmentTrackOrderBinding.recyclerviewOrderStatus.setAdapter(trackOrderAdapter);
            fragmentTrackOrderBinding.btnProductDelivered.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getParentFragmentManager().popBackStackImmediate();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
