package com.sedsoftware.comicser.features.characterslist;

import android.os.Bundle;
import android.support.annotation.NonNull;
import com.google.android.gms.measurement.AppMeasurement.Event;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.analytics.FirebaseAnalytics.Param;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.sedsoftware.comicser.data.model.ComicCharacterInfoList;
import com.sedsoftware.comicser.data.source.remote.ComicRemoteDataHelper;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import java.util.List;
import javax.inject.Inject;
import timber.log.Timber;

@SuppressWarnings("WeakerAccess")
public class CharactersPresenter extends MvpBasePresenter<CharactersView> {

  final FirebaseAnalytics firebaseAnalytics;
  final ComicRemoteDataHelper remoteDataHelper;

  @Inject
  CharactersPresenter(FirebaseAnalytics firebaseAnalytics,
      ComicRemoteDataHelper remoteDataHelper) {
    this.firebaseAnalytics = firebaseAnalytics;
    this.remoteDataHelper = remoteDataHelper;
  }

  void loadCharactersData(String characterName) {
    Timber.d("Load characters by name: " + characterName);
    remoteDataHelper
        .getCharactersListByName(characterName)
        .subscribe(getObserver());
  }

  void logCharacterSearchEvent(String name) {
    Bundle bundle = new Bundle();
    bundle.putString(Param.ITEM_NAME, name);
    bundle.putString(Param.CONTENT_TYPE, "character");
    firebaseAnalytics.logEvent(Event.SEARCH, bundle);
  }

  private SingleObserver<List<ComicCharacterInfoList>> getObserver() {
    return new SingleObserver<List<ComicCharacterInfoList>>() {
      @Override
      public void onSubscribe(@NonNull Disposable d) {
        Timber.d("Characters data loading started...");
        if (isViewAttached()) {
          Timber.d("Displaying loading view...");
          getView().showEmptyView(false);
          getView().showInitialView(false);
          getView().showLoading(true);
        }
      }

      @Override
      public void onSuccess(@NonNull List<ComicCharacterInfoList> list) {
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
        Timber.d("Characters data loading error: " + e.getMessage());
        if (isViewAttached()) {
          Timber.d("Displaying error view...");
          getView().showError(e, false);
          getView().showLoading(false);
        }
      }
    };
  }
}
