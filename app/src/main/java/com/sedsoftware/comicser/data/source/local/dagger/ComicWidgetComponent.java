package com.sedsoftware.comicser.data.source.local.dagger;

import com.sedsoftware.comicser.data.source.local.LocalDataScope;
import com.sedsoftware.comicser.data.source.local.dagger.modules.ComicLocalDataModule;
import com.sedsoftware.comicser.features.widget.ComicWidgetFactory;
import dagger.Subcomponent;

@LocalDataScope
@Subcomponent(modules = {ComicLocalDataModule.class})
public interface ComicWidgetComponent {

  void inject(ComicWidgetFactory factory);
}
