package com.sedsoftware.comicser.features.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import com.sedsoftware.comicser.data.source.local.ComicContract;

public class ComicSyncManager {

  private static final String ACCOUNT_TYPE = "com.sedsoftware";
  private static final String ACCOUNT_NAME = "sync";

  // Sync interval settings
  private static final long SECONDS_PER_MINUTE = 60L;
  private static final long MINUTES_PER_HOUR = 60L;
  private static final long SYNC_INTERVAL_IN_HOURS = 8L;

  private static final long SYNC_INTERVAL =
      SECONDS_PER_MINUTE * MINUTES_PER_HOUR * SYNC_INTERVAL_IN_HOURS;

  private static final String CONTENT_AUTHORITY = ComicContract.CONTENT_AUTHORITY;

  public static void createSyncAccount(Context context) {

//    boolean newAccount = false;

    Account account = getAccount();
    AccountManager accountManager = (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

    if (accountManager.addAccountExplicitly(account, null, null)) {
      ContentResolver.setIsSyncable(account, CONTENT_AUTHORITY, 1);
      ContentResolver.setSyncAutomatically(account, CONTENT_AUTHORITY, true);
      ContentResolver.addPeriodicSync(
          account, CONTENT_AUTHORITY, new Bundle(), SYNC_INTERVAL);
//      newAccount = true;
    }

//    if (newAccount) {
//      forcedRefresh();
//    }
  }

  private static Account getAccount() {
    return new Account(ACCOUNT_NAME, ACCOUNT_TYPE);
  }

//  private static void forcedRefresh() {
//    Bundle bundle = new Bundle();
//    bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
//    bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
//
//    ContentResolver.requestSync(
//        getAccount(),
//        CONTENT_AUTHORITY,
//        bundle);
//  }
}
