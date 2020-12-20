package com.hasawi.sears.ui.main.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.databinding.DataBindingUtil;

import com.hasawi.sears.R;
import com.hasawi.sears.data.api.model.DynamicGridItem;
import com.hasawi.sears.databinding.LayoutHomeDynamicGridItemBinding;

import java.util.ArrayList;

public class DynamicGridAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<DynamicGridItem> gridItemArrayList = new ArrayList<>();

    public DynamicGridAdapter(Context context, ArrayList<DynamicGridItem> gridItems) {
        this.context = context;
        this.gridItemArrayList = gridItems;
    }

    @Override
    public int getCount() {
        return gridItemArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        LayoutHomeDynamicGridItemBinding dynamicGridItemBinding = DataBindingUtil.
                inflate(inflater, R.layout.layout_home_dynamic_grid_item, null, false);
        DynamicGridItem dynamicGridItem = gridItemArrayList.get(position);
        try {
            dynamicGridItemBinding.imageViewItem.setImageDrawable(dynamicGridItem.getItemDrawable());
            if (dynamicGridItem.getItemName() != null && !dynamicGridItem.getItemName().equalsIgnoreCase(""))
                dynamicGridItemBinding.tvItemName.setText(dynamicGridItem.getItemName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dynamicGridItemBinding.getRoot();

    }

}