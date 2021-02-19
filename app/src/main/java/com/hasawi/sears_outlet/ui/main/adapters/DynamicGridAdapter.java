package com.hasawi.sears_outlet.ui.main.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hasawi.sears_outlet.R;
import com.hasawi.sears_outlet.data.api.model.pojo.HomeSectionDetail;
import com.hasawi.sears_outlet.data.api.model.pojo.HomeSectionElement;
import com.hasawi.sears_outlet.databinding.LayoutHomeDynamicGridItemBinding;

import java.util.ArrayList;

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

//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        int marginLeft = 0, marginRight = 0, marginTop = 0, marginBottom = 0;
//        if (homeSectionDetailArrayList.size() > 0) {
//            try {
//                HomeSectionDetail homeSectionDetail = homeSectionDetailArrayList.get(position);
//                holder.dynamicGridItemBinding.tvItemName.setText(homeSectionDetail.getName());
//                if (homeSectionDetail.getName().equals("")) {
//                    holder.dynamicGridItemBinding.tvItemName.setVisibility(View.GONE);
//                } else {
//                    holder.dynamicGridItemBinding.tvItemName.setText(homeSectionDetail.getName());
//                }
//                int imageWidth = 0, imageHeight = 0;
//                try {
//                    Display display = context.getDisplay();
//                    DisplayMetrics outMetrics = new DisplayMetrics();
//                    display.getRealMetrics(outMetrics);
//                    float scWidth = outMetrics.widthPixels;
//
//                    imageWidth = (int) scWidth;
//                    imageHeight = (int) (scWidth * 0.4f);
//                    if (homeSectionDetail.getGrid() == 1) {
//                        imageHeight = LinearLayout.LayoutParams.WRAP_CONTENT;
//                    } else {
//                        imageWidth = imageWidth / homeSectionDetail.getGrid();
//                        marginLeft = 8;
//                        marginRight = 8;
////                        marginTop = 16;
////                        marginBottom = 16;
//                    }
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//
//                List<HomeSectionElement> sectionElementArrayList = (ArrayList<HomeSectionElement>) homeSectionDetail.getHomeSectionElements();
//                for (int i = 0; i < sectionElementArrayList.size(); i++) {
//                    HomeSectionElement sectionElement = sectionElementArrayList.get(i);
//                    holder.dynamicGridItemBinding.imageSlider.addView(getImageView(sectionElement, sectionElement.getImageUrl(), sectionElement.getName(), imageWidth, imageHeight, marginLeft, marginRight, marginTop, marginBottom));
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int marginLeft = 0, marginRight = 0, marginTop = 0, marginBottom = 0;
        if (homeSectionDetailArrayList.size() > 0) {
            try {
                HomeSectionDetail homeSectionDetail = homeSectionDetailArrayList.get(position);
//                holder.dynamicGridItemBinding.tvItemName.setText(homeSectionDetail.getName());
//                if (homeSectionDetail.getName().equals("")) {
//                    holder.dynamicGridItemBinding.tvItemName.setVisibility(View.GONE);
//                } else {
//                    holder.dynamicGridItemBinding.tvItemName.setText(homeSectionDetail.getName());
//                }

                BannerRecyclerAdapter bannerRecyclerAdapter = new BannerRecyclerAdapter(homeSectionDetail, context) {
                    @Override
                    public void onSingleBannerClicked(HomeSectionElement homeSectionElement) {
                        onGridItemClicked(homeSectionElement);
                    }
                };
                LinearLayoutManager layoutManager = new LinearLayoutManager(context,
                        LinearLayoutManager.HORIZONTAL,
                        false);
                holder.dynamicGridItemBinding.recycelrviewBanners.setLayoutManager(layoutManager);
                holder.dynamicGridItemBinding.recycelrviewBanners.setAdapter(bannerRecyclerAdapter);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getItemCount() {
        return homeSectionDetailArrayList.size();
    }

//    private View getImageView(HomeSectionElement homeSectionElement, String imageUrl, String title, int imageWidth, int imageHeight, int marginLeft, int marginRight, int marginTop, int marginBottom) {
//
//        LayoutInflater inflater = LayoutInflater.from(context);
//        LayoutCarouselItemBinding carouselItemBinding = DataBindingUtil.
//                inflate(inflater, R.layout.layout_carousel_item, null, false);
//        carouselItemBinding.tvTitle.setText(title);
//
//        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(imageWidth, LinearLayout.LayoutParams.WRAP_CONTENT);
//        lp.setMargins(marginLeft, marginTop, marginRight, marginBottom);
//        Glide.with(context)
//                .load(imageUrl)
//                .override(imageWidth, LinearLayout.LayoutParams.WRAP_CONTENT)
//                .into(carouselItemBinding.imageItemCarousel);
//        carouselItemBinding.getRoot().setLayoutParams(lp);
////        LinearLayout.LayoutParams newlp = new LinearLayout.LayoutParams( LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
////        lp.setMargins(marginLeft, marginTop, marginRight, marginBottom);
////        carouselItemBinding.cvBackground.setLayoutParams(newlp);
//        if (homeSectionElement.getName().equals(""))
//            carouselItemBinding.tvTitle.setVisibility(View.GONE);
//        else
//            carouselItemBinding.tvTitle.setText(homeSectionElement.getName());
//        carouselItemBinding.cvBackground.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onGridItemClicked(homeSectionElement);
//            }
//        });
//
////        ImageView imgView = new ImageView(context);
//
////        imgView.setLayoutParams(lp);
////        imgView.setScaleType(ImageView.ScaleType.FIT_XY);
////        imgView.setAdjustViewBounds(true);
//
//        return carouselItemBinding.getRoot();
//    }

    class ViewHolder extends RecyclerView.ViewHolder {
        LayoutHomeDynamicGridItemBinding dynamicGridItemBinding;

        public ViewHolder(@NonNull LayoutHomeDynamicGridItemBinding dynamicGridItemBinding) {
            super(dynamicGridItemBinding.getRoot());
            this.dynamicGridItemBinding = dynamicGridItemBinding;
        }
    }

}