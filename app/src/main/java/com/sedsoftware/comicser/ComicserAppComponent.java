package com.sedsoftware.comicser;

import com.sedsoftware.comicser.data.source.local.dagger.ComicDbHelperComponent;
import com.sedsoftware.comicser.data.source.local.dagger.ComicWidgetComponent;
import com.sedsoftware.comicser.data.source.local.dagger.modules.ComicDbHelperModule;
import com.sedsoftware.comicser.data.source.remote.dagger.ComicRemoteDataComponent;
import com.sedsoftware.comicser.data.source.remote.dagger.modules.ComicRemoteDataModule;
import com.sedsoftware.comicser.features.navigation.NavigationActivity;
import com.sedsoftware.comicser.features.preferences.PreferencesHelperModule;
import dagger.Component;
import javax.inject.Singleton;

@Singleton
@Component(modules = {ComicserAppModule.class, PreferencesHelperModule.class})
public interface ComicserAppComponent {

  ComicDbHelperComponent plusDbHelperComponent(ComicDbHelperModule module);

  ComicWidgetComponent plusWidgetComponent();

  ComicRemoteDataComponent plusRemoteComponent(ComicRemoteDataModule module);

  void inject(NavigationActivity activity);
}
