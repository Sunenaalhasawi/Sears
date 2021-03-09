package com.hasawi.sears_outlet.ui.main.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.hasawi.sears_outlet.R;
import com.hasawi.sears_outlet.data.api.model.BrandData;
import com.hasawi.sears_outlet.databinding.BrandListItemBinding;
import com.hasawi.sears_outlet.databinding.IndexItemBinding;

import java.util.ArrayList;
import java.util.List;

public abstract class AlphabeticBrandAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<BrandData> dataList;

    public AlphabeticBrandAdapter(List<BrandData> dataList) {
        this.dataList = dataList;
    }

    public abstract void onBrandSelected(BrandData brandData);

    @Override
    public int getItemViewType(int position) {
        return dataList.get(position).getIndex() != null ? 0 : 1;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        if (getItemViewType(position) == 0) {
            IndexItemBinding indexItemBinding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()),
                    R.layout.index_item, viewGroup, false);
            return new IndexViewHolder(indexItemBinding);
        } else {
            BrandListItemBinding brandListItemBinding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()),
                    R.layout.brand_list_item, viewGroup, false);
            return new DataViewHolder(brandListItemBinding);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        BrandData data = dataList.get(position);
        if (getItemViewType(position) == 0) {
            IndexViewHolder viewHolder = (IndexViewHolder) holder;
            viewHolder.indexItemBinding.tvIndex.setText(data.getIndex());
        } else {
            DataViewHolder viewHolder = (DataViewHolder) holder;
            viewHolder.brandListItemBinding.tvBrandName.setText(data.getName());
            viewHolder.brandListItemBinding.tvBrandName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBrandSelected(dataList.get(position));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void addAll(List<BrandData> itemList) {
        dataList = new ArrayList<>();
        if (itemList != null && itemList.size() > 0) {
            this.dataList = itemList;
        }
        notifyDataSetChanged();
    }

    class DataViewHolder extends RecyclerView.ViewHolder {
        BrandListItemBinding brandListItemBinding;

        public DataViewHolder(@NonNull BrandListItemBinding brandListItemBinding) {
            super(brandListItemBinding.getRoot());
            this.brandListItemBinding = brandListItemBinding;
        }
    }

    class IndexViewHolder extends RecyclerView.ViewHolder {
        IndexItemBinding indexItemBinding;

        public IndexViewHolder(IndexItemBinding indexItemBinding) {
            super(indexItemBinding.getRoot());
            this.indexItemBinding = indexItemBinding;
        }
    }
}