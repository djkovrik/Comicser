package com.sedsoftware.comicser.data.source.local.dagger;

import com.sedsoftware.comicser.data.source.local.LocalDataScope;
import com.sedsoftware.comicser.data.source.local.dagger.modules.ComicLocalDataModule;
import com.sedsoftware.comicser.features.characterdetails.CharacterDetailsComponent;
import com.sedsoftware.comicser.features.characterslist.CharactersComponent;
import com.sedsoftware.comicser.features.issuedetails.IssueDetailsComponent;
import com.sedsoftware.comicser.features.issueslist.IssuesComponent;
import com.sedsoftware.comicser.features.issuesmanager.OwnedIssuesComponent;
import com.sedsoftware.comicser.features.volumedetails.VolumeDetailsComponent;
import com.sedsoftware.comicser.features.volumeslist.VolumesComponent;
import dagger.Subcomponent;

/**
 * Local data component, provides local data helper and preferences injections,
 * depends on remote data component.
 */


@LocalDataScope
@Subcomponent(modules = {ComicLocalDataModule.class})
public interface ComicLocalDataComponent {

  IssuesComponent plusIssuesComponent();

  IssueDetailsComponent plusIssueDetailsComponent();

  VolumesComponent plusVolumesComponent();

  VolumeDetailsComponent plusVolumeDetailsComponent();

  CharactersComponent plusCharactersComponent();

  CharacterDetailsComponent plusCharacterDetailsComponent();

  OwnedIssuesComponent plusOwnedIssuesComponent();
}
