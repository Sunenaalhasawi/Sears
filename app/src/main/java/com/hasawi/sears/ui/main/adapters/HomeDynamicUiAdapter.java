package com.hasawi.sears.ui.main.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hasawi.sears.R;
import com.hasawi.sears.data.api.model.pojo.HomeSectionDetail;
import com.hasawi.sears.data.api.model.pojo.HomeSectionElement;
import com.hasawi.sears.data.api.model.pojo.Section;
import com.hasawi.sears.databinding.LayoutHomeGridViewBannersBinding;

import java.util.ArrayList;
import java.util.List;

public abstract class HomeDynamicUiAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<Section> sectionList;
    Context context;

    public HomeDynamicUiAdapter(List<Section> sectionList, Context context) {
        if (sectionList != null && sectionList.size() > 0) {
            this.sectionList = sectionList;
        } else
            sectionList = new ArrayList<>();
        this.context = context;
    }

    public abstract void onGridClicked(HomeSectionElement homeSectionElement);

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutHomeGridViewBannersBinding homeGridViewBannersBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.layout_home_grid_view_banners, parent, false);
        return new GridViewHolder(homeGridViewBannersBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Section sectionItem = sectionList.get(position);
        GridViewHolder gridViewHolder = (GridViewHolder) holder;
        List<HomeSectionDetail> homeSectionDetailList = sectionItem.getHomeSectionDetails();
        DynamicGridAdapter dynamicGridAdapter = new DynamicGridAdapter(context, (ArrayList<HomeSectionDetail>) homeSectionDetailList) {
            @Override
            public void onGridItemClicked(HomeSectionElement homeSectionElement) {
                onGridClicked(homeSectionElement);
            }
        };
        gridViewHolder.homeGridViewBannersBinding.gridViewBanners.setLayoutManager(new LinearLayoutManager(context));
        gridViewHolder.homeGridViewBannersBinding.gridViewBanners.setAdapter(dynamicGridAdapter);
    }

    @Override
    public int getItemCount() {
        if (sectionList == null)
            return 0;
        else
            return sectionList.size();
    }

    class GridViewHolder extends RecyclerView.ViewHolder {
        LayoutHomeGridViewBannersBinding homeGridViewBannersBinding;

        public GridViewHolder(@NonNull LayoutHomeGridViewBannersBinding homeGridViewBannersBinding) {
            super(homeGridViewBannersBinding.getRoot());
            this.homeGridViewBannersBinding = homeGridViewBannersBinding;
        }
    }

}
