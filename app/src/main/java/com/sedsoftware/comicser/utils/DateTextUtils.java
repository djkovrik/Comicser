package com.sedsoftware.comicser.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateTextUtils {

  public static String getTodayDateString() {
    return getDateString(new Date());
  }

  public static String getDateString(Date date) {
    return new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(date);
  }

  public static String getFormattedDate(String rawDate, String format) {

    SimpleDateFormat oldFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    Date storeDate = new Date();

    try {
      storeDate = oldFormat.parse(rawDate);
    } catch (ParseException e) {
      e.printStackTrace();
    }

    SimpleDateFormat newFormat = new SimpleDateFormat(format, Locale.US);

    return newFormat.format(storeDate);
  }
}
