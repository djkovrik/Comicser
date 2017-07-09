package com.sedsoftware.comicser.features.issuedetails;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.BindView;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import com.hannesdorfmann.mosby3.mvp.viewstate.lce.LceViewState;
import com.hannesdorfmann.mosby3.mvp.viewstate.lce.data.RetainingLceViewState;
import com.sedsoftware.comicser.ComicserApp;
import com.sedsoftware.comicser.R;
import com.sedsoftware.comicser.base.BaseLceFragment;
import com.sedsoftware.comicser.data.model.ComicImages;
import com.sedsoftware.comicser.data.model.ComicIssueInfo;
import com.sedsoftware.comicser.data.source.local.dagger.modules.ComicLocalDataModule;
import com.sedsoftware.comicser.data.source.remote.dagger.modules.ComicRemoteDataModule;
import com.sedsoftware.comicser.utils.ImageUtils;
import timber.log.Timber;


@FragmentWithArgs
public class IssueDetailsFragment
    extends BaseLceFragment<CardView, ComicIssueInfo, IssueDetailsView, IssueDetailsPresenter>
    implements IssueDetailsView {

  @Arg
  long issueId;

  @BindView(R.id.issue_details_screen)
  ImageView issueScreen;
  @BindView(R.id.issue_details_screen_loading)
  ProgressBar progressBar;
  @BindView(R.id.issue_details_name)
  TextView issueName;
  @BindView(R.id.issue_details_volume_name)
  TextView volumeName;
  @BindView(R.id.issue_details_number)
  TextView issueNumber;
  @BindView(R.id.issue_details_cover_date)
  TextView issueCoverDate;
  @BindView(R.id.issue_details_store_date)
  TextView issueStoreDate;
  @BindView(R.id.issue_details_description)
  TextView issueDescription;


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

    final String cacheImageType = "screen_url";

    ComicImages image = issue.image();

    if (image != null) {
      String imageUrl = image.screen_url();
      Timber.d("URL: " + imageUrl);
      ImageUtils.loadImageWithProgress(issueScreen, imageUrl, cacheImageType, progressBar);
    } else {
      issueScreen.setVisibility(View.GONE);
    }

    setUpTextView(issueName, issue.name());
    setUpTextView(volumeName, issue.volume().name());
    setUpTextView(issueNumber, String.valueOf(issue.issue_number()));
    setUpTextView(issueCoverDate, issue.cover_date());
    setUpTextView(issueStoreDate, issue.store_date());
    setUpDescriptionTextView(issueDescription, issue.description());
  }

  private void setUpTextView(TextView textView, String text) {
    if (text != null) {
      textView.setText(text);
    } else {
      textView.setText("-");
    }
  }

  private void setUpDescriptionTextView(TextView textView, String text) {
    if (text != null && !text.isEmpty()) {
      textView.setText(text);
    } else {
      textView.setVisibility(View.GONE);
    }
  }
}