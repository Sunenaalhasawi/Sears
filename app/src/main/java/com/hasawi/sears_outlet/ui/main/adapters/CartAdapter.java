package com.hasawi.sears_outlet.ui.main.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hasawi.sears_outlet.R;
import com.hasawi.sears_outlet.data.api.model.pojo.ShoppingCartItem;
import com.hasawi.sears_outlet.databinding.LayoutSwipeCartItemBinding;
import com.hasawi.sears_outlet.ui.main.view.DashboardActivity;
import com.hasawi.sears_outlet.utils.dialogs.GeneralDialog;

import java.util.ArrayList;
import java.util.List;

public abstract class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    List<ShoppingCartItem> cartedItemsList;
    DashboardActivity dashboardActivity;
    int cartCount = 0;
//    MutableLiveData<Integer> cartCount=new MutableLiveData<>();

    public CartAdapter(DashboardActivity dashboardActivity) {
        this.dashboardActivity = dashboardActivity;
        this.cartedItemsList = new ArrayList<>();
    }

    public abstract void onItemDeleteClicked(ShoppingCartItem cartItem);

    public static void cartItemsUpdateApiResponse(boolean isSuccess) {
        MutableLiveData<Integer> cartCount = new MutableLiveData<>();
//        cartCount.postValue(count);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutSwipeCartItemBinding swipeCartItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.layout_swipe_cart_item, parent, false);
        return new ViewHolder(swipeCartItemBinding);
    }

    public abstract void cartItemsUpdated(ShoppingCartItem cartItem, int quantity, boolean isQuantityIncreased);

    public abstract void onCartItemClicked(ShoppingCartItem cartItem);

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

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            ShoppingCartItem shoppingCartItem = cartedItemsList.get(position);
            cartCount = shoppingCartItem.getQuantity();
            holder.cartAdapterItemBinding.tvProductName.setText(shoppingCartItem.getProduct().getDescriptions().get(0).getProductName());
            if (shoppingCartItem.getQuantity() != null)
                holder.cartAdapterItemBinding.tvItemCount.setText(cartCount + "");
//            if (shoppingCartItem.getProduct().getManufature() != null)
//                holder.cartAdapterItemBinding.tvBrand.setText(shoppingCartItem.getProduct().getManufature().getManufactureDescriptions().get(0).getName());

            if (shoppingCartItem.getProduct().getDiscountPercentage() != 0) {
                holder.cartAdapterItemBinding.tvOffer.setText("FLAT " + shoppingCartItem.getProduct().getDiscountPercentage() + "% OFF");
//                holder.cartRecyclerItemBinding.tvOriginalPrice.setText("KWD " + shoppingCartItem.getProduct().getDiscountPrice());
            } else {
                holder.cartAdapterItemBinding.tvOffer.setVisibility(View.GONE);
//                holder.cartRecyclerItemBinding.tvOriginalPrice.setText("KWD " + shoppingCartItem.getProduct().getOriginalPrice());
            }

            holder.cartAdapterItemBinding.tvAmount.setText("KWD " + shoppingCartItem.getProduct().getOriginalPrice());
            Glide.with(dashboardActivity)
                    .load(shoppingCartItem.getProductConfigurable().getProductImages().get(0).getImageUrl())
                    .centerCrop()
                    .into(holder.cartAdapterItemBinding.imageViewProduct);

            holder.cartAdapterItemBinding.cvBackground.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onCartItemClicked(shoppingCartItem);
                }
            });

//            holder.cartAdapterItemBinding.imageBtnDelete.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    onItemDeleteClicked(shoppingCartItem);
//                }
//            });

//            if (shoppingCartItem.isOutOfStock()) {
//                holder.cartRecyclerItemBinding.tvOutOfStock.setVisibility(View.VISIBLE);
//                holder.cartRecyclerItemBinding.txtAmountToPay.setVisibility(View.GONE);
//                holder.cartRecyclerItemBinding.tvAmountToPay.setVisibility(View.GONE);
//            } else {
//                holder.cartRecyclerItemBinding.tvOutOfStock.setVisibility(View.GONE);
//                holder.cartRecyclerItemBinding.txtAmountToPay.setVisibility(View.VISIBLE);
//                holder.cartRecyclerItemBinding.tvAmountToPay.setVisibility(View.VISIBLE);
//            }
            holder.cartAdapterItemBinding.imageButtonPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cartCount = Integer.parseInt(holder.cartAdapterItemBinding.tvItemCount.getText().toString());
                    if (shoppingCartItem.getProductConfigurable().getQuantity() >= 1 && cartCount < shoppingCartItem.getProductConfigurable().getQuantity()) {
                        if (cartCount >= 0)
                            cartCount++;
                    } else {
                        GeneralDialog generalDialog = new GeneralDialog("Error", "Maximum quantity reached for this product");
                        generalDialog.show(dashboardActivity.getSupportFragmentManager(), "GENERAL_DIALOG");
                    }

                    holder.cartAdapterItemBinding.tvItemCount.setText(cartCount + "");
                    cartItemsUpdated(shoppingCartItem, cartCount, true);
                }
            });
            holder.cartAdapterItemBinding.imageButtonMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cartCount = Integer.parseInt(holder.cartAdapterItemBinding.tvItemCount.getText().toString());
                    if (cartCount > 1) {
                        cartCount--;
                        holder.cartAdapterItemBinding.tvItemCount.setText(cartCount + "");
                        cartItemsUpdated(shoppingCartItem, cartCount, false);
                    }
                }
            });
            if (shoppingCartItem.getProductConfigurable() != null) {
                holder.cartAdapterItemBinding.tvProductSize.setText(shoppingCartItem.getProductConfigurable().getSize());
                holder.cartAdapterItemBinding.tVProductColor.setText(shoppingCartItem.getProductConfigurable().getColor());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeItem(int position) {
        cartedItemsList.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public void restoreItem(ShoppingCartItem item, int position) {
        cartedItemsList.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LayoutSwipeCartItemBinding cartAdapterItemBinding;

        public ViewHolder(@NonNull LayoutSwipeCartItemBinding swipeCartItemBinding) {
            super(swipeCartItemBinding.getRoot());
            this.cartAdapterItemBinding = swipeCartItemBinding;
        }
    }
}
