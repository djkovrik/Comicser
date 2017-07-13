package com.sedsoftware.comicser.features.characterdetails;

import dagger.Subcomponent;

@CharacterDetailsScope
@Subcomponent
public interface CharacterDetailsComponent {

  CharacterDetailsPresenter presenter();

  void inject(CharacterDetailsFragment fragment);
}
