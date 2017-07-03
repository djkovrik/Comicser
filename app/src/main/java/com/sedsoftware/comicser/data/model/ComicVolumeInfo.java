package com.sedsoftware.comicser.data.model;

import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import java.util.List;

/**
 *  Full volume info.
 *
 *  Sample request:
 *  /volume
 *  /4050-18166
 *  ?api_key=API_KEY
 *  &format=json
 *
 *  4050 - constant record type, volume - issue id
 */

@AutoValue
public abstract class ComicVolumeInfo {
  @Nullable public abstract List<ComicCharacterInfoShort> characters();
  public abstract int count_of_issues();
  @Nullable public abstract String deck();
  @Nullable public abstract String description();
  @Nullable public abstract ComicIssueInfoShort first_issue();
  public abstract long id();
  @Nullable public abstract ComicImages image();
  @Nullable public abstract List<ComicIssueInfoShort> issues();
  @Nullable public abstract ComicIssueInfoShort last_issue();
  @Nullable public abstract String name();
  @Nullable public abstract ComicPublisherInfo publisher();
  public abstract int start_year();

  public static TypeAdapter<ComicVolumeInfo> typeAdapter(Gson gson) {
    return new AutoValue_ComicVolumeInfo.GsonTypeAdapter(gson);
  }
}
