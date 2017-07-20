package com.sedsoftware.comicser.features.characterslist;

import com.hannesdorfmann.mosby3.mvp.lce.MvpLceView;
import com.sedsoftware.comicser.data.model.ComicCharacterInfoList;
import java.util.List;

interface CharactersView extends MvpLceView<List<ComicCharacterInfoList>> {

//  Already defined by Mosby:
//  void showLoading(boolean pullToRefresh);
//  void showContent();
//  void showError(Throwable e, boolean pullToRefresh);
//  void setData(List<ComicIssueInfoList> data);
//  void loadData(boolean pullToRefresh);

  void loadDataByName(String name);

  void showInitialView(boolean show);

  void showEmptyView(boolean show);

  void setTitle(String title);
}
