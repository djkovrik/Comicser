package com.sedsoftware.comicser.utils;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.sedsoftware.comicser.R;

public class ImageUtils {

  public static void loadImageWithProgress(ImageView view, String url, ProgressBar progressBar) {
    Glide.clear(view);
    Glide.with(view.getContext())
        .load(url)
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
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .into(view);
  }
}
