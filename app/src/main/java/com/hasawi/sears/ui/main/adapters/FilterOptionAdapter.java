package com.hasawi.sears.ui.main.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.hasawi.sears.R;
import com.hasawi.sears.data.api.model.pojo.FilterAttributeValues;
import com.hasawi.sears.databinding.LayoutFilterOptionBinding;
import com.hasawi.sears.ui.main.listeners.RecyclerviewSingleChoiceClickListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class FilterOptionAdapter extends RecyclerView.Adapter<FilterOptionAdapter.ViewHolder> {

    private static RecyclerviewSingleChoiceClickListener sClickListener;
    private static int sSelected = 0;
    Context context;
    List<String> productAttributeList;
    Map<String, List<FilterAttributeValues>> filterOptionValueMap;
    List<FilterAttributeValues> selectedFilterData = new ArrayList<>();
    RecyclerView filterValueRecycler;

    public FilterOptionAdapter(Context context, RecyclerView filterValueRecycler) {
        this.context = context;
        this.productAttributeList = new ArrayList<>();
        this.filterValueRecycler = filterValueRecycler;
    }

    public abstract void onFilterSelected(List<FilterAttributeValues> filterAttributeValues);

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutFilterOptionBinding filterOptionBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.layout_filter_option, parent, false);
        return new ViewHolder(filterOptionBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.filterOptionBinding.tvFilterValue.setText(productAttributeList.get(position));
        if (sSelected == position) {
            filterValueRecycler.setAdapter(new FilterValueAdapter(context, filterOptionValueMap.get(productAttributeList.get(position))) {
                @Override
                public void onFilterSelected(List<FilterAttributeValues> selectedFilterAttributeValues) {
                    selectedFilterData.addAll(selectedFilterAttributeValues);
                    FilterOptionAdapter.this.onFilterSelected(selectedFilterData);
                }
            });
            holder.filterOptionBinding.tvFilterValue.setTextColor(context.getResources().getColor(R.color.txt_clr_blue));
            holder.filterOptionBinding.tvFilterValue.setBackgroundColor(context.getResources().getColor(R.color.white));
        } else {
            holder.filterOptionBinding.tvFilterValue.setTextColor(context.getResources().getColor(R.color.white));
            holder.filterOptionBinding.tvFilterValue.setBackgroundColor(context.getResources().getColor(R.color.txt_clr_blue));
        }
    }

    public void addAll(Map<String, List<FilterAttributeValues>> filterOptionValueMap, List<String> optionList) {
        productAttributeList.clear();
        this.filterOptionValueMap = filterOptionValueMap;
        if (optionList.size() > 0 && optionList != null) {
            productAttributeList.addAll(optionList);
        }
        notifyDataSetChanged();
    }

    public void selectedItem() {
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return productAttributeList.size();
    }

    public void setOnItemClickListener(RecyclerviewSingleChoiceClickListener clickListener) {
        sClickListener = clickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LayoutFilterOptionBinding filterOptionBinding;

        public ViewHolder(@NonNull LayoutFilterOptionBinding filterOptionBinding) {
            super(filterOptionBinding.getRoot());
            this.filterOptionBinding = filterOptionBinding;
            filterOptionBinding.tvFilterValue.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            sSelected = getAdapterPosition();
            sClickListener.onItemClickListener(getAdapterPosition(), view);
        }

    }
}