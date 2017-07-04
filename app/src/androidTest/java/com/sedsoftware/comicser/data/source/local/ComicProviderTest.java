package com.sedsoftware.comicser.data.source.local;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.test.runner.AndroidJUnit4;
import com.sedsoftware.comicser.data.source.local.ComicContract.IssueEntry;
import com.sedsoftware.comicser.data.source.local.ComicContract.TrackedVolumeEntry;
import com.sedsoftware.comicser.utils.ContentUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ComicProviderTest {

  private static final Uri TEST_TODAY_ISSUES = IssueEntry.CONTENT_URI_TODAY_ISSUES;
  private static final Uri TEST_OWNED_ISSUES = IssueEntry.CONTENT_URI_OWNED_ISSUES;
  private static final Uri TEST_TRACKED_VOLUMES = TrackedVolumeEntry.CONTENT_URI_TRACKED_VOLUMES;

  private static final long TEST_ISSUE_ID = 123;
  private static final long TEST_VOLUME_ID = 321;

  private static final Uri TEST_OWNED_ISSUES_WITH_ID = ContentUtils
      .buildDetailsUri(TEST_OWNED_ISSUES, TEST_ISSUE_ID);

  private static final Uri TEST_TRACKED_VOLUMES_WITH_ID = ContentUtils
      .buildDetailsUri(TEST_TRACKED_VOLUMES, TEST_VOLUME_ID);

  private UriMatcher testMatcher;
  private Context context;
  private ContentResolver contentResolver;

  @Before
  public void setUp() {
    testMatcher = ComicProvider.buildMatcher();
    context = getTargetContext();
    contentResolver = context.getContentResolver();
  }

  @Test
  public void testUriMatcher() {
    assertEquals("Error: Today's issues uri was matched incorrectly.",
        testMatcher.match(TEST_TODAY_ISSUES),
        ComicProvider.CODE_TODAY_ISSUES);

    assertEquals("Error: Owned issues uri was matched incorrectly.",
        testMatcher.match(TEST_OWNED_ISSUES),
        ComicProvider.CODE_OWNED_ISSUES);

    assertEquals("Error: Tracked volumes uri was matched incorrectly.",
        testMatcher.match(TEST_TRACKED_VOLUMES),
        ComicProvider.CODE_TRACKED_VOLUMES);

    assertEquals("Error: Owned issue by id uri was matched incorrectly.",
        testMatcher.match(TEST_OWNED_ISSUES_WITH_ID),
        ComicProvider.CODE_OWNED_ISSUES_WITH_ID);

    assertEquals("Error: Tracked volume by id uri was matched incorrectly.",
        testMatcher.match(TEST_TRACKED_VOLUMES_WITH_ID),
        ComicProvider.CODE_TRACKED_VOLUMES_WITH_ID);
  }

  @Test
  public void testProviderRegistration() {

    String packageName = context.getPackageName();
    PackageManager packageManager = context.getPackageManager();
    ComponentName component = new ComponentName(packageName, ComicProvider.class.getName());

    try {
      ProviderInfo providerInfo = packageManager.getProviderInfo(component, 0);
      assertEquals("Error: Detected ComicProvider authority: " + providerInfo.authority +
              ", required ComicProvider authority: " + ComicContract.CONTENT_AUTHORITY,
          providerInfo.authority, ComicContract.CONTENT_AUTHORITY);
    } catch (PackageManager.NameNotFoundException e) {
      assertTrue("Error: ComicProvider not registered at " + context.getPackageName(), false);
    }
  }

  @Test
  public void testTodayIssuesQuery() {
    ComicDbHelper comicDbHelper = new ComicDbHelper(context);
    SQLiteDatabase database = comicDbHelper.getWritableDatabase();
    ContentValues testValues = ContentUtils.issueInfoToContentValues(TestUtils.getDummyIssueInfo());

    // Insert content via database
    database.insert(IssueEntry.TABLE_NAME_TODAY_ISSUES, null, testValues);
    database.close();

    // Query content via provider
    Cursor queryCursor = contentResolver.query(
        IssueEntry.CONTENT_URI_TODAY_ISSUES,
        null,
        null,
        null,
        null);

    TestUtils.validateCursor("testTodayIssuesQuery", queryCursor, testValues);
  }

  @Test
  public void testOwnedIssuesQuery() {
    ComicDbHelper comicDbHelper = new ComicDbHelper(context);
    SQLiteDatabase database = comicDbHelper.getWritableDatabase();
    ContentValues testValues = ContentUtils
        .issueInfoToContentValues(TestUtils.getDummyIssueInfo());

    // Insert content via database
    database.insert(IssueEntry.TABLE_NAME_OWNED_ISSUES, null, testValues);
    database.close();

    // Query content via provider
    Cursor queryCursor = contentResolver.query(
        IssueEntry.CONTENT_URI_OWNED_ISSUES,
        null,
        null,
        null,
        null);

    TestUtils.validateCursor("testOwnedIssuesQuery", queryCursor, testValues);
  }

  @Test
  public void testTrackedVolumesQuery() {
    ComicDbHelper comicDbHelper = new ComicDbHelper(context);
    SQLiteDatabase database = comicDbHelper.getWritableDatabase();
    ContentValues testValues = ContentUtils
        .volumeInfoToContentValues(TestUtils.getDummyVolumeInfo());

    // Insert content via database
    database.insert(TrackedVolumeEntry.TABLE_NAME_TRACKED_VOLUMES, null, testValues);
    database.close();

    // Query content via provider
    Cursor queryCursor = contentResolver.query(
        TrackedVolumeEntry.CONTENT_URI_TRACKED_VOLUMES,
        null,
        null,
        null,
        null);

    TestUtils.validateCursor("testTrackedVolumesQuery", queryCursor, testValues);
  }

  @Test
  public void testRecordsDeletion() {

    deleteAllRecordsViaProvider();

    Cursor todayIssues = contentResolver
        .query(IssueEntry.CONTENT_URI_TODAY_ISSUES, null, null, null, null);
    assertEquals("Error: Records were not deleted from today issues table", 0,
        todayIssues.getCount());
    todayIssues.close();

    Cursor ownedIssues = contentResolver
        .query(IssueEntry.CONTENT_URI_OWNED_ISSUES, null, null, null, null);
    assertEquals("Error: Records were not deleted from owned issues table", 0,
        ownedIssues.getCount());
    ownedIssues.close();

    Cursor trackedVolumes = contentResolver
        .query(TrackedVolumeEntry.CONTENT_URI_TRACKED_VOLUMES, null, null, null, null);
    assertEquals("Error: Records were not deleted from owned issues table", 0,
        trackedVolumes.getCount());
    trackedVolumes.close();

  }

  @Test
  public void testTodayIssuesInsert() {

    deleteAllRecordsViaProvider();

    ContentValues testValues = ContentUtils.issueInfoToContentValues(TestUtils.getDummyIssueInfo());

    // Insert content via provider
    contentResolver.insert(IssueEntry.CONTENT_URI_TODAY_ISSUES, testValues);

    // Query content via provider
    Cursor queryCursor = contentResolver.query(
        IssueEntry.CONTENT_URI_TODAY_ISSUES,
        null,
        null,
        null,
        null);

    TestUtils.validateCursor("testTodayIssuesInsert", queryCursor, testValues);
  }

  @Test
  public void testOwnedIssuesInsert() {

    deleteAllRecordsViaProvider();

    ContentValues testValues = ContentUtils
        .issueInfoToContentValues(TestUtils.getDummyIssueInfo());

    // Insert content via provider
    contentResolver.insert(IssueEntry.CONTENT_URI_OWNED_ISSUES, testValues);

    // Query content via provider
    Cursor queryCursor = contentResolver.query(
        IssueEntry.CONTENT_URI_OWNED_ISSUES,
        null,
        null,
        null,
        null);

    TestUtils.validateCursor("testOwnedIssuesInsert", queryCursor, testValues);
  }

  @Test
  public void testTrackedVolumesInsert() {

    deleteAllRecordsViaProvider();

    ContentValues testValues = ContentUtils
        .volumeInfoToContentValues(TestUtils.getDummyVolumeInfo());

    // Insert content via provider
    contentResolver.insert(TrackedVolumeEntry.CONTENT_URI_TRACKED_VOLUMES, testValues);

    // Query content via provider
    Cursor queryCursor = contentResolver.query(
        TrackedVolumeEntry.CONTENT_URI_TRACKED_VOLUMES,
        null,
        null,
        null,
        null);

    TestUtils.validateCursor("testTrackedVolumesInsert", queryCursor, testValues);
  }

  @After
  public void cleanUp() {
    deleteAllRecordsViaProvider();
  }

  private void deleteAllRecordsViaProvider() {
    contentResolver.delete(IssueEntry.CONTENT_URI_TODAY_ISSUES, null, null);
    contentResolver.delete(IssueEntry.CONTENT_URI_OWNED_ISSUES, null, null);
    contentResolver.delete(TrackedVolumeEntry.CONTENT_URI_TRACKED_VOLUMES, null, null);
  }
}
