package com.sedsoftware.comicser.features.navigation;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;
import butterknife.BindView;
import com.evernote.android.state.State;
import com.sedsoftware.comicser.R;
import com.sedsoftware.comicser.base.BaseLceFragment;
import com.sedsoftware.comicser.base.BaseMvpActivity;
import com.sedsoftware.comicser.features.navigation.factory.AppNavigation;
import com.sedsoftware.comicser.features.navigation.factory.NavigationFragmentsFactory;
import com.sedsoftware.comicser.utils.FragmentUtils;
import timber.log.Timber;

public class NavigationActivity extends
    BaseMvpActivity<NavigationActivityView, NavigationActivityPresenter>
    implements NavigationActivityView, OnNavigationItemSelectedListener {

  @BindView(R.id.toolbar)
  Toolbar toolbar;
  @BindView(R.id.nav_view)
  NavigationView navigationView;
  @BindView(R.id.drawer_layout)
  DrawerLayout drawer;

  @State
  @AppNavigation.Section int currentSection;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_navigation_view);

    setSupportActionBar(toolbar);
    setUpNavigationDrawerParams();

    Timber.tag("Navigation").d("onCreate from NavigationActivity [" + currentSection + "]");
    navigateToCurrentSection();
  }

  private void setUpNavigationDrawerParams() {
    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
        this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
    drawer.addDrawerListener(toggle);
    toggle.syncState();

    navigationView.setNavigationItemSelectedListener(this);
    navigationView.setCheckedItem(R.id.nav_issues);
  }

  @Override
  public boolean onNavigationItemSelected(@NonNull MenuItem item) {
    int id = item.getItemId();
    handleChosenNavigationMenuItem(id);

    drawer.closeDrawer(GravityCompat.START);
    return true;
  }

  @NonNull
  @Override
  public NavigationActivityPresenter createPresenter() {
    return new NavigationActivityPresenter();
  }

  @SuppressWarnings("StatementWithEmptyBody")
  @Override
  public void handleChosenNavigationMenuItem(int chosenMenuItem) {

    if (chosenMenuItem == R.id.nav_issues) {
      currentSection = AppNavigation.ISSUES;
      Timber.tag("Navigation").d("currentSection set to " + AppNavigation.ISSUES);
    } else if (chosenMenuItem == R.id.nav_volumes) {
      currentSection = AppNavigation.VOLUMES;
      Timber.tag("Navigation").d("currentSection set to " + AppNavigation.VOLUMES);
    } else if (chosenMenuItem == R.id.nav_characters) {
      Toast.makeText(this, "Characters", Toast.LENGTH_SHORT).show();
    } else if (chosenMenuItem == R.id.nav_collection) {
      Toast.makeText(this, "Collection", Toast.LENGTH_SHORT).show();
    } else if (chosenMenuItem == R.id.nav_tracker) {
      Toast.makeText(this, "Release tracker", Toast.LENGTH_SHORT).show();
    } else if (chosenMenuItem == R.id.nav_settings) {
      Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
    }

    navigateToCurrentSection();
  }

  @Override
  public void navigateToCurrentSection() {

    Timber.tag("Navigation").d("Navigating to current section [" + currentSection + "]");

    BaseLceFragment fragment = NavigationFragmentsFactory.getFragment(currentSection);
    FragmentManager manager = getSupportFragmentManager();
    FragmentUtils.replaceFragmentIn(manager, fragment, R.id.content_frame);
  }
}
