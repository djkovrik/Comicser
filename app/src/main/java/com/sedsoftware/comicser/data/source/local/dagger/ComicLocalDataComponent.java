package com.sedsoftware.comicser.data.source.local.dagger;

import com.sedsoftware.comicser.data.source.local.LocalDataScope;
import com.sedsoftware.comicser.data.source.local.dagger.modules.ComicLocalDataModule;
import dagger.Subcomponent;

/**
 * Local data component, provides local data helper and preferences injections,
 * depends on remote data component.
 */


@LocalDataScope
@Subcomponent(modules = {ComicLocalDataModule.class})
public interface ComicLocalDataComponent {

}
