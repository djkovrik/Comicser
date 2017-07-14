package com.sedsoftware.comicser.features.volumeslist;

import android.os.Bundle;
import com.google.android.gms.measurement.AppMeasurement.Event;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.analytics.FirebaseAnalytics.Param;
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

  final FirebaseAnalytics firebaseAnalytics;
  final ComicRemoteDataHelper remoteDataHelper;

  @Inject
  public VolumesPresenter(FirebaseAnalytics firebaseAnalytics,
      ComicRemoteDataHelper remoteDataHelper) {
    this.firebaseAnalytics = firebaseAnalytics;
    this.remoteDataHelper = remoteDataHelper;
  }


  public void loadVolumesData(String volumeName) {
    Timber.d("Load volumes by name: " + volumeName);
    remoteDataHelper
        .getVolumesListByName(volumeName)
        .subscribe(getObserver());
  }

  public void logVolumeSearchEvent(String name) {
    Bundle bundle = new Bundle();
    bundle.putString(Param.ITEM_NAME, name);
    bundle.putString(Param.CONTENT_TYPE, "volume");
    firebaseAnalytics.logEvent(Event.SEARCH, bundle);
  }

  private SingleObserver<List<ComicVolumeInfoList>> getObserver() {
    return new SingleObserver<List<ComicVolumeInfoList>>() {
      @Override
      public void onSubscribe(@NonNull Disposable d) {
        Timber.d("Volumes data loading started...");
        if (isViewAttached()) {
          Timber.d("Displaying loading view...");
          getView().showEmptyView(false);
          getView().showInitialView(false);
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
            getView().showEmptyView(true);
            getView().showLoading(false);
          }
        }
      }

      @Override
      public void onError(@NonNull Throwable e) {
        Timber.d("Volumes data loading error: " + e.getMessage());
        if (isViewAttached()) {
          Timber.d("Displaying error view...");
          getView().showError(e, false);
          getView().showLoading(false);
        }
      }
    };
  }
}
