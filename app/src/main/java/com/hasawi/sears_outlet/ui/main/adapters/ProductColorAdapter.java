package com.hasawi.sears_outlet.ui.main.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.hasawi.sears_outlet.R;
import com.hasawi.sears_outlet.databinding.LayoutColorVariantAdapterItemBinding;
import com.hasawi.sears_outlet.ui.main.listeners.RecyclerviewSingleChoiceClickListener;

import java.util.ArrayList;

public class ProductColorAdapter extends RecyclerView.Adapter<ProductColorAdapter.ViewHolder> {
//    ArrayList<ProductConfigurable> colorList;
ArrayList<String> colorList;
    Context context;
    private static RecyclerviewSingleChoiceClickListener sClickListener;
    private static int sSelected = 0;

    public ProductColorAdapter(Context context, ArrayList<String> colorList) {
        this.context = context;
        this.colorList = colorList;
    }

    public static void setsSelected(int sSelected) {
        ProductColorAdapter.sSelected = sSelected;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutColorVariantAdapterItemBinding colorVariantAdapterItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_color_variant_adapter_item, parent, false);
        return new ViewHolder(colorVariantAdapterItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
//            Drawable drawable = context.getResources().getDrawable(R.drawable.circle);
//            GradientDrawable gradientDrawable = (GradientDrawable) drawable;
//            if (colorList.get(position).getColorCode() != null)
//                gradientDrawable.setColor(Color.parseColor(colorList.get(position).getColorCode()));
//            gradientDrawable.setStroke(3, context.getResources().getColor(R.color.grey));

//            if (sSelected == position) {
//                holder.colorVariantAdapterItemBinding.cvColor.setBackground(context.getResources().getDrawable(R.drawable.grey_outlined_circle));
//            } else {
//                holder.colorVariantAdapterItemBinding.cvColor.setBackground(context.getResources().getDrawable(R.drawable.plane_circle));
//            }
//            holder.colorVariantAdapterItemBinding.imageViewColorVariant.setImageDrawable(
//                    gradientDrawable);

//            if (colorList.get(position).getColor() != null && !colorList.get(position).getColor().equalsIgnoreCase(""))
            if (colorList.get(position) != null && !colorList.get(position).equalsIgnoreCase(""))
                holder.colorVariantAdapterItemBinding.tvColorVariant.setText(colorList.get(position));
            else
                holder.colorVariantAdapterItemBinding.tvColorVariant.setVisibility(View.GONE);
            if (sSelected == position) {
                holder.colorVariantAdapterItemBinding.tvColorVariant.setBackground(context.getResources().getDrawable(R.drawable.green_rounded_rectangle_8dp));
                holder.colorVariantAdapterItemBinding.tvColorVariant.setTextColor(context.getResources().getColor(R.color.white));
            } else {
                holder.colorVariantAdapterItemBinding.tvColorVariant.setBackground(context.getResources().getDrawable(R.drawable.grey_outlined_rounded_rectangle));
                holder.colorVariantAdapterItemBinding.tvColorVariant.setTextColor(context.getResources().getColor(R.color.text_grey));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setOnItemClickListener(RecyclerviewSingleChoiceClickListener clickListener) {
        sClickListener = clickListener;
    }


    public void selectedItem() {
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return colorList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LayoutColorVariantAdapterItemBinding colorVariantAdapterItemBinding;

        public ViewHolder(@NonNull LayoutColorVariantAdapterItemBinding layoutColorVariantAdapterItemBinding) {
            super(layoutColorVariantAdapterItemBinding.getRoot());
            this.colorVariantAdapterItemBinding = layoutColorVariantAdapterItemBinding;
            colorVariantAdapterItemBinding.tvColorVariant.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            sSelected = getAdapterPosition();
            sClickListener.onItemClickListener(getAdapterPosition(), v);
        }
    }
}
