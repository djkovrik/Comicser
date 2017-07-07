package com.sedsoftware.comicser.features.issueslist;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.sedsoftware.comicser.data.model.ComicIssueInfoList;
import com.sedsoftware.comicser.data.source.local.ComicLocalDataHelper;
import com.sedsoftware.comicser.data.source.local.PreferencesHelper;
import com.sedsoftware.comicser.data.source.remote.ComicRemoteDataHelper;
import com.sedsoftware.comicser.utils.DateTextUtils;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.SingleObserver;
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

  boolean issuesListNotEmpty;

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

    issuesListNotEmpty = true;
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
          .subscribe(getIssuesObserver(true));
    } else {
      localDataHelper
          .getTodaysIssuesFromDb()
          .subscribe(getIssuesObserver(false));
    }
  }

  public void loadIssuesByDate(String date) {
    remoteDataHelper
        .getIssuesListByDate(date)
        .subscribe(getIssuesByDateObserver(date));
  }

  public void loadIssuesByDateAndName(String date, String name) {
    remoteDataHelper
        .getIssuesListByDate(date)
        .flatMap(Observable::fromIterable)
        .filter(issue -> issue.volume() != null)
        .filter(issue -> issue.volume().name().contains(name))
        .toList()
        .subscribe(getFilteredIssuesObserver(name));
  }

  private Observer<List<ComicIssueInfoList>> getIssuesObserver(boolean forcedSync) {
    return new Observer<List<ComicIssueInfoList>>() {
      @Override
      public void onSubscribe(@NonNull Disposable d) {
        Timber.tag("Comicser").d("Data loading started (forcedSync = " + forcedSync + ")");
        issuesListNotEmpty = false;
        if (isViewAttached()) {
          getView().showEmptyView(false);
          getView().showLoading(forcedSync);
        }
      }

      @Override
      public void onNext(@NonNull List<ComicIssueInfoList> list) {
        Timber.tag("Comicser").d("Loaded data size: " + list.size());
        if (forcedSync) {
          Timber.tag("Comicser").d("Data loaded from server, saving to db...");
          localDataHelper.removeAllTodaysIssuesFromDb();
          localDataHelper.saveTodaysIssuesToDb(list);
        } else {
          Timber.tag("Comicser").d("Data loaded from db.");
        }

        if (isViewAttached()) {
          if (list.size() > 0) {
            getView().setData(list);
            issuesListNotEmpty = true;
          }
        }
      }

      @Override
      public void onError(@NonNull Throwable e) {
        Timber.tag("Comicser").d("Data loading error: " + e.getMessage());
        if (isViewAttached()) {
          getView().showError(e, forcedSync);
        }
      }

      @Override
      public void onComplete() {
        Timber.tag("Comicser").d("Data loading completed!");
        if (forcedSync) {
          preferencesHelper.setSyncDate(currentDate);
        }
        if (isViewAttached()) {
          if (issuesListNotEmpty) {
            getView().showContent();
            getView().setTitle("Today");
          } else {
            getView().showEmptyView(true);
          }
        }
      }
    };
  }

  private Observer<List<ComicIssueInfoList>> getIssuesByDateObserver(String date) {
    return new Observer<List<ComicIssueInfoList>>() {
      @Override
      public void onSubscribe(@NonNull Disposable d) {
        Timber.tag("Comicser").d("Issues data loading started...");
        issuesListNotEmpty = false;
        if (isViewAttached()) {
          getView().showEmptyView(false);
          getView().showLoading(true);
        }
      }

      @Override
      public void onNext(@NonNull List<ComicIssueInfoList> list) {
        Timber.tag("Comicser").d("Loaded data size: " + list.size());
        if (isViewAttached()) {
          if (list.size() > 0) {
            getView().setData(list);
            issuesListNotEmpty = true;
          }
        }
      }

      @Override
      public void onError(@NonNull Throwable e) {
        Timber.tag("Comicser").d("Data loading error: " + e.getMessage());
        if (isViewAttached()) {
          getView().showError(e, false);
        }
      }

      @Override
      public void onComplete() {
        Timber.tag("Comicser").d("Data loading completed!");
        if (isViewAttached()) {
          if (issuesListNotEmpty) {
            getView().showContent();
            getView().setTitle(DateTextUtils.getFormattedDate(date, "MMM d, yyyy"));
          } else {
            getView().showEmptyView(true);
          }
        }
      }
    };
  }

  private SingleObserver<List<ComicIssueInfoList>> getFilteredIssuesObserver(String name) {
    return new SingleObserver<List<ComicIssueInfoList>>() {
      @Override
      public void onSubscribe(@NonNull Disposable d) {
        Timber.tag("Comicser").d("Issues data loading started...");
        issuesListNotEmpty = false;
        if (isViewAttached()) {
          getView().showEmptyView(false);
          getView().showLoading(true);
        }
      }

      @Override
      public void onSuccess(@NonNull List<ComicIssueInfoList> list) {
        Timber.tag("Comicser").d("Loaded data size: " + list.size());
        if (isViewAttached()) {
          if (list.size() > 0) {
            getView().setData(list);
            issuesListNotEmpty = true;
          }
        }
        if (isViewAttached()) {
          if (issuesListNotEmpty) {
            getView().showContent();
            getView().setTitle(name);
          } else {
            getView().showEmptyView(true);
          }
        }
      }

      @Override
      public void onError(@NonNull Throwable e) {
        Timber.tag("Comicser").d("Data loading error: " + e.getMessage());
        if (isViewAttached()) {
          getView().showError(e, false);
        }
      }
    };
  }
}
