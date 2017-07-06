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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import butterknife.BindString;
import butterknife.BindView;
import com.evernote.android.state.State;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import com.hannesdorfmann.mosby3.mvp.viewstate.lce.LceViewState;
import com.hannesdorfmann.mosby3.mvp.viewstate.lce.data.RetainingLceViewState;
import com.sedsoftware.comicser.ComicserApp;
import com.sedsoftware.comicser.R;
import com.sedsoftware.comicser.base.BaseLceFragment;
import com.sedsoftware.comicser.data.model.ComicIssueInfoList;
import com.sedsoftware.comicser.data.source.local.dagger.modules.ComicLocalDataModule;
import com.sedsoftware.comicser.data.source.remote.dagger.modules.ComicRemoteDataModule;
import com.sedsoftware.comicser.features.navigation.NavigationActivity;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import timber.log.Timber;

@FragmentWithArgs
public class IssuesFragment extends
    BaseLceFragment<SwipeRefreshLayout, List<ComicIssueInfoList>, IssuesView, IssuesPresenter>
    implements IssuesView, SwipeRefreshLayout.OnRefreshListener {

  @BindString(R.string.error_data_not_available)
  String errorViewText;
  @BindString(R.string.msg_no_issues_today)
  String emptyViewText;
  @BindString(R.string.issues_title_format)
  String titleFormatString;

  @BindView(R.id.emptyView)
  TextView emptyView;
  @BindView(R.id.contentView)
  SwipeRefreshLayout contentView;
  @BindView(R.id.recyclerView)
  RecyclerView recyclerView;

  @State
  String title;

  IssuesComponent issuesComponent;
  IssuesAdapter adapter;

  private OnDateSetListener callback;

  @Override
  protected int getLayoutRes() {
    return R.layout.fragment_issues;
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    contentView.setOnRefreshListener(this);

    adapter = new IssuesAdapter();
    adapter.setHasStableIds(true);

    StaggeredGridLayoutManager manager =
        new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

    recyclerView.setLayoutManager(manager);
    recyclerView.setHasFixedSize(true);
    recyclerView.setAdapter(adapter);

    loadData(false);

    setHasOptionsMenu(true);

    callback = createOnDateSetListenerCallback();
  }

  private void updateTitle() {
    ActionBar supportActionBar = ((NavigationActivity) getActivity()).getSupportActionBar();

    if (supportActionBar != null) {
      supportActionBar.setTitle(title);
    }
  }

  private OnDateSetListener createOnDateSetListenerCallback() {
    return (view, year, monthOfYear, dayOfMonth) -> {
      String date = String.format(Locale.US, "%d-%02d-%02d", year, monthOfYear + 1, dayOfMonth);
      presenter.loadIssuesByDate(date);
    };
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    inflater.inflate(R.menu.issues_view, menu);

    // Tint Menu icons
    Drawable search = menu.findItem(R.id.action_search).getIcon();
    search = DrawableCompat.wrap(search);
    DrawableCompat
        .setTint(search, ContextCompat.getColor(getContext(), R.color.material_color_white));
    menu.findItem(R.id.action_search).setIcon(search);

    Drawable filter = menu.findItem(R.id.action_filter).getIcon();
    filter = DrawableCompat.wrap(filter);
    DrawableCompat
        .setTint(filter, ContextCompat.getColor(getContext(), R.color.material_color_white));
    menu.findItem(R.id.action_filter).setIcon(filter);

    super.onCreateOptionsMenu(menu, inflater);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.action_filter:
        choseDateAndLoadData();
        break;
      case R.id.action_search:

        break;
    }
    return true;
  }

  @Override
  public List<ComicIssueInfoList> getData() {
    return adapter == null ? null : adapter.getIssues();
  }

  @NonNull
  @Override
  public LceViewState<List<ComicIssueInfoList>, IssuesView> createViewState() {
    return new RetainingLceViewState<>();
  }

  @NonNull
  @Override
  public IssuesPresenter createPresenter() {
    return issuesComponent.presenter();
  }

  @Override
  protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
    return e.getMessage();
  }

  @Override
  public void showEmptyView(boolean show) {
    if (show) {
      emptyView.setText(emptyViewText);
      emptyView.setVisibility(View.VISIBLE);
      recyclerView.setVisibility(View.GONE);
    } else {
      recyclerView.setVisibility(View.VISIBLE);
      emptyView.setVisibility(View.GONE);
    }
  }

  @Override
  public void setTitle(String date) {
    Timber.d("Set title called: " + date);
    title = String.format(Locale.US, titleFormatString, date);
    updateTitle();
  }

  @Override
  public void choseDateAndLoadData() {
    Calendar now = Calendar.getInstance();
    DatePickerDialog dpd = DatePickerDialog.newInstance(
        callback,
        now.get(Calendar.YEAR),
        now.get(Calendar.MONTH),
        now.get(Calendar.DAY_OF_MONTH));

    dpd.show(getActivity().getFragmentManager(), "DatePickerDialog");
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

  @Override
  public void showContent() {
    super.showContent();
    contentView.setRefreshing(false);
  }

  @Override
  public void showError(Throwable e, boolean pullToRefresh) {
    super.showError(e, pullToRefresh);
    contentView.setRefreshing(false);
  }

  @Override
  public void showLoading(boolean pullToRefresh) {
    super.showLoading(pullToRefresh);
    contentView.setRefreshing(pullToRefresh);
  }

  @Override
  public void onRefresh() {
    loadData(true);
  }

  @Override
  protected void injectDependencies() {
    issuesComponent = ComicserApp.getAppComponent()
        .plusRemoteComponent(new ComicRemoteDataModule())
        .plusLocalComponent(new ComicLocalDataModule())
        .plusIssuesComponent();
    issuesComponent.inject(this);
  }
}
