package com.sedsoftware.comicser.features.widget;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import com.sedsoftware.comicser.ComicserApp;
import com.sedsoftware.comicser.R;
import com.sedsoftware.comicser.data.source.local.ComicContract.IssueEntry;
import com.sedsoftware.comicser.features.navigation.NavigationActivity;
import com.sedsoftware.comicser.utils.IssueTextUtils;
import javax.inject.Inject;
import timber.log.Timber;

public class ComicWidgetFactory implements RemoteViewsService.RemoteViewsFactory {

  // Query projection
  private static final String[] PROJECTION = {
      IssueEntry.COLUMN_ISSUE_VOLUME_NAME,
      IssueEntry.COLUMN_ISSUE_NUMBER
  };

  // Columns indexes
  private static final int INDEX_VOLUME_NAME = 0;
  private static final int INDEX_ISSUE_NUMBER = 1;

  @Inject
  ContentResolver contentResolver;

  private Context context;
  private Cursor cursor;

  ComicWidgetFactory(Context context) {

    this.context = context;
    this.cursor = null;

    ComicserApp
        .getAppComponent()
        .plusWidgetComponent()
        .inject(this);
  }

  @Override
  public void onCreate() {

  }

  @Override
  public void onDataSetChanged() {

    if (cursor != null) {
      cursor.close();
    }

    final long identityToken = Binder.clearCallingIdentity();

    cursor = contentResolver.query(
        IssueEntry.CONTENT_URI_TODAY_ISSUES,
        PROJECTION,
        null,
        null,
        null
    );

    Binder.restoreCallingIdentity(identityToken);

    Timber.d("Cursor size: " + cursor.getCount());
  }

  @Override
  public void onDestroy() {
    if (cursor != null) {
      cursor.close();
      cursor = null;
    }
  }

  @Override
  public int getCount() {
    return cursor != null ? cursor.getCount() : 0;
  }

  @Override
  public RemoteViews getViewAt(int position) {

    if (cursor == null || !cursor.moveToPosition(position)) {
      return null;
    }

    RemoteViews views = new RemoteViews(context.getPackageName(),
        R.layout.today_issues_widget_list_item);

    String volume = cursor.getString(INDEX_VOLUME_NAME);
    int issueNumber = cursor.getInt(INDEX_ISSUE_NUMBER);

    String issueName = IssueTextUtils.getFormattedIssueTitle(volume, issueNumber);

    views.setTextViewText(R.id.widget_issue_name, issueName);

    Intent intent = new Intent(context, NavigationActivity.class);
    views.setOnClickFillInIntent(R.id.widget_list_item, intent);

    return views;
  }

  @Override
  public RemoteViews getLoadingView() {
    return new RemoteViews(context.getPackageName(), R.layout.today_issues_widget_list_item);
  }

  @Override
  public int getViewTypeCount() {
    return 1;
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public boolean hasStableIds() {
    return true;
  }
}
