package com.sedsoftware.comicser.features.issuelist;

import dagger.Subcomponent;

@IssueScope
@Subcomponent
public interface IssuesComponent {

  IssuesPresenter presenter();

  void inject(IssuesFragment fragment);
}
