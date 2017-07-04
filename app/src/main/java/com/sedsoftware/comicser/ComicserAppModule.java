package com.sedsoftware.comicser;

import android.content.Context;
import android.support.annotation.NonNull;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module
public class ComicserAppModule {

  @NonNull
  private final ComicserApp app;

  public ComicserAppModule(@NonNull ComicserApp app) {
    this.app = app;
  }

  @Provides
  @NonNull
  @Singleton
  Context provideContext() {
    return app;
  }
}
