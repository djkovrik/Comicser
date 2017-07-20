package com.sedsoftware.comicser.features.issuesmanager;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.sedsoftware.comicser.data.model.ComicIssueInfoList;
import com.sedsoftware.comicser.data.source.local.ComicLocalDataHelper;
import io.reactivex.Observable;
import io.reactivex.SingleObserver;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import java.util.List;
import javax.inject.Inject;
import timber.log.Timber;

public class OwnedIssuesPresenter extends MvpBasePresenter<OwnedIssuesView> {

  private final ComicLocalDataHelper localDataHelper;

  @Inject
  OwnedIssuesPresenter(ComicLocalDataHelper localDataHelper) {
    this.localDataHelper = localDataHelper;
  }

  void loadOwnedIssues() {
    localDataHelper
        .getOwnedIssuesFromDb()
        .subscribe(getObserver());
  }

  void loadOwnedIssuesFilteredByName(String name) {
    localDataHelper
        .getOwnedIssuesFromDb()
        .flatMapObservable(Observable::fromIterable)
        .filter(issue -> issue.volume() != null)
        .filter(issue -> issue.volume().name().contains(name))
        .toList()
        .subscribe(getObserver());
  }

  private SingleObserver<List<ComicIssueInfoList>> getObserver() {

    return new SingleObserver<List<ComicIssueInfoList>>() {
      @Override
      public void onSubscribe(@NonNull Disposable d) {
        getView().showEmptyView(false);
        getView().showLoading(true);
      }

      @Override
      public void onSuccess(@NonNull List<ComicIssueInfoList> list) {

        if (isViewAttached()) {
          if (list.size() > 0) {
            // Display content
            Timber.d("Displaying content...");
            getView().setData(list);
            getView().showContent();
          } else {
            // Display empty view
            Timber.d("Displaying empty view...");
            getView().showLoading(false);
            getView().showEmptyView(true);
          }
        }
      }

      @Override
      public void onError(@NonNull Throwable e) {
        Timber.d("Data loading error: " + e.getMessage());
        if (isViewAttached()) {
          Timber.d("Displaying error view...");
          getView().showError(e, false);
        }
      }
    };
  }
}
