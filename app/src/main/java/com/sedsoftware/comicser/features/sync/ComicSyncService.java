package com.sedsoftware.comicser.features.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

public class ComicSyncService extends Service {

  private static ComicSyncAdapter syncAdapter;
  private static final Object syncAdapterLock = new Object();

  @Override
  public void onCreate() {
    synchronized (syncAdapterLock) {
      if (syncAdapter == null) {
        syncAdapter = new ComicSyncAdapter(getApplicationContext());
      }
    }
  }

  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    return syncAdapter.getSyncAdapterBinder();
  }
}
