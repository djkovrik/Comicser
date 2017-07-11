package com.sedsoftware.comicser.features.volumeslist;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.hannesdorfmann.mosby3.mvp.viewstate.lce.LceViewState;
import com.sedsoftware.comicser.ComicserApp;
import com.sedsoftware.comicser.base.BaseLceFragment;
import com.sedsoftware.comicser.data.model.ComicVolumeInfoList;
import com.sedsoftware.comicser.data.source.local.dagger.modules.ComicLocalDataModule;
import com.sedsoftware.comicser.data.source.remote.dagger.modules.ComicRemoteDataModule;
import java.util.List;

public class VolumesFragment extends
    BaseLceFragment<RecyclerView, List<ComicVolumeInfoList>, VolumesView, VolumesPresenter>
    implements VolumesView {

  VolumesComponent volumesComponent;

  // --- FRAGMENTS LIFECYCLE ---

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
  }

  // --- BASE LCE FRAGMENT ---

  @Override
  protected int getLayoutRes() {
    return 0;
  }

  @Override
  protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
    return null;
  }

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
    return null;
  }

  @NonNull
  @Override
  public LceViewState<List<ComicVolumeInfoList>, VolumesView> createViewState() {
    return null;
  }


  // --- MVP VIEW ---

  @Override
  public void setData(List<ComicVolumeInfoList> data) {

  }

  @Override
  public void loadData(boolean pullToRefresh) {

  }


}
