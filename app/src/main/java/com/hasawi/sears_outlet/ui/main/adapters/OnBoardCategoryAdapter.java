package com.hasawi.sears_outlet.ui.main.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.hasawi.sears_outlet.R;
import com.hasawi.sears_outlet.data.api.model.pojo.Category;
import com.hasawi.sears_outlet.databinding.LayoutOnboardCategoryAdapterItemBinding;
import com.hasawi.sears_outlet.ui.main.listeners.RecyclerviewSingleChoiceClickListener;

import java.util.ArrayList;

public class OnBoardCategoryAdapter extends RecyclerView.Adapter<OnBoardCategoryAdapter.ViewHolder> {
    private static RecyclerviewSingleChoiceClickListener sClickListener;
    private static int sSelected = -1;
    Context context;
    private ArrayList<Category> categoryArrayList;

    public OnBoardCategoryAdapter(Context context, ArrayList<Category> categoryArrayList) {
        this.context = context;
        this.categoryArrayList = categoryArrayList;
    }

    public static void setsSelected(int sSelected) {
        OnBoardCategoryAdapter.sSelected = sSelected;
    }

    @NonNull
    @Override
    public OnBoardCategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutOnboardCategoryAdapterItemBinding layoutOnboardCategoryAdapterItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.layout_onboard_category_adapter_item, parent, false);
        return new OnBoardCategoryAdapter.ViewHolder(layoutOnboardCategoryAdapterItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull OnBoardCategoryAdapter.ViewHolder holder, int position) {

        holder.layoutOnboardCategoryAdapterItemBinding.btnCategoryItem.setText(categoryArrayList.get(position).getDescriptions().get(0).getCategoryName());
        if (sSelected == position) {
            holder.layoutOnboardCategoryAdapterItemBinding.btnCategoryItem.setBackground(context.getResources().getDrawable(R.drawable.bright_blue_rounded_rectangle_20dp));
            holder.layoutOnboardCategoryAdapterItemBinding.btnCategoryItem.setTextColor(context.getResources().getColor(R.color.white));
        } else {
            holder.layoutOnboardCategoryAdapterItemBinding.btnCategoryItem.setBackground(context.getResources().getDrawable(R.drawable.grey_outlined_rounded_rectangle_20dp));
            holder.layoutOnboardCategoryAdapterItemBinding.btnCategoryItem.setTextColor(context.getResources().getColor(R.color.cart_grey));
        }
    }

    public void selectedItem() {
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return categoryArrayList.size();
    }

    public void setOnItemClickListener(RecyclerviewSingleChoiceClickListener clickListener) {
        sClickListener = clickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LayoutOnboardCategoryAdapterItemBinding layoutOnboardCategoryAdapterItemBinding;

        public ViewHolder(@NonNull LayoutOnboardCategoryAdapterItemBinding layoutOnboardCategoryAdapterItemBinding) {
            super(layoutOnboardCategoryAdapterItemBinding.getRoot());
            this.layoutOnboardCategoryAdapterItemBinding = layoutOnboardCategoryAdapterItemBinding;
            layoutOnboardCategoryAdapterItemBinding.btnCategoryItem.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            sSelected = getAdapterPosition();
            sClickListener.onItemClickListener(getAdapterPosition(), view);
        }
    }
}

