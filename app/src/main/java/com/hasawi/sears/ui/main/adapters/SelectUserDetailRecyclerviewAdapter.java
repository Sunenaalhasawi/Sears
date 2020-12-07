package com.hasawi.sears.ui.main.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.hasawi.sears.R;
import com.hasawi.sears.data.api.model.SelectUserDetailAdapterItem;
import com.hasawi.sears.databinding.LayoutSelectUserDetailAdapterItemUnselectedBinding;
import com.hasawi.sears.ui.main.listeners.RecyclerviewSingleChoiceClickListener;

import java.util.ArrayList;

public class SelectUserDetailRecyclerviewAdapter extends RecyclerView.Adapter<SelectUserDetailRecyclerviewAdapter.ViewHolder> {
    private static RecyclerviewSingleChoiceClickListener sClickListener;
    private static int sSelected = -1;
    ArrayList<SelectUserDetailAdapterItem> adapterItemArrayList;
    Context context;

    public SelectUserDetailRecyclerviewAdapter(Context context, ArrayList<SelectUserDetailAdapterItem> adapterItemArrayList) {
        this.context = context;
        this.adapterItemArrayList = adapterItemArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutSelectUserDetailAdapterItemUnselectedBinding userDetailAdapterItemUnselectedBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_select_user_detail_adapter_item_unselected, parent, false);
        return new ViewHolder(userDetailAdapterItemUnselectedBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SelectUserDetailAdapterItem adapterItem = adapterItemArrayList.get(position);
        holder.selectUserDetailAdapterItemUnselectedBinding.tvItemName.setText(adapterItem.getLanguageItem().getName());
        holder.selectUserDetailAdapterItemUnselectedBinding.imageViewItem.setImageDrawable(context.getResources().getDrawable(adapterItem.getUnselectedImage()));
        if (sSelected == position) {
            selectItem(holder, adapterItem);
        } else {
            unselectItem(holder, adapterItem);
        }
    }

    public void selectedItem() {
        notifyDataSetChanged();
    }

    public void unselectItem(ViewHolder holder, SelectUserDetailAdapterItem adapterItem) {
        holder.selectUserDetailAdapterItemUnselectedBinding.cvBackground.setBackground(context.getResources().getDrawable(R.drawable.blue_outlined_rounded_rectangle_24dp));
        holder.selectUserDetailAdapterItemUnselectedBinding.imageViewItem.setBackground(context.getResources().getDrawable(adapterItem.getUnselectedImage()));
        holder.selectUserDetailAdapterItemUnselectedBinding.tvItemName.setTextColor(context.getResources().getColor(R.color.txt_clr_blue));
        adapterItem.setSelected(false);
    }

    public void selectItem(ViewHolder holder, SelectUserDetailAdapterItem adapterItem) {
        holder.selectUserDetailAdapterItemUnselectedBinding.cvBackground.setBackground(context.getResources().getDrawable(R.drawable.blue_rounded_rectangle));
        holder.selectUserDetailAdapterItemUnselectedBinding.imageViewItem.setBackground(context.getResources().getDrawable(adapterItem.getSelectedImage()));
        holder.selectUserDetailAdapterItemUnselectedBinding.tvItemName.setTextColor(context.getResources().getColor(R.color.white));
        adapterItem.setSelected(true);
    }

    @Override
    public int getItemCount() {
        return adapterItemArrayList.size();
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
