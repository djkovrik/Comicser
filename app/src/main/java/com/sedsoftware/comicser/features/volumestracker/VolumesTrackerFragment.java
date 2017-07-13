package com.sedsoftware.comicser.features.volumestracker;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import butterknife.BindInt;
import butterknife.BindView;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import com.sedsoftware.comicser.R;
import com.sedsoftware.comicser.base.BaseFragment;
import com.sedsoftware.comicser.data.source.local.ComicContract.TrackedVolumeEntry;
import com.sedsoftware.comicser.features.navigation.NavigationActivity;
import com.sedsoftware.comicser.features.volumedetails.VolumeDetailsActivity;

// LOADERS USED BECAUSE OF RUBRIC REQUIREMENT
@FragmentWithArgs
public class VolumesTrackerFragment extends BaseFragment
    implements LoaderManager.LoaderCallbacks<Cursor> {

  private static final int TRACKER_LOADER_ID = 1234;

  @BindView(R.id.contentView)
  RecyclerView contentView;

  @BindInt(R.integer.grid_columns_count)
  int gridColumnsCount;

  private VolumesTrackerAdapter adapter;

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    adapter = new VolumesTrackerAdapter(
        volumeId -> startActivity(VolumeDetailsActivity.prepareIntent(getContext(), volumeId)));

    adapter.setHasStableIds(true);

    StaggeredGridLayoutManager manager =
        new StaggeredGridLayoutManager(gridColumnsCount, StaggeredGridLayoutManager.VERTICAL);

    contentView.setLayoutManager(manager);
    contentView.setHasFixedSize(true);
    contentView.setAdapter(adapter);

    ActionBar supportActionBar = ((NavigationActivity) getActivity()).getSupportActionBar();

    if (supportActionBar != null) {
      supportActionBar.setTitle(R.string.volumes_tracker_fragment_title);
    }

    getActivity().getSupportLoaderManager().initLoader(TRACKER_LOADER_ID, null, this);
  }

  @Override
  public void onResume() {
    super.onResume();
    getActivity().getSupportLoaderManager().restartLoader(TRACKER_LOADER_ID, null, this);
  }

  @Override
  protected int getLayoutRes() {
    return R.layout.fragment_volumes_tracker;
  }

  @Override
  public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    return new AsyncTaskLoader<Cursor>(getContext()) {

      Cursor data = null;

      @Override
      protected void onStartLoading() {
        if (data != null) {
          deliverResult(data);
        } else {
          forceLoad();
        }
      }

      @Override
      public Cursor loadInBackground() {
        return getActivity().getContentResolver()
            .query(TrackedVolumeEntry.CONTENT_URI_TRACKED_VOLUMES,
                null, null, null, null);
      }

      @Override
      public void deliverResult(Cursor cursor) {
        data = cursor;
        super.deliverResult(cursor);
      }
    };
  }

  @Override
  public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
    adapter.swapCursor(data);
  }

  @Override
  public void onLoaderReset(Loader<Cursor> loader) {
    adapter.swapCursor(null);
  }
}
