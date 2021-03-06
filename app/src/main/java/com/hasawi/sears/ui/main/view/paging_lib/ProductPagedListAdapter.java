package com.hasawi.sears.ui.main.view.paging_lib;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hasawi.sears.data.api.model.pojo.Content;
import com.hasawi.sears.databinding.LayoutProductItemBinding;
import com.hasawi.sears.databinding.NetworkItemBinding;
import com.hasawi.sears.utils.NetworkState;

public abstract class ProductPagedListAdapter extends PagedListAdapter<Content, RecyclerView.ViewHolder> {

    private static final int TYPE_PROGRESS = 0;
    private static final int TYPE_ITEM = 1;

    private Context context;
    private NetworkState networkState;

    public ProductPagedListAdapter(Context context) {
        super(Content.DIFF_CALLBACK);
        this.context = context;
    }

    public abstract void onLikeClicked(Content product, boolean checked);

    public abstract void onItemClicked(Content productContent);

    public abstract void onLoadedProducts();

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        if (viewType == TYPE_PROGRESS) {
            NetworkItemBinding headerBinding = NetworkItemBinding.inflate(layoutInflater, parent, false);
            NetworkStateItemViewHolder viewHolder = new NetworkStateItemViewHolder(headerBinding);
            return viewHolder;

        } else {
            LayoutProductItemBinding itemBinding = LayoutProductItemBinding.inflate(layoutInflater, parent, false);
            ProductItemViewHolder viewHolder = new ProductItemViewHolder(itemBinding);
            return viewHolder;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ProductItemViewHolder) {
            ((ProductItemViewHolder) holder).bindTo(getItem(position));
        } else {
            ((NetworkStateItemViewHolder) holder).bindView(networkState);
        }
        if (position == 2)
            onLoadedProducts();
    }


    private boolean hasExtraRow() {
        if (networkState != null && networkState != NetworkState.LOADED) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (hasExtraRow() && position == getItemCount() - 1) {
            return TYPE_PROGRESS;
        } else {
            return TYPE_ITEM;
        }
    }

    public void setNetworkState(NetworkState newNetworkState) {
        NetworkState previousState = this.networkState;
        boolean previousExtraRow = hasExtraRow();
        this.networkState = newNetworkState;
        boolean newExtraRow = hasExtraRow();
        if (previousExtraRow != newExtraRow) {
            if (previousExtraRow) {
                notifyItemRemoved(getItemCount());
            } else {
                notifyItemInserted(getItemCount());
            }
        } else if (newExtraRow && previousState != newNetworkState) {
            notifyItemChanged(getItemCount() - 1);
        }
    }


    public class ProductItemViewHolder extends RecyclerView.ViewHolder {

        LayoutProductItemBinding productItemBinding;

        public ProductItemViewHolder(LayoutProductItemBinding binding) {
            super(binding.getRoot());
            this.productItemBinding = binding;
        }

        public void bindTo(Content content) {
            Content productContent = content;
            if (productContent.getWishlist())
                productItemBinding.radioButtonWishlist.setChecked(true);
            else
                productItemBinding.radioButtonWishlist.setChecked(false);
            productItemBinding.tvProductName.setText(productContent.getDescriptions().get(0).getProductName());
            productItemBinding.tvOfferPercent.setText(productContent.getDiscountPercentage() + "%");
            Glide.with(context)
                    .load(productContent.getProductImages().get(0).getImageName())
                    .centerCrop()
                    .into(productItemBinding.imageViewProductImage);
            Glide.with(context)
                    .load(productContent.getBrandLogoUrl())
                    .centerCrop()
                    .into(productItemBinding.imageViewBrangLogo);

            try {
                productItemBinding.tvOriginalPrice.setText("KWD " + productContent.getDiscountPrice());
                productItemBinding.tvOurPrice.setText("KWD " + productContent.getOriginalPrice());

                if (productContent.getDiscountPrice() > 0) {
                    productItemBinding.tvOriginalPrice.setVisibility(View.VISIBLE);
                    productItemBinding.tvOriginalPrice.setPaintFlags(productItemBinding.tvOriginalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            productItemBinding.radioButtonWishlist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onLikeClicked(productContent, productItemBinding.radioButtonWishlist.isChecked());
                }
            });

            productItemBinding.cvBackground.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClicked(productContent);
                }
            });
//                }
        }
    }


    public class NetworkStateItemViewHolder extends RecyclerView.ViewHolder {

        private NetworkItemBinding binding;

        public NetworkStateItemViewHolder(NetworkItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindView(NetworkState networkState) {
            if (networkState != null && networkState.getStatus() == NetworkState.Status.RUNNING) {
                binding.progressBar.setVisibility(View.VISIBLE);
            } else {
                binding.progressBar.setVisibility(View.GONE);
            }

            if (networkState != null && networkState.getStatus() == NetworkState.Status.FAILED) {
                binding.errorMsg.setVisibility(View.VISIBLE);
                binding.errorMsg.setText(networkState.getMsg());
            } else {
                binding.errorMsg.setVisibility(View.GONE);
            }
        }
    }
}
