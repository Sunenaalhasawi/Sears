package com.hasawi.sears_outlet.ui.main.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.hasawi.sears_outlet.R;
import com.hasawi.sears_outlet.databinding.IndexListItemBinding;

import java.util.ArrayList;
import java.util.List;

public abstract class IndexAdapter extends RecyclerView.Adapter<IndexAdapter.ViewHolder> {

    Context context;
    private List<String> alphabetsList;

    public IndexAdapter(List<String> alphabetsList, Context context) {
        this.context = context;
        this.alphabetsList = alphabetsList;
    }

    public abstract void onSelectedIndex(String index, int position);

    @NonNull
    @Override
    public IndexAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        IndexListItemBinding indexItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.index_list_item, parent, false);
        return new IndexAdapter.ViewHolder(indexItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull IndexAdapter.ViewHolder holder, int position) {
        holder.indexItemBinding.tvIndex.setText(alphabetsList.get(position));
        holder.indexItemBinding.tvIndex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelectedIndex(alphabetsList.get(position), position);
            }
        });
    }

    public void selectedItem() {
        notifyDataSetChanged();
    }

    public void addAll(List<String> itemList) {
        alphabetsList = new ArrayList<>();
        if (itemList != null && itemList.size() > 0) {
            this.alphabetsList = itemList;
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return alphabetsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        IndexListItemBinding indexItemBinding;

        public ViewHolder(@NonNull IndexListItemBinding indexItemBinding) {
            super(indexItemBinding.getRoot());
            this.indexItemBinding = indexItemBinding;
        }

    }
}
