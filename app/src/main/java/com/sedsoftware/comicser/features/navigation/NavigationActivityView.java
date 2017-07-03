package com.sedsoftware.comicser.features.navigation;

import com.hannesdorfmann.mosby3.mvp.MvpView;

public interface NavigationActivityView extends MvpView {

  void handleChosenNavigationMenuItem(int chosenMenuItem);

  void showTodayIssuesFragment();

  void showVolumesFragment();

  void showCharactersFragment();

  void showCollectionManagerFragment();

  void showReleaseTrackerFragment();

  void showSettingsScreen();
}