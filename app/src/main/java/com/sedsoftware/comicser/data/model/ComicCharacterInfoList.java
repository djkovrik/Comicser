package com.sedsoftware.comicser.data.model;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

@AutoValue
public abstract class ComicCharacterInfoList {
  public abstract String api_detail_url();
  public abstract long id();
  public abstract String name();
  public abstract ComicImages image();

  public static TypeAdapter<ComicCharacterInfoList> typeAdapter(Gson gson) {
    return new AutoValue_ComicCharacterInfoList.GsonTypeAdapter(gson);
  }
}