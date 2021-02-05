package com.hasawi.sears.ui.main.adapters;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hasawi.sears.R;
import com.hasawi.sears.data.api.model.pojo.HomeSectionDetail;
import com.hasawi.sears.data.api.model.pojo.HomeSectionElement;
import com.hasawi.sears.databinding.LayoutCarouselItemBinding;

import java.util.List;

public abstract class BannerRecyclerAdapter extends RecyclerView.Adapter<BannerRecyclerAdapter.ViewHolder> {

    List<HomeSectionElement> sectionElementArrayList;
    HomeSectionDetail homeSectionDetail;
    Context context;

    public BannerRecyclerAdapter(HomeSectionDetail homeSectionDetail, Context context) {
        this.homeSectionDetail = homeSectionDetail;
        this.sectionElementArrayList = homeSectionDetail.getHomeSectionElements();
        this.context = context;
    }

    public abstract void onSingleBannerClicked(HomeSectionElement homeSectionElement);

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        LayoutCarouselItemBinding carouselItemBinding = DataBindingUtil.
                inflate(inflater, R.layout.layout_carousel_item, null, false);
        return new ViewHolder(carouselItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        int marginLeft = 0, marginRight = 0, marginTop = 0, marginBottom = 0;
        try {
            int imageWidth = 0, imageHeight = 0;
            try {
                Display display = context.getDisplay();
                DisplayMetrics outMetrics = new DisplayMetrics();
                display.getRealMetrics(outMetrics);
                float scWidth = outMetrics.widthPixels;

                imageWidth = (int) scWidth;
                imageHeight = (int) (scWidth * 0.4f);
                if (homeSectionDetail.getGrid() == 1) {
                    imageHeight = LinearLayout.LayoutParams.WRAP_CONTENT;
                } else {
                    imageWidth = imageWidth / homeSectionDetail.getGrid();
                    marginLeft = 8;
                    marginRight = 8;
//                        marginTop = 16;
//                        marginBottom = 16;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            HomeSectionElement homeSectionElement = sectionElementArrayList.get(position);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(imageWidth, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(marginLeft, marginTop, marginRight, marginBottom);
            Glide.with(context)
                    .load(homeSectionElement.getImageUrl())
                    .override(imageWidth, LinearLayout.LayoutParams.WRAP_CONTENT)
                    .into(holder.carouselItemBinding.imageItemCarousel);
            holder.carouselItemBinding.getRoot().setLayoutParams(lp);

            holder.carouselItemBinding.cvBackground.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onSingleBannerClicked(homeSectionElement);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return sectionElementArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        LayoutCarouselItemBinding carouselItemBinding;

        public ViewHolder(@NonNull LayoutCarouselItemBinding carouselItemBinding) {
            super(carouselItemBinding.getRoot());
            this.carouselItemBinding = carouselItemBinding;
        }
    }
}
