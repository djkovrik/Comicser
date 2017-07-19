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
import com.sedsoftware.comicser.features.preferences.ComicPreferencesHelper;
import com.sedsoftware.comicser.utils.DateTextUtils;
import com.sedsoftware.comicser.utils.NotificationUtils;
import java.util.Set;
import javax.inject.Inject;
import timber.log.Timber;

public class ComicSyncAdapter extends AbstractThreadedSyncAdapter {

  public static final String ACTION_DATA_UPDATED =
      "com.sedsoftware.comicser.ACTION_DATA_UPDATED";

  @Inject
  ComicLocalDataHelper localDataHelper;

  @Inject
  ComicPreferencesHelper preferencesHelper;

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
              preferencesHelper.setLastSyncDate(date);
              preferencesHelper.clearDisplayedVolumesIdList();
              sendDataUpdatedBroadcast();
              checkForTrackedVolumesUpdates();
              Timber.d("Scheduled sync completed.");
            },
            // onError
            throwable ->
                Timber.d("Scheduled sync error!")
        );
  }

  private void checkForTrackedVolumesUpdates() {

    Set<Long> trackedVolumes = localDataHelper.getTrackedVolumesIdsFromDb();
    Set<Long> todayVolumes = localDataHelper.getTodayVolumesIdsFromDb();
    Set<String> alreadyDisplayedVolumes = preferencesHelper.getDisplayedVolumesIdList();

    StringBuilder notificationText = new StringBuilder();

    for (Long trackedVolumeId : trackedVolumes) {
      // Tracked volume detected
      if (todayVolumes.contains(trackedVolumeId)) {
        // Volume notification was already displayed, continue
        if (alreadyDisplayedVolumes.contains(String.valueOf(trackedVolumeId))) {
          continue;
        }

        // Add volume name to notification
        String volumeName = localDataHelper.getTrackedVolumeNameById(trackedVolumeId);
        notificationText.append(volumeName);
        notificationText.append(", ");

        // Mark volume as displayed
        preferencesHelper.saveDisplayedVolumeId(trackedVolumeId);
      }
    }

    // If notification text is not empty, display it
    if (notificationText.length() > 0) {
      NotificationUtils.notifyUserAboutUpdate(
          getContext(),
          notificationText.deleteCharAt(notificationText.length() - 2).toString());
    }
  }

  private void sendDataUpdatedBroadcast() {
    Context context = getContext();
    Intent dataUpdatedIntent = new Intent(ACTION_DATA_UPDATED).setPackage(context.getPackageName());
    context.sendBroadcast(dataUpdatedIntent);
  }
}
