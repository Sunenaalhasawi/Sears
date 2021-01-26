package com.hasawi.sears.ui.main.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hasawi.sears.R;
import com.hasawi.sears.data.api.model.pojo.Product;
import com.hasawi.sears.databinding.ItemLoadingBinding;
import com.hasawi.sears.databinding.LayoutProductItemBinding;

import java.util.ArrayList;
import java.util.List;

public abstract class ProductPaginationRecyclerAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_NORMAL = 1;
    Context context;
    private boolean isLoaderVisible = false;
    private List<Product> productList;

    public ProductPaginationRecyclerAdapter(Context context) {
//        this.productList = productList;
        productList = new ArrayList<>();
        this.context = context;
    }

    public abstract void onLikeClicked(Product product, int position);

    public abstract void onItemClicked(Product productContent);

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                LayoutProductItemBinding productItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_product_item, parent, false);
                return new ViewHolder(productItemBinding);
            case VIEW_TYPE_LOADING:
                ItemLoadingBinding itemLoadingBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_loading, parent, false);
                return new ProgressHolder(itemLoadingBinding);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemViewType(int position) {
        if (isLoaderVisible) {
            return position == productList.size() - 1 ? VIEW_TYPE_LOADING : VIEW_TYPE_NORMAL;
        } else {
            return VIEW_TYPE_NORMAL;
        }
    }

    @Override
    public int getItemCount() {
        return productList == null ? 0 : productList.size();
    }

    public void addItems(List<Product> postItems) {
        try {
            productList.addAll(postItems);
        } catch (Exception e) {
            e.printStackTrace();
        }
        notifyDataSetChanged();
    }

    public void addAll(List<Product> list) {
        if (!productList.contains(list))
            productList.addAll(list);
    }

    public void addLoading() {
        isLoaderVisible = true;
        productList.add(new Product());
        notifyItemInserted(productList.size() - 1);
    }

    public void removeLoading() {
        isLoaderVisible = false;
        int position = productList.size() - 1;
        Product productContent = getItem(position);
        if (productContent != null) {
            productList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        productList.clear();
        notifyDataSetChanged();
    }

    Product getItem(int position) {
        return productList.get(position);
    }

    public void refreshList() {
        notifyDataSetChanged();
    }

    public class ViewHolder extends BaseViewHolder {
        LayoutProductItemBinding productItemBinding;

        public ViewHolder(@NonNull LayoutProductItemBinding layoutProductItemBinding) {
            super(layoutProductItemBinding.getRoot());
            this.productItemBinding = layoutProductItemBinding;
        }

        @Override
        public void onBind(int position) {
            super.onBind(position);
            try {
//                if (position > 0) {

                Product productContent = productList.get(position);
                productItemBinding.tvProductName.setText(productContent.getDescriptions().get(0).getProductName());
                Glide.with(context)
                        .load(productContent.getProductConfigurables().get(0).getProductImages().get(0).getImageUrl())
                        .centerCrop()
                        .into(productItemBinding.imageViewProductImage);
                productItemBinding.radioButtonWishlist.setChecked(false);

                productItemBinding.radioButtonWishlist.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        productItemBinding.radioButtonWishlist.setChecked(true);
                        onLikeClicked(productContent, position);
                    }
                });

                productItemBinding.radioButtonWishlist.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked)
                            productItemBinding.radioButtonWishlist.setChecked(false);
                        else
                            productItemBinding.radioButtonWishlist.setChecked(true);
                    }
                });

                productItemBinding.cvBackground.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClicked(productContent);
                    }
                });
//                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void clear() {
        }
    }

    public class ProgressHolder extends BaseViewHolder {
        ItemLoadingBinding itemLoadingBinding;

        public ProgressHolder(@NonNull ItemLoadingBinding itemLoadingBinding) {
            super(itemLoadingBinding.getRoot());
            this.itemLoadingBinding = itemLoadingBinding;
        }

        @Override
        protected void clear() {
        }
    }
}