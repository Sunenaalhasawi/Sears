package com.hasawi.sears_outlet.ui.main.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hasawi.sears_outlet.R;
import com.hasawi.sears_outlet.data.api.model.pojo.ProductSearch;
import com.hasawi.sears_outlet.databinding.LayoutSearchProductItemBinding;

import java.util.List;

public class SearchProductAdapter extends RecyclerView.Adapter<SearchProductAdapter.ViewHolder> {
    List<ProductSearch> productSearchList;
    Context context;

    public SearchProductAdapter(Context context, List<ProductSearch> productSearchList) {
        this.context = context;
        this.productSearchList = productSearchList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutSearchProductItemBinding searchProductItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.layout_search_product_item, parent, false);
        return new ViewHolder(searchProductItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.searchProductItemBinding.tvProductName.setText(productSearchList.get(position).getNameEn());
        holder.searchProductItemBinding.tvProductDescrption.setText(productSearchList.get(position).getBrand());
        Glide.with(context)
                .load(productSearchList.get(position).getImageUrl())
                .centerCrop()
                .into(holder.searchProductItemBinding.imageViewProduct);
        holder.searchProductItemBinding.tvProductPrice.setText("KWD " + productSearchList.get(position).getPrice());
    }

    @Override
    public int getItemCount() {
        return productSearchList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LayoutSearchProductItemBinding searchProductItemBinding;

        public ViewHolder(@NonNull LayoutSearchProductItemBinding searchProductItemBinding) {
            super(searchProductItemBinding.getRoot());
            this.searchProductItemBinding = searchProductItemBinding;
        }
    }
}
