package com.hasawi.sears_outlet.ui.main.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.hasawi.sears_outlet.R;
import com.hasawi.sears_outlet.data.api.model.pojo.ShoppingCartItem;
import com.hasawi.sears_outlet.databinding.LayoutCheckoutProductListItemBinding;

import java.util.ArrayList;

public class CheckoutProductListAdapter extends RecyclerView.Adapter<CheckoutProductListAdapter.ViewHolder> {
    ArrayList<ShoppingCartItem> shoppingCartItemArrayList;
    Context context;

    public CheckoutProductListAdapter(ArrayList<ShoppingCartItem> shoppingCartItemArrayList, Context context) {
        this.shoppingCartItemArrayList = shoppingCartItemArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutCheckoutProductListItemBinding checkoutProductListItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_checkout_product_list_item, parent, false);
        return new ViewHolder(checkoutProductListItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ShoppingCartItem shoppingCartItem = shoppingCartItemArrayList.get(position);
        holder.checkoutProductListItemBinding.tvProductName.setText(shoppingCartItem.getProduct().getDescriptions().get(0).getProductName());
        holder.checkoutProductListItemBinding.tvProductPrice.setText("KWD " + shoppingCartItem.getOneTimePrice());
    }

    @Override
    public int getItemCount() {
        return shoppingCartItemArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LayoutCheckoutProductListItemBinding checkoutProductListItemBinding;

        public ViewHolder(@NonNull LayoutCheckoutProductListItemBinding checkoutProductListItemBinding) {
            super(checkoutProductListItemBinding.getRoot());
            this.checkoutProductListItemBinding = checkoutProductListItemBinding;
        }
    }
}
