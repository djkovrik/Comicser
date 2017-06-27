package com.sedsoftware.comicser.data.model;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

/**
 *  Basic sever response
 *  @param <T> - main record type
 */

@AutoValue
public abstract class ServerResponse<T> {
  public abstract String error();
  public abstract long limit();
  public abstract long offset();
  public abstract long number_of_page_results();
  public abstract long number_of_total_results();
  public abstract long status_code();
  public abstract T results();
  public abstract double version();

  public static <T> TypeAdapter<ServerResponse<T>> typeAdapter(Gson gson,
      TypeToken<? extends ServerResponse<T>> typeToken) {
    return new AutoValue_ServerResponse.GsonTypeAdapter<>(gson, typeToken);
  }
}
