package com.sedsoftware.comicser.data.source.local;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.sedsoftware.comicser.data.source.local.ComicContract.IssueEntry;
import com.sedsoftware.comicser.data.source.local.ComicContract.OwnedIssueEntry;
import com.sedsoftware.comicser.data.source.local.ComicContract.TrackedVolumeEntry;

public class ComicProvider extends ContentProvider {

  private static final int CODE_TODAY_ISSUES = 100;
  private static final int CODE_OWNED_ISSUES = 200;
  private static final int CODE_OWNED_ISSUES_WITH_ID = 201;
  private static final int CODE_TRACKED_VOLUMES = 300;
  private static final int CODE_TRACKED_VOLUMES_WITH_ID = 301;

  private static final UriMatcher uriMatcher = buildMatcher();

  private ComicDbHelper comicDbHelper;

  public static UriMatcher buildMatcher() {
    final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
    final String authority = ComicContract.CONTENT_AUTHORITY;

    matcher.addURI(authority, ComicContract.PATH_TODAY_ISSUES, CODE_TODAY_ISSUES);
    matcher.addURI(authority, ComicContract.PATH_OWNED_ISSUES, CODE_OWNED_ISSUES);
    matcher.addURI(authority, ComicContract.PATH_OWNED_ISSUES + "/#", CODE_OWNED_ISSUES_WITH_ID);
    matcher.addURI(authority, ComicContract.PATH_TRACKED_VOLUMES, CODE_TRACKED_VOLUMES);
    matcher.addURI(authority, ComicContract.PATH_TRACKED_VOLUMES + "/#", CODE_TRACKED_VOLUMES_WITH_ID);

    return matcher;
  }

  @Override
  public boolean onCreate() {
    comicDbHelper = new ComicDbHelper(getContext());
    return true;
  }

