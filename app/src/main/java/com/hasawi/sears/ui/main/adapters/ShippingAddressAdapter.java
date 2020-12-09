package com.hasawi.sears.ui.main.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.hasawi.sears.R;
import com.hasawi.sears.data.api.model.pojo.Address;
import com.hasawi.sears.databinding.LayoutAddressAdapterItemBinding;
import com.hasawi.sears.ui.main.listeners.RecyclerviewSingleChoiceClickListener;
import com.hasawi.sears.utils.PreferenceHandler;

import java.util.ArrayList;

public abstract class ShippingAddressAdapter extends RecyclerView.Adapter<ShippingAddressAdapter.ViewHolder> {
    private static RecyclerviewSingleChoiceClickListener sClickListener;
    private static int sSelected = -1;
    ArrayList<Address> addressArrayList;
    Context context;

    public ShippingAddressAdapter(Context context, ArrayList<Address> addresses) {
        this.context = context;
        this.addressArrayList = addresses;
    }

    public static void setsSelected(int sSelected) {
        ShippingAddressAdapter.sSelected = sSelected;
    }

    public abstract void onEditClicked(Address address);

    @NonNull
    @Override
    public ShippingAddressAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutAddressAdapterItemBinding addressAdapterItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_address_adapter_item, parent, false);
        return new ShippingAddressAdapter.ViewHolder(addressAdapterItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ShippingAddressAdapter.ViewHolder holder, int position) {
        Address addressItem = addressArrayList.get(position);
        holder.addressAdapterItemBinding.tvLocation.setText("Street: " + addressItem.getStreet());
        holder.addressAdapterItemBinding.tvPost.setText("Area: " + addressItem.getArea());
        holder.addressAdapterItemBinding.tvName.setText(new PreferenceHandler(context, PreferenceHandler.TOKEN_LOGIN).getData(PreferenceHandler.LOGIN_USERNAME, ""));
        holder.addressAdapterItemBinding.tvContact.setText("Flat: " + addressItem.getFlat() + " " + addressItem.getBlock());
        holder.addressAdapterItemBinding.tvEmail.setText("Country: " + addressItem.getCountry());
        if (sSelected == position) {
            holder.addressAdapterItemBinding.cvBackgroundAddress.setBackground(context.getResources().getDrawable(R.drawable.blue_outlined_rounded_rectangle_12dp));
            holder.addressAdapterItemBinding.radioButtonSelectAddress.setChecked(true);
        } else {
            holder.addressAdapterItemBinding.cvBackgroundAddress.setBackground(context.getResources().getDrawable(R.drawable.grey_outlined_rounded_rectangle_12dp));
            holder.addressAdapterItemBinding.radioButtonSelectAddress.setChecked(false);
        }

        holder.addressAdapterItemBinding.imageButtonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEditClicked(addressItem);
            }
        });
        holder.addressAdapterItemBinding.radioButtonSelectAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sSelected == position)
                    holder.addressAdapterItemBinding.radioButtonSelectAddress.setChecked(true);
                else
                    holder.addressAdapterItemBinding.radioButtonSelectAddress.setChecked(false);
            }
        });
    }

    public void selectedItem() {
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return addressArrayList.size();
    }

    public void setOnItemClickListener(RecyclerviewSingleChoiceClickListener clickListener) {
        sClickListener = clickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LayoutAddressAdapterItemBinding addressAdapterItemBinding;

        public ViewHolder(@NonNull LayoutAddressAdapterItemBinding addressAdapterItemBinding) {
            super(addressAdapterItemBinding.getRoot());
            this.addressAdapterItemBinding = addressAdapterItemBinding;
            addressAdapterItemBinding.cvBackgroundAddress.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            sSelected = getAdapterPosition();
            sClickListener.onItemClickListener(getAdapterPosition(), view);
        }

    }
}



