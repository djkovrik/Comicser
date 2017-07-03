package com.sedsoftware.comicser.data.model;

import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

/**
 *  Short volume info block, used in main issues list
 */

@AutoValue
public abstract class ComicVolumeInfoList {
  public abstract int count_of_issues();
  public abstract long id();
  @Nullable public abstract ComicImages image();
  @Nullable public abstract String name();
  @Nullable public abstract ComicPublisherInfo publisher();

  public static TypeAdapter<ComicVolumeInfoList> typeAdapter(Gson gson) {
    return new AutoValue_ComicVolumeInfoList.GsonTypeAdapter(gson);
  }
}
