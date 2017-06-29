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
}
