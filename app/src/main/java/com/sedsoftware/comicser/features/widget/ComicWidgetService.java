package com.sedsoftware.comicser.features.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class ComicWidgetService extends RemoteViewsService {

  @Override
  public RemoteViewsFactory onGetViewFactory(Intent intent) {
    return new ComicWidgetFactory(getApplicationContext());
  }
}
