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
import com.hasawi.sears.data.api.model.pojo.ShoppingCartItem;
import com.hasawi.sears.databinding.LayoutCartRecyclerItemBinding;

import java.util.ArrayList;
import java.util.List;

public abstract class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    List<ShoppingCartItem> cartedItemsList;
    Context context;
    int cartCount = 0;

    public CartAdapter(Context context) {
        this.context = context;
        this.cartedItemsList = new ArrayList<>();
    }

    public abstract void onItemDeleteClicked(ShoppingCartItem cartItem);

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutCartRecyclerItemBinding cartRecyclerItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.layout_cart_recycler_item, parent, false);
        return new ViewHolder(cartRecyclerItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            ShoppingCartItem shoppingCartItem = cartedItemsList.get(position);
            cartCount = shoppingCartItem.getQuantity();
            holder.cartRecyclerItemBinding.tvProductName.setText(shoppingCartItem.getProduct().getDescriptions().get(0).getProductName());
            if (shoppingCartItem.getQuantity() != null)
                holder.cartRecyclerItemBinding.tvCartCount.setText(cartCount + "");
            if (shoppingCartItem.getProduct().getManufature() != null)
                holder.cartRecyclerItemBinding.tvBrand.setText(shoppingCartItem.getProduct().getManufature().toString());

            if (shoppingCartItem.getProduct().getDiscountPercentage() != 0) {
                holder.cartRecyclerItemBinding.tvOfferPercent.setText("FLAT " + shoppingCartItem.getProduct().getDiscountPercentage() + "% OFF");
                holder.cartRecyclerItemBinding.tvOriginalPrice.setText("KWD " + shoppingCartItem.getProduct().getDiscountPrice());
            } else {
                holder.cartRecyclerItemBinding.tvOfferPercent.setVisibility(View.GONE);
                holder.cartRecyclerItemBinding.tvOriginalPrice.setText("KWD " + shoppingCartItem.getProduct().getOriginalPrice());
            }

            holder.cartRecyclerItemBinding.tvAmountToPay.setText("KWD " + shoppingCartItem.getProduct().getOriginalPrice());
            Glide.with(context)
                    .load(shoppingCartItem.getProduct().getProductImages().get(0).getImageName())
                    .centerCrop()
                    .into(holder.cartRecyclerItemBinding.imageProduct);

            holder.cartRecyclerItemBinding.imageBtnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemDeleteClicked(shoppingCartItem);
                }
            });

            if (shoppingCartItem.isOutOfStock()) {
                holder.cartRecyclerItemBinding.tvOutOfStock.setVisibility(View.VISIBLE);
                holder.cartRecyclerItemBinding.txtAmountToPay.setVisibility(View.GONE);
                holder.cartRecyclerItemBinding.tvAmountToPay.setVisibility(View.GONE);
            } else {
                holder.cartRecyclerItemBinding.tvOutOfStock.setVisibility(View.GONE);
                holder.cartRecyclerItemBinding.txtAmountToPay.setVisibility(View.VISIBLE);
                holder.cartRecyclerItemBinding.tvAmountToPay.setVisibility(View.VISIBLE);
            }
            holder.cartRecyclerItemBinding.imageAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (cartCount >= 0)
                        cartCount++;
                    holder.cartRecyclerItemBinding.tvCartCount.setText(cartCount + "");
                }
            });
            holder.cartRecyclerItemBinding.imageSubtract.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (cartCount > 1)
                        cartCount--;
                    holder.cartRecyclerItemBinding.tvCartCount.setText(cartCount + "");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addAll(List<ShoppingCartItem> itemList) {
        cartedItemsList = new ArrayList<>();
        if (itemList != null && itemList.size() > 0) {
            this.cartedItemsList = itemList;
        }
        notifyDataSetChanged();
    }

    public void removeOneItem(ShoppingCartItem cartItem, int position) {
        if (cartedItemsList.contains(cartItem)) {
            cartedItemsList.remove(cartItem);
        }
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return cartedItemsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LayoutCartRecyclerItemBinding cartRecyclerItemBinding;

        public ViewHolder(@NonNull LayoutCartRecyclerItemBinding cartRecyclerItemBinding) {
            super(cartRecyclerItemBinding.getRoot());
            this.cartRecyclerItemBinding = cartRecyclerItemBinding;
        }
    }
}
