package com.sedsoftware.comicser.utils.custom;

import com.google.gson.TypeAdapterFactory;
import com.ryanharter.auto.value.gson.GsonTypeAdapterFactory;
import com.sedsoftware.comicser.utils.AutoValueGson_MyGsonAdapterFactory;

@GsonTypeAdapterFactory
public abstract class MyGsonAdapterFactory implements TypeAdapterFactory {

  public static TypeAdapterFactory create() {
    return new AutoValueGson_MyGsonAdapterFactory();
  }
}
