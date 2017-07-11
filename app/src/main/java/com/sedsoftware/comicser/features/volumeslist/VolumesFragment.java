package com.sedsoftware.comicser.features.volumeslist;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import butterknife.BindInt;
import butterknife.ButterKnife;
import com.evernote.android.state.State;
import com.hannesdorfmann.mosby3.mvp.viewstate.lce.LceViewState;
import com.hannesdorfmann.mosby3.mvp.viewstate.lce.data.RetainingLceViewState;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.sedsoftware.comicser.ComicserApp;
import com.sedsoftware.comicser.R;
import com.sedsoftware.comicser.base.BaseLceFragment;
import com.sedsoftware.comicser.data.model.ComicVolumeInfoList;
import com.sedsoftware.comicser.data.source.local.dagger.modules.ComicLocalDataModule;
import com.sedsoftware.comicser.data.source.remote.dagger.modules.ComicRemoteDataModule;
import com.sedsoftware.comicser.utils.ViewUtils;
import java.util.List;
import timber.log.Timber;

public class VolumesFragment extends
    BaseLceFragment<RecyclerView, List<ComicVolumeInfoList>, VolumesView, VolumesPresenter>
    implements VolumesView {

  @BindInt(R.integer.issues_grid_columns_count)
  int gridColumnsCount;

  @State
  String chosenName;

  VolumesComponent volumesComponent;
  VolumesAdapter adapter;

  // --- FRAGMENTS LIFECYCLE ---

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    adapter = new VolumesAdapter(volumeId -> Timber.d("Clicked id: " + volumeId));
    adapter.setHasStableIds(true);

    StaggeredGridLayoutManager manager =
        new StaggeredGridLayoutManager(gridColumnsCount, StaggeredGridLayoutManager.VERTICAL);

    contentView.setLayoutManager(manager);
    contentView.setHasFixedSize(true);
    contentView.setAdapter(adapter);

    setHasOptionsMenu(true);

    if (savedInstanceState != null) {
      loadData(true);
    }
  }

  // --- OPTIONS MENU ---

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    inflater.inflate(R.menu.fragment_volume_list, menu);

    ViewUtils.tintMenuIcon(getContext(), menu, R.id.action_search, R.color.material_color_white);

    setUpSearchItem(menu);

    super.onCreateOptionsMenu(menu, inflater);
  }

  // --- BASE LCE FRAGMENT ---

  @Override
  protected int getLayoutRes() {
    return R.layout.fragment_volumes;
  }

  @Override
  protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
    return e.getMessage();
  }

  @NonNull
  @Override
  public VolumesPresenter createPresenter() {
    return volumesComponent.presenter();
  }

  @Override
  protected void injectDependencies() {

    volumesComponent = ComicserApp.getAppComponent()
        .plusRemoteComponent(new ComicRemoteDataModule())
        .plusLocalComponent(new ComicLocalDataModule())
        .plusVolumesComponent();
    volumesComponent.inject(this);
  }

  // --- MVP VIEW STATE ---

  @Override
  public List<ComicVolumeInfoList> getData() {
    return adapter == null ? null : adapter.getVolumes();
  }

  @NonNull
  @Override
  public LceViewState<List<ComicVolumeInfoList>, VolumesView> createViewState() {
    return new RetainingLceViewState<>();
  }

  // --- MVP VIEW ---

  @Override
  public void setData(List<ComicVolumeInfoList> data) {
    adapter.setVolumes(data);
    adapter.notifyDataSetChanged();
  }

  @Override
  public void loadData(boolean pullToRefresh) {
    presenter.loadVolumesData(chosenName);
  }

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
        chosenName = query;

        if (chosenName.length() > 0) {
          loadData(true);
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
}
