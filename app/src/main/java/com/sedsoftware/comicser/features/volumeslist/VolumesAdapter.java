package com.sedsoftware.comicser.features.volumeslist;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.sedsoftware.comicser.R;
import com.sedsoftware.comicser.data.model.ComicImages;
import com.sedsoftware.comicser.data.model.ComicVolumeInfoList;
import com.sedsoftware.comicser.features.volumeslist.VolumesAdapter.VolumeViewHolder;
import com.sedsoftware.comicser.utils.ImageUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

class VolumesAdapter extends RecyclerView.Adapter<VolumeViewHolder> {

  private List<ComicVolumeInfoList> volumes;
  private final OnVolumeClickListener listener;

  public VolumesAdapter(OnVolumeClickListener listener) {
    volumes = new ArrayList<>(0);
    this.listener = listener;
  }

  @Override
  public VolumeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.fragment_volumes_list_item, parent, false);

    return new VolumeViewHolder(view);
  }

  @Override
  public void onBindViewHolder(VolumeViewHolder holder, int position) {
    holder.bindTo(volumes.get(position));
  }

  @Override
  public int getItemCount() {
    return volumes == null ? 0 : volumes.size();
  }

  List<ComicVolumeInfoList> getVolumes() {
    return volumes;
  }

  void setVolumes(List<ComicVolumeInfoList> volumes) {
    this.volumes = volumes;
  }

  class VolumeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private long currentVolumeId;

    @BindView(R.id.volume_image_layout)
    FrameLayout imageLayout;
    @BindView(R.id.volume_cover)
    ImageView volumeCover;
    @BindView(R.id.volume_cover_progressbar)
    ProgressBar progressBar;
    @BindView(R.id.volume_name)
    TextView volumeName;
    @BindView(R.id.volume_publisher)
    TextView volumePublisher;
    @BindString(R.string.volumes_publisher_text)
    String publisherFormat;
    @BindView(R.id.volume_year_and_count)
    TextView volumeYearAndCount;
    @BindString(R.string.volumes_year_and_count)
    String yearCountFormat;

    VolumeViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }

    void bindTo(ComicVolumeInfoList volume) {

      currentVolumeId = volume.id();

      ComicImages image = volume.image();
      if (image != null) {
        String url = image.small_url();
        ImageUtils.loadImageWithProgress(volumeCover, url, progressBar);
      } else {
        imageLayout.setVisibility(View.GONE);
      }

      volumeName.setText(volume.name());

      String publisher = String.format(Locale.US, publisherFormat, volume.publisher().name());
      volumePublisher.setText(publisher);

      String yearCount = String.format(Locale.US, yearCountFormat, volume.start_year(), volume.count_of_issues());
      volumeYearAndCount.setText(yearCount);
    }

    @Override
    public void onClick(View v) {
      listener.volumeClicked(currentVolumeId);
    }
  }

  interface OnVolumeClickListener {

    void volumeClicked(long volumeId);
  }
}
