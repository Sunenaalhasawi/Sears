package com.hasawi.sears.ui.main.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.hasawi.sears.R;
import com.hasawi.sears.databinding.LayoutOfferItemBinding;

import java.util.ArrayList;
import java.util.List;

public class OffersAdapter extends ArrayAdapter<String> {

    Context context;
    List<String> offersList;


    public OffersAdapter(@NonNull Context context, int resource, @NonNull ArrayList<String> list) {
        super(context, resource, list);
        this.context = context;
        this.offersList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        LayoutOfferItemBinding layoutOfferItemBinding = DataBindingUtil.
                inflate(inflater, R.layout.layout_offer_item, null, false);
        layoutOfferItemBinding.tvOffer.setText(offersList.get(position));
        return layoutOfferItemBinding.getRoot();
    }

}
