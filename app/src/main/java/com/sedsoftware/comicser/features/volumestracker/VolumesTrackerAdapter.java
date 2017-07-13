package com.sedsoftware.comicser.features.volumestracker;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.sedsoftware.comicser.R;
import com.sedsoftware.comicser.data.source.local.ComicContract.TrackedVolumeEntry;
import com.sedsoftware.comicser.features.volumestracker.VolumesTrackerAdapter.VolumeViewHolder;
import com.sedsoftware.comicser.utils.ImageUtils;
import timber.log.Timber;

public class VolumesTrackerAdapter extends RecyclerView.Adapter<VolumeViewHolder> {

  private OnVolumeClickListener listener;
  Cursor cursor;

  public VolumesTrackerAdapter(OnVolumeClickListener listener) {
    this.listener = listener;
  }

  @Override
  public VolumeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.fragment_volumes_tracker_item, parent, false);

    return new VolumeViewHolder(view);
  }

  @Override
  public void onBindViewHolder(VolumeViewHolder holder, int position) {

    holder.bindTo(position);
}

  @Override
  public int getItemCount() {
    return (cursor != null) ? cursor.getCount() : 0;
  }

  public Cursor swapCursor(Cursor data) {

    Timber.d("Swapping cursor...");

    if (data != null) {
      Timber.d("Cursor size: " + data.getCount());
    } else {
      Timber.d("Cursor is null.");
    }


    if (cursor == data) {
      return null;
    }

    Cursor temp = cursor;
    this.cursor = data;

    if (data != null) {
      notifyDataSetChanged();
    }
    return temp;
  }

  class VolumeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private long currentId;

    @BindView(R.id.volume_cover)
    ImageView volumeCover;
    @BindView(R.id.volume_cover_progressbar)
    ProgressBar progressBar;
    @BindView(R.id.volume_name)
    TextView volumeName;

    VolumeViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);

      itemView.setOnClickListener(this);
    }

    public void bindTo(int position) {

      int idIndex = cursor.getColumnIndexOrThrow(TrackedVolumeEntry.COLUMN_VOLUME_ID);
      int coverIndex = cursor.getColumnIndexOrThrow(TrackedVolumeEntry.COLUMN_VOLUME_SMALL_IMAGE);
      int nameIndex = cursor.getColumnIndexOrThrow(TrackedVolumeEntry.COLUMN_VOLUME_NAME);

      Timber.d("Cursor size is " + cursor.getCount());
      Timber.d("Cursor: id index is " + idIndex);
      Timber.d("Cursor: cover index is " + coverIndex);
      Timber.d("Cursor: name index is " + nameIndex);

      cursor.moveToPosition(position);

      Timber.d("Cursor current position" + cursor.getPosition());

      Timber.d("Trying to get id");
      currentId = cursor.getLong(idIndex);
      Timber.d("Trying to get cover");
      String coverUrl = cursor.getString(coverIndex);
      Timber.d("Trying to get name");
      String name = cursor.getString(nameIndex);

      ImageUtils.loadImageWithProgress(volumeCover, coverUrl, progressBar);
      volumeName.setText(name);
    }

    @Override
    public void onClick(View v) {
      listener.volumeClicked(currentId);
    }
  }

  interface OnVolumeClickListener {

    void volumeClicked(long volumeId);
  }
}
