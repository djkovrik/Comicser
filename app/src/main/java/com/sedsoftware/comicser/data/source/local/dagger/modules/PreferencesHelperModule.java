package com.sedsoftware.comicser.data.source.local.dagger.modules;

import android.content.Context;
import com.sedsoftware.comicser.data.source.local.LocalDataScope;
import com.sedsoftware.comicser.data.source.local.PreferencesHelper;
import dagger.Module;
import dagger.Provides;

@Module
public class PreferencesHelperModule {

  @Provides
  @LocalDataScope
  PreferencesHelper providePreferencesHelper(Context context) {
    return new PreferencesHelper(context);
  }
}
