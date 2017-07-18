package com.sedsoftware.comicser.features.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.Intent;
import android.content.SyncResult;
import android.os.Bundle;
import com.sedsoftware.comicser.ComicserApp;
import com.sedsoftware.comicser.data.source.local.ComicLocalDataHelper;
import com.sedsoftware.comicser.data.source.local.dagger.modules.ComicLocalDataModule;
import com.sedsoftware.comicser.data.source.remote.ComicRemoteDataHelper;
import com.sedsoftware.comicser.data.source.remote.dagger.modules.ComicRemoteDataModule;
import com.sedsoftware.comicser.utils.DateTextUtils;
import javax.inject.Inject;
import timber.log.Timber;

public class ComicSyncAdapter extends AbstractThreadedSyncAdapter {

  public static final String ACTION_DATA_UPDATED =
      "com.sedsoftware.comicser.ACTION_DATA_UPDATED";

  @Inject
  ComicLocalDataHelper localDataHelper;

  @Inject
  ComicRemoteDataHelper remoteDataHelper;

  public ComicSyncAdapter(Context context) {
    super(context, true);

    ComicserApp
        .getAppComponent()
        .plusRemoteComponent(new ComicRemoteDataModule())
        .plusLocalComponent(new ComicLocalDataModule())
        .inject(this);
  }

  @Override
  public void onPerformSync(Account account, Bundle extras, String authority,
      ContentProviderClient provider, SyncResult syncResult) {

    Timber.d("Scheduled sync started...");

    String date = DateTextUtils.getTodayDateString();

    remoteDataHelper
        .getIssuesListByDate(date)
        .subscribe(
            // onSuccess
            list -> {
              localDataHelper.removeAllTodayIssuesFromDb();
              localDataHelper.saveTodayIssuesToDb(list);
              sendDataUpdatedBroadcast();
              Timber.d("Scheduled sync completed.");
            },
            // onError
            throwable ->
                Timber.d("Scheduled sync error!")
        );
  }

  private void sendDataUpdatedBroadcast() {
    Context context = getContext();
    Intent dataUpdatedIntent = new Intent(ACTION_DATA_UPDATED).setPackage(context.getPackageName());
    context.sendBroadcast(dataUpdatedIntent);
  }
}
