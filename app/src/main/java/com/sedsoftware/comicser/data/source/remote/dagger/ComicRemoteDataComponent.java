package com.sedsoftware.comicser.data.source.remote.dagger;

import com.sedsoftware.comicser.data.source.local.dagger.ComicLocalDataComponent;
import com.sedsoftware.comicser.data.source.local.dagger.modules.ComicLocalDataModule;
import com.sedsoftware.comicser.data.source.remote.RemoteDataScope;
import com.sedsoftware.comicser.data.source.remote.dagger.modules.ComicRemoteDataModule;
import dagger.Subcomponent;

/**
 * Remote data component. Provides remote data helper injection, depends on App component.
 */

@RemoteDataScope
@Subcomponent(modules = {ComicRemoteDataModule.class})
public interface ComicRemoteDataComponent {

  ComicLocalDataComponent plusLocalComponent(ComicLocalDataModule module);
  // Other injections here
}
