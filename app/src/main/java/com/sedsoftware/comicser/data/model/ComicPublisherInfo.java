package com.sedsoftware.comicser.data.model;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

/**
 *  Publisher info, short version only
 */

@AutoValue
public abstract class ComicPublisherInfo {
  public abstract long id();
  public abstract String name();

  public static TypeAdapter<ComicPublisherInfo> typeAdapter(Gson gson) {
    return new AutoValue_ComicPublisherInfo.GsonTypeAdapter(gson);
  }
}
