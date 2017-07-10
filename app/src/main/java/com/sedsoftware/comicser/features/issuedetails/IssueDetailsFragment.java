package com.sedsoftware.comicser.features.issuedetails;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.BindView;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import com.hannesdorfmann.mosby3.mvp.viewstate.lce.LceViewState;
import com.hannesdorfmann.mosby3.mvp.viewstate.lce.data.RetainingLceViewState;
import com.sedsoftware.comicser.ComicserApp;
import com.sedsoftware.comicser.R;
import com.sedsoftware.comicser.base.BaseLceFragment;
import com.sedsoftware.comicser.data.model.ComicCharacterInfoShort;
import com.sedsoftware.comicser.data.model.ComicImages;
import com.sedsoftware.comicser.data.model.ComicIssueInfo;
import com.sedsoftware.comicser.data.source.local.dagger.modules.ComicLocalDataModule;
import com.sedsoftware.comicser.data.source.remote.dagger.modules.ComicRemoteDataModule;
import com.sedsoftware.comicser.utils.HtmlUtils;
import com.sedsoftware.comicser.utils.ImageUtils;
import com.sedsoftware.comicser.utils.IssueTextUtils;
import com.sedsoftware.comicser.utils.ViewUtils;
import java.util.ArrayList;
import java.util.List;
import timber.log.Timber;


@FragmentWithArgs
public class IssueDetailsFragment
    extends BaseLceFragment<LinearLayout, ComicIssueInfo, IssueDetailsView, IssueDetailsPresenter>
    implements IssueDetailsView {

  @Arg
  long issueId;

  @BindView(R.id.issue_details_screen)
  ImageView issueScreen;
  @BindView(R.id.issue_details_full_name)
  TextView issueFullTitleName;
  @BindView(R.id.issue_details_issue_name)
  TextView issueSeparateName;
  @BindView(R.id.issue_details_cover_date)
  TextView issueCoverDate;
  @BindView(R.id.issue_details_store_date)
  TextView issueStoreDate;
  @BindView(R.id.issue_details_description)
  TextView issueDescription;
  @BindView(R.id.issue_details_characters_card)
  CardView charactersView;
  @BindView(R.id.issue_details_characters_list)
  ListView charactersList;

  IssueDetailsComponent issueDetailsComponent;

  private ComicIssueInfo comicIssueInfo;
  private IssueDetailsCharacterAdapter listAdapter;

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    setRetainInstance(true);

    listAdapter = new IssueDetailsCharacterAdapter(new ArrayList<>(0));

    charactersList.setAdapter(listAdapter);

    charactersList.setDivider(
        new ColorDrawable(ContextCompat.getColor(getContext(), R.color.colorAccentDark)));

    charactersList.setDividerHeight(1);

    // TODO() Replace with character details activity launch
    charactersList.setOnItemClickListener(
        (parent, view1, position, id) -> Timber.d("Clicked id: " + id));

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

  // --- UI BINDING UTILS ---

  private void bindIssueDataToUi(ComicIssueInfo issue) {

    loadHeaderImage(issueScreen, issue.image());

    String volumeName = issue.volume().name();
    int issueNumber = issue.issue_number();
    setUpHeaderText(issueFullTitleName, volumeName, issueNumber);
    setUpOtherText(issueSeparateName, issue.name());
    setUpDate(issueCoverDate, issue.cover_date());
    setUpDate(issueStoreDate, issue.store_date());
    setUpDescription(issueDescription, issue.description());
    setUpCharactersList(charactersView, charactersList, issue.character_credits());
  }

  private void loadHeaderImage(ImageView header, ComicImages image) {
    if (image != null) {
      String imageUrl = image.small_url();
      ImageUtils.loadImageWithTopCrop(header, imageUrl);
    } else {
      header.setVisibility(View.GONE);
    }
  }

  private void setUpHeaderText(TextView textView, String volumeName, int number) {
    textView.setText(IssueTextUtils.getFormattedIssueDetailsTitle(volumeName, number));
  }

  private void setUpOtherText(TextView textView, String name) {
    if (name != null) {
      textView.setText(name);
    } else {
      textView.setVisibility(View.GONE);
    }
  }

  private void setUpDate(TextView textView, String date) {
    if (date != null) {
      textView.setText(date);
    } else {
      textView.setText("-");
    }
  }

  private void setUpDescription(TextView textView, String description) {
    if (description != null) {
      textView.setText(HtmlUtils.parseHtmlText(description));
    } else {
      textView.setVisibility(View.GONE);
    }
  }

  private void setUpCharactersList(CardView parent, ListView listView,
      List<ComicCharacterInfoShort> characters) {
    if (characters != null && !characters.isEmpty()) {
      listAdapter.replaceCharacters(characters);
      ViewUtils.setListViewHeightBasedOnChildren(listView);
    } else {
      parent.setVisibility(View.GONE);
    }
  }
}