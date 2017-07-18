package com.sedsoftware.comicser.features.issueslist;

import android.content.Context;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.sedsoftware.comicser.R;
import com.sedsoftware.comicser.data.model.ComicIssueInfoList;
import com.sedsoftware.comicser.data.source.local.ComicLocalDataHelper;
import com.sedsoftware.comicser.features.preferences.PreferencesHelper;
import com.sedsoftware.comicser.data.source.remote.ComicRemoteDataHelper;
import com.sedsoftware.comicser.features.sync.ComicSyncManager;
import com.sedsoftware.comicser.utils.DateTextUtils;
import com.sedsoftware.comicser.utils.NetworkUtils;
import io.reactivex.Observable;
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
  final Context context;

  @Inject
  public IssuesPresenter(
      PreferencesHelper preferencesHelper,
      ComicLocalDataHelper localDataHelper,
      ComicRemoteDataHelper remoteDataHelper,
      Context context) {
    this.preferencesHelper = preferencesHelper;
    this.localDataHelper = localDataHelper;
    this.remoteDataHelper = remoteDataHelper;
    this.context = context;
  }

  public boolean shouldNotDisplayShowcases() {
    return preferencesHelper.wasIssuesViewShowcased();
  }

  public void showcaseWasDisplayed() {
    preferencesHelper.markIssuesViewAsShowcased();
  }

  public void loadTodayIssues(boolean pullToRefresh) {
    if (pullToRefresh) {
      // Sync data with apps sync manager
      Timber.d("Loading and sync started...");
      loadTodayIssuesFromServer();
    } else {
      // Load and display issues from db
      Timber.d("Loading issues from db started...");
      loadTodayIssuesFromDB();
    }
  }

  public void loadTodayIssuesFromServer() {

    if (NetworkUtils.isNetworkConnected(context)) {
      ComicSyncManager.syncImmediately();
    } else {
      Timber.d("Network is not available!");
      if (isViewAttached()) {
        getView().showLoading(false);
        getView().showContent();
        getView().showErrorToast(context.getString(R.string.error_data_not_available_short));
      }
    }
  }

  public void loadTodayIssuesFromDB() {
    localDataHelper
        .getTodayIssuesFromDb()
        .subscribe(getObserver());
  }

  public void loadIssuesByDate(String date) {
    Timber.d("Load issues by date: " + date);
    remoteDataHelper
        .getIssuesListByDate(date)
        .subscribe(getObserverFiltered(date, true));
  }

  public void loadIssuesByDateAndName(String date, String name) {
    Timber.d("Load issues by date: " + date + " and name: " + name);
    remoteDataHelper
        .getIssuesListByDate(date)
        .flatMapObservable(Observable::fromIterable)
        .filter(issue -> issue.volume() != null)
        .filter(issue -> issue.volume().name().contains(name))
        .toList()
        .subscribe(getObserverFiltered(name, false));
  }

  private SingleObserver<List<ComicIssueInfoList>> getObserver() {
    return new SingleObserver<List<ComicIssueInfoList>>() {
      @Override
      public void onSubscribe(@NonNull Disposable d) {
        Timber.d("Data loading started...");
        if (isViewAttached()) {
          getView().showEmptyView(false);
          getView().showLoading(true);
        }
      }

      @Override
      public void onSuccess(@NonNull List<ComicIssueInfoList> list) {

        if (isViewAttached()) {
          getView().setTitle("Today");

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

  private SingleObserver<List<ComicIssueInfoList>> getObserverFiltered(String str, boolean isDate) {
    return new SingleObserver<List<ComicIssueInfoList>>() {
      @Override
      public void onSubscribe(@NonNull Disposable d) {
        Timber.d("Issues data loading started...");
        if (isViewAttached()) {
          getView().showEmptyView(false);
          getView().showLoading(true);
        }
      }

      @Override
      public void onSuccess(@NonNull List<ComicIssueInfoList> list) {

        if (isViewAttached()) {
          String title = isDate ? DateTextUtils.getFormattedDate(str, "MMM d, yyyy") : str;
          getView().setTitle(title);

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
