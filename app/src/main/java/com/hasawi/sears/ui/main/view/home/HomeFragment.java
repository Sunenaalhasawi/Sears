package com.hasawi.sears.ui.main.view.home;

import android.widget.ImageView;

import com.hasawi.sears.R;
import com.hasawi.sears.databinding.FragmentHomeNewBinding;
import com.hasawi.sears.ui.base.BaseFragment;
import com.synnapps.carouselview.ImageListener;

public class HomeFragment extends BaseFragment {
    FragmentHomeNewBinding fragmentHomeNewBinding;
    int[] sampleImages = {R.drawable.banner_1, R.drawable.banner_1, R.drawable.banner_1, R.drawable.banner_1, R.drawable.banner_1};
    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            imageView.setImageResource(sampleImages[position]);
//            Glide.with(getActivity())
//                    .asBitmap()
//                    .load(imgUrls.get(position))
//                    .into(new CustomTarget<Bitmap>() {
//                        @Override
//                        public void onResourceReady(@Nullable Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
//                            imageView.setImageBitmap(resource);
//                        }
//
//                        @Override
//                        public void onLoadCleared(@Nullable Drawable placeholder) {
//
//                        }
//
//                    });
        }
    };

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_home_new;
    }

    @Override
    protected void setup() {
        fragmentHomeNewBinding = (FragmentHomeNewBinding) viewDataBinding;
        fragmentHomeNewBinding.carouselViewBanners.setPageCount(sampleImages.length);
        fragmentHomeNewBinding.carouselViewBanners.setImageListener(imageListener);


    }
}
