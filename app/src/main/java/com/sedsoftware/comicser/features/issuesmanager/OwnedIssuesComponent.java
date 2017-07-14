package com.sedsoftware.comicser.features.issuesmanager;

import dagger.Subcomponent;

@OwnedIssueScope
@Subcomponent
public interface OwnedIssuesComponent {

  OwnedIssuesPresenter presenter();

  void inject(OwnedIssuesFragment fragment);
}