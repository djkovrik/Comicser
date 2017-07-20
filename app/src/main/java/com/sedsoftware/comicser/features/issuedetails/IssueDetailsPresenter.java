package com.sedsoftware.comicser.features.issuedetails;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.sedsoftware.comicser.data.model.ComicIssueInfo;
import com.sedsoftware.comicser.data.source.local.ComicLocalDataHelper;
import com.sedsoftware.comicser.data.source.remote.ComicRemoteDataHelper;
import com.sedsoftware.comicser.utils.ContentUtils;
import io.reactivex.SingleObserver;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import javax.inject.Inject;
import timber.log.Timber;

@SuppressWarnings("WeakerAccess")
public class IssueDetailsPresenter extends MvpBasePresenter<IssueDetailsView> {

  final ComicLocalDataHelper localDataHelper;
  final ComicRemoteDataHelper remoteDataHelper;

  @Inject
  IssueDetailsPresenter(
      ComicLocalDataHelper localDataHelper,
      ComicRemoteDataHelper remoteDataHelper) {
    this.localDataHelper = localDataHelper;
    this.remoteDataHelper = remoteDataHelper;
  }

  void setUpBookmarkIconState(long issueId) {
    if (isViewAttached()) {
      if (isCurrentIssueBookmarked(issueId)) {
        getView().markAsBookmarked();
      } else {
        getView().unmarkAsBookmarked();
      }
    }
  }

  boolean isCurrentIssueBookmarked(long issueId) {
    return localDataHelper.isIssueBookmarked(issueId);
  }

  void bookmarkIssue(ComicIssueInfo issue) {
    localDataHelper.saveOwnedIssueToDb(ContentUtils.shortenIssueInfo(issue));
  }

  void removeBookmark(long issueId) {
    localDataHelper.removeOwnedIssueFromDb(issueId);
  }

  void loadIssueDetails(long issueId) {
    remoteDataHelper
        .getIssueDetailsById(issueId)
        .subscribe(getIssueDetailsObserver());
  }

  private SingleObserver<ComicIssueInfo> getIssueDetailsObserver() {
    return new SingleObserver<ComicIssueInfo>() {
      @Override
      public void onSubscribe(@NonNull Disposable d) {
        Timber.d("Issue details loading started...");
        getView().showLoading(true);
      }

      @Override
      public void onSuccess(@NonNull ComicIssueInfo comicIssueInfo) {
        Timber.d("Issue details loading completed.");
        if (isViewAttached()) {
          getView().showLoading(false);
          getView().setData(comicIssueInfo);
        }
      }

      @Override
      public void onError(@NonNull Throwable e) {
        Timber.d("Issue details loading error: " + e.getMessage());
        getView().showError(e, false);
      }
    };
  }
}
