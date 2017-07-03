package com.sedsoftware.comicser.data.model;

import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

/**
 *  Short issue info block, used in main issues list
 */

@AutoValue
public abstract class ComicIssueInfoList {
  public abstract long id();
  @Nullable public abstract ComicImages image();
  public abstract int issue_number();
  @Nullable public abstract String name();
  @Nullable public abstract String store_date();
  @Nullable public abstract ComicVolumeInfoShort volume();

  public static TypeAdapter<ComicIssueInfoList> typeAdapter(Gson gson) {
    return new AutoValue_ComicIssueInfoList.GsonTypeAdapter(gson);
  }

  public static Builder builder() {
    return new AutoValue_ComicIssueInfoList.Builder();
  }

  @AutoValue.Builder
  public abstract static class Builder {

    public abstract Builder id(long id);
    public abstract Builder image(ComicImages image);
    public abstract Builder issue_number(int issue_number);
    public abstract Builder name(String name);
    public abstract Builder store_date(String store_date);
    public abstract Builder volume(ComicVolumeInfoShort volume);

    public abstract ComicIssueInfoList build();
  }
}
