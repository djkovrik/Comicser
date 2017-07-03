package com.sedsoftware.comicser.data.source.remote;

import com.sedsoftware.comicser.data.model.ComicCharacterInfo;
import com.sedsoftware.comicser.data.model.ComicCharacterInfoList;
import com.sedsoftware.comicser.data.model.ComicIssueInfo;
import com.sedsoftware.comicser.data.model.ComicIssueInfoList;
import com.sedsoftware.comicser.data.model.ComicVolumeInfo;
import com.sedsoftware.comicser.data.model.ComicVolumeInfoList;
import com.sedsoftware.comicser.data.model.ServerResponse;
import io.reactivex.Observable;
import java.util.List;
import java.util.Map;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface ComicVineService {

  String ENDPOINT = "https://comicvine.gamespot.com/";
  String ISSUE_TYPE_CODE = "4000";
  String VOLUME_TYPE_CODE = "4050";
  String CHARACTER_TYPE_CODE = "4005";

  // Request issues list
  @GET("/api/issues/")
  Observable<ServerResponse<List<ComicIssueInfoList>>> getIssuesList(
      @QueryMap Map<String, String> options);

  // Request issue details
  @GET("/api/issue/" + ISSUE_TYPE_CODE + "-{id}/")
  Observable<ServerResponse<ComicIssueInfo>> getIssueDetails(
      @Path("id") long issueId,
      @QueryMap Map<String, String> options);

  // Request volumes list
  @GET("/api/volumes/")
  Observable<ServerResponse<List<ComicVolumeInfoList>>> getVolumesList(
      @QueryMap Map<String, String> options);

  // Request volume details
  @GET("/api/volume/" + VOLUME_TYPE_CODE + "-{id}/")
  Observable<ServerResponse<ComicVolumeInfo>> getVolumeDetails(
      @Path("id") long volumeId,
      @QueryMap Map<String, String> options);

  // Request characters list
  @GET("/api/characters/")
  Observable<ServerResponse<List<ComicCharacterInfoList>>> getCharactersList(
      @QueryMap Map<String, String> options);

  // Request character details
  @GET("/api/character/" + CHARACTER_TYPE_CODE + "-{id}/")
  Observable<ServerResponse<ComicCharacterInfo>> getCharacterDetails(
      @Path("id") long characterId,
      @QueryMap Map<String, String> options);

}
