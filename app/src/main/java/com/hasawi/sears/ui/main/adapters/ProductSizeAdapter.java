package com.hasawi.sears.ui.main.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.hasawi.sears.R;
import com.hasawi.sears.databinding.LayoutProductSizeAdapterItemBinding;
import com.hasawi.sears.ui.main.listeners.RecyclerviewSingleChoiceClickListener;

import java.util.ArrayList;

public class ProductSizeAdapter extends RecyclerView.Adapter<ProductSizeAdapter.ViewHolder> {

    private static RecyclerviewSingleChoiceClickListener sClickListener;
    public static int sSelected = 0;
    ArrayList<String> sizeList;
    Context context;


    public ProductSizeAdapter(Context context, ArrayList<String> sizeList) {
        this.context = context;
        this.sizeList = sizeList;
    }

    public static void setsSelected(int sSelected) {
        ProductSizeAdapter.sSelected = sSelected;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutProductSizeAdapterItemBinding productSizeAdapterItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_product_size_adapter_item, parent, false);
        return new ViewHolder(productSizeAdapterItemBinding);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.productSizeAdapterItemBinding.tvSize.setText(sizeList.get(position));
        if (sSelected == position) {
            holder.productSizeAdapterItemBinding.tvSize.setBackground(context.getResources().getDrawable(R.drawable.blue_outlined_rounded_rectangle));
            holder.productSizeAdapterItemBinding.tvSize.setTextColor(R.color.txt_clr_blue);
        } else {
            holder.productSizeAdapterItemBinding.tvSize.setBackground(context.getResources().getDrawable(R.drawable.grey_outlined_rounded_rectangle));
            holder.productSizeAdapterItemBinding.tvSize.setTextColor(R.color.text_grey);
        }

    }

    public void selectedItem() {
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return sizeList.size();
    }

    public void setOnItemClickListener(RecyclerviewSingleChoiceClickListener clickListener) {
        sClickListener = clickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LayoutProductSizeAdapterItemBinding productSizeAdapterItemBinding;

        public ViewHolder(@NonNull LayoutProductSizeAdapterItemBinding layoutProductSizeAdapterItemBinding) {
            super(layoutProductSizeAdapterItemBinding.getRoot());
            this.productSizeAdapterItemBinding = layoutProductSizeAdapterItemBinding;
            productSizeAdapterItemBinding.cvBackgroundSize.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            sSelected = getAdapterPosition();
            sClickListener.onItemClickListener(getAdapterPosition(), view);
        }
    }
}

