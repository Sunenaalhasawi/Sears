package com.hasawi.sears.ui.main.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.hasawi.sears.R;
import com.hasawi.sears.databinding.LayoutSortRecyclerItemBinding;
import com.hasawi.sears.ui.main.listeners.RecyclerviewSingleChoiceClickListener;

import java.util.ArrayList;
import java.util.List;

public class SortAdapter extends RecyclerView.Adapter<SortAdapter.ViewHolder> {
    private static RecyclerviewSingleChoiceClickListener sClickListener;
    private static int sSelected = -1;
    List<String> sortList = new ArrayList<>();

    public SortAdapter(ArrayList<String> sortList) {
        this.sortList = sortList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutSortRecyclerItemBinding sortRecyclerItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.layout_sort_recycler_item, parent, false);
        return new ViewHolder(sortRecyclerItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.sortRecyclerItemBinding.tvSortText.setText(sortList.get(position));
        if (sSelected == position) {
            holder.sortRecyclerItemBinding.radioBtnSort.setChecked(true);
        } else {
            holder.sortRecyclerItemBinding.radioBtnSort.setChecked(false);
        }
    }

    public void selectedItem() {
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return sortList.size();
    }

    public void setOnItemClickListener(RecyclerviewSingleChoiceClickListener clickListener) {
        sClickListener = clickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LayoutSortRecyclerItemBinding sortRecyclerItemBinding;

        public ViewHolder(@NonNull LayoutSortRecyclerItemBinding sortRecyclerItemBinding) {
            super(sortRecyclerItemBinding.getRoot());
            this.sortRecyclerItemBinding = sortRecyclerItemBinding;
            sortRecyclerItemBinding.rvBackground.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            sSelected = getAdapterPosition();
            sClickListener.onItemClickListener(getAdapterPosition(), view);
        }

    }
}