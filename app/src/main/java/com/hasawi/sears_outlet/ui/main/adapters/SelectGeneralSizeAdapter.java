package com.hasawi.sears_outlet.ui.main.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.hasawi.sears_outlet.R;
import com.hasawi.sears_outlet.databinding.LayoutSelectUserDetailAdapterItemUnselectedBinding;
import com.hasawi.sears_outlet.ui.main.listeners.RecyclerviewSingleChoiceClickListener;

import java.util.ArrayList;

public class SelectGeneralSizeAdapter extends RecyclerView.Adapter<SelectGeneralSizeAdapter.ViewHolder> {
    private static RecyclerviewSingleChoiceClickListener sClickListener;
    private static int sSelected = -1;
    ArrayList<String> sizeList;
    Context context;

    public SelectGeneralSizeAdapter(Context context, ArrayList<String> sizeList) {
        this.context = context;
        this.sizeList = sizeList;
    }

    @NonNull
    @Override
    public SelectGeneralSizeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutSelectUserDetailAdapterItemUnselectedBinding userDetailAdapterItemUnselectedBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_select_user_detail_adapter_item_unselected, parent, false);
        return new SelectGeneralSizeAdapter.ViewHolder(userDetailAdapterItemUnselectedBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectGeneralSizeAdapter.ViewHolder holder, int position) {
        String adapterItem = sizeList.get(position);
        holder.selectUserDetailAdapterItemUnselectedBinding.tvItemName.setText(adapterItem);
        holder.selectUserDetailAdapterItemUnselectedBinding.imageViewItem.setImageDrawable(context.getResources().getDrawable(R.drawable.tshirt));
        if (sSelected == position) {
            selectItem(holder, adapterItem);
        } else {
            unselectItem(holder, adapterItem);
        }
    }

    public void selectedItem() {
        notifyDataSetChanged();
    }

    public void unselectItem(ViewHolder holder, String adapterItem) {
        holder.selectUserDetailAdapterItemUnselectedBinding.cvBackground.setBackground(context.getResources().getDrawable(R.drawable.blue_outlined_rounded_rectangle_24dp));
        holder.selectUserDetailAdapterItemUnselectedBinding.imageViewItem.setColorFilter(ContextCompat.getColor(context,
                R.color.txt_clr_blue));
        holder.selectUserDetailAdapterItemUnselectedBinding.tvItemName.setTextColor(context.getResources().getColor(R.color.txt_clr_blue));
    }

    public void selectItem(ViewHolder holder, String adapterItem) {
        holder.selectUserDetailAdapterItemUnselectedBinding.cvBackground.setBackground(context.getResources().getDrawable(R.drawable.blue_rounded_rectangle));
        holder.selectUserDetailAdapterItemUnselectedBinding.imageViewItem.setColorFilter(ContextCompat.getColor(context,
                R.color.white));
        holder.selectUserDetailAdapterItemUnselectedBinding.tvItemName.setTextColor(context.getResources().getColor(R.color.white));
    }

    @Override
    public int getItemCount() {
        return sizeList.size();
    }

    public void setOnItemClickListener(RecyclerviewSingleChoiceClickListener clickListener) {
        sClickListener = clickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LayoutSelectUserDetailAdapterItemUnselectedBinding selectUserDetailAdapterItemUnselectedBinding;

        public ViewHolder(@NonNull LayoutSelectUserDetailAdapterItemUnselectedBinding adapterItemUnselectedBinding) {
            super(adapterItemUnselectedBinding.getRoot());
            this.selectUserDetailAdapterItemUnselectedBinding = adapterItemUnselectedBinding;
            selectUserDetailAdapterItemUnselectedBinding.cvBackground.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            sSelected = getAdapterPosition();
            sClickListener.onItemClickListener(getAdapterPosition(), view);
        }

    }
}





