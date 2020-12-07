package com.hasawi.sears.ui.main.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.hasawi.sears.R;
import com.hasawi.sears.data.api.model.pojo.ProductConfigurable;
import com.hasawi.sears.databinding.LayoutColorVariantAdapterItemBinding;

import java.util.ArrayList;

public class ProductColorAdapter extends RecyclerView.Adapter<ProductColorAdapter.ViewHolder> {
    ArrayList<ProductConfigurable> colorList;
    Context context;

    public ProductColorAdapter(Context context, ArrayList<ProductConfigurable> colorList) {
        this.context = context;
        this.colorList = colorList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutColorVariantAdapterItemBinding colorVariantAdapterItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_color_variant_adapter_item, parent, false);
        return new ViewHolder(colorVariantAdapterItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            Drawable drawable = context.getResources().getDrawable(R.drawable.circle);
            GradientDrawable gradientDrawable = (GradientDrawable) drawable;
            gradientDrawable.setColor(Color.parseColor(colorList.get(position).getColorCode()));
            gradientDrawable.setSize(100, 100);
            holder.colorVariantAdapterItemBinding.imageViewColorVariant.setImageDrawable(

                    gradientDrawable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return colorList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LayoutColorVariantAdapterItemBinding colorVariantAdapterItemBinding;

        public ViewHolder(@NonNull LayoutColorVariantAdapterItemBinding layoutColorVariantAdapterItemBinding) {
            super(layoutColorVariantAdapterItemBinding.getRoot());
            this.colorVariantAdapterItemBinding = layoutColorVariantAdapterItemBinding;
        }
    }
}
