package com.kymjs.gallery;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.ViewPropertyAnimation;
import com.bumptech.glide.request.target.Target;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;
import uk.co.senab.photoview.log.Logger;

/**
 * ViewPager适配器
 *
 * @author kymjs (http://www.kymjs.com/) on 10/19/15.
 */
public class SamplePagerAdapter extends PagerAdapter {
    private Activity aty;
    private String[] imageUrls;

    public SamplePagerAdapter(Activity aty, String[] imageUrls) {
        this.aty = aty;
        this.imageUrls = imageUrls;
    }

    @Override
    public int getCount() {
        return imageUrls.length;
    }

    @Override
    public View instantiateItem(ViewGroup container, int position) {
        View root = View.inflate(aty, R.layout.item_pager, null);
        final PhotoView photoView = (PhotoView) root.findViewById(R.id.images);
        final ProgressBar mProgressBar = (ProgressBar) root.findViewById(R.id.progress);
        mProgressBar.setVisibility(View.VISIBLE);

        Glide.with(aty).load(imageUrls[position])
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        mProgressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        mProgressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(photoView);

        photoView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                aty.finish();
            }
        });

        container.addView(root, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams
                .MATCH_PARENT);
        return root;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

}
