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
  public abstract int start_year();

  public static TypeAdapter<ComicVolumeInfoList> typeAdapter(Gson gson) {
    return new AutoValue_ComicVolumeInfoList.GsonTypeAdapter(gson);
  }

  public static Builder builder() {
    return new AutoValue_ComicVolumeInfoList.Builder();
  }

  @AutoValue.Builder
  public abstract static class Builder {

    public abstract Builder count_of_issues(int count_of_issues);
    public abstract Builder id(long id);
    public abstract Builder image(ComicImages image);
    public abstract Builder name(String name);
    public abstract Builder publisher(ComicPublisherInfo publisher);
    public abstract Builder start_year(int start_year);

    public abstract ComicVolumeInfoList build();
  }
}
