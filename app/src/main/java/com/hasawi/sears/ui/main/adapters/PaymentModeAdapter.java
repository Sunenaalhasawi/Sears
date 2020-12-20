package com.hasawi.sears.ui.main.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hasawi.sears.R;
import com.hasawi.sears.data.api.model.pojo.PaymentMode;
import com.hasawi.sears.databinding.LayoutPaymentModeRecyclerItemBinding;
import com.hasawi.sears.ui.main.listeners.RecyclerviewSingleChoiceClickListener;

import java.util.ArrayList;

public class PaymentModeAdapter extends RecyclerView.Adapter<PaymentModeAdapter.ViewHolder> {
    private static RecyclerviewSingleChoiceClickListener sClickListener;
    private static int sSelected = -1;
    Context context;
    private ArrayList<PaymentMode> paymentModeArrayList;

    public PaymentModeAdapter(Context context, ArrayList<PaymentMode> paymentModeArrayList) {
        this.context = context;
        this.paymentModeArrayList = paymentModeArrayList;
    }

    public static void setsSelected(int sSelected) {
        PaymentModeAdapter.sSelected = sSelected;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutPaymentModeRecyclerItemBinding layoutPaymentModeRecyclerItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.layout_payment_mode_recycler_item, parent, false);
        return new ViewHolder(layoutPaymentModeRecyclerItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context)
                .load(paymentModeArrayList.get(position).getIcon())
                .centerCrop()
                .into(holder.layoutPaymentModeRecyclerItemBinding.imagePaymentIcon);
        holder.layoutPaymentModeRecyclerItemBinding.tvPaymentName.setText(paymentModeArrayList.get(position).getName());
        if (sSelected == position) {
            holder.layoutPaymentModeRecyclerItemBinding.lvPaymentModeCod.setBackground(context.getResources().getDrawable(R.drawable.blue_outlined_rounded_rectangle_12dp));
            holder.layoutPaymentModeRecyclerItemBinding.tvPaymentName.setTextColor(context.getResources().getColor(R.color.txt_clr_blue));
        } else {
            holder.layoutPaymentModeRecyclerItemBinding.lvPaymentModeCod.setBackground(context.getResources().getDrawable(R.drawable.grey_outlined_rounded_rectangle_12dp));
            holder.layoutPaymentModeRecyclerItemBinding.tvPaymentName.setTextColor(context.getResources().getColor(R.color.cart_grey));
        }
    }

    public void selectedItem() {
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return paymentModeArrayList.size();
    }

    public void setOnItemClickListener(RecyclerviewSingleChoiceClickListener clickListener) {
        sClickListener = clickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LayoutPaymentModeRecyclerItemBinding layoutPaymentModeRecyclerItemBinding;

        public ViewHolder(@NonNull LayoutPaymentModeRecyclerItemBinding layoutPaymentModeRecyclerItemBinding) {
            super(layoutPaymentModeRecyclerItemBinding.getRoot());
            this.layoutPaymentModeRecyclerItemBinding = layoutPaymentModeRecyclerItemBinding;
            layoutPaymentModeRecyclerItemBinding.lvPaymentModeCod.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            sSelected = getAdapterPosition();
            sClickListener.onItemClickListener(getAdapterPosition(), view);
        }
    }
}
