package com.sedsoftware.comicser.features.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import com.sedsoftware.comicser.data.source.local.ComicContract;
import timber.log.Timber;

public class ComicSyncManager {

  private static final String ACCOUNT_TYPE = "com.sedsoftware";
  private static final String ACCOUNT_NAME = "sync";

  // Sync interval settings
  private static final long SECONDS_PER_MINUTE = 60L;
  private static final long MINUTES_PER_HOUR = 60L;

  private static final String CONTENT_AUTHORITY = ComicContract.CONTENT_AUTHORITY;

  public static void createSyncAccount(Context context, int hours) {

    final long syncInterval = SECONDS_PER_MINUTE * MINUTES_PER_HOUR * hours;

    Account account = getAccount();
    AccountManager accountManager = (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

    if (accountManager.addAccountExplicitly(account, null, null)) {
      ContentResolver.setIsSyncable(account, CONTENT_AUTHORITY, 1);
      ContentResolver.setSyncAutomatically(account, CONTENT_AUTHORITY, true);
      ContentResolver.addPeriodicSync(
          account, CONTENT_AUTHORITY, new Bundle(), syncInterval);
    }
  }

  public static void updateSyncPeriod(Context context, int hours) {

    final long newSyncInterval = SECONDS_PER_MINUTE * MINUTES_PER_HOUR * hours;

    AccountManager accountManager = (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);
    Account[] accounts = accountManager.getAccountsByType(ACCOUNT_TYPE);

    if (accounts.length == 1) {
      Timber.d("New sync period: " + hours + " hours");
      ContentResolver.addPeriodicSync(
          accounts[0], CONTENT_AUTHORITY, new Bundle(), newSyncInterval);
    }
  }

  public static void syncImmediately() {
    Bundle bundle = new Bundle();
    bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
    bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);

    ContentResolver.requestSync(
        getAccount(),
        CONTENT_AUTHORITY,
        bundle);
  }

  private static Account getAccount() {
    return new Account(ACCOUNT_NAME, ACCOUNT_TYPE);
  }
}
