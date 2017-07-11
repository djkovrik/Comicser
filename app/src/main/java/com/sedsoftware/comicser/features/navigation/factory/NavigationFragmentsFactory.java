package com.sedsoftware.comicser.features.navigation.factory;

import com.sedsoftware.comicser.base.BaseLceFragment;
import com.sedsoftware.comicser.features.issueslist.IssuesFragmentBuilder;
import com.sedsoftware.comicser.features.volumeslist.VolumesFragmentBuilder;

public class NavigationFragmentsFactory {

  public static BaseLceFragment getFragment(@AppNavigation.Section int type) {

    switch (type) {
      case AppNavigation.ISSUES:
        return new IssuesFragmentBuilder().build();
      case AppNavigation.VOLUMES:
        return new VolumesFragmentBuilder().build();
      default:
        return null;
    }
  }
}
