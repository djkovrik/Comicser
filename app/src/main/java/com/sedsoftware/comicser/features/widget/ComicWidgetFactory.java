package com.sedsoftware.comicser.features.widget;

import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import com.sedsoftware.comicser.ComicserApp;
import com.sedsoftware.comicser.data.source.local.ComicLocalDataHelper;
import javax.inject.Inject;

public class ComicWidgetFactory implements RemoteViewsService.RemoteViewsFactory {

  @Inject
  ComicLocalDataHelper localDataHelper;

  ComicWidgetFactory() {
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

  }

  @Override
  public void onDestroy() {

  }

  @Override
  public int getCount() {
    return 0;
  }

  @Override
  public RemoteViews getViewAt(int position) {
    return null;
  }

  @Override
  public RemoteViews getLoadingView() {
    return null;
  }

  @Override
  public int getViewTypeCount() {
    return 0;
  }

  @Override
  public long getItemId(int position) {
    return 0;
  }

  @Override
  public boolean hasStableIds() {
    return false;
  }
}
