package com.hasawi.sears_outlet.ui.main.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.hasawi.sears_outlet.R;
import com.hasawi.sears_outlet.data.api.model.pojo.Manufacture;
import com.hasawi.sears_outlet.databinding.LayoutOnboradBrandAdapterItemBinding;

import java.util.ArrayList;

public abstract class OnBoardBrandAdapter extends RecyclerView.Adapter<OnBoardBrandAdapter.ViewHolder> {
    Context context;
    private ArrayList<Manufacture> brandList;
    private ArrayList<Manufacture> selectedBrandList = new ArrayList<>();

    public OnBoardBrandAdapter(Context context, ArrayList<Manufacture> brandList) {
        this.context = context;
        this.brandList = brandList;
    }

    public abstract void onBrandSelected(ArrayList<Manufacture> brandList);

    @NonNull
    @Override
    public OnBoardBrandAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutOnboradBrandAdapterItemBinding layoutOnboradBrandAdapterItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.layout_onborad_brand_adapter_item, parent, false);
        return new OnBoardBrandAdapter.ViewHolder(layoutOnboradBrandAdapterItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull OnBoardBrandAdapter.ViewHolder holder, int position) {
        if (brandList.get(position).isChecked()) {
            holder.layoutOnboradBrandAdapterItemBinding.checkBox.setChecked(true);
            holder.layoutOnboradBrandAdapterItemBinding.checkBox.setVisibility(View.VISIBLE);
        } else {
            holder.layoutOnboradBrandAdapterItemBinding.checkBox.setChecked(false);
            holder.layoutOnboradBrandAdapterItemBinding.checkBox.setVisibility(View.GONE);
        }
        holder.layoutOnboradBrandAdapterItemBinding.tvBrandName.setText(brandList.get(position).getManufactureDescriptions().get(0).getName());
        holder.layoutOnboradBrandAdapterItemBinding.cvBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (holder.layoutOnboradBrandAdapterItemBinding.checkBox.getVisibility() == View.VISIBLE) {
                    holder.layoutOnboradBrandAdapterItemBinding.checkBox.setVisibility(View.GONE);
                    brandList.get(position).setChecked(false);
                    holder.layoutOnboradBrandAdapterItemBinding.tvBrandName.setTextColor(context.getResources().getColor(R.color.txt_grey));
                    if (selectedBrandList.contains(brandList.get(position)))
                        selectedBrandList.remove(brandList.get(position));
                } else {
                    if (!selectedBrandList.contains(brandList.get(position)))
                        selectedBrandList.add(brandList.get(position));
                    brandList.get(position).setChecked(true);
                    holder.layoutOnboradBrandAdapterItemBinding.checkBox.setVisibility(View.VISIBLE);
                    holder.layoutOnboradBrandAdapterItemBinding.checkBox.setChecked(true);
                    holder.layoutOnboradBrandAdapterItemBinding.tvBrandName.setTextColor(context.getResources().getColor(R.color.bright_blue));
                }
                onBrandSelected(selectedBrandList);
            }
        });

        holder.layoutOnboradBrandAdapterItemBinding.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    holder.layoutOnboradBrandAdapterItemBinding.checkBox.setVisibility(View.GONE);
                    holder.layoutOnboradBrandAdapterItemBinding.tvBrandName.setTextColor(context.getResources().getColor(R.color.txt_grey));
                }
            }
        });


//        holder.layoutOnboradBrandAdapterItemBinding.checkBox.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (((AppCompatCheckBox) view).isChecked()) {
//
//                } else {
//
//                }
//                onBrandSelected(selectedBrandList);
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return brandList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        LayoutOnboradBrandAdapterItemBinding layoutOnboradBrandAdapterItemBinding;

        public ViewHolder(@NonNull LayoutOnboradBrandAdapterItemBinding layoutOnboradBrandAdapterItemBinding) {
            super(layoutOnboradBrandAdapterItemBinding.getRoot());
            this.layoutOnboradBrandAdapterItemBinding = layoutOnboradBrandAdapterItemBinding;
        }
    }
}
