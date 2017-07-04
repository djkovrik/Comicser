package com.sedsoftware.comicser;

import com.sedsoftware.comicser.data.source.local.dagger.ComicDbHelperComponent;
import com.sedsoftware.comicser.data.source.local.dagger.modules.ComicDbHelperModule;
import com.sedsoftware.comicser.data.source.remote.dagger.ComicRemoteDataComponent;
import com.sedsoftware.comicser.data.source.remote.dagger.modules.ComicRemoteDataModule;
import dagger.Component;
import javax.inject.Singleton;

@Singleton
@Component(modules = {ComicserAppModule.class})
public interface ComicserAppComponent {

  ComicRemoteDataComponent plusRemoteComponent(ComicRemoteDataModule module);
  ComicDbHelperComponent plusDbHelperComponent(ComicDbHelperModule module);
}
