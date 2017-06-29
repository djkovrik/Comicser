package com.sedsoftware.comicser.data.model;

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
  public abstract String aliases();
  public abstract String birth();
  public abstract List<ComicCharacterInfoShort> character_enemies();
  public abstract List<ComicCharacterInfoShort> character_friends();
  public abstract int count_of_issue_appearances();
  public abstract String deck();
  public abstract String description();
  public abstract ComicIssueInfoShort first_appeared_in_issue();
  public abstract int gender();
  public abstract long id();
  public abstract ComicImages image();
  public abstract String name();
  public abstract ComicOriginInfoShort origin();
  public abstract String real_name();

  public static TypeAdapter<ComicCharacterInfo> typeAdapter(Gson gson) {
    return new AutoValue_ComicCharacterInfo.GsonTypeAdapter(gson);
  }
}
