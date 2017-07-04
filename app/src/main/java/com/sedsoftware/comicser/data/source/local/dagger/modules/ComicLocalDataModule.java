package com.sedsoftware.comicser.data.source.local.dagger.modules;

import android.content.ContentResolver;
import com.sedsoftware.comicser.data.source.local.ComicLocalDataHelper;
import com.sedsoftware.comicser.data.source.local.LocalDataScope;
import dagger.Module;
import dagger.Provides;

@Module(includes = {
    ContentResolverModule.class,
    PreferencesHelperModule.class})

public class ComicLocalDataModule {

  @Provides
  @LocalDataScope
  ComicLocalDataHelper provideComicLocalDataHelper(ContentResolver resolver) {
    return new ComicLocalDataHelper(resolver);
  }
}
