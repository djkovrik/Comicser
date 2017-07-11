package com.sedsoftware.comicser.features.navigation.factory;

import android.support.annotation.IntDef;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class AppNavigation {

  public static final int ISSUES = 0;
  public static final int VOLUMES = 1;

  @Retention(RetentionPolicy.CLASS)
  @IntDef({ISSUES, VOLUMES})
  public @interface Section {

  }
}