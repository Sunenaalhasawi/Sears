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
import com.hasawi.sears.databinding.LayoutHomeDynamicGridItemBinding;

import java.util.ArrayList;
import java.util.List;

public abstract class DynamicGridAdapter extends RecyclerView.Adapter<DynamicGridAdapter.ViewHolder> {
    private Context context;
    private ArrayList<HomeSectionDetail> homeSectionDetailArrayList;

    public DynamicGridAdapter(Context context, ArrayList<HomeSectionDetail> homeSectionDetails) {
        this.context = context;
        if (homeSectionDetails != null && homeSectionDetails.size() > 0)
            this.homeSectionDetailArrayList = homeSectionDetails;
        else
            homeSectionDetailArrayList = new ArrayList<>();

    }

    public abstract void onGridItemClicked(HomeSectionElement homeSectionElement);

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        LayoutHomeDynamicGridItemBinding dynamicGridItemBinding = DataBindingUtil.
                inflate(inflater, R.layout.layout_home_dynamic_grid_item, null, false);
        return new ViewHolder(dynamicGridItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (homeSectionDetailArrayList.size() > 0) {
            try {
                HomeSectionDetail homeSectionDetail = homeSectionDetailArrayList.get(position);
                holder.dynamicGridItemBinding.tvItemName.setText(homeSectionDetail.getName());
                if (homeSectionDetail.getName().equals("")) {
                    holder.dynamicGridItemBinding.tvItemName.setVisibility(View.GONE);
                } else {
                    holder.dynamicGridItemBinding.tvItemName.setText(homeSectionDetail.getName());
                }
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
                    } else
                        imageWidth = imageWidth / homeSectionDetail.getGrid();
                } catch (Exception e) {
                    e.printStackTrace();
                }


                List<HomeSectionElement> sectionElementArrayList = (ArrayList<HomeSectionElement>) homeSectionDetail.getHomeSectionElements();
                for (int i = 0; i < sectionElementArrayList.size(); i++) {
                    HomeSectionElement sectionElement = sectionElementArrayList.get(i);
                    holder.dynamicGridItemBinding.imageSlider.addView(getImageView(sectionElement, sectionElement.getImageUrl(), sectionElement.getName(), imageWidth, imageHeight));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getItemCount() {
        return homeSectionDetailArrayList.size();
    }

    private View getImageView(HomeSectionElement homeSectionElement, String imageUrl, String title, int imageWidth, int imageHeight) {

        LayoutInflater inflater = LayoutInflater.from(context);
        LayoutCarouselItemBinding carouselItemBinding = DataBindingUtil.
                inflate(inflater, R.layout.layout_carousel_item, null, false);
        carouselItemBinding.tvTitle.setText(title);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(imageWidth, LinearLayout.LayoutParams.WRAP_CONTENT);
//        lp.setMargins(0, 0, 16, 0);
        Glide.with(context)
                .load(imageUrl)
                .override(imageWidth, LinearLayout.LayoutParams.WRAP_CONTENT)
                .into(carouselItemBinding.imageItemCarousel);
        carouselItemBinding.getRoot().setLayoutParams(lp);
        if (homeSectionElement.getName().equals(""))
            carouselItemBinding.tvTitle.setVisibility(View.GONE);
        else
            carouselItemBinding.tvTitle.setText(homeSectionElement.getName());
        carouselItemBinding.cvBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onGridItemClicked(homeSectionElement);
            }
        });

//        ImageView imgView = new ImageView(context);

//        imgView.setLayoutParams(lp);
//        imgView.setScaleType(ImageView.ScaleType.FIT_XY);
//        imgView.setAdjustViewBounds(true);

        return carouselItemBinding.getRoot();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        LayoutHomeDynamicGridItemBinding dynamicGridItemBinding;

        public ViewHolder(@NonNull LayoutHomeDynamicGridItemBinding dynamicGridItemBinding) {
            super(dynamicGridItemBinding.getRoot());
            this.dynamicGridItemBinding = dynamicGridItemBinding;
        }
    }

}