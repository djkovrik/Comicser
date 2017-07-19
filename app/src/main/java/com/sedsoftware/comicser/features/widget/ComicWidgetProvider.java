package com.sedsoftware.comicser.features.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import com.sedsoftware.comicser.R;
import com.sedsoftware.comicser.features.navigation.NavigationActivity;
import com.sedsoftware.comicser.features.sync.ComicSyncAdapter;
import com.sedsoftware.comicser.features.sync.ComicSyncManager;

public class ComicWidgetProvider extends AppWidgetProvider {

  public static final String ACTION_CLICK_REFRESH_BUTTON = "ACTION_CLICK_REFRESH_BUTTON";

  @Override
  public void onReceive(Context context, Intent intent) {
    super.onReceive(context, intent);

    // Handle data updated broadcast
    if (ComicSyncAdapter.ACTION_DATA_UPDATED.equals(intent.getAction())) {
      AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

      int[] appWidgetIds = appWidgetManager.getAppWidgetIds(
          new ComponentName(context, getClass()));

      appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list);
    }

    // Run forced sync when refresh button clicked
    if (ACTION_CLICK_REFRESH_BUTTON.equals(intent.getAction())) {
      ComicSyncManager.syncImmediately();
    }
  }

  @Override
  public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
    for (int appWidgetId : appWidgetIds) {
      RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.today_issues_widget);
      views.setRemoteAdapter(R.id.widget_list, new Intent(context, ComicWidgetService.class));
      views.setEmptyView(R.id.widget_list, R.id.widget_empty);

      // Set intent to refresh button
      Intent refreshIntent = new Intent(context, ComicWidgetProvider.class);
      refreshIntent.setAction(ACTION_CLICK_REFRESH_BUTTON);
      PendingIntent refreshPendingIntent = PendingIntent.getBroadcast(context, 0, refreshIntent, 0);
      views.setOnClickPendingIntent(R.id.widget_refresh_button, refreshPendingIntent);

      // Set pending intent to list item
      Intent intent = new Intent(context, NavigationActivity.class);
      PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
      views.setPendingIntentTemplate(R.id.widget_list, pendingIntent);

      appWidgetManager.updateAppWidget(appWidgetId, views);
    }
  }
}
