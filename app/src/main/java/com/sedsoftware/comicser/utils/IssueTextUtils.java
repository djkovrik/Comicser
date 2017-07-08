package com.sedsoftware.comicser.utils;

import java.util.Locale;

public class IssueTextUtils {

  public static String getFormattedIssueName(String issue, String volume, int number) {

    String name;

    if (issue != null) {
      name = String.format(Locale.US, "%s #%d - %s", volume, number, issue);
    } else {
      name = String.format(Locale.US, "%s #%d", volume, number);
    }

    return name;
  }

  public static String getFormattedIssueDetailsTitle(String volume, int number) {
    return String.format(Locale.US, "%s - Issue #%d", volume, number);
  }
}
