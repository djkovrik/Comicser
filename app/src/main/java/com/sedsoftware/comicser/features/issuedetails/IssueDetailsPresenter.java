package com.sedsoftware.comicser.features.issuedetails;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.sedsoftware.comicser.data.model.ComicIssueInfo;
import com.sedsoftware.comicser.data.source.remote.ComicRemoteDataHelper;
import io.reactivex.SingleObserver;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import javax.inject.Inject;

public class IssueDetailsPresenter extends MvpBasePresenter<IssueDetailsView> {

  final ComicRemoteDataHelper remoteDataHelper;

  @Inject
  public IssueDetailsPresenter(
      ComicRemoteDataHelper remoteDataHelper) {
    this.remoteDataHelper = remoteDataHelper;
  }

  public void loadIssueDetails(long issueId) {
    remoteDataHelper
        .getIssueDetailsById(issueId)
        .subscribe(getIssueDetailsObserver());
  }

  private SingleObserver<ComicIssueInfo> getIssueDetailsObserver() {
    return new SingleObserver<ComicIssueInfo>() {
      @Override
      public void onSubscribe(@NonNull Disposable d) {

      }

      @Override
      public void onSuccess(@NonNull ComicIssueInfo comicIssueInfo) {

      }

      @Override
      public void onError(@NonNull Throwable e) {

      }
    };
  }
}
