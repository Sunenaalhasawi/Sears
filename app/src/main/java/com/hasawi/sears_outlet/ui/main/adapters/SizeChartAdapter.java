package com.hasawi.sears_outlet.ui.main.adapters;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hasawi.sears_outlet.databinding.LayoutSizeChartItemBinding;

public class SizeChartAdapter extends RecyclerView.Adapter<SizeChartAdapter.ViewHolder> {

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LayoutSizeChartItemBinding sizeChartItemBinding;

        public ViewHolder(@NonNull LayoutSizeChartItemBinding sizeChartItemBinding) {
            super(sizeChartItemBinding.getRoot());
            this.sizeChartItemBinding = sizeChartItemBinding;
            sizeChartItemBinding.cvBackground.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
//            sSelected = getAdapterPosition();
//            sClickListener.onItemClickListener(getAdapterPosition(), view);
        }

    }
}
