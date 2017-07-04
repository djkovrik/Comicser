package com.sedsoftware.comicser.utils;

import android.content.ContentValues;
import android.net.Uri;
import android.support.annotation.NonNull;
import com.sedsoftware.comicser.data.model.ComicIssueInfoList;
import com.sedsoftware.comicser.data.model.ComicVolumeInfoList;
import com.sedsoftware.comicser.data.source.local.ComicContract.IssueEntry;
import com.sedsoftware.comicser.data.source.local.ComicContract.TrackedVolumeEntry;

public class ContentUtils {

  public static ContentValues issueInfoToContentValues(@NonNull ComicIssueInfoList issue) {

    ContentValues values = new ContentValues();
    values.put(IssueEntry.COLUMN_ISSUE_ID, issue.id());
    values.put(IssueEntry.COLUMN_ISSUE_NUMBER, issue.issue_number());
    values.put(IssueEntry.COLUMN_ISSUE_NAME, issue.name());
    values.put(IssueEntry.COLUMN_ISSUE_STORE_DATE, issue.store_date());
    values.put(IssueEntry.COLUMN_ISSUE_COVER_DATE, issue.cover_date());
    values.put(IssueEntry.COLUMN_ISSUE_SMALL_IMAGE, issue.image().small_url());
    values.put(IssueEntry.COLUMN_ISSUE_MEDIUM_IMAGE, issue.image().medium_url());
    values.put(IssueEntry.COLUMN_ISSUE_HD_IMAGE, issue.image().super_url());
    values.put(IssueEntry.COLUMN_ISSUE_VOLUME_ID, issue.volume().id());
    values.put(IssueEntry.COLUMN_ISSUE_VOLUME_NAME, issue.volume().name());

    return values;
  }

  public static ContentValues volumeInfoToContentValues(@NonNull ComicVolumeInfoList volume) {

    ContentValues values = new ContentValues();

    values.put(TrackedVolumeEntry.COLUMN_VOLUME_ID, volume.id());
    values.put(TrackedVolumeEntry.COLUMN_VOLUME_NAME, volume.name());
    values.put(TrackedVolumeEntry.COLUMN_VOLUME_ISSUES_COUNT, volume.count_of_issues());
    values.put(TrackedVolumeEntry.COLUMN_VOLUME_PUBLISHER_NAME, volume.publisher().name());
    values.put(TrackedVolumeEntry.COLUMN_VOLUME_START_YEAR, volume.start_year());
    values.put(TrackedVolumeEntry.COLUMN_VOLUME_SMALL_IMAGE, volume.image().small_url());
    values.put(TrackedVolumeEntry.COLUMN_VOLUME_MEDIUM_IMAGE, volume.image().medium_url());
    values.put(TrackedVolumeEntry.COLUMN_VOLUME_HD_IMAGE, volume.image().super_url());

    return values;
  }


  public static Uri buildDetailsUri(Uri baseUri, long recordId) {
    return baseUri.buildUpon()
        .appendPath(String.valueOf(recordId))
        .build();
  }
}
