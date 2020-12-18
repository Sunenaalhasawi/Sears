package com.hasawi.sears.ui.main.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.hasawi.sears.R;
import com.hasawi.sears.data.api.model.pojo.OrderProduct;
import com.hasawi.sears.databinding.LayoutOrderHistoryImageItemBinding;

import java.util.ArrayList;

public class OrderHistoryProductImagesAdapter extends RecyclerView.Adapter<OrderHistoryProductImagesAdapter.ViewHolder> {
    ArrayList<OrderProduct> orderProductArrayList;
    Context context;

    public OrderHistoryProductImagesAdapter(ArrayList<OrderProduct> orderProducts, Context context) {
        this.orderProductArrayList = orderProducts;
        this.context = context;
    }

    @NonNull
    @Override
    public OrderHistoryProductImagesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutOrderHistoryImageItemBinding imageItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_order_history_image_item, parent, false);
        return new OrderHistoryProductImagesAdapter.ViewHolder(imageItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHistoryProductImagesAdapter.ViewHolder holder, int position) {
        try {
//            Glide.with(context)
//                    .load(orderProductArrayList.get(position))
//                    .centerCrop()
//                    .into(holder.imageItemBinding.imageViewProduct);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return orderProductArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LayoutOrderHistoryImageItemBinding imageItemBinding;

        public ViewHolder(@NonNull LayoutOrderHistoryImageItemBinding imageItemBinding) {
            super(imageItemBinding.getRoot());
            this.imageItemBinding = imageItemBinding;
        }
    }
}