package com.hasawi.sears.ui.main.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hasawi.sears.R;
import com.hasawi.sears.data.api.model.pojo.Product;
import com.hasawi.sears.databinding.LayoutRelatedProductItemBinding;

import java.util.ArrayList;
import java.util.List;

public abstract class RelatedProductsRecyclerviewAdapter extends RecyclerView.Adapter<RelatedProductsRecyclerviewAdapter.ViewHolder> {
    ArrayList<Product> productArrayList;
    Context context;

    public RelatedProductsRecyclerviewAdapter(Context context, ArrayList<Product> products) {
        this.context = context;
        this.productArrayList = products;
    }

    public abstract void onLikeClicked(Product product, boolean isWishlisted);

    public abstract void onItemClicked(Product productContent);

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutRelatedProductItemBinding productItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_related_product_item, parent, false);
        return new ViewHolder(productItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            Product productContent = productArrayList.get(position);
            if (productContent.getWishlist())
                holder.productItemBinding.radioButtonWishlist.setChecked(true);
            else
                holder.productItemBinding.radioButtonWishlist.setChecked(false);
            holder.productItemBinding.tvProductName.setText(productContent.getDescriptions().get(0).getProductName());
            if (productContent.getDiscountPercentage() == null || productContent.getDiscountPercentage() == 0)
                holder.productItemBinding.tvOfferPercent.setVisibility(View.GONE);
            else {
                holder.productItemBinding.tvOfferPercent.setText(productContent.getDiscountPercentage() + "% OFF");
                holder.productItemBinding.tvOfferPercent.setVisibility(View.VISIBLE);
            }

            Glide.with(context)
                    .load(productContent.getProductConfigurables().get(0).getProductImages().get(0).getImageUrl())
                    .centerCrop()
                    .into(holder.productItemBinding.imageViewProductImage);
            try {
                if (productContent.getDiscountPrice() == null)
                    holder.productItemBinding.tvOriginalPrice.setText("KWD 0");
                else
                    holder.productItemBinding.tvOriginalPrice.setText("KWD " + productContent.getDiscountPrice());
                if (productContent.getOriginalPrice() == null)
                    holder.productItemBinding.tvOurPrice.setText("KWD 0");
                else
                    holder.productItemBinding.tvOurPrice.setText("KWD " + productContent.getOriginalPrice());

                if (productContent.getDiscountPrice() > 0) {
                    holder.productItemBinding.tvOriginalPrice.setVisibility(View.VISIBLE);
                    holder.productItemBinding.tvOriginalPrice.setPaintFlags(holder.productItemBinding.tvOriginalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            holder.productItemBinding.radioButtonWishlist.setChecked(false);

            holder.productItemBinding.radioButtonWishlist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onLikeClicked(productContent, holder.productItemBinding.radioButtonWishlist.isChecked());
                }
            });

            holder.productItemBinding.cvBackground.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClicked(productContent);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void selectedItem() {
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return productArrayList.size();
    }

    public void addAll(List<Product> list) {
        productArrayList.addAll(list);
    }

    public void refreshList() {
        notifyDataSetChanged();
    }

    public void clear() {
        productArrayList.clear();
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LayoutRelatedProductItemBinding productItemBinding;

        public ViewHolder(@NonNull LayoutRelatedProductItemBinding layoutProductItemBinding) {
            super(layoutProductItemBinding.getRoot());
            this.productItemBinding = layoutProductItemBinding;
        }

    }

}
