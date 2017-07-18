package com.sedsoftware.comicser.features.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import com.sedsoftware.comicser.R;
import com.sedsoftware.comicser.features.sync.ComicSyncAdapter;

public class ComicWidgetProvider extends AppWidgetProvider {

  @Override
  public void onReceive(Context context, Intent intent) {
    super.onReceive(context, intent);

    if (ComicSyncAdapter.ACTION_DATA_UPDATED.equals(intent.getAction())) {
      AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

      int[] appWidgetIds = appWidgetManager.getAppWidgetIds(
          new ComponentName(context, getClass()));

      appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list);
    }
  }

  @Override
  public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
    for (int appWidgetId : appWidgetIds) {
      RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.today_issues_widget);
      views.setRemoteAdapter(R.id.widget_list, new Intent(context, ComicWidgetService.class));
      views.setEmptyView(R.id.widget_list, R.id.widget_empty);
      appWidgetManager.updateAppWidget(appWidgetId, views);
    }
  }
}
