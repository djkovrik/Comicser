package com.sedsoftware.comicser.features.volumeslist;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.sedsoftware.comicser.data.source.local.ComicLocalDataHelper;
import com.sedsoftware.comicser.data.source.remote.ComicRemoteDataHelper;
import javax.inject.Inject;

public class VolumesPresenter extends MvpBasePresenter<VolumesView> {

  final ComicLocalDataHelper localDataHelper;
  final ComicRemoteDataHelper remoteDataHelper;

  @Inject
  public VolumesPresenter(
      ComicLocalDataHelper localDataHelper,
      ComicRemoteDataHelper remoteDataHelper) {
    this.localDataHelper = localDataHelper;
    this.remoteDataHelper = remoteDataHelper;
  }
}
