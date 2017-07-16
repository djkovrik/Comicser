package com.sedsoftware.comicser.data.source.local.dagger;

import com.sedsoftware.comicser.data.source.local.LocalDataScope;
import com.sedsoftware.comicser.data.source.local.dagger.modules.ComicLocalDataModule;
import com.sedsoftware.comicser.features.issuedetails.IssueDetailsComponent;
import com.sedsoftware.comicser.features.issueslist.IssuesComponent;
import com.sedsoftware.comicser.features.issuesmanager.OwnedIssuesComponent;
import com.sedsoftware.comicser.features.sync.ComicSyncAdapter;
import com.sedsoftware.comicser.features.volumedetails.VolumeDetailsComponent;
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

  VolumeDetailsComponent plusVolumeDetailsComponent();

  OwnedIssuesComponent plusOwnedIssuesComponent();

  void inject(ComicSyncAdapter adapter);
}
