package com.sedsoftware.comicser.features.issuedetails;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.BindBool;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
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
import com.sedsoftware.comicser.features.characterdetails.CharacterDetailsActivity;
import com.sedsoftware.comicser.features.characterdetails.CharacterDetailsFragmentBuilder;
import com.sedsoftware.comicser.utils.FragmentUtils;
import com.sedsoftware.comicser.utils.HtmlUtils;
import com.sedsoftware.comicser.utils.ImageUtils;
import com.sedsoftware.comicser.utils.IssueTextUtils;
import com.sedsoftware.comicser.utils.ViewUtils;
import java.util.ArrayList;
import java.util.List;


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

  @BindString(R.string.msg_bookmarked)
  String messageBookmarked;
  @BindString(R.string.msg_bookmark_removed)
  String messageBookmarkRemoved;
  @BindBool(R.bool.is_tablet_layout)
  boolean twoPaneMode;

  IssueDetailsComponent issueDetailsComponent;

  private ComicIssueInfo currentIssue;
  private IssueDetailsCharacterAdapter listAdapter;
  private Menu currentMenu;

  // --- FRAGMENT LIFECYCLE ---

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    setRetainInstance(true);

    setHasOptionsMenu(true);

    listAdapter = new IssueDetailsCharacterAdapter(new ArrayList<>(0));

    charactersList.setAdapter(listAdapter);

    charactersList.setDivider(
        new ColorDrawable(ContextCompat.getColor(getContext(), R.color.colorAccentDark)));

    charactersList.setDividerHeight(1);

    charactersList.setOnItemClickListener((parent, view1, position, id) -> {
      if (twoPaneMode) {
        FragmentManager manager = getActivity().getSupportFragmentManager();
        Fragment fragment = new CharacterDetailsFragmentBuilder(id).build();

        FragmentUtils.replaceFragmentIn(
            manager, fragment, R.id.content_frame, "CharacterDetailsFragment", true);
      } else {
        startActivity(CharacterDetailsActivity.prepareIntent(getContext(), id));
      }
    });

    if (savedInstanceState != null) {
      loadData(false);
    }
  }

  // --- OPTIONS MENU ---

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    inflater.inflate(R.menu.fragment_issue_details, menu);

    currentMenu = menu;

    presenter.setUpBookmarkIconState(issueId);
    super.onCreateOptionsMenu(menu, inflater);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.action_bookmark:
        onBookmarkClick();
        break;
    }

    return super.onOptionsItemSelected(item);
  }

  // --- BASE LCE FRAGMENT ---

  @Override
  protected int getLayoutRes() {
    return R.layout.fragment_issue_details;
  }

  @Override
  protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
    return e.getMessage();
  }

  @NonNull
  @Override
  public IssueDetailsPresenter createPresenter() {
    return issueDetailsComponent.presenter();
  }

  @Override
  protected void injectDependencies() {
    issueDetailsComponent = ComicserApp.getAppComponent()
        .plusRemoteComponent(new ComicRemoteDataModule())
        .plusLocalComponent(new ComicLocalDataModule())
        .plusIssueDetailsComponent();
    issueDetailsComponent.inject(this);
  }
  // --- MVP VIEW STATE ---

  @Override
  public ComicIssueInfo getData() {
    return currentIssue;
  }

  @NonNull
  @Override
  public LceViewState<ComicIssueInfo, IssueDetailsView> createViewState() {
    return new RetainingLceViewState<>();
  }

  // --- MVP VIEW ---

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
    currentIssue = data;
    bindIssueDataToUi(currentIssue);
  }

  @Override
  public void loadData(boolean pullToRefresh) {
    presenter.loadIssueDetails(issueId);
  }

  @Override
  public void markAsBookmarked() {
    currentMenu.findItem(R.id.action_bookmark).setIcon(R.drawable.ic_bookmark_black_24dp);

    ViewUtils.tintMenuIcon(getContext(), currentMenu, R.id.action_bookmark,
        R.color.material_color_white);
  }

  @Override
  public void unmarkAsBookmarked() {
    currentMenu.findItem(R.id.action_bookmark).setIcon(R.drawable.ic_bookmark_border_black_24dp);

    ViewUtils.tintMenuIcon(getContext(), currentMenu, R.id.action_bookmark,
        R.color.material_color_white);
  }

  @Override
  public void onBookmarkClick() {

    String message;

    boolean isBookmarkedNow = presenter.isCurrentIssueBookmarked(issueId);

    if (isBookmarkedNow) {
      presenter.removeBookmark(issueId);
      message = messageBookmarkRemoved;
    } else {
      presenter.bookmarkIssue(currentIssue);
      message = messageBookmarked;
    }

    presenter.setUpBookmarkIconState(issueId);

    int parentLayoutId = (twoPaneMode) ?
        R.id.main_constraint_layout :
        R.id.issue_details_activity_layout;

    Snackbar.make(
        ButterKnife.findById(getActivity(), parentLayoutId),
        message,
        Snackbar.LENGTH_SHORT)
        .show();
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