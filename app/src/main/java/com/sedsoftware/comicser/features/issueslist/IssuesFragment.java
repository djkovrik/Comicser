package com.sedsoftware.comicser.features.issueslist;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import butterknife.BindInt;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.evernote.android.state.State;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.SimpleShowcaseEventListener;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import com.hannesdorfmann.mosby3.mvp.viewstate.lce.LceViewState;
import com.hannesdorfmann.mosby3.mvp.viewstate.lce.data.RetainingLceViewState;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.sedsoftware.comicser.ComicserApp;
import com.sedsoftware.comicser.R;
import com.sedsoftware.comicser.base.BaseLceFragment;
import com.sedsoftware.comicser.data.model.ComicIssueInfoList;
import com.sedsoftware.comicser.data.source.local.dagger.modules.ComicLocalDataModule;
import com.sedsoftware.comicser.data.source.remote.dagger.modules.ComicRemoteDataModule;
import com.sedsoftware.comicser.features.ToolbarActionItemTarget;
import com.sedsoftware.comicser.features.issuedetails.IssueDetailsActivity;
import com.sedsoftware.comicser.features.navigation.NavigationActivity;
import com.sedsoftware.comicser.utils.DateTextUtils;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

@FragmentWithArgs
public class IssuesFragment extends
    BaseLceFragment<SwipeRefreshLayout, List<ComicIssueInfoList>, IssuesView, IssuesPresenter>
    implements IssuesView, SwipeRefreshLayout.OnRefreshListener {

  @BindString(R.string.error_data_not_available)
  String errorNoInternetText;
  @BindString(R.string.msg_no_issues_today)
  String emptyViewText;
  @BindString(R.string.issues_title_format)
  String titleFormatString;
  @BindInt(R.integer.issues_grid_columns_count)
  int gridColumnsCount;

  @BindView(R.id.emptyView)
  TextView emptyView;
  @BindView(R.id.refreshLayout)
  SwipeRefreshLayout refreshLayout;
  @BindView(R.id.contentView)
  RecyclerView contentView;

  MaterialSearchView searchView;

  @State
  String title;

  @State
  String chosenDate;

  @State
  String searchQuery;

  IssuesComponent issuesComponent;
  IssuesAdapter adapter;

  private Menu currentMenu;

  // --- FRAGMENTS LIFECYCLE ---

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    setRetainInstance(true);

    refreshLayout.setOnRefreshListener(this);

    adapter = new IssuesAdapter(issueId ->
        startActivity(IssueDetailsActivity.prepareIntent(getContext(), issueId)));
    adapter.setHasStableIds(true);

    StaggeredGridLayoutManager manager =
        new StaggeredGridLayoutManager(gridColumnsCount, StaggeredGridLayoutManager.VERTICAL);

    contentView.setLayoutManager(manager);
    contentView.setHasFixedSize(true);
    contentView.setAdapter(adapter);

    setHasOptionsMenu(true);

    if (isNotNullOrEmpty(searchQuery)) {
      loadDataFiltered(searchQuery);
    } else if (isNotNullOrEmpty(chosenDate)) {
      loadDataForChosenDate(chosenDate);
    } else if (savedInstanceState != null) {
      loadData(false);
    }
  }

  // --- OPTIONS MENU ---

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    inflater.inflate(R.menu.fragment_issues_list, menu);

    currentMenu = menu;

    tintMenuIcons(menu);
    setUpSearchItem(menu);
    showcaseToolbarItems();

    if (isNotNullOrEmpty(searchQuery)) {
      showClearQueryMenuItem(true);
    } else {
      showClearQueryMenuItem(false);
    }

    super.onCreateOptionsMenu(menu, inflater);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.action_choose_date:
        choseDateAndLoadData();
        break;
      case R.id.action_clear_search_query:
        showClearQueryMenuItem(false);
        searchQuery = "";
        if (isNotNullOrEmpty(chosenDate)) {
          loadDataForChosenDate(chosenDate);
        } else {
          loadData(false);
        }
        break;
    }
    return true;
  }

  // --- BASE LCE FRAGMENT ---

  @Override
  protected int getLayoutRes() {
    return R.layout.fragment_issues;
  }

  @Override
  protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
    String actualMessage = e.getMessage();
    if (actualMessage.contains("Unable to resolve host") ||
        actualMessage.contains("timeout")) {
      return errorNoInternetText;
    }
    return e.getMessage();
  }

  @NonNull
  @Override
  public IssuesPresenter createPresenter() {
    return issuesComponent.presenter();
  }

  @Override
  protected void injectDependencies() {
    issuesComponent = ComicserApp.getAppComponent()
        .plusRemoteComponent(new ComicRemoteDataModule())
        .plusLocalComponent(new ComicLocalDataModule())
        .plusIssuesComponent();
    issuesComponent.inject(this);
  }

  @Override
  public boolean isSearchViewOpened() {
    return searchView.isSearchOpen();
  }

  @Override
  public void closeSearchView() {
    searchView.closeSearch();
  }

  // --- MVP VIEW STATE ---

  @Override
  public List<ComicIssueInfoList> getData() {
    return adapter == null ? null : adapter.getIssues();
  }

  @NonNull
  @Override
  public LceViewState<List<ComicIssueInfoList>, IssuesView> createViewState() {
    return new RetainingLceViewState<>();
  }

  // --- MVP VIEW ---

  @Override
  public void setTitle(String date) {
    title = String.format(Locale.US, titleFormatString, date);
    updateTitle();
  }

  @Override
  public void showEmptyView(boolean show) {
    refreshLayout.setRefreshing(false);
    if (show) {
      emptyView.setText(emptyViewText);
      emptyView.setVisibility(View.VISIBLE);
      contentView.setVisibility(View.GONE);
      errorView.setVisibility(View.GONE);
    } else {
      emptyView.setVisibility(View.GONE);
    }
  }

  @Override
  public void showContent() {
    super.showContent();
    refreshLayout.setRefreshing(false);
  }

  @Override
  public void showError(Throwable e, boolean pullToRefresh) {
    super.showError(e, pullToRefresh);
    refreshLayout.setRefreshing(false);
    loadingView.setVisibility(View.GONE);
  }

  @Override
  public void showLoading(boolean pullToRefresh) {
    super.showLoading(pullToRefresh);
    refreshLayout.setRefreshing(pullToRefresh);

    if (!pullToRefresh) {
      loadingView.setVisibility(View.GONE);
    }
  }

  @Override
  public void choseDateAndLoadData() {
    Calendar now = Calendar.getInstance();
    DatePickerDialog dpd = DatePickerDialog.newInstance(
        (view, year, monthOfYear, dayOfMonth) -> {
          chosenDate = String.format(Locale.US, "%d-%02d-%02d", year, monthOfYear + 1, dayOfMonth);
          loadDataForChosenDate(chosenDate);
        },
        now.get(Calendar.YEAR),
        now.get(Calendar.MONTH),
        now.get(Calendar.DAY_OF_MONTH));

    dpd.setAccentColor(ContextCompat.getColor(getContext(), R.color.colorAccentDark));
    dpd.show(getActivity().getFragmentManager(), "DatePickerDialog");
  }

  @Override
  public void loadDataForChosenDate(String date) {
    presenter.loadIssuesByDate(date);
  }

  @Override
  public void loadDataFiltered(String filter) {
    String date = (chosenDate != null) ? chosenDate : DateTextUtils.getTodayDateString();
    presenter.loadIssuesByDateAndName(date, filter);
  }

  @Override
  public void setData(List<ComicIssueInfoList> data) {
    adapter.setIssues(data);
    adapter.notifyDataSetChanged();
  }

  @Override
  public void loadData(boolean pullToRefresh) {
    presenter.loadTodayIssues(pullToRefresh);
  }

  // --- SWIPE TO REFRESH LAYOUT ---

  @Override
  public void onRefresh() {
    loadData(true);
  }

  // --- MISC UTILITY FUNCTIONS ---

  private void tintMenuIcons(Menu menu) {
    // Tint Menu icons
    Drawable search = menu.findItem(R.id.action_search).getIcon();
    search = DrawableCompat.wrap(search);
    DrawableCompat
        .setTint(search, ContextCompat.getColor(getContext(), R.color.material_color_white));
    menu.findItem(R.id.action_search).setIcon(search);

    Drawable filter = menu.findItem(R.id.action_choose_date).getIcon();
    filter = DrawableCompat.wrap(filter);
    DrawableCompat
        .setTint(filter, ContextCompat.getColor(getContext(), R.color.material_color_white));
    menu.findItem(R.id.action_choose_date).setIcon(filter);
  }

  private void setUpSearchItem(Menu menu) {
    // Find items
    searchView = ButterKnife.findById(getActivity(), R.id.search_view);
    MenuItem menuItem = menu.findItem(R.id.action_search);

    // Tweaks
    searchView.setVoiceSearch(false);
    searchView.setMenuItem(menuItem);

    // Listeners
    searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
      @Override
      public boolean onQueryTextSubmit(String query) {
        searchQuery = query;

        if (searchQuery.length() > 0) {
          showClearQueryMenuItem(true);
          loadDataFiltered(searchQuery);
        }
        return false;
      }

      @Override
      public boolean onQueryTextChange(String newText) {
        //Do some magic
        return false;
      }
    });
  }

  private void updateTitle() {
    ActionBar supportActionBar = ((NavigationActivity) getActivity()).getSupportActionBar();

    if (supportActionBar != null) {
      supportActionBar.setTitle(title);
    }
  }

  private void showcaseToolbarItems() {

    if (presenter.shouldNotDisplayShowcases()) {
      return;
    }

    Toolbar toolbar = ButterKnife.findById(getActivity(), R.id.toolbar);

    if (toolbar != null) {

      // Show first showcase
      new ShowcaseView.Builder(getActivity())
          .setTarget(new ToolbarActionItemTarget(toolbar, R.id.action_choose_date))
          .withMaterialShowcase()
          .hideOnTouchOutside()
          .setStyle(R.style.CustomShowcaseTheme)
          .setContentTitle(R.string.showcase_issues_title)
          .setContentText(R.string.showcase_issues_datepicker)
          .setShowcaseEventListener(new SimpleShowcaseEventListener() {
            @Override
            public void onShowcaseViewHide(ShowcaseView showcaseView) {
              super.onShowcaseViewHide(showcaseView);
              // Show second showcase
              new ShowcaseView.Builder(getActivity())
                  .setTarget(new ToolbarActionItemTarget(toolbar, R.id.action_search))
                  .withMaterialShowcase()
                  .hideOnTouchOutside()
                  .setStyle(R.style.CustomShowcaseTheme)
                  .setContentTitle(R.string.showcase_issues_title)
                  .setContentText(R.string.showcase_issues_search)
                  .build()
                  .show();
            }
          })
          .build()
          .show();

      presenter.showcaseWasDisplayed();
    }
  }

  private boolean isNotNullOrEmpty(String str) {
    return str != null && !str.isEmpty();
  }

  void showClearQueryMenuItem(boolean show) {
    currentMenu.findItem(R.id.action_choose_date).setVisible(!show);
    currentMenu.findItem(R.id.action_search).setVisible(!show);
    currentMenu.findItem(R.id.action_clear_search_query).setVisible(show);
  }
}
