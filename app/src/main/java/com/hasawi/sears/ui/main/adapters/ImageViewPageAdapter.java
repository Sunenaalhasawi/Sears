package com.hasawi.sears.ui.main.adapters;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.hasawi.sears.R;
import com.hasawi.sears.databinding.LayoutViewPagerImageItemBinding;

import java.util.Objects;

public class ImageViewPageAdapter extends PagerAdapter implements ViewPager.OnPageChangeListener {

    Context context;
    int[] images;
    LayoutInflater mLayoutInflater;
    ViewPager viewPagerImageSlider;
    LayoutViewPagerImageItemBinding viewPagerImageItemBinding;


    // Viewpager Constructor
    public ImageViewPageAdapter(Context context, ViewPager viewPagerProductImages, int[] images) {
        this.context = context;
        this.viewPagerImageSlider = viewPagerProductImages;
        viewPagerImageSlider.addOnPageChangeListener(this);
        this.images = images;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // return the number of images
        return images.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((ConstraintLayout) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        viewPagerImageItemBinding = DataBindingUtil.inflate(mLayoutInflater, R.layout.layout_view_pager_image_item, container, false);
        // setting the image in the imageView
        viewPagerImageItemBinding.imageviewViewPager.setImageResource(images[position]);
        // Adding the View
        Objects.requireNonNull(container).addView(viewPagerImageItemBinding.getRoot());

        return viewPagerImageItemBinding.getRoot();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        container.removeView((ConstraintLayout) object);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onPageSelected(int position) {
        viewPagerImageItemBinding.imageviewViewPager.setBackground(context.getResources().getDrawable(R.drawable.white_rounded_rectangle_16dp));
        viewPagerImageItemBinding.imageviewViewPager.setElevation(context.getResources().getDimension(R.dimen.std_elevation));
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}