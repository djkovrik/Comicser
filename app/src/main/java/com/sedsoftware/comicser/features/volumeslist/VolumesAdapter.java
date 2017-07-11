package com.sedsoftware.comicser.features.volumeslist;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import com.sedsoftware.comicser.features.volumeslist.VolumesAdapter.VolumeViewHolder;

class VolumesAdapter extends RecyclerView.Adapter<VolumeViewHolder> {

  @Override
  public VolumeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return null;
  }

  @Override
  public void onBindViewHolder(VolumeViewHolder holder, int position) {

  }

  @Override
  public int getItemCount() {
    return 0;
  }

  class VolumeViewHolder extends RecyclerView.ViewHolder {

    public VolumeViewHolder(View itemView) {
      super(itemView);
    }
  }
}
