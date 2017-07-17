package com.sedsoftware.comicser.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class IssueTextUtilsTest {

  private static final String ISSUE_NAME = "Road\'s End";
  private static final String VOLUME_NAME = "The Walking Dead";
  private static final int ISSUE_NUMBER = 168;

  private static final String FORMATTED_NAME_FULL = "The Walking Dead #168 - Road\'s End";
  private static final String FORMATTED_NAME_SHORT = "The Walking Dead #168";

  @Test
  public void testGetFormattedIssueName() {

    // Assert
    assertEquals("getFormattedIssueName method returned incorrect string!",
        FORMATTED_NAME_FULL,
        IssueTextUtils.getFormattedIssueName(ISSUE_NAME, VOLUME_NAME, ISSUE_NUMBER));

    assertEquals("getFormattedIssueName method returned incorrect string!",
        FORMATTED_NAME_SHORT,
        IssueTextUtils.getFormattedIssueName(null, VOLUME_NAME, ISSUE_NUMBER));
  }

  @Test
  public void testGetFormattedIssueDetailsTitle() {
    assertEquals("getFormattedIssueTitle method returned incorrect string!",
        FORMATTED_NAME_SHORT,
        IssueTextUtils.getFormattedIssueTitle(VOLUME_NAME, ISSUE_NUMBER));
  }

}
