package com.hasawi.sears_outlet.ui.main.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hasawi.sears_outlet.R;
import com.hasawi.sears_outlet.data.api.model.pojo.Category;
import com.hasawi.sears_outlet.databinding.LayoutCategoryBinding;
import com.hasawi.sears_outlet.ui.main.listeners.RecyclerviewSingleChoiceClickListener;

import java.util.ArrayList;
import java.util.List;

public class CategoryRecyclerviewAdapter extends RecyclerView.Adapter<CategoryRecyclerviewAdapter.ViewHolder> {

    private static RecyclerviewSingleChoiceClickListener sClickListener;
    private static int sSelected = -1;
    private List<Category> categoryArrayList;
    private Context context;

    public CategoryRecyclerviewAdapter(Context context, List<Category> categories) {
        this.categoryArrayList = categories;
        if (categoryArrayList == null)
            categoryArrayList = new ArrayList<>();
        this.context = context;
    }

    public static void setsSelected(int sSelected) {
        CategoryRecyclerviewAdapter.sSelected = sSelected;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutCategoryBinding categoryBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.layout_category, parent, false);
        return new ViewHolder(categoryBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            Category categoryItem = categoryArrayList.get(position);
//        holder.categoryBinding.imageCategory.setImageDrawable(context.getResources().getDrawable());
            Glide.with(context)
                    .load(categoryItem.getDescriptions().get(0).getImageUrl())
                    .centerCrop()
                    .into(holder.categoryBinding.imageCategory);
//            Picasso.get().load(categoryItem.getDescriptions().get(0).getImageUrl()).into(holder.categoryBinding.imageCategory);
            holder.categoryBinding.tvCategory.setText(categoryItem.getDescriptions().get(0).getCategoryName());
            if (sSelected == position) {
                selectItem(holder, categoryItem);
            } else {
                unselectItem(holder, categoryItem);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void unselectItem(CategoryRecyclerviewAdapter.ViewHolder holder, Category adapterItem) {
        holder.categoryBinding.cvBackground.setBackground(context.getResources().getDrawable(R.drawable.grey_outlined_rounded_rectangle_20dp));
//        holder.categoryBinding.imageCategory.setColorFilter(ContextCompat.getColor(context,
//                R.color.txt_clr_blue));
        holder.categoryBinding.tvCategory.setTextColor(context.getResources().getColor(R.color.txt_clr_blue));
    }

    public void selectItem(CategoryRecyclerviewAdapter.ViewHolder holder, Category adapterItem) {
        holder.categoryBinding.cvBackground.setBackground(context.getResources().getDrawable(R.drawable.blue_outlined_rounded_rectangle_20dp));
//        holder.categoryBinding.imageCategory.setColorFilter(ContextCompat.getColor(context,
//                R.color.txt_clr_blue));
        holder.categoryBinding.tvCategory.setTextColor(context.getResources().getColor(R.color.txt_clr_blue));
    }

    public void selectedItem() {
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (categoryArrayList == null)
            return 0;
        else
            return categoryArrayList.size();
    }

    public void setOnItemClickListener(RecyclerviewSingleChoiceClickListener clickListener) {
        sClickListener = clickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        LayoutCategoryBinding categoryBinding;

        public ViewHolder(@NonNull LayoutCategoryBinding layoutCategoryBinding) {
            super(layoutCategoryBinding.getRoot());
            this.categoryBinding = layoutCategoryBinding;
            categoryBinding.cvBackground.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            sSelected = getAdapterPosition();
            sClickListener.onItemClickListener(getAdapterPosition(), view);
        }

    }
}