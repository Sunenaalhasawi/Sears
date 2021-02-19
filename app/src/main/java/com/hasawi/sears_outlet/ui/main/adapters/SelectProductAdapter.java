package com.hasawi.sears_outlet.ui.main.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.hasawi.sears_outlet.R;
import com.hasawi.sears_outlet.databinding.LayoutSelectProductRecyclerItemBinding;

import java.util.ArrayList;

public class SelectProductAdapter extends RecyclerView.Adapter<SelectProductAdapter.ViewHolder> {
    ArrayList<String> productList;
    Context context;

    public SelectProductAdapter(Context context, ArrayList<String> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutSelectProductRecyclerItemBinding productRecyclerItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_select_product_recycler_item, parent, false);
        return new ViewHolder(productRecyclerItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.selectProductRecyclerItemBinding.textView33.setText(productList.get(position));
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LayoutSelectProductRecyclerItemBinding selectProductRecyclerItemBinding;

        public ViewHolder(@NonNull LayoutSelectProductRecyclerItemBinding selectProductRecyclerItemBinding) {
            super(selectProductRecyclerItemBinding.getRoot());
            this.selectProductRecyclerItemBinding = selectProductRecyclerItemBinding;
        }
    }
}
