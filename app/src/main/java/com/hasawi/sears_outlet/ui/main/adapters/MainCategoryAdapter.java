package com.hasawi.sears_outlet.ui.main.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.hasawi.sears_outlet.R;
import com.hasawi.sears_outlet.data.api.model.pojo.Category;
import com.hasawi.sears_outlet.databinding.LayoutCategoryButtonBinding;

import java.util.List;

public class MainCategoryAdapter extends RecyclerView.Adapter<MainCategoryAdapter.ViewHolder> {

    List<Category> mainCategoryList;

    public MainCategoryAdapter(List<Category> mainCategoryList) {
        this.mainCategoryList = mainCategoryList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutCategoryButtonBinding categoryButtonBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.layout_category_button, parent, false);
        return new ViewHolder(categoryButtonBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String name = mainCategoryList.get(position).getDescriptions().get(0).getCategoryName();
        String upperCase = name.toUpperCase();
        holder.categoryButtonBinding.btnSelectMainCategory.setText("SHOP " + upperCase);

    }

    @Override
    public int getItemCount() {
        return mainCategoryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LayoutCategoryButtonBinding categoryButtonBinding;

        public ViewHolder(@NonNull LayoutCategoryButtonBinding categoryButtonBinding) {
            super(categoryButtonBinding.getRoot());
            this.categoryButtonBinding = categoryButtonBinding;
        }
    }
}
