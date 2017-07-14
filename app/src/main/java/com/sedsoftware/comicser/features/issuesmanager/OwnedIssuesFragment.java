package com.sedsoftware.comicser.features.issuesmanager;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
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
import com.sedsoftware.comicser.features.issuedetails.IssueDetailsActivity;
import com.sedsoftware.comicser.features.navigation.NavigationActivity;
import com.sedsoftware.comicser.utils.ViewUtils;
import java.util.List;
import java.util.Locale;

@FragmentWithArgs
public class OwnedIssuesFragment extends
    BaseLceFragment<RecyclerView, List<ComicIssueInfoList>, OwnedIssuesView, OwnedIssuesPresenter>
    implements OwnedIssuesView {


  @BindString(R.string.msg_no_issues_today)
  String emptyViewText;
  @BindString(R.string.issues_title_format)
  String titleFormatString;
  @BindInt(R.integer.grid_columns_count)
  int gridColumnsCount;

  @BindView(R.id.emptyView)
  TextView emptyView;
  @BindView(R.id.contentView)
  RecyclerView contentView;

  @State
  String title;

  @State
  String searchQuery;

  OwnedIssuesComponent ownedIssuesComponent;
  OwnedIssuesAdapter adapter;

  private Menu currentMenu;

  // --- FRAGMENTS LIFECYCLE ---

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    setRetainInstance(true);

    adapter = new OwnedIssuesAdapter(issueId ->
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
    } else if (savedInstanceState != null) {
      loadData(false);
    }
  }

  @Override
  public void onResume() {
    super.onResume();
    loadData(false);
  }

  // --- OPTIONS MENU ---

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    inflater.inflate(R.menu.fragment_owned_issues_list, menu);

    currentMenu = menu;

    ViewUtils.tintMenuIcon(getContext(), menu, R.id.action_search, R.color.material_color_white);
    ViewUtils.tintMenuIcon(getContext(), menu, R.id.action_clear_search_query,
        R.color.material_color_white);

    setUpSearchItem(menu);

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
      case R.id.action_clear_search_query:
        showClearQueryMenuItem(false);
        loadData(false);
        break;
    }
    return true;
  }

  // --- BASE LCE FRAGMENT ---

  @Override
  protected int getLayoutRes() {
    return R.layout.fragment_owned_issues;
  }

  @Override
  protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
    return e.getMessage();
  }

  @NonNull
  @Override
  public OwnedIssuesPresenter createPresenter() {
    return ownedIssuesComponent.presenter();
  }

  @Override
  protected void injectDependencies() {
    ownedIssuesComponent = ComicserApp.getAppComponent()
        .plusRemoteComponent(new ComicRemoteDataModule())
        .plusLocalComponent(new ComicLocalDataModule())
        .plusOwnedIssuesComponent();
    ownedIssuesComponent.inject(this);
  }

  // --- MVP VIEW STATE ---

  @Override
  public List<ComicIssueInfoList> getData() {
    return adapter == null ? null : adapter.getIssues();
  }

  @NonNull
  @Override
  public LceViewState<List<ComicIssueInfoList>, OwnedIssuesView> createViewState() {
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
  public void showError(Throwable e, boolean pullToRefresh) {
    super.showError(e, pullToRefresh);
    loadingView.setVisibility(View.GONE);
  }

  @Override
  public void showLoading(boolean pullToRefresh) {
    super.showLoading(pullToRefresh);

    if (!pullToRefresh) {
      loadingView.setVisibility(View.GONE);
    }
  }

  @Override
  public void setData(List<ComicIssueInfoList> data) {
    adapter.setIssues(data);
    adapter.notifyDataSetChanged();
  }

  @Override
  public void loadData(boolean pullToRefresh) {
    setTitle("My collection");
    presenter.loadOwnedIssues();
  }

  @Override
  public void loadDataFiltered(String filter) {
    setTitle(filter);
    presenter.loadOwnedIssuesFilteredByName(filter);
  }

  // --- MISC UTILITY FUNCTIONS ---

  private void setUpSearchItem(Menu menu) {
    // Find items
    MaterialSearchView searchView = ButterKnife.findById(getActivity(), R.id.search_view);
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

  private boolean isNotNullOrEmpty(String str) {
    return str != null && !str.isEmpty();
  }

  void showClearQueryMenuItem(boolean show) {
    currentMenu.findItem(R.id.action_search).setVisible(!show);
    currentMenu.findItem(R.id.action_clear_search_query).setVisible(show);
  }
}
