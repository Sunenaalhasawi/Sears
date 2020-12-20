package com.hasawi.sears.ui.main.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.hasawi.sears.R;
import com.hasawi.sears.data.api.model.pojo.DynamicData;
import com.hasawi.sears.databinding.LayoutHomeBrandsBinding;
import com.hasawi.sears.databinding.LayoutHomeDynamicListViewBinding;
import com.hasawi.sears.databinding.LayoutHomeGridViewBannersBinding;
import com.hasawi.sears.databinding.LayoutHomeNewProductsBinding;
import com.hasawi.sears.databinding.LayoutHomeSingleBannerItemBinding;
import com.hasawi.sears.utils.AppConstants;

import java.util.ArrayList;
import java.util.List;

public class HomeDynamicUiAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<DynamicData> dynamicDataList;
    Context context;

    public HomeDynamicUiAdapter(List<DynamicData> dynamicDataList, Context context) {
        this.dynamicDataList = dynamicDataList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        switch (viewType) {
            case AppConstants
                    .GRID_VIEW:
                LayoutHomeGridViewBannersBinding homeGridViewBannersBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.layout_home_grid_view_banners, parent, false);
                return new GridViewHolder(homeGridViewBannersBinding);
            case AppConstants.NEW_PRODUCTS_VIEW:
                LayoutHomeNewProductsBinding homeNewProductsBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.layout_home_new_products, parent, false);
                return new NewProductsViewHolder(homeNewProductsBinding);
            case AppConstants.SINGLE_BANNER_VIEW:
                LayoutHomeSingleBannerItemBinding singleBannerItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.layout_home_single_banner_item, parent, false);
                return new CustomBannerViewHolder(singleBannerItemBinding);
//            case 3:
//                LayoutHomeBrandsBinding homeBrandsBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
//                        R.layout.layout_home_brands, parent, false);
//                return new BrandsViewHolder(homeBrandsBinding);
//            case 4:
//                LayoutHomeDynamicListViewBinding dynamicListViewBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
//                        R.layout.layout_home_dynamic_list_view, parent, false);
//                return new CustomListViewHolder(dynamicListViewBinding);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        DynamicData dynamicData = dynamicDataList.get(position);
        switch (dynamicData.getVIEW_TYPE()) {
            case AppConstants.GRID_VIEW:
                GridViewHolder gridViewHolder = (GridViewHolder) holder;
                DynamicGridAdapter dynamicGridAdapter = new DynamicGridAdapter(context, (ArrayList<Integer>) dynamicData.getData().getGridBannerList());
                gridViewHolder.homeGridViewBannersBinding.gridViewBanners.setAdapter(dynamicGridAdapter);
                gridViewHolder.homeGridViewBannersBinding.gridViewBanners.setNumColumns(2);

//                grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view,
//                                            int position, long id) {
//                        Toast.makeText(MainActivity.this, "You Clicked at " +web[+ position], Toast.LENGTH_SHORT).show();
//
//                    }
//                });
                break;
            case AppConstants.NEW_PRODUCTS_VIEW:
                NewProductsViewHolder newProductsViewHolder = (NewProductsViewHolder) holder;
                break;
            case AppConstants.SINGLE_BANNER_VIEW:
                CustomBannerViewHolder customBannerViewHolder = (CustomBannerViewHolder) holder;
                break;
        }
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class GridViewHolder extends RecyclerView.ViewHolder {
        LayoutHomeGridViewBannersBinding homeGridViewBannersBinding;

        public GridViewHolder(@NonNull LayoutHomeGridViewBannersBinding homeGridViewBannersBinding) {
            super(homeGridViewBannersBinding.getRoot());
            this.homeGridViewBannersBinding = homeGridViewBannersBinding;
        }
    }

    class NewProductsViewHolder extends RecyclerView.ViewHolder {

        LayoutHomeNewProductsBinding homeNewProductsBinding;

        public NewProductsViewHolder(@NonNull LayoutHomeNewProductsBinding homeNewProductsBinding) {
            super(homeNewProductsBinding.getRoot());
            this.homeNewProductsBinding = homeNewProductsBinding;
        }
    }

    class CustomBannerViewHolder extends RecyclerView.ViewHolder {

        LayoutHomeSingleBannerItemBinding singleBannerItemBinding;

        public CustomBannerViewHolder(@NonNull LayoutHomeSingleBannerItemBinding singleBannerItemBinding) {
            super(singleBannerItemBinding.getRoot());
            this.singleBannerItemBinding = singleBannerItemBinding;
        }
    }

    class BrandsViewHolder extends RecyclerView.ViewHolder {

        LayoutHomeBrandsBinding layoutHomeBrandsBinding;

        public BrandsViewHolder(@NonNull LayoutHomeBrandsBinding layoutHomeBrandsBinding) {
            super(layoutHomeBrandsBinding.getRoot());
            this.layoutHomeBrandsBinding = layoutHomeBrandsBinding;
        }
    }

    class CustomListViewHolder extends RecyclerView.ViewHolder {

        LayoutHomeDynamicListViewBinding dynamicListViewBinding;

        public CustomListViewHolder(@NonNull LayoutHomeDynamicListViewBinding dynamicListViewBinding) {
            super(dynamicListViewBinding.getRoot());
            this.dynamicListViewBinding = dynamicListViewBinding;
        }
    }
}
