package com.hasawi.sears.ui.main.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hasawi.sears.R;
import com.hasawi.sears.data.api.model.pojo.ProductSearch;
import com.hasawi.sears.databinding.LayoutSearchProductItemBinding;

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
        Glide.with(context)
                .load(productSearchList.get(position).getImageUrl())
                .centerCrop()
                .into(holder.searchProductItemBinding.imageViewProduct);
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
