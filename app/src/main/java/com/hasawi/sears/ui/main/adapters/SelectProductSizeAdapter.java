package com.hasawi.sears.ui.main.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.hasawi.sears.R;
import com.hasawi.sears.databinding.LayoutSelectProductSizeRecyclerItemBinding;

import java.util.ArrayList;

public class SelectProductSizeAdapter extends RecyclerView.Adapter<SelectProductSizeAdapter.ViewHolder> {
    ArrayList<String> sizeList;
    Context context;

    public SelectProductSizeAdapter(Context context, ArrayList<String> sizeList) {
        this.context = context;
        this.sizeList = sizeList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutSelectProductSizeRecyclerItemBinding productSizeRecyclerItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_select_product_size_recycler_item, parent, false);
        return new ViewHolder(productSizeRecyclerItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.productSizeRecyclerItemBinding.textView34.setText(sizeList.get(position));
    }

    @Override
    public int getItemCount() {
        return sizeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LayoutSelectProductSizeRecyclerItemBinding productSizeRecyclerItemBinding;

        public ViewHolder(@NonNull LayoutSelectProductSizeRecyclerItemBinding productSizeRecyclerItemBinding) {
            super(productSizeRecyclerItemBinding.getRoot());
            this.productSizeRecyclerItemBinding = productSizeRecyclerItemBinding;
        }
    }
}
