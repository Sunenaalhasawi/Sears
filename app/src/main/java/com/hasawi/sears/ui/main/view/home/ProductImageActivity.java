package com.hasawi.sears.ui.main.view.home;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.hasawi.sears.R;
import com.hasawi.sears.databinding.ActivityImageShowingBinding;
import com.hasawi.sears.ui.base.BaseActivity;

public class ProductImageActivity extends BaseActivity {

    ActivityImageShowingBinding imageShowingBinding;
    private ScaleGestureDetector mScaleGestureDetector;
    private float mScaleFactor = 1.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_showing);
        imageShowingBinding = DataBindingUtil.setContentView(this, R.layout.activity_image_showing);
        try {
            String imageUrl = getIntent().getStringExtra("image_url");
            Glide.with(this)
                    .load(imageUrl)
                    .centerCrop()
                    .into(imageShowingBinding.imageViewSelected);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mScaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        mScaleGestureDetector.onTouchEvent(motionEvent);
        return true;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            mScaleFactor *= scaleGestureDetector.getScaleFactor();
            mScaleFactor = Math.max(0.1f,
                    Math.min(mScaleFactor, 10.0f));
            imageShowingBinding.imageViewSelected.setScaleX(mScaleFactor);
            imageShowingBinding.imageViewSelected.setScaleY(mScaleFactor);
            return true;
        }
    }
}
