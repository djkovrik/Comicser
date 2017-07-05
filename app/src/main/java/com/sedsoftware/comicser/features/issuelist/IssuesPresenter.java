package com.sedsoftware.comicser.features.issuelist;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.sedsoftware.comicser.ComicserApp;
import com.sedsoftware.comicser.data.model.ComicIssueInfoList;
import com.sedsoftware.comicser.data.source.local.ComicLocalDataHelper;
import com.sedsoftware.comicser.data.source.local.PreferencesHelper;
import com.sedsoftware.comicser.data.source.local.dagger.modules.ComicLocalDataModule;
import com.sedsoftware.comicser.data.source.remote.ComicRemoteDataHelper;
import com.sedsoftware.comicser.data.source.remote.dagger.modules.ComicRemoteDataModule;
import com.sedsoftware.comicser.utils.DateUtils;
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

    currentDate = DateUtils.getTodayDateString();
    lastSyncDate = preferencesHelper.getLastSyncDate();
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
        .subscribe(getIssuesObserver(false));
  }

  private Observer<List<ComicIssueInfoList>> getIssuesObserver(boolean forcedSync) {
    return new Observer<List<ComicIssueInfoList>>() {
      @Override
      public void onSubscribe(@NonNull Disposable d) {
        if (isViewAttached()) {
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
            getView().showContent();
          } else {
            getView().showEmptyView();
          }
        }
      }

      @Override
      public void onError(@NonNull Throwable e) {
        if (isViewAttached()) {
          getView().showLoading(false);
          getView().showError(e, forcedSync);
        }
      }

      @Override
      public void onComplete() {
        if (forcedSync) {
          preferencesHelper.setSyncDate(currentDate);
        }
        if (isViewAttached()) {
          getView().showLoading(false);
        }
      }
    };
  }
}
