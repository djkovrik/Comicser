package com.sedsoftware.comicser.features.preferences;

import android.content.Context;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module
public class PreferencesHelperModule {

  @Provides
  @Singleton
  PreferencesHelper providePreferencesHelper(Context context) {
    return new PreferencesHelper(context);
  }
}
