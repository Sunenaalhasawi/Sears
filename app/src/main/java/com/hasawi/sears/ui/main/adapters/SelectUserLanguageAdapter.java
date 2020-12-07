package com.hasawi.sears.ui.main.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.hasawi.sears.R;
import com.hasawi.sears.data.api.model.Language;
import com.hasawi.sears.databinding.LayoutSelectUserDetailAdapterItemUnselectedBinding;
import com.hasawi.sears.ui.main.listeners.RecyclerviewSingleChoiceClickListener;

import java.util.ArrayList;

public class SelectUserLanguageAdapter extends RecyclerView.Adapter<SelectUserLanguageAdapter.ViewHolder> {
    private static RecyclerviewSingleChoiceClickListener sClickListener;
    private static int sSelected = 0;
    ArrayList<Language> languageList;
    Context context;

    public SelectUserLanguageAdapter(Context context, ArrayList<Language> languageList) {
        this.context = context;
        this.languageList = languageList;
    }

    @NonNull
    @Override
    public SelectUserLanguageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutSelectUserDetailAdapterItemUnselectedBinding userDetailAdapterItemUnselectedBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_select_user_detail_adapter_item_unselected, parent, false);
        return new SelectUserLanguageAdapter.ViewHolder(userDetailAdapterItemUnselectedBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectUserLanguageAdapter.ViewHolder holder, int position) {
        Language adapterItem = languageList.get(position);
        holder.selectUserDetailAdapterItemUnselectedBinding.tvItemName.setText(adapterItem.getName());
        holder.selectUserDetailAdapterItemUnselectedBinding.imageViewItem.setImageDrawable(context.getResources().getDrawable(R.drawable.language));
        if (sSelected == position) {
            selectItem(holder, adapterItem);
        } else {
            unselectItem(holder, adapterItem);
        }
    }

    public void selectedItem() {
        notifyDataSetChanged();
    }

    public void unselectItem(ViewHolder holder, Language adapterItem) {
        holder.selectUserDetailAdapterItemUnselectedBinding.cvBackground.setBackground(context.getResources().getDrawable(R.drawable.blue_outlined_rounded_rectangle_24dp));
        holder.selectUserDetailAdapterItemUnselectedBinding.imageViewItem.setColorFilter(ContextCompat.getColor(context,
                R.color.txt_clr_blue));
        holder.selectUserDetailAdapterItemUnselectedBinding.tvItemName.setTextColor(context.getResources().getColor(R.color.txt_clr_blue));
    }

    public void selectItem(ViewHolder holder, Language adapterItem) {
        holder.selectUserDetailAdapterItemUnselectedBinding.cvBackground.setBackground(context.getResources().getDrawable(R.drawable.blue_rounded_rectangle));
        holder.selectUserDetailAdapterItemUnselectedBinding.imageViewItem.setColorFilter(ContextCompat.getColor(context,
                R.color.white));
        holder.selectUserDetailAdapterItemUnselectedBinding.tvItemName.setTextColor(context.getResources().getColor(R.color.white));
    }

    @Override
    public int getItemCount() {
        return languageList.size();
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





