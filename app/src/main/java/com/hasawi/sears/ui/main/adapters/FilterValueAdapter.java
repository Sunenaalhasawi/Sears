package com.hasawi.sears.ui.main.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.hasawi.sears.R;
import com.hasawi.sears.data.api.model.pojo.FilterAttributeValues;
import com.hasawi.sears.databinding.LayoutFilterValueBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class FilterValueAdapter extends RecyclerView.Adapter<FilterValueAdapter.ViewHolder> {

    Context context;
    Map<String, List<FilterAttributeValues>> filterOptionValueMap;
    List<FilterAttributeValues> filterAttributeValuesList;
    List<FilterAttributeValues> selectedFiltersList = new ArrayList<>();

    public FilterValueAdapter(Context context, List<FilterAttributeValues> filterAttributeValuesList) {
        this.context = context;
        this.filterAttributeValuesList = filterAttributeValuesList;
    }

    public abstract void onFilterSelected(List<FilterAttributeValues> selectedFilterAttributeValues);

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutFilterValueBinding filterValueBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.layout_filter_value, parent, false);
        return new ViewHolder(filterValueBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FilterAttributeValues filterAttributeValues = filterAttributeValuesList.get(position);
        if (filterAttributeValues.isChecked())
            holder.filterValueBinding.checkBoxFilter.setChecked(true);
        else
            holder.filterValueBinding.checkBoxFilter.setChecked(false);
        holder.filterValueBinding.checkBoxFilter.setText(filterAttributeValuesList.get(position).getName());

//        holder.filterValueBinding.cvBackground.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (holder.filterValueBinding.checkBoxFilter.isChecked())
//                    filterAttributeValues.setSelected(true);
//                else
//                    filterAttributeValues.setSelected(false);
////                onFilterSelected(selectedFiltersList);
//            }
//        });

        holder.filterValueBinding.checkBoxFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((AppCompatCheckBox) view).isChecked()) {
                    System.out.println("Checked");
                    filterAttributeValues.setChecked(true);
                    if (!selectedFiltersList.contains(filterAttributeValues))
                        selectedFiltersList.add(filterAttributeValues);
                } else {
                    System.out.println("Un-Checked");
                    filterAttributeValues.setChecked(false);
                    if (selectedFiltersList.contains(filterAttributeValues))
                        selectedFiltersList.remove(filterAttributeValues);
                }
                onFilterSelected(selectedFiltersList);
            }
        });

//        holder.filterValueBinding.checkBoxFilter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    filterAttributeValues.setChecked(true);
//                    if (!selectedFiltersList.contains(filterAttributeValues))
//                        selectedFiltersList.add(filterAttributeValues);
//                } else {
//                    filterAttributeValues.setChecked(false);
//                    if (selectedFiltersList.contains(filterAttributeValues))
//                        selectedFiltersList.remove(filterAttributeValues);
//                }
//
//                onFilterSelected(selectedFiltersList);
//            }
//        });
    }

    public void addAll(List<FilterAttributeValues> valueList) {
        filterAttributeValuesList.clear();
        if (valueList.size() > 0 && valueList != null) {
            filterAttributeValuesList.addAll(valueList);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return filterAttributeValuesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LayoutFilterValueBinding filterValueBinding;

        public ViewHolder(@NonNull LayoutFilterValueBinding filterValueBinding) {
            super(filterValueBinding.getRoot());
            this.filterValueBinding = filterValueBinding;
        }
    }
}
