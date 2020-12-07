package com.hasawi.sears.ui.main.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.hasawi.sears.R;
import com.hasawi.sears.data.api.model.pojo.Category;
import com.hasawi.sears.databinding.LayoutSelectUserCategoryItemBinding;
import com.hasawi.sears.ui.main.listeners.RecyclerviewSingleChoiceClickListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SelectUserCategoryAdapter extends RecyclerView.Adapter<SelectUserCategoryAdapter.ViewHolder> {
    private static RecyclerviewSingleChoiceClickListener sClickListener;
    private static int sSelected = -1;
    ArrayList<Category> categories;
    Context context;

    public SelectUserCategoryAdapter(Context context, ArrayList<Category> categories) {
        this.context = context;
        this.categories = categories;
    }

    public static void setsSelected(int sSelected) {
        SelectUserCategoryAdapter.sSelected = sSelected;
    }

    @NonNull
    @Override
    public SelectUserCategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutSelectUserCategoryItemBinding categoryItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_select_user_category_item, parent, false);
        return new SelectUserCategoryAdapter.ViewHolder(categoryItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectUserCategoryAdapter.ViewHolder holder, int position) {
        Category category = categories.get(position);
        holder.categoryItemBinding.tvItemName.setText(category.getDescriptions().get(0).getCategoryName());
        try {
            Picasso.get().load(category.getDescriptions().get(0).getImageUrl()).into(holder.categoryItemBinding.imageViewItem);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (sSelected == position) {
            selectItem(holder, category);
        } else {
            unselectItem(holder, category);
        }
    }

    public void selectedItem() {
        notifyDataSetChanged();
    }

    public void unselectItem(SelectUserCategoryAdapter.ViewHolder holder, Category adapterItem) {
        holder.categoryItemBinding.cvBackground.setBackground(context.getResources().getDrawable(R.drawable.blue_outlined_rounded_rectangle_24dp));
        holder.categoryItemBinding.imageViewItem.setColorFilter(ContextCompat.getColor(context,
                R.color.txt_clr_blue));
        holder.categoryItemBinding.tvItemName.setTextColor(context.getResources().getColor(R.color.txt_clr_blue));
    }

    public void selectItem(SelectUserCategoryAdapter.ViewHolder holder, Category adapterItem) {
        holder.categoryItemBinding.cvBackground.setBackground(context.getResources().getDrawable(R.drawable.blue_rounded_rectangle));
        holder.categoryItemBinding.imageViewItem.setColorFilter(ContextCompat.getColor(context,
                R.color.white));
        holder.categoryItemBinding.tvItemName.setTextColor(context.getResources().getColor(R.color.white));
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public void setOnItemClickListener(RecyclerviewSingleChoiceClickListener clickListener) {
        sClickListener = clickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LayoutSelectUserCategoryItemBinding categoryItemBinding;

        public ViewHolder(@NonNull LayoutSelectUserCategoryItemBinding categoryItemBinding) {
            super(categoryItemBinding.getRoot());
            this.categoryItemBinding = categoryItemBinding;
            categoryItemBinding.cvBackground.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            sSelected = getAdapterPosition();
            sClickListener.onItemClickListener(getAdapterPosition(), view);
        }

    }
}





