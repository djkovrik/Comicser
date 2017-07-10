package com.sedsoftware.comicser.data.source.local;


import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import com.sedsoftware.comicser.data.model.ComicIssueInfoList;
import com.sedsoftware.comicser.data.model.ComicVolumeInfoList;
import com.sedsoftware.comicser.data.source.local.ComicContract.IssueEntry;
import com.sedsoftware.comicser.data.source.local.ComicContract.TrackedVolumeEntry;
import com.sedsoftware.comicser.utils.ContentUtils;
import io.reactivex.Observable;
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

  public Observable<List<ComicIssueInfoList>> getTodaysIssuesFromDb() {

    return Observable.create(e -> {

      Cursor query = contentResolver
          .query(IssueEntry.CONTENT_URI_TODAY_ISSUES, null, null, null, null);

      if (query != null) {
        List<ComicIssueInfoList> list = ContentUtils.issueInfoFromCursor(query);
        query.close();
        e.onNext(list);
      }
      e.onComplete();
    });
  }

  public void removeAllTodaysIssuesFromDb() {
    contentResolver.delete(IssueEntry.CONTENT_URI_TODAY_ISSUES, null, null);
  }

  public boolean isIssueBookmarked(long issueId) {
    Cursor query = contentResolver.query(
        IssueEntry.CONTENT_URI_OWNED_ISSUES,
        null,
        IssueEntry.COLUMN_ISSUE_ID + " = ?",
        new String[]{String.valueOf(issueId)},
        null);

    return (query != null && query.getCount() > 0);
  }

  public void saveOwnedIssueToDb(@NonNull ComicIssueInfoList issue) {
    contentResolver.insert(
        IssueEntry.CONTENT_URI_OWNED_ISSUES,
        ContentUtils.issueInfoToContentValues(issue));
  }

  public void removeOwnedIssueFromDb(long issueId) {
    Uri deletionUri = ContentUtils
        .buildDetailsUri(IssueEntry.CONTENT_URI_OWNED_ISSUES, issueId);

    contentResolver.delete(deletionUri, null, null);
  }

  public void saveTrackedVolumeToDb(@NonNull ComicVolumeInfoList volume) {
    contentResolver.insert(
        TrackedVolumeEntry.CONTENT_URI_TRACKED_VOLUMES,
        ContentUtils.volumeInfoToContentValues(volume));
  }

  public void removeTrackedVolumeFromDb(long volumeId) {
    Uri deletionUri = ContentUtils
        .buildDetailsUri(TrackedVolumeEntry.CONTENT_URI_TRACKED_VOLUMES, volumeId);

    contentResolver.delete(deletionUri, null, null);
  }
}
