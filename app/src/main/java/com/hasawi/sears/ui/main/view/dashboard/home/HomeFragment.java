package com.hasawi.sears.ui.main.view.dashboard.home;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.hasawi.sears.R;
import com.hasawi.sears.data.api.model.pojo.Banner;
import com.hasawi.sears.data.api.model.pojo.HomeSectionElement;
import com.hasawi.sears.data.api.model.pojo.Section;
import com.hasawi.sears.data.api.response.DynamicUiResponse;
import com.hasawi.sears.databinding.FragmentHomeBinding;
import com.hasawi.sears.ui.base.BaseFragment;
import com.hasawi.sears.ui.main.adapters.HomeDynamicUiAdapter;
import com.hasawi.sears.ui.main.view.DashboardActivity;
import com.hasawi.sears.ui.main.view.dashboard.product.SelectedProductDetailsFragment;
import com.hasawi.sears.ui.main.view.dashboard.product.paging.ProductListingFragment;
import com.hasawi.sears.ui.main.viewmodel.HomeViewModel;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends BaseFragment {
    FragmentHomeBinding fragmentHomeBinding;
    HomeViewModel homeViewModel;
    List<Banner> bannerList;
    List<Section> sectionList;
    String selectedCategory;
    DashboardActivity dashboardActivity;
    DynamicUiResponse.UiData UiData;
    HomeDynamicUiAdapter homeDynamicUiAdapter;
    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            Glide.with(getActivity())
                    .asBitmap()
                    .load(bannerList.get(position).getImageUrl())
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@Nullable Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            imageView.setImageBitmap(resource);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {

                        }

                    });

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String type = bannerList.get(position).getType();
                    String callback = bannerList.get(position).getCallBack();
                    if (type.equalsIgnoreCase("C")) {
                        Bundle bundle = new Bundle();
                        bundle.putString("category_id", callback);
                        bundle.putString("category_name", bannerList.get(position).getCategory());
                        dashboardActivity.replaceFragment(R.id.fragment_replacer, new ProductListingFragment(), bundle, true, false);

                    } else if (type.equalsIgnoreCase("P")) {
                        Bundle productBundle = new Bundle();
                        productBundle.putString("product_object_id", callback);
                        dashboardActivity.replaceFragment(R.id.fragment_replacer, new SelectedProductDetailsFragment(), productBundle, true, false);
                        dashboardActivity.handleActionMenuBar(true, false, "");

                    } else if (type.equalsIgnoreCase("E")) {
                        Bundle webviewBundle = new Bundle();
                        webviewBundle.putString("url", callback);
                        dashboardActivity.replaceFragment(R.id.fragment_replacer, new WebviewFragment(), webviewBundle, true, false);
                        dashboardActivity.handleActionMenuBar(true, false, "");

                    }
                }
            });
        }
    };

    public HomeFragment(String selectedCategory, DynamicUiResponse.UiData uiData) {
        this.selectedCategory = selectedCategory;
        this.UiData = uiData;
        try {
            if (UiData != null) {
                bannerList = UiData.getBannerList();
                sectionList = UiData.getSection();
                if (bannerList == null)
                    bannerList = new ArrayList<>();
                if (sectionList == null)
                    sectionList = new ArrayList<>();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Fragment newInstance(int position, String selectedCategory, DynamicUiResponse.UiData uiData) {
        HomeFragment homeFragment = new HomeFragment(selectedCategory, uiData);
        return homeFragment;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void setup() {
        fragmentHomeBinding = (FragmentHomeBinding) viewDataBinding;
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        dashboardActivity = (DashboardActivity) getActivity();
        dashboardActivity.handleActionBarIcons(true);
        fragmentHomeBinding.carouselViewBanners.setImageListener(imageListener);
        try {
            if (bannerList != null)
                fragmentHomeBinding.carouselViewBanners.setPageCount(bannerList.size());
            else
                fragmentHomeBinding.carouselViewBanners.setPageCount(0);
            homeDynamicUiAdapter = new HomeDynamicUiAdapter(sectionList, getContext()) {
                @Override
                public void onGridClicked(HomeSectionElement homeSectionElement) {
                    String type = homeSectionElement.getType();
                    String callback = homeSectionElement.getCallBack();
                    if (type.equalsIgnoreCase("C")) {
                        Bundle bundle = new Bundle();
                        bundle.putString("category_id", callback);
//                    bundle.putString("category_name",callback);
                        dashboardActivity.replaceFragment(R.id.fragment_replacer, new ProductListingFragment(), bundle, true, false);

                    } else if (type.equalsIgnoreCase("P")) {
                        Bundle productBundle = new Bundle();
                        productBundle.putString("product_object_id", callback);
                        dashboardActivity.replaceFragment(R.id.fragment_replacer, new SelectedProductDetailsFragment(), productBundle, true, false);
                        dashboardActivity.handleActionMenuBar(true, false, "");

                    } else if (type.equalsIgnoreCase("E")) {
                        Bundle webviewBundle = new Bundle();
                        webviewBundle.putString("url", callback);
                        dashboardActivity.replaceFragment(R.id.fragment_replacer, new WebviewFragment(), webviewBundle, true, false);
                        dashboardActivity.handleActionMenuBar(true, false, "");

                    }
                }
            };
            fragmentHomeBinding.recyclerviewDynamicUI.setLayoutManager(new LinearLayoutManager(getContext()));
            fragmentHomeBinding.recyclerviewDynamicUI.setAdapter(homeDynamicUiAdapter);
        } catch (Exception e) {
            e.printStackTrace();
            FirebaseCrashlytics.getInstance().log(e.getMessage());
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        dashboardActivity.handleActionMenuBar(false, true, "");
    }
}
