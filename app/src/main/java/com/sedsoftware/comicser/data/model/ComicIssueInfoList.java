package com.sedsoftware.comicser.data.model;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

/**
 *  Short issue info block, used in main issues list
 */

@AutoValue
public abstract class ComicIssueInfoList {
  public abstract long id();
  public abstract ComicImages image();
  public abstract int issue_number();
  public abstract String name();
  public abstract String store_date();
  public abstract ComicVolumeInfoShort volume();

  public static TypeAdapter<ComicIssueInfoList> typeAdapter(Gson gson) {
    return new AutoValue_ComicIssueInfoList.GsonTypeAdapter(gson);
  }
}
