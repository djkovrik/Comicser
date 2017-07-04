package com.sedsoftware.comicser.data.source.local;

import android.content.ContentResolver;
import android.net.Uri;
import android.support.annotation.NonNull;
import com.sedsoftware.comicser.data.model.ComicIssueInfoList;
import com.sedsoftware.comicser.data.model.ComicIssueInfoShort;
import com.sedsoftware.comicser.data.model.ComicVolumeInfoShort;
import com.sedsoftware.comicser.data.source.local.ComicContract.IssueEntry;
import com.sedsoftware.comicser.data.source.local.ComicContract.OwnedIssueEntry;
import com.sedsoftware.comicser.data.source.local.ComicContract.TrackedVolumeEntry;
import com.sedsoftware.comicser.utils.ContentUtils;
import java.util.List;
import javax.inject.Inject;

public class ComicLocalDataHelper {

  private final ContentResolver contentResolver;

  @Inject
  public ComicLocalDataHelper(ContentResolver contentResolver) {
    this.contentResolver = contentResolver;
  }

  public void saveTodaysIssuesToDb(@NonNull List<ComicIssueInfoList> issues) {

    for (ComicIssueInfoList issue : issues) {
      contentResolver.insert(
          IssueEntry.CONTENT_URI_TODAY_ISSUES,
          ContentUtils.issueInfoToContentValues(issue));
    }
  }

  public void removeAllTodaysIssuesFromDb() {
    contentResolver.delete(IssueEntry.CONTENT_URI_TODAY_ISSUES, null, null);
  }

  public void saveOwnedIssueToDb(@NonNull ComicIssueInfoShort issue) {
    contentResolver.insert(
        OwnedIssueEntry.CONTENT_URI_OWNED_ISSUES,
        ContentUtils.ownedIssueToContentValues(issue));
  }

  public void removeOwnedIssueFromDb(long issueId) {
    Uri deletionUri = ContentUtils
        .buildDetailsUri(OwnedIssueEntry.CONTENT_URI_OWNED_ISSUES, issueId);

    contentResolver.delete(deletionUri, null, null);
  }

  public void saveTrackedVolumeToDb(@NonNull ComicVolumeInfoShort volume) {
    contentResolver.insert(
        TrackedVolumeEntry.CONTENT_URI_TRACKED_VOLUMES,
        ContentUtils.trackedVolumeToContentValues(volume));
  }

  public void removeTrackedVolumeFromDb(long volumeId) {
    Uri deletionUri = ContentUtils
        .buildDetailsUri(TrackedVolumeEntry.CONTENT_URI_TRACKED_VOLUMES, volumeId);

    contentResolver.delete(deletionUri, null, null);
  }
}
