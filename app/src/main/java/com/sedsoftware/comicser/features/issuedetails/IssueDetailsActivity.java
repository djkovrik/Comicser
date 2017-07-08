package com.sedsoftware.comicser.features.issuedetails;

import android.support.annotation.NonNull;
import android.widget.FrameLayout;
import com.hannesdorfmann.mosby3.mvp.viewstate.lce.LceViewState;
import com.sedsoftware.comicser.base.BaseLceActivity;
import com.sedsoftware.comicser.data.model.ComicIssueInfo;

// TODO(1) Replace FrameLayout with actual content view!
public class IssueDetailsActivity
    extends BaseLceActivity<FrameLayout, ComicIssueInfo, IssueDetailsView, IssueDetailsPresenter>
    implements IssueDetailsView {

  @Override
  public ComicIssueInfo getData() {
    return null;
  }

  @Override
  protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
    return null;
  }

  @NonNull
  @Override
  public IssueDetailsPresenter createPresenter() {
    return null;
  }

  @NonNull
  @Override
  public LceViewState<ComicIssueInfo, IssueDetailsView> createViewState() {
    return null;
  }

  @Override
  public void showContent() {
    super.showContent();
  }

  @Override
  public void showError(Throwable e, boolean pullToRefresh) {
    super.showError(e, pullToRefresh);
  }

  @Override
  public void showLoading(boolean pullToRefresh) {
    super.showLoading(pullToRefresh);
  }

  @Override
  public void setData(ComicIssueInfo data) {

  }

  @Override
  public void loadData(boolean pullToRefresh) {

  }
}
