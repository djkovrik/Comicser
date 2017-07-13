package com.sedsoftware.comicser.features.navigation.factory;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import com.sedsoftware.comicser.features.characterslist.CharactersFragment;
import com.sedsoftware.comicser.features.characterslist.CharactersFragmentBuilder;
import com.sedsoftware.comicser.features.issueslist.IssuesFragment;
import com.sedsoftware.comicser.features.issueslist.IssuesFragmentBuilder;
import com.sedsoftware.comicser.features.volumeslist.VolumesFragment;
import com.sedsoftware.comicser.features.volumeslist.VolumesFragmentBuilder;
import com.sedsoftware.comicser.features.volumestracker.VolumesTrackerFragment;
import com.sedsoftware.comicser.features.volumestracker.VolumesTrackerFragmentBuilder;

public class NavigationFragmentsFactory {

  public static Fragment getFragment(FragmentManager manager, @AppNavigation.Section int type) {

    Fragment fragment = manager.findFragmentByTag(getFragmentTag(type));

    if (fragment != null) {
      return fragment;
    }

    switch (type) {
      case AppNavigation.ISSUES:
        return new IssuesFragmentBuilder().build();
      case AppNavigation.VOLUMES:
        return new VolumesFragmentBuilder().build();
      case AppNavigation.CHARACTERS:
        return new CharactersFragmentBuilder().build();
      case AppNavigation.TRACKER:
        return new VolumesTrackerFragmentBuilder().build();
      default:
        return null;
    }
  }

  public static String getFragmentTag(@AppNavigation.Section int type) {
    switch (type) {
      case AppNavigation.ISSUES:
        return IssuesFragment.class.getSimpleName();
      case AppNavigation.VOLUMES:
        return VolumesFragment.class.getSimpleName();
      case AppNavigation.CHARACTERS:
        return CharactersFragment.class.getSimpleName();
      case AppNavigation.TRACKER:
        return VolumesTrackerFragment.class.getSimpleName();
      default:
        return "";
    }
  }
}
