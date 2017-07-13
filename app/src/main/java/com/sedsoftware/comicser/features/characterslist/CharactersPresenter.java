package com.sedsoftware.comicser.features.characterslist;

import android.support.annotation.NonNull;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.sedsoftware.comicser.data.model.ComicCharacterInfoList;
import com.sedsoftware.comicser.data.source.remote.ComicRemoteDataHelper;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import java.util.List;
import javax.inject.Inject;
import timber.log.Timber;

public class CharactersPresenter extends MvpBasePresenter<CharactersView> {

  final ComicRemoteDataHelper remoteDataHelper;

  @Inject
  CharactersPresenter(ComicRemoteDataHelper remoteDataHelper) {
    this.remoteDataHelper = remoteDataHelper;
  }

  public void loadCharactersData(String characterName) {
    Timber.d("Load characters by name: " + characterName);
    remoteDataHelper
        .getCharactersListByName(characterName)
        .subscribe(getObserver());
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