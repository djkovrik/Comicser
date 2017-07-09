package com.sedsoftware.comicser.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.Menu;

public class ViewUtils {

  public static void tintMenuIcon(Context context, Menu menu, int itemId, int color) {
    Drawable icon = menu.findItem(itemId).getIcon();
    icon = DrawableCompat.wrap(icon);
    DrawableCompat.setTint(icon, ContextCompat.getColor(context, color));
    menu.findItem(itemId).setIcon(icon);
  }
}
