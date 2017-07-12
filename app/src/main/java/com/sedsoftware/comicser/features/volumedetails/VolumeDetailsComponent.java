package com.sedsoftware.comicser.features.volumedetails;

import dagger.Subcomponent;

@VolumeDetailsScope
@Subcomponent
public interface VolumeDetailsComponent {

  VolumeDetailsPresenter presenter();

  void inject(VolumeDetailsFragment fragment);
}
