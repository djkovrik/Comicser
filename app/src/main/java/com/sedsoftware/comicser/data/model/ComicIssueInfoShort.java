package com.sedsoftware.comicser.data.model;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

/**
 *  Short issue info, used in some server responses
 */

@AutoValue
public abstract class ComicIssueInfoShort {
  public abstract String api_detail_url();
  public abstract long id();
  public abstract String name();
  public abstract int issue_number();

  public static TypeAdapter<ComicIssueInfoShort> typeAdapter(Gson gson) {
    return new AutoValue_ComicIssueInfoShort.GsonTypeAdapter(gson);
  }
}
