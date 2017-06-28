package com.sedsoftware.comicser.data.source.remote;

import com.sedsoftware.comicser.data.model.ComicCharacterInfo;
import com.sedsoftware.comicser.data.model.ComicCharacterInfoList;
import com.sedsoftware.comicser.data.model.ComicIssueInfo;
import com.sedsoftware.comicser.data.model.ComicIssueInfoList;
import com.sedsoftware.comicser.data.model.ComicVolumeInfo;
import com.sedsoftware.comicser.data.model.ComicVolumeInfoList;
import com.sedsoftware.comicser.data.model.ServerResponse;
import com.sedsoftware.comicser.utils.ClassUtils;
import com.sedsoftware.comicser.utils.RxUtils;
import io.reactivex.Observable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ComicVineDataSource {

  private static final String API_KEY = "API_KEY";
  ComicVineService comicVineService;

  public ComicVineDataSource() {
    comicVineService = ComicVineServiceFactory
        .createFrom(ComicVineService.class, ComicVineService.ENDPOINT);
  }

  /**
   * Request issues list (search by: current date).
   *
   * @param date Date string in YYYY-MM-DD format.
   * @return Issue info list.
   */
  public Observable<List<ComicIssueInfoList>> getIssuesListByDate(String date) {

    String fields = ClassUtils.getMethodsList(ComicIssueInfoList.class);

    Map<String, String> options = new HashMap<>();
    options.put("api_key", API_KEY);
    options.put("field_list", fields);
    options.put("filter", "store_date:" + date);
    options.put("sort", "name:asc");
    options.put("format", "json");

    return comicVineService
        .getIssuesList(options)
        .compose(RxUtils.applySchedulers())
        .map(ServerResponse::results);

  }

  /**
   * Request issues list (search by: specified date period).
   *
   * @param date1 Start date string in YYYY-MM-DD format.
   * @param date2 End date string in YYYY-MM-DD format.
   * @return Issue info list.
   */
  public Observable<List<ComicIssueInfoList>> getIssuesListByPeriod(String date1, String date2) {

    String fields = ClassUtils.getMethodsList(ComicIssueInfoList.class);

    Map<String, String> options = new HashMap<>();
    options.put("api_key", API_KEY);
    options.put("field_list", fields);
    options.put("filter", "store_date:" + date1 + "|" + date2);
    options.put("sort", "store_date:asc");
    options.put("format", "json");

    return comicVineService
        .getIssuesList(options)
        .compose(RxUtils.applySchedulers())
        .map(ServerResponse::results);

  }

  /**
   * Request issues list (search by: specified name).
   *
   * @param name Target issue name.
   * @return Issue info list.
   */
  public Observable<List<ComicIssueInfoList>> getIssuesListByName(String name) {

    String fields = ClassUtils.getMethodsList(ComicIssueInfoList.class);

    Map<String, String> options = new HashMap<>();
    options.put("api_key", API_KEY);
    options.put("filter", "name:" + name);
    options.put("field_list", fields);
    options.put("sort", "name:asc");
    options.put("format", "json");

    return comicVineService
        .getIssuesList(options)
        .compose(RxUtils.applySchedulers())
        .map(ServerResponse::results);

  }

  /**
   * Request issue details (search by: issue id).
   *
   * @param issueId Target issue id (!= issue number).
   * @return Detailed issue info.
   */
  public Observable<ComicIssueInfo> getIssueDetailsById(long issueId) {

    String fields = ClassUtils.getMethodsList(ComicIssueInfo.class);

    Map<String, String> options = new HashMap<>();
    options.put("api_key", API_KEY);
    options.put("field_list", fields);
    options.put("format", "json");

    return comicVineService
        .getIssueDetails(issueId, options)
        .compose(RxUtils.applySchedulers())
        .map(ServerResponse::results);
  }

  /**
   * Request volumes list (search by: specified name)
   *
   * @param name Target volume name.
   * @return Volume info list.
   */
  public Observable<List<ComicVolumeInfoList>> getVolumesListByName(String name) {

    String fields = ClassUtils.getMethodsList(ComicVolumeInfoList.class);

    Map<String, String> options = new HashMap<>();
    options.put("api_key", API_KEY);
    options.put("filter", "name:" + name);
    options.put("field_list", fields);
    options.put("format", "json");

    return comicVineService
        .getVolumesList(options)
        .compose(RxUtils.applySchedulers())
        .map(ServerResponse::results);
  }

  /**
   * Request volume details (search by: volume id).
   *
   * @param volumeId Target volume id.
   * @return Detailed volume info.
   */
  public Observable<ComicVolumeInfo> getVolumeDetailsById(long volumeId) {

    String fields = ClassUtils.getMethodsList(ComicVolumeInfo.class);

    Map<String, String> options = new HashMap<>();
    options.put("api_key", API_KEY);
    options.put("field_list", fields);
    options.put("format", "json");

    return comicVineService
        .getVolumeDetails(volumeId, options)
        .compose(RxUtils.applySchedulers())
        .map(ServerResponse::results);

  }

  /**
   * Request characters list (search by: specified name)
   *
   * @param name Target character name to perform search.
   * @return Characters info list.
   */
  public Observable<List<ComicCharacterInfoList>> getCharactersListByName(String name) {

    String fields = ClassUtils.getMethodsList(ComicCharacterInfoList.class);

    Map<String, String> options = new HashMap<>();
    options.put("api_key", API_KEY);
    options.put("filter", "name:" + name);
    options.put("field_list", fields);
    options.put("format", "json");

    return comicVineService
        .getCharactersList(options)
        .compose(RxUtils.applySchedulers())
        .map(ServerResponse::results);
  }

  /**
   * Request character details (search by: character id)
   *
   * @param characterId Target character ud.
   * @return Detailed character info.
   */
  public Observable<ComicCharacterInfo> getCharacterDetailsById(long characterId) {

    String fields = ClassUtils.getMethodsList(ComicCharacterInfo.class);

    Map<String, String> options = new HashMap<>();
    options.put("api_key", API_KEY);
    options.put("field_list", fields);
    options.put("format", "json");

    return comicVineService
        .getCharacterDetails(characterId, options)
        .compose(RxUtils.applySchedulers())
        .map(ServerResponse::results);
  }
}
