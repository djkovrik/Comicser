package com.sedsoftware.comicser.features.navigation.factory;

import android.support.v4.app.FragmentManager;
import com.sedsoftware.comicser.base.BaseLceFragment;
import com.sedsoftware.comicser.features.characterslist.CharactersFragment;
import com.sedsoftware.comicser.features.characterslist.CharactersFragmentBuilder;
import com.sedsoftware.comicser.features.issueslist.IssuesFragment;
import com.sedsoftware.comicser.features.issueslist.IssuesFragmentBuilder;
import com.sedsoftware.comicser.features.volumeslist.VolumesFragment;
import com.sedsoftware.comicser.features.volumeslist.VolumesFragmentBuilder;

public class NavigationFragmentsFactory {

  public static BaseLceFragment getFragment(FragmentManager manager, @AppNavigation.Section int type) {

    BaseLceFragment fragment = (BaseLceFragment) manager.findFragmentByTag(getFragmentTag(type));

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
      default:
        return "";
    }
  }
}
