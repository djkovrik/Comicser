package com.sedsoftware.comicser.features.characterslist;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import butterknife.BindInt;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.evernote.android.state.State;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import com.hannesdorfmann.mosby3.mvp.viewstate.lce.LceViewState;
import com.hannesdorfmann.mosby3.mvp.viewstate.lce.data.RetainingLceViewState;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.sedsoftware.comicser.ComicserApp;
import com.sedsoftware.comicser.R;
import com.sedsoftware.comicser.base.BaseLceFragment;
import com.sedsoftware.comicser.data.model.ComicCharacterInfoList;
import com.sedsoftware.comicser.data.source.local.dagger.modules.ComicLocalDataModule;
import com.sedsoftware.comicser.data.source.remote.dagger.modules.ComicRemoteDataModule;
import com.sedsoftware.comicser.features.characterdetails.CharacterDetailsActivity;
import com.sedsoftware.comicser.features.navigation.NavigationActivity;
import com.sedsoftware.comicser.utils.ViewUtils;
import java.util.List;

@FragmentWithArgs
public class CharactersFragment extends
    BaseLceFragment<RecyclerView, List<ComicCharacterInfoList>, CharactersView, CharactersPresenter>
    implements CharactersView {

  @BindInt(R.integer.grid_columns_count)
  int gridColumnsCount;
  @BindString(R.string.characters_fragment_title)
  String fragmentTitle;
  @BindString(R.string.msg_no_characters_found)
  String emptyViewText;
  @BindString(R.string.msg_characters_start)
  String initialViewText;

  @BindView(R.id.initialView)
  TextView initialView;
  @BindView(R.id.emptyView)
  TextView emptyView;

  @State
  String chosenName;

  @State
  String title;

  CharactersComponent charactersComponent;
  CharactersAdapter adapter;

  // --- FRAGMENTS LIFECYCLE ---

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    adapter = new CharactersAdapter(characterId ->
        startActivity(CharacterDetailsActivity.prepareIntent(getContext(), characterId)));

    adapter.setHasStableIds(true);

    StaggeredGridLayoutManager manager =
        new StaggeredGridLayoutManager(gridColumnsCount, StaggeredGridLayoutManager.VERTICAL);

    contentView.setLayoutManager(manager);
    contentView.setHasFixedSize(true);
    contentView.setAdapter(adapter);

    setHasOptionsMenu(true);

    if (chosenName != null && chosenName.length() > 0) {
      loadDataByName(chosenName);
      setTitle(chosenName);
    } else {
      setTitle(fragmentTitle);
      showInitialView(true);
    }
  }

  // --- OPTIONS MENU ---

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    inflater.inflate(R.menu.fragment_volume_list, menu);

    ViewUtils.tintMenuIcon(getContext(), menu, R.id.action_search, R.color.material_color_white);

    setUpSearchItem(menu);

    super.onCreateOptionsMenu(menu, inflater);
  }

  // --- BASE LCE FRAGMENT ---

  @Override
  protected int getLayoutRes() {
    return R.layout.fragment_characters;
  }

  @Override
  protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
    return e.getMessage();
  }

  @NonNull
  @Override
  public CharactersPresenter createPresenter() {
    return charactersComponent.presenter();
  }

  @Override
  protected void injectDependencies() {

    charactersComponent = ComicserApp.getAppComponent()
        .plusRemoteComponent(new ComicRemoteDataModule())
        .plusLocalComponent(new ComicLocalDataModule())
        .plusCharactersComponent();
    charactersComponent.inject(this);
  }

  // --- MVP VIEW STATE ---

  @Override
  public List<ComicCharacterInfoList> getData() {
    return adapter == null ? null : adapter.getCharacters();
  }

  @NonNull
  @Override
  public LceViewState<List<ComicCharacterInfoList>, CharactersView> createViewState() {
    return new RetainingLceViewState<>();
  }

  // --- MVP VIEW ---

  @Override
  public void setData(List<ComicCharacterInfoList> data) {
    adapter.setCharacters(data);
    adapter.notifyDataSetChanged();
  }

  @Override
  public void loadData(boolean pullToRefresh) {
    showLoading(false);
    showInitialView(true);
  }

  @Override
  public void loadDataByName(String name) {
    presenter.loadCharactersData(name);
  }

  private void setUpSearchItem(Menu menu) {
    // Find items
    MaterialSearchView searchView = ButterKnife.findById(getActivity(), R.id.search_view);
    MenuItem menuItem = menu.findItem(R.id.action_search);

    // Tweaks
    searchView.setVoiceSearch(false);
    searchView.setMenuItem(menuItem);

    // Listeners
    searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
      @Override
      public boolean onQueryTextSubmit(String query) {
        chosenName = query;

        if (chosenName.length() > 0) {
          loadDataByName(chosenName);
          setTitle(chosenName);
        }
        return false;
      }

      @Override
      public boolean onQueryTextChange(String newText) {
        //Do some magic
        return false;
      }
    });
  }

  @Override
  public void showContent() {
    showInitialView(false);
    showEmptyView(false);
    super.showContent();
  }

  @Override
  public void showLoading(boolean pullToRefresh) {
    if (pullToRefresh) {
      errorView.setVisibility(View.GONE);
      contentView.setVisibility(View.GONE);
      loadingView.setVisibility(View.VISIBLE);
    } else {
      loadingView.setVisibility(View.GONE);
    }
  }

  @Override
  public void showInitialView(boolean show) {
    if (show) {
      initialView.setText(initialViewText);
      initialView.setVisibility(View.VISIBLE);
      contentView.setVisibility(View.GONE);
      errorView.setVisibility(View.GONE);
    } else {
      initialView.setVisibility(View.GONE);
    }
  }

  @Override
  public void showEmptyView(boolean show) {
    if (show) {
      emptyView.setText(emptyViewText);
      emptyView.setVisibility(View.VISIBLE);
      contentView.setVisibility(View.GONE);
      errorView.setVisibility(View.GONE);
    } else {
      emptyView.setVisibility(View.GONE);
    }
  }

  @Override
  public void setTitle(String title) {
    this.title = title;
    updateTitle();
  }

  private void updateTitle() {
    ActionBar supportActionBar = ((NavigationActivity) getActivity()).getSupportActionBar();

    if (supportActionBar != null) {
      supportActionBar.setTitle(title);
    }
  }

}
