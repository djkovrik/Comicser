package com.sedsoftware.comicser.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtils {

  public static boolean isNetworkConnected(Context context) {

    ConnectivityManager manager =
        (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

    NetworkInfo activeNetwork = manager.getActiveNetworkInfo();

    return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
  }
}