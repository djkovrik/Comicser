package com.sedsoftware.comicser.features.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class ComicAuthenticatorService extends Service {

  private ComicAuthenticator authenticator;

  @Override
  public void onCreate() {
    authenticator = new ComicAuthenticator(this);
  }

  @Override
  public IBinder onBind(Intent intent) {
    return authenticator.getIBinder();
  }
}
