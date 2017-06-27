package com.sedsoftware.comicser.data.model;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

/**
 *  Images block, used everywhere
 */

@AutoValue
public abstract class ComicImages {
  public abstract String icon_url();
  public abstract String medium_url();
  public abstract String screen_url();
  public abstract String small_url();
  public abstract String super_url();
  public abstract String thumb_url();
  public abstract String tiny_url();

  public static TypeAdapter<ComicImages> typeAdapter(Gson gson) {
    return new AutoValue_ComicImages.GsonTypeAdapter(gson);
  }
}
