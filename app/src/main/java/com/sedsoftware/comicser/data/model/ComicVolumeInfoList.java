package com.sedsoftware.comicser.data.model;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

/**
 *  Short volume info block, used in main issues list
 */

@AutoValue
public abstract class ComicVolumeInfoList {
  public abstract String api_detail_url();
  public abstract int count_of_issues();
  public abstract long id();
  public abstract ComicImages image();
  public abstract String name();
  public abstract ComicPublisherInfo publisher();

  public static TypeAdapter<ComicVolumeInfoList> typeAdapter(Gson gson) {
    return new AutoValue_ComicVolumeInfoList.GsonTypeAdapter(gson);
  }
}
