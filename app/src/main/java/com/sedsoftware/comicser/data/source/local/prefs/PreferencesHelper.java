package com.sedsoftware.comicser.data.source.local.prefs;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesHelper {

  private static final String PREFS_FILE_NAME = "comicser_prefs";
  private static final String PREFERENCE_SYNC_PERIOD = "comicser_sync_period";
  private static final long PREFERENCE_DEFAULT_SYNC_PERIOD = 86400L; // once per day

  private final SharedPreferences sharedPreferences;

  public PreferencesHelper(Context context) {
    sharedPreferences = context.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE);
  }

  public void setSyncPeriod(long period) {
    sharedPreferences.edit().putLong(PREFERENCE_SYNC_PERIOD, period).apply();
  }

  public long getSyncPeriod() {
    return sharedPreferences.getLong(PREFERENCE_SYNC_PERIOD, PREFERENCE_DEFAULT_SYNC_PERIOD);
  }
}
