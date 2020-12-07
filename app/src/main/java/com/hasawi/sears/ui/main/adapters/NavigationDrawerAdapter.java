package com.hasawi.sears.ui.main.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.hasawi.sears.R;
import com.hasawi.sears.data.api.model.NavigationMenuItem;
import com.hasawi.sears.databinding.LayoutNavigationDrawerItemBinding;
import com.hasawi.sears.utils.AppConstants;

import java.util.List;

public class NavigationDrawerAdapter extends ArrayAdapter<NavigationMenuItem> {

    Context context;
    List<NavigationMenuItem> menuItemList;

    public NavigationDrawerAdapter(@NonNull Context context, int resource, @NonNull List<NavigationMenuItem> objects) {
        super(context, resource, objects);
        this.context = context;
        this.menuItemList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        LayoutNavigationDrawerItemBinding navigationDrawerItemBinding = DataBindingUtil.
                inflate(inflater, R.layout.layout_navigation_drawer_item, null, false);
        navigationDrawerItemBinding.tvNavDrawerItemName.setText(menuItemList.get(position).getName());
        if (menuItemList.get(position).isEnabled())
            navigationDrawerItemBinding.tvNavDrawerItemName.setTextColor(context.getResources().getColor(R.color.txt_clr_blue));
        else
            navigationDrawerItemBinding.tvNavDrawerItemName.setTextColor(context.getResources().getColor(R.color.txt_clr_grey));
        if (menuItemList.get(position).get_ID() == AppConstants.ID_MENU_NOTIFICATION) {
            navigationDrawerItemBinding.imageView3.setVisibility(View.GONE);
            navigationDrawerItemBinding.swOnOff.setVisibility(View.VISIBLE);
        }
        return navigationDrawerItemBinding.getRoot();

    }

}
