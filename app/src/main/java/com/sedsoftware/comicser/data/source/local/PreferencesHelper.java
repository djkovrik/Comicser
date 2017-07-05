package com.sedsoftware.comicser.data.source.local;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesHelper {

  private static final String PREFS_FILE_NAME = "comicser_prefs";

  private static final String PREFERENCE_SYNC_PERIOD = "comicser_sync_period";
  private static final long PREFERENCE_SYNC_PERIOD_DEFAULT= 86400L; // once per day

  private static final String PREFERENCE_LAST_SYNC_DATE = "comicser_last_sync_date";
  private static final String PREFERENCE_LAST_SYNC_DATE_DEFAULT = "2017-01-01";

  private final SharedPreferences sharedPreferences;

  public PreferencesHelper(Context context) {
    sharedPreferences = context.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE);
  }

  public void setSyncPeriod(long period) {
    sharedPreferences.edit().putLong(PREFERENCE_SYNC_PERIOD, period).apply();
  }

  public long getSyncPeriod() {
    return sharedPreferences.getLong(PREFERENCE_SYNC_PERIOD, PREFERENCE_SYNC_PERIOD_DEFAULT);
  }

  public void setSyncDate(String date) {
    sharedPreferences.edit().putString(PREFERENCE_LAST_SYNC_DATE, date).apply();
  }

  public String getLastSyncDate() {
    return sharedPreferences.getString(PREFERENCE_LAST_SYNC_DATE, PREFERENCE_LAST_SYNC_DATE_DEFAULT);
  }
}
