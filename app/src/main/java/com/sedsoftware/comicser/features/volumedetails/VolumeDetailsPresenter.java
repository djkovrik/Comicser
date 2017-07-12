package com.sedsoftware.comicser.features.volumedetails;

import android.support.annotation.NonNull;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.sedsoftware.comicser.data.model.ComicVolumeInfo;
import com.sedsoftware.comicser.data.source.local.ComicLocalDataHelper;
import com.sedsoftware.comicser.data.source.remote.ComicRemoteDataHelper;
import com.sedsoftware.comicser.utils.ContentUtils;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import javax.inject.Inject;
import timber.log.Timber;

public class VolumeDetailsPresenter extends MvpBasePresenter<VolumeDetailsView> {

  final ComicLocalDataHelper localDataHelper;
  final ComicRemoteDataHelper remoteDataHelper;

  @Inject
  VolumeDetailsPresenter(
      ComicLocalDataHelper localDataHelper,
      ComicRemoteDataHelper remoteDataHelper) {
    this.localDataHelper = localDataHelper;
    this.remoteDataHelper = remoteDataHelper;
  }

  void setUpBookmarkIconState(long volumeId) {
    if (isViewAttached()) {
      if (isCurrentVolumeTracked(volumeId)) {
        getView().markAsTracked();
      } else {
        getView().unmarkAsTracked();
      }
    }
  }

  boolean isCurrentVolumeTracked(long volumeId) {
    return localDataHelper.isVolumeTracked(volumeId);
  }

  void trackVolime(ComicVolumeInfo volume) {
    localDataHelper.saveTrackedVolumeToDb(ContentUtils.shortenVolumeInfo(volume));
  }

  void removeBookmark(long volumeId) {
    localDataHelper.removeTrackedVolumeFromDb(volumeId);
  }

  void loadIssueDetails(long volumeId) {
    remoteDataHelper
        .getVolumeDetailsById(volumeId)
        .subscribe(getVolumeDetailsObserver());
  }

  private SingleObserver<ComicVolumeInfo> getVolumeDetailsObserver() {
    return new SingleObserver<ComicVolumeInfo>() {
      @Override
      public void onSubscribe(@NonNull Disposable d) {
        Timber.d("Volume details loading started...");
        getView().showLoading(true);
      }

      @Override
      public void onSuccess(@NonNull ComicVolumeInfo comicVolumeInfo) {
        Timber.d("Volume details loading completed.");
        if (isViewAttached()) {
          getView().showLoading(false);
          getView().setData(comicVolumeInfo);
        }
      }

      @Override
      public void onError(@NonNull Throwable e) {
        Timber.d("Volume details loading error: " + e.getMessage());
        getView().showError(e, false);
      }
    };
  }
}
