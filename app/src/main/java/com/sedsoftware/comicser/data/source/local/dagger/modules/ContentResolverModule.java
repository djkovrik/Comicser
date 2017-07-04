package com.sedsoftware.comicser.data.source.local.dagger.modules;

import android.content.ContentResolver;
import android.content.Context;
import com.sedsoftware.comicser.data.source.local.LocalDataScope;
import dagger.Module;
import dagger.Provides;

@Module
public class ContentResolverModule {

  @Provides
  @LocalDataScope
  ContentResolver provideContentResolver(Context context) {
    return context.getContentResolver();
  }
}
