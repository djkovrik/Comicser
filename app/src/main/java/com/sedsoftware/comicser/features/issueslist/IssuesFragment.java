package com.sedsoftware.comicser.features.issueslist;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import butterknife.BindString;
import butterknife.BindView;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import com.hannesdorfmann.mosby3.mvp.viewstate.lce.LceViewState;
import com.hannesdorfmann.mosby3.mvp.viewstate.lce.data.RetainingLceViewState;
import com.sedsoftware.comicser.ComicserApp;
import com.sedsoftware.comicser.R;
import com.sedsoftware.comicser.base.BaseLceFragment;
import com.sedsoftware.comicser.data.model.ComicIssueInfoList;
import com.sedsoftware.comicser.data.source.local.dagger.modules.ComicLocalDataModule;
import com.sedsoftware.comicser.data.source.remote.dagger.modules.ComicRemoteDataModule;
import java.util.List;

@FragmentWithArgs
public class IssuesFragment extends
    BaseLceFragment<SwipeRefreshLayout, List<ComicIssueInfoList>, IssuesView, IssuesPresenter>
    implements IssuesView, SwipeRefreshLayout.OnRefreshListener {

  @BindString(R.string.error_data_not_available)
  String errorViewText;
  @BindString(R.string.msg_no_issues_today)
  String emptyViewText;

  @BindView(R.id.emptyView)
  TextView emptyView;
  @BindView(R.id.contentView)
  SwipeRefreshLayout contentView;
  @BindView(R.id.recyclerView)
  RecyclerView recyclerView;

  IssuesComponent issuesComponent;

  IssuesAdapter adapter;

  @Override
  protected int getLayoutRes() {
    return R.layout.fragment_issues;
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    contentView.setOnRefreshListener(this);

    // Setup recyclerview
    adapter = new IssuesAdapter();
    adapter.setHasStableIds(true);

    GridLayoutManager manager = new GridLayoutManager(getContext(), 2);

    recyclerView.setLayoutManager(manager);
    recyclerView.setHasFixedSize(true);
    recyclerView.setAdapter(adapter);

    loadData(false);
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
    } else {
      emptyView.setVisibility(View.GONE);
    }
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
