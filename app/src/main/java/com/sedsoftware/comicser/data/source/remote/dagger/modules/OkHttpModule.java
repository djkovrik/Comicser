package com.sedsoftware.comicser.data.source.remote.dagger.modules;

import android.support.annotation.NonNull;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.sedsoftware.comicser.data.source.remote.RemoteDataScope;
import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

@Module
public class OkHttpModule {

  @Provides
  @NonNull
  @RemoteDataScope
  OkHttpClient provideOkHttpClient() {
    return new OkHttpClient.Builder()
        .addNetworkInterceptor(new StethoInterceptor())
        .build();
  }
}
