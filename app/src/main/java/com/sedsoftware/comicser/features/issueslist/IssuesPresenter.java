package com.sedsoftware.comicser.features.issueslist;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.sedsoftware.comicser.data.model.ComicIssueInfoList;
import com.sedsoftware.comicser.data.source.local.ComicLocalDataHelper;
import com.sedsoftware.comicser.data.source.local.PreferencesHelper;
import com.sedsoftware.comicser.data.source.remote.ComicRemoteDataHelper;
import com.sedsoftware.comicser.utils.DateTextUtils;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import java.util.List;
import javax.inject.Inject;
import timber.log.Timber;

@SuppressWarnings("WeakerAccess")
public class IssuesPresenter extends MvpBasePresenter<IssuesView> {

  final PreferencesHelper preferencesHelper;
  final ComicLocalDataHelper localDataHelper;
  final ComicRemoteDataHelper remoteDataHelper;

  final String currentDate;
  final String lastSyncDate;

  @Inject
  public IssuesPresenter(
      PreferencesHelper preferencesHelper,
      ComicLocalDataHelper localDataHelper,
      ComicRemoteDataHelper remoteDataHelper) {
    this.preferencesHelper = preferencesHelper;
    this.localDataHelper = localDataHelper;
    this.remoteDataHelper = remoteDataHelper;

    currentDate = DateTextUtils.getTodayDateString();
    lastSyncDate = preferencesHelper.getLastSyncDate();
  }

  public boolean shouldNotDisplayShowcases() {
    return preferencesHelper.wasIssuesViewShowcased();
  }

  public void showcaseWasDisplayed() {
    preferencesHelper.markIssuesViewAsShowcased();
  }

  public void loadTodayIssues(boolean pullToRefresh) {

    boolean forcedSync = (pullToRefresh || !(currentDate.equals(lastSyncDate)));

    if (forcedSync) {
      remoteDataHelper
          .getIssuesListByDate(currentDate)
          .doOnSubscribe(disposable -> Timber.tag("Comicser").d("Sync started..."))
          .doOnError(t -> Timber.tag("Comicser").d("Sync error: " + t.getMessage()))
          .doOnComplete(() -> Timber.tag("Comicser").d("Sync completed."))
          .subscribe(getIssuesObserver(true));
    } else {
      localDataHelper
          .getTodaysIssuesFromDb()
          .doOnSubscribe(disposable -> Timber.tag("Comicser").d("Db data loading started..."))
          .doOnError(t -> Timber.tag("Comicser").d("Db data loading error: " + t.getMessage()))
          .doOnComplete(() -> Timber.tag("Comicser").d("Db data loading completed."))
          .subscribe(getIssuesObserver(false));
    }
  }

  public void loadIssuesByDate(String date) {
    remoteDataHelper
        .getIssuesListByDate(date)
        .doOnSubscribe(disposable -> Timber.tag("Comicser").d("Issues data loading started..."))
        .doOnError(t -> Timber.tag("Comicser").d("Issues data loading error: " + t.getMessage()))
        .doOnComplete(() -> Timber.tag("Comicser").d("Issues data loading completed."))
        .subscribe(getIssuesByDateObserver(date));
  }

  private Observer<List<ComicIssueInfoList>> getIssuesObserver(boolean forcedSync) {
    return new Observer<List<ComicIssueInfoList>>() {
      @Override
      public void onSubscribe(@NonNull Disposable d) {
        if (isViewAttached()) {
          getView().showEmptyView(false);
          getView().showLoading(forcedSync);
        }
      }

      @Override
      public void onNext(@NonNull List<ComicIssueInfoList> list) {

        if (forcedSync) {
          localDataHelper.removeAllTodaysIssuesFromDb();
          localDataHelper.saveTodaysIssuesToDb(list);
        }

        if (isViewAttached()) {
          if (list.size() > 0) {
            getView().setData(list);
          } else {
            getView().showEmptyView(true);
          }
        }
      }

      @Override
      public void onError(@NonNull Throwable e) {
        if (isViewAttached()) {
          getView().showError(e, forcedSync);
        }
      }

      @Override
      public void onComplete() {
        if (forcedSync) {
          preferencesHelper.setSyncDate(currentDate);
        }
        if (isViewAttached()) {
          getView().showContent();
          getView().setTitle("today");
        }
      }
    };
  }

  private Observer<List<ComicIssueInfoList>> getIssuesByDateObserver(String date) {
    return new Observer<List<ComicIssueInfoList>>() {
      @Override
      public void onSubscribe(@NonNull Disposable d) {
        if (isViewAttached()) {
          getView().showEmptyView(false);
          getView().showLoading(true);
        }
      }

      @Override
      public void onNext(@NonNull List<ComicIssueInfoList> list) {
        if (isViewAttached()) {
          if (list.size() > 0) {
            getView().setData(list);
          } else {
            getView().showEmptyView(true);
          }
        }
      }

      @Override
      public void onError(@NonNull Throwable e) {
        if (isViewAttached()) {
          getView().showError(e, false);
        }
      }

      @Override
      public void onComplete() {
        if (isViewAttached()) {
          getView().showContent();
          getView().setTitle(DateTextUtils.getFormattedDate(date, "MMM d, yyyy"));
        }
      }
    };
  }
}
