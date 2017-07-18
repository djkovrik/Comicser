package com.sedsoftware.comicser.data.source.local;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesHelper {

  private static final String PREFS_FILE_NAME = "comicser_prefs";

  private static final String PREFERENCE_SHOULD_SHOWCASE_ISSUES = "comicser_views_showcased";

  private final SharedPreferences sharedPreferences;

  public PreferencesHelper(Context context) {
    sharedPreferences = context.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE);
  }

  public void markIssuesViewAsShowcased() {
    sharedPreferences.edit().putBoolean(PREFERENCE_SHOULD_SHOWCASE_ISSUES, true).apply();
  }

  public boolean wasIssuesViewShowcased() {
    return sharedPreferences.getBoolean(PREFERENCE_SHOULD_SHOWCASE_ISSUES, false);
  }
}
