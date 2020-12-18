package com.hasawi.sears.ui.main.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hasawi.sears.R;
import com.hasawi.sears.data.api.model.pojo.Order;
import com.hasawi.sears.data.api.model.pojo.OrderProduct;
import com.hasawi.sears.databinding.LayoutOrderHistoryRecyclerItemBinding;

import java.util.ArrayList;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder> {
    ArrayList<Order> orderList;
    Context context;
    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    private LinearLayoutManager layoutManager;

    public OrderHistoryAdapter(ArrayList<Order> orderList, Context context) {
        this.orderList = orderList;
        this.context = context;
    }

    @NonNull
    @Override
    public OrderHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutOrderHistoryRecyclerItemBinding orderHistoryRecyclerItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_order_history_recycler_item, parent, false);
        return new ViewHolder(orderHistoryRecyclerItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHistoryAdapter.ViewHolder holder, int position) {
        Order orderItem = orderList.get(position);
        holder.orderHistoryRecyclerItemBinding.tvOrderId.setText(orderItem.getOrderId());
        holder.orderHistoryRecyclerItemBinding.tvOrderAmount.setText("KWD " + orderItem.getTotal());
        holder.orderHistoryRecyclerItemBinding.tvOrderedDate.setText(orderItem.getDateOfPurchase());
        holder.orderHistoryRecyclerItemBinding.tvOrderQuantity.setText(orderItem.getOrderProducts().size());
        holder.orderHistoryRecyclerItemBinding.tvOrderStatus.setText("Order Placed");
        String[] orderStatus = {"Order Placed", "In Process", "In Transit", "Delivered"};
        String currentStatus = orderStatus[2];
        switch (currentStatus) {
            case "Order Placed":
                holder.orderHistoryRecyclerItemBinding.tvOrderStatus.setText(currentStatus);
                holder.orderHistoryRecyclerItemBinding.tvOrderStatus.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.order_placed, 0, 0, 0);
                break;
            case "In Process":
                holder.orderHistoryRecyclerItemBinding.tvOrderStatus.setText(currentStatus);
                holder.orderHistoryRecyclerItemBinding.tvOrderStatus.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ongoing, 0, 0, 0);
                break;
            case "In Transit":
                holder.orderHistoryRecyclerItemBinding.tvOrderStatus.setText(currentStatus);
                holder.orderHistoryRecyclerItemBinding.tvOrderStatus.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.in_transit, 0, 0, 0);
                break;
            case "Delivered":
                holder.orderHistoryRecyclerItemBinding.tvOrderStatus.setText(currentStatus);
                holder.orderHistoryRecyclerItemBinding.tvOrderStatus.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.delivered, 0, 0, 0);
                break;
            default:
                break;
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(holder.orderHistoryRecyclerItemBinding.recyclerviewProductList.getContext(),
                LinearLayoutManager.HORIZONTAL,
                false);

        // Since this is a nested layout, so
        // to define how many child items
        // should be prefetched when the
        // child RecyclerView is nested
        // inside the parent RecyclerView,
        // we use the following method
        layoutManager.setInitialPrefetchItemCount(orderItem.getOrderProducts().size());

        // Create an instance of the child
        // item view adapter and set its
        // adapter, layout manager and RecyclerViewPool
        OrderHistoryProductImagesAdapter productImagesAdapter = new OrderHistoryProductImagesAdapter((ArrayList<OrderProduct>) orderItem.getOrderProducts(), context);
        holder.orderHistoryRecyclerItemBinding.recyclerviewProductList.setLayoutManager(layoutManager);
        holder.orderHistoryRecyclerItemBinding.recyclerviewProductList.setAdapter(productImagesAdapter);
        holder.orderHistoryRecyclerItemBinding.recyclerviewProductList.setRecycledViewPool(viewPool);

    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LayoutOrderHistoryRecyclerItemBinding orderHistoryRecyclerItemBinding;

        public ViewHolder(@NonNull LayoutOrderHistoryRecyclerItemBinding orderHistoryRecyclerItemBinding) {
            super(orderHistoryRecyclerItemBinding.getRoot());
            this.orderHistoryRecyclerItemBinding = orderHistoryRecyclerItemBinding;
        }
    }
}