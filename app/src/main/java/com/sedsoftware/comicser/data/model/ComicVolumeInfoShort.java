package com.sedsoftware.comicser.data.model;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

/**
 *  Short volume info, used in some server responses
 */

@AutoValue
public abstract class ComicVolumeInfoShort {
  public abstract long id();
  public abstract String name();

  public static TypeAdapter<ComicVolumeInfoShort> typeAdapter(Gson gson) {
    return new AutoValue_ComicVolumeInfoShort.GsonTypeAdapter(gson);
  }

  public static Builder builder() {
    return new AutoValue_ComicVolumeInfoShort.Builder();
  }

  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder id(long id);
    public abstract Builder name(String name);

    public abstract ComicVolumeInfoShort build();
  }
}
