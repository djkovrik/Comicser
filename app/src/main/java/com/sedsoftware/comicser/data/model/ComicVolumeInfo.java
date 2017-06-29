package com.sedsoftware.comicser.data.model;

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
  public abstract List<ComicCharacterInfoShort> characters();
  public abstract int count_of_issues();
  public abstract String deck();
  public abstract String description();
  public abstract ComicIssueInfoShort first_issue();
  public abstract long id();
  public abstract ComicImages image();
  public abstract List<ComicIssueInfoShort> issues();
  public abstract ComicIssueInfoShort last_issue();
  public abstract String name();
  public abstract ComicPublisherInfo publisher();
  public abstract int start_year();

  public static TypeAdapter<ComicVolumeInfo> typeAdapter(Gson gson) {
    return new AutoValue_ComicVolumeInfo.GsonTypeAdapter(gson);
  }
}
