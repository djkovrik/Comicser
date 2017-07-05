package com.sedsoftware.comicser.features.issuelist;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.sedsoftware.comicser.ComicserApp;
import com.sedsoftware.comicser.data.source.local.ComicLocalDataHelper;
import com.sedsoftware.comicser.data.source.local.PreferencesHelper;
import com.sedsoftware.comicser.data.source.local.dagger.modules.ComicLocalDataModule;
import com.sedsoftware.comicser.data.source.remote.ComicRemoteDataHelper;
import com.sedsoftware.comicser.data.source.remote.dagger.modules.ComicRemoteDataModule;
import javax.inject.Inject;

public class IssuesPresenter extends MvpBasePresenter<IssuesView> {

  private PreferencesHelper preferencesHelper;
  private ComicLocalDataHelper localDataHelper;
  private ComicRemoteDataHelper remoteDataHelper;

  @Inject
  public IssuesPresenter(
      PreferencesHelper preferencesHelper,
      ComicLocalDataHelper localDataHelper,
      ComicRemoteDataHelper remoteDataHelper) {
    this.preferencesHelper = preferencesHelper;
    this.localDataHelper = localDataHelper;
    this.remoteDataHelper = remoteDataHelper;
  }

  @Override
  public void attachView(IssuesView view) {
    super.attachView(view);

    ComicserApp
        .getAppComponent()
        .plusRemoteComponent(new ComicRemoteDataModule())
        .plusLocalComponent(new ComicLocalDataModule())
        .inject(this);
  }
}
