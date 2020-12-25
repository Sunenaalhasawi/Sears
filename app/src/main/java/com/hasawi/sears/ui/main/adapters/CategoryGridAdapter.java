package com.hasawi.sears.ui.main.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.hasawi.sears.R;
import com.hasawi.sears.data.api.model.pojo.Category;
import com.hasawi.sears.databinding.LayoutCategoryGridBinding;

import java.util.ArrayList;

public class CategoryGridAdapter extends BaseAdapter {

    private final Context mContext;
    ArrayList<Category> categoryArrayList = new ArrayList<>();

    public CategoryGridAdapter(Context context, ArrayList<Category> categories) {
        this.mContext = context;
        this.categoryArrayList = categories;
    }

    @Override
    public int getCount() {
        if (categoryArrayList == null)
            return 0;
        else
            return categoryArrayList.size();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        LayoutCategoryGridBinding categoryGridBinding = DataBindingUtil.
                inflate(inflater, R.layout.layout_category_grid, null, false);
        try {
            Glide.with(mContext)
                    .load(categoryArrayList.get(position).getDescriptions().get(0).getImageUrl())
                    .into(categoryGridBinding.imageViewCategory);
            categoryGridBinding.tvCategoryTitle.setText(categoryArrayList.get(position).getDescriptions().get(0).getCategoryName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return categoryGridBinding.getRoot();
    }

}
