package com.hasawi.sears.ui.main.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.hasawi.sears.R;
import com.hasawi.sears.data.api.model.pojo.ShoppingCartItem;
import com.hasawi.sears.databinding.LayoutCartRecyclerItemBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public abstract class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    List<ShoppingCartItem> cartedItemsList;

    public CartAdapter() {
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
        ShoppingCartItem shoppingCartItem = cartedItemsList.get(position);
        holder.cartRecyclerItemBinding.tvProductName.setText(shoppingCartItem.getProduct().getDescriptions().get(0).getProductName());
        if (shoppingCartItem.getQuantity() != null)
            holder.cartRecyclerItemBinding.tvCartCount.setText(shoppingCartItem.getQuantity() + "");
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
        Picasso.get().load(shoppingCartItem.getProduct().getProductImages().get(0).getImageName()).into(holder.cartRecyclerItemBinding.imageProduct);

        holder.cartRecyclerItemBinding.imageBtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemDeleteClicked(shoppingCartItem);
            }
        });
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
