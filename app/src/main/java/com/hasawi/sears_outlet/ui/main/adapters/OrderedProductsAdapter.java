package com.hasawi.sears_outlet.ui.main.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hasawi.sears_outlet.R;
import com.hasawi.sears_outlet.data.api.model.pojo.Order;
import com.hasawi.sears_outlet.data.api.model.pojo.OrderProduct;
import com.hasawi.sears_outlet.data.api.model.pojo.OrderTrack;
import com.hasawi.sears_outlet.databinding.LayoutOrderItemHistoryDetailBinding;

import java.util.ArrayList;
import java.util.List;

public class OrderedProductsAdapter extends RecyclerView.Adapter<OrderedProductsAdapter.ViewHolder> {
    Order selectedOrder;
    ArrayList<OrderProduct> orderProductArrayList = new ArrayList<>();
    Context context;

    public OrderedProductsAdapter(Order selectedOrder, Context context) {
        this.selectedOrder = selectedOrder;
        orderProductArrayList = (ArrayList<OrderProduct>) selectedOrder.getOrderProducts();
        this.context = context;
    }

    @NonNull
    @Override
    public OrderedProductsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutOrderItemHistoryDetailBinding orderItemHistoryDetailBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_order_item_history_detail, parent, false);
        return new OrderedProductsAdapter.ViewHolder(orderItemHistoryDetailBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderedProductsAdapter.ViewHolder holder, int position) {
        try {
            OrderProduct orderProductItem = orderProductArrayList.get(position);
            if (orderProductItem.getQuantity() != null)
                holder.orderItemHistoryDetailBinding.tvQunatity.setText(orderProductItem.getQuantity() + "");
            else
                holder.orderItemHistoryDetailBinding.tvQunatity.setText("--");
            if (orderProductItem.getAmount() != null)
                holder.orderItemHistoryDetailBinding.tvAmountPaid.setText("KWD " + orderProductItem.getAmount());
            else
                holder.orderItemHistoryDetailBinding.tvAmountPaid.setText("--");
            holder.orderItemHistoryDetailBinding.tvProductName.setText(orderProductItem.getProductName());
            holder.orderItemHistoryDetailBinding.tvSize.setText(orderProductItem.getProductSize());
            holder.orderItemHistoryDetailBinding.tvColor.setText(orderProductItem.getProductColorCode());

            try {
                Glide.with(context)
                        .load(orderProductItem.getProductImage())
                        .centerCrop().into(holder.orderItemHistoryDetailBinding.imageProduct);
            } catch (Exception e) {
                e.printStackTrace();
            }

            List<OrderTrack> orderTrackList = selectedOrder.getOrderTrackList();
            String currentStatus = "";
            switch (orderTrackList.size()) {
                case 1:
                    currentStatus = "Order Placed";
                    break;
                case 2:
                    currentStatus = "In Process";
                    break;
                case 3:
                    currentStatus = "In Transit";
                    break;
                case 4:
                    currentStatus = "Delivered";
                    break;
                default:
                    break;
            }
            holder.orderItemHistoryDetailBinding.tvOrderSatus.setText(currentStatus);
            switch (currentStatus) {
                case "Order Placed":
                    holder.orderItemHistoryDetailBinding.tvOrderSatus.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.order_placed, 0, 0, 0);
                    for (int i = 1; i <= orderTrackList.size(); i++) {
                        if (orderTrackList.get(i).getSortOrder() == 1) {
                            holder.orderItemHistoryDetailBinding.tvDate.setText(orderTrackList.get(i).getFinishedDate());
                        }
                    }
                    break;
                case "In Process":
                    holder.orderItemHistoryDetailBinding.tvOrderSatus.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ongoing, 0, 0, 0);
                    for (int i = 1; i <= orderTrackList.size(); i++) {
                        if (orderTrackList.get(i).getSortOrder() == 2) {
                            holder.orderItemHistoryDetailBinding.tvDate.setText(orderTrackList.get(i).getFinishedDate());
                        }
                    }
                    break;
                case "In Transit":
                    holder.orderItemHistoryDetailBinding.tvOrderSatus.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.in_transit, 0, 0, 0);
                    for (int i = 1; i <= orderTrackList.size(); i++) {
                        if (orderTrackList.get(i).getSortOrder() == 3) {
                            holder.orderItemHistoryDetailBinding.tvDate.setText(orderTrackList.get(i).getFinishedDate());
                        }
                    }
                    break;
                case "Delivered":
                    holder.orderItemHistoryDetailBinding.tvOrderSatus.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.delivered, 0, 0, 0);
                    for (int i = 1; i <= orderTrackList.size(); i++) {
                        if (orderTrackList.get(i).getSortOrder() == 4) {
                            holder.orderItemHistoryDetailBinding.tvDate.setText(orderTrackList.get(i).getFinishedDate());
                        }
                    }
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return orderProductArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LayoutOrderItemHistoryDetailBinding orderItemHistoryDetailBinding;

        public ViewHolder(@NonNull LayoutOrderItemHistoryDetailBinding orderItemHistoryDetailBinding) {
            super(orderItemHistoryDetailBinding.getRoot());
            this.orderItemHistoryDetailBinding = orderItemHistoryDetailBinding;
        }
    }
}
