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

  public static Builder builder() {
    return new AutoValue_ComicImages.Builder();
  }

  @AutoValue.Builder
  public abstract static class Builder {

    public abstract Builder icon_url(String icon_url);
    public abstract Builder medium_url(String medium_url);
    public abstract Builder screen_url(String screen_url);
    public abstract Builder small_url(String small_url);
    public abstract Builder super_url(String super_url);
    public abstract Builder thumb_url(String thumb_url);
    public abstract Builder tiny_url(String tiny_url);

    public abstract ComicImages build();
  }
}
