package com.sedsoftware.comicser.data.source.remote.dagger.modules;

import com.sedsoftware.comicser.data.source.remote.ComicRemoteDataHelper;
import com.sedsoftware.comicser.data.source.remote.ComicVineService;
import com.sedsoftware.comicser.data.source.remote.RemoteDataScope;
import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module(includes = {GsonModule.class, OkHttpModule.class, RetrofitModule.class})
public class ComicRemoteDataModule {

  @Provides
  @RemoteDataScope
  ComicVineService provideComicVineService(Retrofit retrofit) {
    return retrofit.create(ComicVineService.class);
  }

  @Provides
  @RemoteDataScope
  ComicRemoteDataHelper provideComicRemoteDataHelper(ComicVineService service) {
    return new ComicRemoteDataHelper(service);
  }
}
