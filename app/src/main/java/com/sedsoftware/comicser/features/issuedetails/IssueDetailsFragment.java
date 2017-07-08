package com.sedsoftware.comicser.features.issuedetails;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import com.hannesdorfmann.mosby3.mvp.viewstate.lce.LceViewState;
import com.hannesdorfmann.mosby3.mvp.viewstate.lce.data.RetainingLceViewState;
import com.sedsoftware.comicser.ComicserApp;
import com.sedsoftware.comicser.R;
import com.sedsoftware.comicser.base.BaseLceFragment;
import com.sedsoftware.comicser.data.model.ComicIssueInfo;
import com.sedsoftware.comicser.data.source.local.dagger.modules.ComicLocalDataModule;
import com.sedsoftware.comicser.data.source.remote.dagger.modules.ComicRemoteDataModule;
import timber.log.Timber;

// TODO(1) Replace TextView with actual content view!
@FragmentWithArgs
public class IssueDetailsFragment
    extends BaseLceFragment<TextView, ComicIssueInfo, IssueDetailsView, IssueDetailsPresenter>
    implements IssueDetailsView {

  @Arg
  long issueId;

  IssueDetailsComponent issueDetailsComponent;

  private ComicIssueInfo comicIssueInfo;

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    setRetainInstance(true);

    if (savedInstanceState != null) {
      loadData(false);
    }
  }

  @Override
  protected int getLayoutRes() {
    return R.layout.fragment_issue_details;
  }

  @Override
  public ComicIssueInfo getData() {
    return comicIssueInfo;
  }

  @NonNull
  @Override
  public IssueDetailsPresenter createPresenter() {
    return issueDetailsComponent.presenter();
  }

  @NonNull
  @Override
  public LceViewState<ComicIssueInfo, IssueDetailsView> createViewState() {
    return new RetainingLceViewState<>();
  }

  @Override
  protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
    return e.getMessage();
  }

  @Override
  public void showContent() {
    contentView.setVisibility(View.VISIBLE);
    errorView.setVisibility(View.GONE);
    loadingView.setVisibility(View.GONE);
  }

  @Override
  public void showError(Throwable e, boolean pullToRefresh) {
    errorView.setText(R.string.error_issue_not_available);
    contentView.setVisibility(View.GONE);
    loadingView.setVisibility(View.GONE);
    errorView.setVisibility(View.VISIBLE);
  }

  @Override
  public void showLoading(boolean pullToRefresh) {
    if (pullToRefresh) {
      contentView.setVisibility(View.GONE);
      errorView.setVisibility(View.GONE);
      loadingView.setVisibility(View.VISIBLE);
    } else {
      contentView.setVisibility(View.VISIBLE);
      errorView.setVisibility(View.GONE);
      loadingView.setVisibility(View.GONE);
    }
  }

  @Override
  public void setData(ComicIssueInfo data) {
    comicIssueInfo = data;
    //TODO() Bind data to UI
    Timber.d("Loading finished!");
    Timber.d(" - issue: " + comicIssueInfo.volume().name());
    bindIssueDataToUi(comicIssueInfo);
  }

  @Override
  public void loadData(boolean pullToRefresh) {
    presenter.loadIssueDetails(issueId);
  }

  @Override
  protected void injectDependencies() {
    issueDetailsComponent = ComicserApp.getAppComponent()
        .plusRemoteComponent(new ComicRemoteDataModule())
        .plusLocalComponent(new ComicLocalDataModule())
        .plusIssueDetailsComponent();
    issueDetailsComponent.inject(this);
  }

  private void bindIssueDataToUi(ComicIssueInfo issue) {
    contentView.setText(issue.volume().name());
  }
}
