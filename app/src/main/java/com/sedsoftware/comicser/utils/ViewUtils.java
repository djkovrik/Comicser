package com.sedsoftware.comicser.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

public class ViewUtils {

  /**
   * Tints menu item in the given menu with chosen color.
   *
   * @param context Current context
   * @param menu Target menu
   * @param itemId Target menu item
   * @param color Desired color
   */
  public static void tintMenuIcon(Context context, Menu menu, int itemId, int color) {
    Drawable icon = menu.findItem(itemId).getIcon();
    icon = DrawableCompat.wrap(icon);
    DrawableCompat.setTint(icon, ContextCompat.getColor(context, color));
    menu.findItem(itemId).setIcon(icon);
  }

  /**
   * Sets ListView height dynamically based on all items heights sum.
   * Function assumes that all items have the same height.
   *
   * @param listView Target listView
   */
  public static void setListViewHeightBasedOnChildren(ListView listView) {

    ListAdapter listAdapter = listView.getAdapter();
    if (listAdapter == null) {
      return;
    }

    int itemsCount = listAdapter.getCount();
    View listItem = listAdapter.getView(0, null, listView);

    listItem.measure(0, 0);
    int singleItemHeight = listItem.getMeasuredHeight();

    int totalHeight = singleItemHeight * itemsCount;
    int dividersHeight = listView.getDividerHeight() * (itemsCount - 1);

    totalHeight += dividersHeight;

    ViewGroup.LayoutParams params = listView.getLayoutParams();
    params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
    listView.setLayoutParams(params);
    listView.requestLayout();
  }
}
