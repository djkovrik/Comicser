package com.sedsoftware.comicser.data.model;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import java.util.List;

/**
 *  Full issue info.
 *
 *  Sample request:
 *  /issue
 *  /4000-599906
 *  ?api_key=API_KEY
 *  &format=json
 *
 *  4000 - constant record type, 599906 - issue id
 */

@AutoValue
public abstract class ComicIssueInfo {
  public abstract String api_detail_url();
  public abstract List<ComicCharacterInfoShort> character_credits();
  public abstract String cover_date();
  public abstract String deck();
  public abstract String description();
  public abstract long id();
  public abstract ComicImages image();
  public abstract int issue_number();
  public abstract String name();
  public abstract String store_date();
  public abstract ComicVolumeInfoShort volume();

  public static TypeAdapter<ComicIssueInfo> typeAdapter(Gson gson) {
    return new AutoValue_ComicIssueInfo.GsonTypeAdapter(gson);
  }
}
