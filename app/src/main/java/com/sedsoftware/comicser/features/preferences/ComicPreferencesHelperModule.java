package com.sedsoftware.comicser.features.preferences;

import android.content.Context;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module
public class ComicPreferencesHelperModule {

  @Provides
  @Singleton
  ComicPreferencesHelper providePreferencesHelper(Context context) {
    return new ComicPreferencesHelper(context);
  }
}
