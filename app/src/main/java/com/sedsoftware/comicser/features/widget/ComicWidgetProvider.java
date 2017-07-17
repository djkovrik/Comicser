package com.sedsoftware.comicser.features.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import com.sedsoftware.comicser.R;

public class ComicWidgetProvider extends AppWidgetProvider {

  @Override
  public void onReceive(Context context, Intent intent) {
    super.onReceive(context, intent);
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
