package com.sedsoftware.comicser.features.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import com.sedsoftware.comicser.R;

public class PreferencesHelper {

  private final Context context;
  private final SharedPreferences sharedPreferences;

  public PreferencesHelper(Context context) {
    this.context = context;

    sharedPreferences = context
        .getSharedPreferences(context.getString(R.string.prefs_file_name), Context.MODE_PRIVATE);
  }

  public void markIssuesViewAsShowcased() {
    sharedPreferences
        .edit()
        .putBoolean(context.getString(R.string.prefs_issues_showcased_key), true)
        .apply();
  }

  public boolean wasIssuesViewShowcased() {
    return sharedPreferences
        .getBoolean(context.getString(R.string.prefs_issues_showcased_key), false);
  }

  public String getSyncPeriod() {
    return sharedPreferences
        .getString(context.getString(R.string.prefs_sync_period_key), "24");
  }
}
