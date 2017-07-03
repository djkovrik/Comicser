package com.sedsoftware.comicser.data.model;

import android.support.annotation.Nullable;
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
  public abstract List<ComicCharacterInfoShort> character_credits();
  @Nullable public abstract String cover_date();
  @Nullable public abstract String deck();
  @Nullable public abstract String description();
  public abstract long id();
  @Nullable public abstract ComicImages image();
  public abstract int issue_number();
  @Nullable public abstract String name();
  @Nullable public abstract String store_date();
  public abstract ComicVolumeInfoShort volume();

  public static TypeAdapter<ComicIssueInfo> typeAdapter(Gson gson) {
    return new AutoValue_ComicIssueInfo.GsonTypeAdapter(gson);
  }
}