  @Nullable
  @Override
  public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
      @Nullable String[] selectionArgs, @Nullable String sortOrder) {

    Cursor cursor;
    String recordId;

    switch (uriMatcher.match(uri)) {

      case CODE_TODAY_ISSUES:
        cursor = comicDbHelper.getReadableDatabase().query(
            IssueEntry.TABLE_NAME_TODAY_ISSUES,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            sortOrder);
        break;

      case CODE_OWNED_ISSUES:
        cursor = comicDbHelper.getReadableDatabase().query(
            OwnedIssueEntry.TABLE_NAME_OWNED_ISSUES,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            sortOrder);
        break;

      case CODE_TRACKED_VOLUMES:
        cursor = comicDbHelper.getReadableDatabase().query(
            TrackedVolumeEntry.TABLE_NAME_TRACKED_VOLUMES,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            sortOrder);
        break;

      case CODE_OWNED_ISSUES_WITH_ID:
        recordId = uri.getLastPathSegment();
        cursor = comicDbHelper.getReadableDatabase().query(
            OwnedIssueEntry.TABLE_NAME_OWNED_ISSUES,
            projection,
            OwnedIssueEntry.COLUMN_ISSUE_ID + " = ?",
            new String[]{recordId},
            null,
            null,
            sortOrder);
        break;

      case CODE_TRACKED_VOLUMES_WITH_ID:
        recordId = uri.getLastPathSegment();
        cursor = comicDbHelper.getReadableDatabase().query(
            TrackedVolumeEntry.TABLE_NAME_TRACKED_VOLUMES,
            projection,
            TrackedVolumeEntry.COLUMN_VOLUME_ID + " = ?",
            new String[]{recordId},
            null,
            null,
            sortOrder);
        break;

      default:
        throw new UnsupportedOperationException("Unknown uri (query): " + uri);
    }

    //noinspection ConstantConditions
    cursor.setNotificationUri(getContext().getContentResolver(), uri);
    return cursor;
  }

  @Nullable
  @Override
  public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

    final SQLiteDatabase db = comicDbHelper.getWritableDatabase();
    Uri returnUri;
    long ids;

    switch (uriMatcher.match(uri)) {

      case CODE_TODAY_ISSUES:
        ids = db.insert(IssueEntry.TABLE_NAME_TODAY_ISSUES, null, values);
        if (ids > 0) {
          returnUri = ContentUris.withAppendedId(IssueEntry.CONTENT_URI_TODAY_ISSUES, ids);
        } else {
          throw new SQLException("Failed to insert row into " + uri);
        }
        break;

      case CODE_OWNED_ISSUES:
        ids = db.insert(OwnedIssueEntry.TABLE_NAME_OWNED_ISSUES, null, values);
        if (ids > 0) {
          returnUri = ContentUris.withAppendedId(OwnedIssueEntry.CONTENT_URI_OWNED_ISSUES, ids);
        } else {
          throw new SQLException("Failed to insert row into " + uri);
        }
        break;

      case CODE_TRACKED_VOLUMES:
        ids = db.insert(TrackedVolumeEntry.TABLE_NAME_TRACKED_VOLUMES, null, values);
        if (ids > 0) {
          returnUri = ContentUris.withAppendedId(TrackedVolumeEntry.CONTENT_URI_TRACKED_VOLUMES, ids);
        } else {
          throw new SQLException("Failed to insert row into " + uri);
        }
        break;

      default:
        throw new UnsupportedOperationException("Unknown uri (insert): " + uri);
    }

    //noinspection ConstantConditions
    getContext().getContentResolver().notifyChange(uri, null);
    return returnUri;
  }

  @Override
  public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {

    final SQLiteDatabase db = comicDbHelper.getWritableDatabase();
    int rowsInserted = 0;

    switch (uriMatcher.match(uri)) {

      case CODE_TODAY_ISSUES:
        db.beginTransaction();
        try {
          for (ContentValues value : values) {
            long ids = db.insert(IssueEntry.TABLE_NAME_TODAY_ISSUES, null, value);
            if (ids != -1) {
              rowsInserted++;
            }
          }
          db.setTransactionSuccessful();
        } finally {
          db.endTransaction();
        }

        if (rowsInserted > 0) {
          //noinspection ConstantConditions
          getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsInserted;

      case CODE_OWNED_ISSUES:
        db.beginTransaction();
        try {
          for (ContentValues value : values) {
            long ids = db.insert(OwnedIssueEntry.TABLE_NAME_OWNED_ISSUES, null, value);
            if (ids != -1) {
              rowsInserted++;
            }
          }
          db.setTransactionSuccessful();
        } finally {
          db.endTransaction();
        }

        if (rowsInserted > 0) {
          //noinspection ConstantConditions
          getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsInserted;

      case CODE_TRACKED_VOLUMES:
        db.beginTransaction();
        try {
          for (ContentValues value : values) {
            long ids = db.insert(TrackedVolumeEntry.TABLE_NAME_TRACKED_VOLUMES, null, value);
            if (ids != -1) {
              rowsInserted++;
            }
          }
          db.setTransactionSuccessful();
        } finally {
          db.endTransaction();
        }

        if (rowsInserted > 0) {
          //noinspection ConstantConditions
          getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsInserted;

      default:
        return super.bulkInsert(uri, values);
    }
  }

  @Override
  public int delete(@NonNull Uri uri, @Nullable String selection,
      @Nullable String[] selectionArgs) {

    final SQLiteDatabase db = comicDbHelper.getWritableDatabase();
    int rowsDeleted;
    String recordId;

    if (null == selection) {
      selection = "1";
    }

    switch(uriMatcher.match(uri)) {

      case CODE_TODAY_ISSUES:
        rowsDeleted = db.delete(
            IssueEntry.TABLE_NAME_TODAY_ISSUES,
            selection,
            selectionArgs);
        break;

      case CODE_OWNED_ISSUES:
        rowsDeleted = db.delete(
            OwnedIssueEntry.TABLE_NAME_OWNED_ISSUES,
            selection,
            selectionArgs);
        break;

      case CODE_TRACKED_VOLUMES:
        rowsDeleted = db.delete(
            TrackedVolumeEntry.TABLE_NAME_TRACKED_VOLUMES,
            selection,
            selectionArgs);
        break;

      case CODE_OWNED_ISSUES_WITH_ID:
        recordId = uri.getLastPathSegment();
        rowsDeleted = db.delete(
            OwnedIssueEntry.TABLE_NAME_OWNED_ISSUES,
            OwnedIssueEntry.COLUMN_ISSUE_ID  + " = ?",
            new String[]{recordId});
        break;

      case CODE_TRACKED_VOLUMES_WITH_ID:
        recordId = uri.getLastPathSegment();
        rowsDeleted = db.delete(
            TrackedVolumeEntry.TABLE_NAME_TRACKED_VOLUMES,
            TrackedVolumeEntry.COLUMN_VOLUME_ID + " = ?",
            new String[]{recordId});
        break;

      default:
        throw new UnsupportedOperationException("Unknown uri (delete): " + uri);
    }

    if (rowsDeleted != 0) {
      //noinspection ConstantConditions
      getContext().getContentResolver().notifyChange(uri, null);
    }

    return rowsDeleted;
  }

  @Override
  public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection,
      @Nullable String[] selectionArgs) {
    throw new UnsupportedOperationException("update method not implemented");
  }

  @Nullable
  @Override
  public String getType(@NonNull Uri uri) {
    throw new UnsupportedOperationException("getType method not implemented");
  }
}
