package com.sedsoftware.comicser.utils;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.StringSignature;
import com.sedsoftware.comicser.R;

public class ImageUtils {

  /**
   * External image loading function.
   *
   * @param view Target imageView
   * @param url Image url
   * @param sign Unique sign key which helps to cache images with the same file name separately
   * @param progressBar ProgressBar view which shown while image loading
   */
  public static void loadImageWithProgress(ImageView view, String url, String sign,
      ProgressBar progressBar) {
    Glide.clear(view);
    Glide.with(view.getContext())
        .load(url)
        .signature(new StringSignature(sign))
        .fitCenter()
        .crossFade()
        .listener(new RequestListener<String, GlideDrawable>() {
          @Override
          public boolean onException(Exception e, String model, Target<GlideDrawable> target,
              boolean isFirstResource) {
            progressBar.setVisibility(View.GONE);
            return false;
          }

          @Override
          public boolean onResourceReady(GlideDrawable resource, String model,
              Target<GlideDrawable> target,
              boolean isFromMemoryCache, boolean isFirstResource) {
            progressBar.setVisibility(View.GONE);
            return false;
          }
        })
        .error(R.drawable.placeholder_error)
        .skipMemoryCache(true)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .into(view);
  }

  /**
   * External image loading function.
   *
   * @param view Target imageView
   * @param url Image url
   * @param sign Unique sign key which helps to cache images with the same file name separately
   * @param callback Callback which runs when image loading finished with any result
   */
  public static void loadImageWithCallback(ImageView view, String url, String sign,
      ImageLoadingCallback callback) {
    Glide.clear(view);
    Glide.with(view.getContext())
        .load(url)
        .signature(new StringSignature(sign))
        .fitCenter()
        .crossFade()
        .listener(new RequestListener<String, GlideDrawable>() {
          @Override
          public boolean onException(Exception e, String model, Target<GlideDrawable> target,
              boolean isFirstResource) {
            callback.onFinish(false);
            return false;
          }

          @Override
          public boolean onResourceReady(GlideDrawable resource, String model,
              Target<GlideDrawable> target,
              boolean isFromMemoryCache, boolean isFirstResource) {
            callback.onFinish(true);
            return false;
          }
        })
        .error(R.drawable.placeholder_error)
        .skipMemoryCache(true)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .into(view);
  }

  interface ImageLoadingCallback {

    void onFinish(boolean successful);
  }
}
