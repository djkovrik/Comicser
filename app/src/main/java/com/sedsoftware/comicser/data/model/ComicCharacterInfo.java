package com.sedsoftware.comicser.data.model;

import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import java.util.List;

/**
 *  Full character info:
 *
 *  Sample request:
 *  /character
 *  /4005-42443
 *  ?api_key=API_KEY
 *  &format=json
 *
 *  4005 - constant record type, 42443 - character id
 */

@AutoValue
public abstract class ComicCharacterInfo {
  @Nullable public abstract String aliases();
  @Nullable public abstract String birth();
  @Nullable public abstract List<ComicCharacterInfoShort> character_enemies();
  @Nullable public abstract List<ComicCharacterInfoShort> character_friends();
  public abstract int count_of_issue_appearances();
  @Nullable public abstract String deck();
  @Nullable public abstract String description();
  @Nullable public abstract ComicIssueInfoShort first_appeared_in_issue();
  public abstract int gender();
  public abstract long id();
  @Nullable public abstract ComicImages image();
  @Nullable public abstract String name();
  @Nullable public abstract ComicOriginInfoShort origin();
  @Nullable public abstract String real_name();

  public static TypeAdapter<ComicCharacterInfo> typeAdapter(Gson gson) {
    return new AutoValue_ComicCharacterInfo.GsonTypeAdapter(gson);
  }
}
