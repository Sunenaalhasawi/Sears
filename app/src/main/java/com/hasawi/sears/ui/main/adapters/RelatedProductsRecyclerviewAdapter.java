package com.hasawi.sears.ui.main.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hasawi.sears.R;
import com.hasawi.sears.data.api.model.pojo.Content;
import com.hasawi.sears.databinding.LayoutRelatedProductItemBinding;

import java.util.ArrayList;
import java.util.List;

public abstract class RelatedProductsRecyclerviewAdapter extends RecyclerView.Adapter<RelatedProductsRecyclerviewAdapter.ViewHolder> {
    ArrayList<Content> productArrayList;
    Context context;

    public RelatedProductsRecyclerviewAdapter(Context context, ArrayList<Content> products) {
        this.context = context;
        this.productArrayList = products;
    }

    public abstract void onLikeClicked(Content product);

    public abstract void onItemClicked(Content productContent);

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutRelatedProductItemBinding productItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_related_product_item, parent, false);
        return new ViewHolder(productItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            Content productContent = productArrayList.get(position);
            holder.productItemBinding.tvProductName.setText(productContent.getDescriptions().get(0).getProductName());
            holder.productItemBinding.tvOfferPercent.setText(productContent.getDiscountPercentage() + "% OFF");
            Glide.with(context)
                    .load(productContent.getProductImages().get(0).getImageName())
                    .centerCrop()
                    .into(holder.productItemBinding.imageViewProductImage);
            try {
                holder.productItemBinding.tvOriginalPrice.setText("KWD " + productContent.getDiscountPrice());
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
                    holder.productItemBinding.radioButtonWishlist.setChecked(true);
                    onLikeClicked(productContent);
                }
            });

            holder.productItemBinding.radioButtonWishlist.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked)
                        holder.productItemBinding.radioButtonWishlist.setChecked(false);
                    else
                        holder.productItemBinding.radioButtonWishlist.setChecked(true);
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

    public void addAll(List<Content> list) {
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
