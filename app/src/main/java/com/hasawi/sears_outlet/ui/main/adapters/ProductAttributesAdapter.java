package com.hasawi.sears_outlet.ui.main.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.hasawi.sears_outlet.R;
import com.hasawi.sears_outlet.data.api.model.pojo.ProductAttribute;
import com.hasawi.sears_outlet.databinding.LayoutProductAttributeRecyclerItemBinding;

import java.util.List;

public class ProductAttributesAdapter extends RecyclerView.Adapter<ProductAttributesAdapter.ViewHolder> {
    Context context;
    List<ProductAttribute> productAttributeList;

    public ProductAttributesAdapter(Context context, List<ProductAttribute> productAttributeList) {
        this.context = context;
        this.productAttributeList = productAttributeList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutProductAttributeRecyclerItemBinding productAttributeRecyclerItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.layout_product_attribute_recycler_item, parent, false);
        return new ViewHolder(productAttributeRecyclerItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            holder.layoutProductAttributeRecyclerItemBinding.tvDescriptionName.setText(productAttributeList.get(position).getOption().getProductOptionDescriptions().get(0).getName());
            holder.layoutProductAttributeRecyclerItemBinding.tvDescriptionValue.setText(productAttributeList.get(position).getOptionValue().getProductOptionValueDescriptions().get(0).getName());


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return productAttributeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LayoutProductAttributeRecyclerItemBinding layoutProductAttributeRecyclerItemBinding;

        public ViewHolder(@NonNull LayoutProductAttributeRecyclerItemBinding layoutProductAttributeRecyclerItemBinding) {
            super(layoutProductAttributeRecyclerItemBinding.getRoot());
            this.layoutProductAttributeRecyclerItemBinding = layoutProductAttributeRecyclerItemBinding;
        }
    }
}
