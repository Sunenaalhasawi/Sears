package com.hasawi.sears.ui.main.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hasawi.sears.R;
import com.hasawi.sears.data.api.model.pojo.Category;
import com.hasawi.sears.databinding.LayoutMainCategoryItemBinding;

import java.util.ArrayList;

public class MainCategoryAdapter extends RecyclerView.Adapter<MainCategoryAdapter.ViewHolder> {
    ArrayList<Category> categories;
    Context context;

    public MainCategoryAdapter(Context context, ArrayList<Category> categories) {
        this.context = context;
        this.categories = categories;
    }

    @NonNull
    @Override
    public MainCategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutMainCategoryItemBinding mainCategoryItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_main_category_item, parent, false);
        return new MainCategoryAdapter.ViewHolder(mainCategoryItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MainCategoryAdapter.ViewHolder holder, int position) {
        Category category = categories.get(position);
        try {
            Glide.with(context)
                    .load(category.getDescriptions().get(0).getImageUrl())
                    .centerCrop()
                    .into(holder.mainCategoryItemBinding.imageViewItem);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LayoutMainCategoryItemBinding mainCategoryItemBinding;

        public ViewHolder(@NonNull LayoutMainCategoryItemBinding mainCategoryItemBinding) {
            super(mainCategoryItemBinding.getRoot());
            this.mainCategoryItemBinding = mainCategoryItemBinding;
        }


    }
}





