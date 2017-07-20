package com.sedsoftware.comicser.data.source.local.dagger.modules;

import android.content.ContentResolver;
import android.content.Context;
import com.sedsoftware.comicser.data.source.local.ComicLocalDataHelper;
import com.sedsoftware.comicser.data.source.local.LocalDataScope;
import dagger.Module;
import dagger.Provides;

@Module
public class ComicLocalDataModule {

  @Provides
  @LocalDataScope
  ComicLocalDataHelper provideComicLocalDataHelper(ContentResolver resolver) {
    return new ComicLocalDataHelper(resolver);
  }

  @Provides
  @LocalDataScope
  ContentResolver provideContentResolver(Context context) {
    return context.getContentResolver();
  }
}
