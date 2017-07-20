package com.sedsoftware.comicser.features.characterdetails;

import android.support.annotation.NonNull;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.sedsoftware.comicser.data.model.ComicCharacterInfo;
import com.sedsoftware.comicser.data.source.remote.ComicRemoteDataHelper;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import javax.inject.Inject;
import timber.log.Timber;

public class CharacterDetailsPresenter extends MvpBasePresenter<CharacterDetailsView> {

  private final ComicRemoteDataHelper remoteDataHelper;

  @Inject
  CharacterDetailsPresenter(ComicRemoteDataHelper remoteDataHelper) {
    this.remoteDataHelper = remoteDataHelper;
  }

  void loadCharacterDetails(long characterId) {
    remoteDataHelper
        .getCharacterDetailsById(characterId)
        .subscribe(getCharacterDetailsObserver());
  }

  private SingleObserver<ComicCharacterInfo> getCharacterDetailsObserver() {
    return new SingleObserver<ComicCharacterInfo>() {
      @Override
      public void onSubscribe(@NonNull Disposable d) {
        Timber.d("Character details loading started...");
        getView().showLoading(true);
      }

      @Override
      public void onSuccess(@NonNull ComicCharacterInfo comicCharacterInfo) {
        Timber.d("Character details loading completed.");
        if (isViewAttached()) {
          getView().showLoading(false);
          getView().setData(comicCharacterInfo);
        }
      }

      @Override
      public void onError(@NonNull Throwable e) {
        Timber.d("Character details loading error: " + e.getMessage());
        getView().showError(e, false);
      }
    };
  }
}
