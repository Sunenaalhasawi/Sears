package com.hasawi.sears.ui.main.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hasawi.sears.R;
import com.hasawi.sears.data.api.model.pojo.Content;
import com.hasawi.sears.data.api.model.pojo.Wishlist;
import com.hasawi.sears.databinding.LayoutWishlistAdapterItemBinding;

import java.util.ArrayList;
import java.util.List;

public abstract class WishListAdapter extends RecyclerView.Adapter<WishListAdapter.ViewHolder> {

    List<Wishlist> wishlists;
    Context context;

    public WishListAdapter(Context context) {
        this.context = context;
        this.wishlists = new ArrayList<>();
    }

    public abstract void onWishListClicked(Content selectedProduct, boolean isChecked, int position);

    public abstract void onItemClicked(Content selectedProduct);

    @NonNull
    @Override
    public WishListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutWishlistAdapterItemBinding wishlistAdapterItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.layout_wishlist_adapter_item, parent, false);
        return new WishListAdapter.ViewHolder(wishlistAdapterItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull WishListAdapter.ViewHolder holder, int position) {
        Wishlist wishlistItem = wishlists.get(position);
        Content wishlistedProduct = wishlistItem.getProduct();
        try {
            holder.wishlistAdapterItemBinding.tvProductName.setText(wishlistedProduct.getDescriptions().get(0).getProductName());
            holder.wishlistAdapterItemBinding.tvOffer.setText(wishlistedProduct.getDiscountPercentage() + "%");
            holder.wishlistAdapterItemBinding.tvProductPrice.setText("KWD " + wishlistedProduct.getOriginalPrice());
            Glide.with(context)
                    .load(wishlistedProduct.getProductImages().get(0).getImageName())
                    .centerCrop()
                    .into(holder.wishlistAdapterItemBinding.imageViewProductImage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.wishlistAdapterItemBinding.checkboxWishlist.setChecked(true);
        holder.wishlistAdapterItemBinding.checkboxWishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onWishListClicked(wishlistedProduct, holder.wishlistAdapterItemBinding.checkboxWishlist.isChecked(), position);
            }
        });
        holder.wishlistAdapterItemBinding.cvBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClicked(wishlistedProduct);
            }
        });
        if (!wishlistItem.getProduct().isAvailable()) {
            holder.wishlistAdapterItemBinding.tvOutOfStock.setVisibility(View.VISIBLE);
        }
    }

    public void addAll(List<Wishlist> wishlistList) {
        if (wishlistList != null && wishlistList.size() > 0) {
            wishlists = wishlistList;
        }
        notifyDataSetChanged();
    }

    public void removeItemFromWishList(Content product, int position) {
        if (wishlists.contains(product))
            wishlists.remove(product);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return wishlists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LayoutWishlistAdapterItemBinding wishlistAdapterItemBinding;

        public ViewHolder(@NonNull LayoutWishlistAdapterItemBinding wishlistAdapterItemBinding) {
            super(wishlistAdapterItemBinding.getRoot());
            this.wishlistAdapterItemBinding = wishlistAdapterItemBinding;
        }
    }
}
