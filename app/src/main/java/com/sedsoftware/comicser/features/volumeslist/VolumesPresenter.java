package com.sedsoftware.comicser.features.volumeslist;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.sedsoftware.comicser.data.model.ComicVolumeInfoList;
import com.sedsoftware.comicser.data.source.remote.ComicRemoteDataHelper;
import io.reactivex.SingleObserver;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import java.util.List;
import javax.inject.Inject;
import timber.log.Timber;

public class VolumesPresenter extends MvpBasePresenter<VolumesView> {

  final ComicRemoteDataHelper remoteDataHelper;

  @Inject
  VolumesPresenter(ComicRemoteDataHelper remoteDataHelper) {
    this.remoteDataHelper = remoteDataHelper;
  }

  public void loadVolumesData(String volumeName) {
    Timber.d("Load volumes by name: " + volumeName);
    remoteDataHelper
        .getVolumesListByName(volumeName)
        .subscribe(getObserver());
  }

  private SingleObserver<List<ComicVolumeInfoList>> getObserver() {
    return new SingleObserver<List<ComicVolumeInfoList>>() {
      @Override
      public void onSubscribe(@NonNull Disposable d) {
        Timber.d("Volumes data loading started...");
        if (isViewAttached()) {
          Timber.d("Displaying loading view...");
          getView().showLoading(true);
        }
      }

      @Override
      public void onSuccess(@NonNull List<ComicVolumeInfoList> list) {
        if (isViewAttached()) {
          if (list.size() > 0) {
            // Display content
            Timber.d("Displaying content...");
            getView().setData(list);
            getView().showContent();
          } else {
            // Display empty view
            Timber.d("Displaying empty view...");
          }
        }
      }

      @Override
      public void onError(@NonNull Throwable e) {
        Timber.d("Volumes data loading error: " + e.getMessage());
        if (isViewAttached()) {
          Timber.d("Displaying error view...");
          getView().showError(e, false);
        }
      }
    };
  }
}
