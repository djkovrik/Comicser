package com.sedsoftware.comicser;

import android.app.Application;
import android.content.Context;
import com.facebook.stetho.Stetho;
import com.squareup.leakcanary.LeakCanary;
import timber.log.Timber;

public class ComicserApp extends Application {

  private static ComicserAppComponent comicserAppComponent;

  @Override
  protected void attachBaseContext(Context base) {
    super.attachBaseContext(base);

    if (comicserAppComponent == null) {
      comicserAppComponent = DaggerComicserAppComponent.builder()
          .comicserAppModule(new ComicserAppModule(this))
          .build();
    }
  }

  @Override
  public void onCreate() {
    super.onCreate();

    if (LeakCanary.isInAnalyzerProcess(this)) {
      // This process is dedicated to LeakCanary for heap analysis.
      // We should not init our app in this process.
      return;
    }
    LeakCanary.install(this);
    // Normal app init code below.

    if (BuildConfig.DEBUG) {
      Timber.uprootAll();
      Timber.plant(new Timber.DebugTree());

      Stetho.initializeWithDefaults(this);
    }
  }

  public static ComicserAppComponent getAppComponent() {
    return comicserAppComponent;
  }
}
