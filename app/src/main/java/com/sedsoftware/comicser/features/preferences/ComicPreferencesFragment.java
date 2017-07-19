package com.sedsoftware.comicser.features.preferences;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import butterknife.ButterKnife;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import com.sedsoftware.comicser.R;
import com.sedsoftware.comicser.base.BaseFragment;
import com.sedsoftware.comicser.features.navigation.NavigationActivity;
import com.sedsoftware.comicser.features.sync.ComicSyncManager;

@FragmentWithArgs
public class ComicPreferencesFragment extends PreferenceFragmentCompat
    implements OnSharedPreferenceChangeListener {

  Toolbar toolbar;

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    ActionBar supportActionBar = ((NavigationActivity) getActivity()).getSupportActionBar();

    if (supportActionBar != null) {
      supportActionBar.setTitle(R.string.navigation_settings);
    }

    toolbar = ButterKnife.findById(getActivity(), R.id.toolbar);

    if (savedInstanceState == null) {
      hideToolbar();
      startToolbarAnimation();
    }
  }

  @Override
  public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
    addPreferencesFromResource(R.xml.comicser_preferences);

    Preference currentPreference =
        getPreferenceScreen().findPreference(getString(R.string.prefs_sync_period_key));

    setPreferenceSummary(currentPreference,
        getPreferenceScreen().getSharedPreferences().getString(currentPreference.getKey(), ""));
  }

  @Override
  public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    Preference preference = findPreference(key);

    String value = getPreferenceScreen().getSharedPreferences().getString(preference.getKey(), "");
    setPreferenceSummary(preference, value);
    int hours = Integer.parseInt(value);
    ComicSyncManager.updateSyncPeriod(getContext(), hours);
  }

  @Override
  public void onResume() {
    super.onResume();
    getPreferenceScreen().getSharedPreferences()
        .registerOnSharedPreferenceChangeListener(this);
  }

  @Override
  public void onPause() {
    super.onPause();
    getPreferenceScreen().getSharedPreferences()
        .unregisterOnSharedPreferenceChangeListener(this);
  }

  private void setPreferenceSummary(Preference preference, Object value) {

    String stringValue = value.toString();
    ListPreference listPreference = (ListPreference) preference;

    int prefIndex = listPreference.findIndexOfValue(stringValue);
    if (prefIndex >= 0) {
      preference.setSummary(listPreference.getEntries()[prefIndex]);
    }
  }

  private void hideToolbar() {
    int toolbarSize = toolbar.getHeight();
    toolbar.setTranslationY(-toolbarSize);
  }

  private void startToolbarAnimation() {
    toolbar.animate()
        .translationY(0)
        .setDuration(BaseFragment.TOOLBAR_ANIMATION_DURATION)
        .setStartDelay(BaseFragment.TOOLBAR_ANIMATION_DELAY);
  }
}
