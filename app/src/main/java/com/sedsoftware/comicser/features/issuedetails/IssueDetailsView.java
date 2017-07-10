package com.sedsoftware.comicser.features.issuedetails;

import com.hannesdorfmann.mosby3.mvp.lce.MvpLceView;
import com.sedsoftware.comicser.data.model.ComicIssueInfo;

public interface IssueDetailsView extends MvpLceView<ComicIssueInfo> {

  //  Already defined by Mosby:
//  void showLoading(boolean pullToRefresh);
//  void showContent();
//  void showError(Throwable e, boolean pullToRefresh);
//  void setData(List<ComicIssueInfoList> data);
//  void loadData(boolean pullToRefresh);

  void markAsBookmarked();

  void unmarkAsBookmarked();
}
