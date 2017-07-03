package com.sedsoftware.comicser.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

  public static String getTodayDateString() {
    return new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(new Date());
  }

  public static String getDateString(Date date) {
    return new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(date);
  }
}
