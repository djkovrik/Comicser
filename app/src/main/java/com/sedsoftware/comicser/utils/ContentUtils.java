package com.sedsoftware.comicser.utils;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;
import com.sedsoftware.comicser.data.model.ComicImages;
import com.sedsoftware.comicser.data.model.ComicIssueInfoList;
import com.sedsoftware.comicser.data.model.ComicIssueInfoShort;
import com.sedsoftware.comicser.data.model.ComicVolumeInfoShort;
import com.sedsoftware.comicser.data.source.local.ComicContract.IssueEntry;
import com.sedsoftware.comicser.data.source.local.ComicContract.OwnedIssueEntry;
import com.sedsoftware.comicser.data.source.local.ComicContract.TrackedVolumeEntry;

public class ContentUtils {

  public static ContentValues issueInfoToContentValues(@NonNull ComicIssueInfoList issue) {

    ContentValues values = new ContentValues();
    values.put(IssueEntry.COLUMN_ISSUE_ID, issue.id());
    values.put(IssueEntry.COLUMN_ISSUE_NUMBER, issue.issue_number());
    values.put(IssueEntry.COLUMN_ISSUE_NAME, issue.name());
    values.put(IssueEntry.COLUMN_ISSUE_STORE_DATE, issue.store_date());
    values.put(IssueEntry.COLUMN_ISSUE_SMALL_IMAGE, issue.image().small_url());
    values.put(IssueEntry.COLUMN_ISSUE_MEDIUM_IMAGE, issue.image().medium_url());
    values.put(IssueEntry.COLUMN_ISSUE_HD_IMAGE, issue.image().super_url());
    values.put(IssueEntry.COLUMN_ISSUE_VOLUME_ID, issue.volume().id());
    values.put(IssueEntry.COLUMN_ISSUE_VOLUME_NAME, issue.volume().name());

    return values;
  }

  public static ComicIssueInfoList getIssueInfoFromCursor(@NonNull Cursor cursor) {

    long id = cursor.getLong(cursor.getColumnIndexOrThrow(IssueEntry.COLUMN_ISSUE_ID));
    String name = cursor.getString(cursor.getColumnIndexOrThrow(IssueEntry.COLUMN_ISSUE_NAME));
    int number = cursor.getInt(cursor.getColumnIndexOrThrow(IssueEntry.COLUMN_ISSUE_NUMBER));
    String date = cursor.getString(cursor.getColumnIndexOrThrow(IssueEntry.COLUMN_ISSUE_STORE_DATE));
    String small = cursor.getString(cursor.getColumnIndexOrThrow(IssueEntry.COLUMN_ISSUE_SMALL_IMAGE));
    String medium = cursor.getString(cursor.getColumnIndexOrThrow(IssueEntry.COLUMN_ISSUE_MEDIUM_IMAGE));
    String hd = cursor.getString(cursor.getColumnIndexOrThrow(IssueEntry.COLUMN_ISSUE_HD_IMAGE));
    long volumeId = cursor.getLong(cursor.getColumnIndexOrThrow(IssueEntry.COLUMN_ISSUE_VOLUME_ID));
    String volumeName = cursor.getString(cursor.getColumnIndexOrThrow(IssueEntry.COLUMN_ISSUE_VOLUME_NAME));

    return ComicIssueInfoList.builder()
        .id(id)
        .issue_number(number)
        .name(name)
        .store_date(date)
        .image(
            ComicImages.builder()
            .icon_url("")
            .medium_url(medium)
            .screen_url("")
            .small_url(small)
            .super_url(hd)
            .thumb_url("")
            .tiny_url("")
            .build())
        .volume(
            ComicVolumeInfoShort.builder()
            .id(volumeId)
            .name(volumeName)
            .build()
        )
        .build();
  }

  public static ContentValues ownedIssueToContentValues(@NonNull ComicIssueInfoShort issue) {

    ContentValues values = new ContentValues();
    values.put(OwnedIssueEntry.COLUMN_ISSUE_ID, issue.id());
    values.put(OwnedIssueEntry.COLUMN_ISSUE_NAME, issue.name());
    values.put(OwnedIssueEntry.COLUMN_ISSUE_NUMBER, issue.issue_number());

    return values;
  }

  public static ComicIssueInfoShort getShortIssueInfoFromCursor(Cursor cursor) {

    long id = cursor.getLong(cursor.getColumnIndexOrThrow(OwnedIssueEntry.COLUMN_ISSUE_ID));
    String name = cursor.getString(cursor.getColumnIndexOrThrow(OwnedIssueEntry.COLUMN_ISSUE_NAME));
    int number = cursor.getInt(cursor.getColumnIndexOrThrow(OwnedIssueEntry.COLUMN_ISSUE_NUMBER));

    return ComicIssueInfoShort.builder()
        .id(id)
        .name(name)
        .issue_number(number)
        .build();
  }

  public static ContentValues trackedVolumeToContentValues(@NonNull ComicVolumeInfoShort volume) {

    ContentValues values = new ContentValues();
    values.put(TrackedVolumeEntry.COLUMN_VOLUME_ID, volume.id());
    values.put(TrackedVolumeEntry.COLUMN_VOLUME_NAME, volume.name());

    return values;
  }

  public static ComicVolumeInfoShort getShortVolumeInfoFromCursor(Cursor cursor) {

    long id = cursor.getLong(cursor.getColumnIndexOrThrow(TrackedVolumeEntry.COLUMN_VOLUME_ID));
    String name = cursor.getString(cursor.getColumnIndexOrThrow(TrackedVolumeEntry.COLUMN_VOLUME_NAME));

    return ComicVolumeInfoShort.builder()
        .id(id)
        .name(name)
        .build();
  }
}
