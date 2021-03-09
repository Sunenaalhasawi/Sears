package com.hasawi.sears_outlet.ui.main.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hasawi.sears_outlet.R;
import com.hasawi.sears_outlet.data.api.model.pojo.FilterAttributeValues;
import com.hasawi.sears_outlet.databinding.LayoutFilterOptionBinding;
import com.hasawi.sears_outlet.ui.main.listeners.RecyclerviewSingleChoiceClickListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class FilterOptionAdapter extends RecyclerView.Adapter<FilterOptionAdapter.ViewHolder> {

    private static RecyclerviewSingleChoiceClickListener sClickListener;
    private static int sSelected = 0;
    Context context;
    List<String> productAttributeList;
    Map<String, List<FilterAttributeValues>> filterOptionValueMap;
    Map<String, List<FilterAttributeValues>> filterCountMap;
    List<FilterAttributeValues> selectedFilterData = new ArrayList<>();
    List<FilterAttributeValues> selectedBrands = new ArrayList<>();
    List<FilterAttributeValues> selectedColors = new ArrayList<>();
    List<FilterAttributeValues> selectedSizes = new ArrayList<>();
    List<FilterAttributeValues> currentlySelectedFilter = new ArrayList<>();
    RecyclerView filterValueRecycler;
    int filteValueCount = 0;

    public FilterOptionAdapter(Context context, RecyclerView filterValueRecycler) {
        this.context = context;
        this.productAttributeList = new ArrayList<>();
        this.filterValueRecycler = filterValueRecycler;
        filterCountMap = new HashMap<>();
    }

    public abstract void onFilterSelected(List<FilterAttributeValues> filterAttributeValues, List<FilterAttributeValues> selectedBrands, List<FilterAttributeValues> selectedColors, List<FilterAttributeValues> selectedSizes);

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutFilterOptionBinding filterOptionBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.layout_filter_option, parent, false);
        return new ViewHolder(filterOptionBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String filterOption = productAttributeList.get(position);
        holder.filterOptionBinding.tvFilterValue.setText(filterOption);
        List<FilterAttributeValues> filterAttributeValuesList = filterOptionValueMap.get(productAttributeList.get(position));
        try {

//            if (filterCountMap != null && filterCountMap.get(filterOption).size() > 0)
//                filteValueCount = filterCountMap.get(filterOption).size();
//            else
//                filteValueCount = 0;
//            currentlySelectedFilter=filterCountMap.get(filterOption);
            filteValueCount = 0;
            for (int i = 0; i < filterAttributeValuesList.size(); i++) {
                if (filterAttributeValuesList.get(i).isChecked())
                    filteValueCount++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (filteValueCount == 0)
            holder.filterOptionBinding.tvFilterValueCount.setVisibility(View.GONE);
        else {
            holder.filterOptionBinding.tvFilterValueCount.setVisibility(View.VISIBLE);
            holder.filterOptionBinding.tvFilterValueCount.setText(filteValueCount + "");
        }
//        for (int i = 0; i < filterAttributeValuesList.size(); i++) {
//            if (filterAttributeValuesList.get(i).isChecked())
//                filteValueCount++;
//        }
        filterValueRecycler.setLayoutManager(new LinearLayoutManager(context));

        if (sSelected == position) {
            filterValueRecycler.setAdapter(new FilterValueAdapter(context, filterAttributeValuesList) {
                @Override
                public void onFilterSelected(List<FilterAttributeValues> selectedFilterAttributeValues) {
                    holder.filterOptionBinding.tvFilterValueCount.setVisibility(View.VISIBLE);
                    if (filterOption.equalsIgnoreCase("brands"))
                        selectedBrands = selectedFilterAttributeValues;
                    else if (filterOption.equalsIgnoreCase("colors"))
                        selectedColors = selectedFilterAttributeValues;
                    else if (filterOption.equalsIgnoreCase("sizes"))
                        selectedSizes = selectedFilterAttributeValues;
                    else
                        selectedFilterData.addAll(selectedFilterAttributeValues);
                    FilterOptionAdapter.this.onFilterSelected(selectedFilterData, selectedBrands, selectedColors, selectedSizes);
                    for (int i = 0; i < filterAttributeValuesList.size(); i++) {
                        for (int j = 0; j < selectedFilterAttributeValues.size(); j++) {
                            if (selectedFilterAttributeValues.get(j) == filterAttributeValuesList.get(i)) {
                                filterAttributeValuesList.get(i).setChecked(true);
                            }
                        }
                    }
                    filteValueCount = 0;
                    for (int i = 0; i < filterAttributeValuesList.size(); i++) {
                        if (filterAttributeValuesList.get(i).isChecked())
                            filteValueCount++;
                    }
                    holder.filterOptionBinding.tvFilterValueCount.setText(filteValueCount + "");
                }
            });
            holder.filterOptionBinding.tvFilterValue.setTextColor(context.getResources().getColor(R.color.cart_grey));
            holder.filterOptionBinding.tvFilterValue.setBackgroundColor(context.getResources().getColor(R.color.white));
        } else {
            holder.filterOptionBinding.tvFilterValue.setTextColor(context.getResources().getColor(R.color.txt_clr_blue));
            holder.filterOptionBinding.tvFilterValue.setBackgroundColor(context.getResources().getColor(R.color.filter_bg));
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

    public void clear() {
        productAttributeList.clear();
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