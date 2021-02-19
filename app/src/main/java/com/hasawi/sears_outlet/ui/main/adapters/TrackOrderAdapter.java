package com.hasawi.sears_outlet.ui.main.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.hasawi.sears_outlet.R;
import com.hasawi.sears_outlet.data.api.model.pojo.OrderTrack;
import com.hasawi.sears_outlet.databinding.LayoutOrderStatusRecyclerItemBinding;

import java.util.ArrayList;

public class TrackOrderAdapter extends RecyclerView.Adapter<TrackOrderAdapter.ViewHolder> {
    ArrayList<OrderTrack> orderTrackArrayList;
    ArrayList<String> orderStatus = new ArrayList<>();
    Context context;

    public TrackOrderAdapter(ArrayList<OrderTrack> orderTracks, Context context) {
        this.orderTrackArrayList = orderTracks;
        this.context = context;
        orderStatus.add("Order Placed");
        orderStatus.add("In Process");
        orderStatus.add("In Transit");
        orderStatus.add("Delivered");
    }

    @NonNull
    @Override
    public TrackOrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutOrderStatusRecyclerItemBinding orderStatusRecyclerItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_order_status_recycler_item, parent, false);
        return new TrackOrderAdapter.ViewHolder(orderStatusRecyclerItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull TrackOrderAdapter.ViewHolder holder, int position) {
        try {

            for (int i = 0; i < orderTrackArrayList.size(); i++) {
                if (orderTrackArrayList.get(i).getSortOrder() == position + 1) {
                    holder.orderStatusRecyclerItemBinding.tvDate.setText(orderTrackArrayList.get(i).getFinishedDate());
                    holder.orderStatusRecyclerItemBinding.radioButtonOrderStatus.setChecked(true);
                    break;
                }
            }
            if (position == 3) {
                holder.orderStatusRecyclerItemBinding.viewLine.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.orderStatusRecyclerItemBinding.tvOrderStatus.setText(orderStatus.get(position));

    }

    @Override
    public int getItemCount() {
        return orderStatus.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LayoutOrderStatusRecyclerItemBinding orderStatusRecyclerItemBinding;

        public ViewHolder(@NonNull LayoutOrderStatusRecyclerItemBinding orderStatusRecyclerItemBinding) {
            super(orderStatusRecyclerItemBinding.getRoot());
            this.orderStatusRecyclerItemBinding = orderStatusRecyclerItemBinding;
        }
    }
}