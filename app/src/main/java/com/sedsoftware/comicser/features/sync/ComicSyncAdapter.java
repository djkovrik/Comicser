package com.sedsoftware.comicser.features.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import com.sedsoftware.comicser.ComicserApp;
import com.sedsoftware.comicser.data.source.local.ComicContract.IssueEntry;
import com.sedsoftware.comicser.data.source.local.dagger.modules.ComicLocalDataModule;
import com.sedsoftware.comicser.data.source.remote.ComicRemoteDataHelper;
import com.sedsoftware.comicser.data.source.remote.dagger.modules.ComicRemoteDataModule;
import com.sedsoftware.comicser.utils.ContentUtils;
import com.sedsoftware.comicser.utils.DateTextUtils;
import javax.inject.Inject;
import timber.log.Timber;

public class ComicSyncAdapter extends AbstractThreadedSyncAdapter {

  @Inject
  ContentResolver contentResolver;

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

    String date = DateTextUtils.getTodayDateString();

    // TODO() Check how this works with local data helper
    remoteDataHelper
        .getIssuesListByDate(date)
        .subscribe(
            // onSuccess
            list -> {
              Timber.d("Scheduled sync completed.");

              if (list.size() > 0) {
                contentResolver.delete(IssueEntry.CONTENT_URI_TODAY_ISSUES, null, null);
                contentResolver.bulkInsert(
                    IssueEntry.CONTENT_URI_TODAY_ISSUES,
                    ContentUtils.issuesToContentValues(list));
              }
            },
            // onError
            throwable ->
                Timber.d("Scheduled sync error!")
        );
  }
}
