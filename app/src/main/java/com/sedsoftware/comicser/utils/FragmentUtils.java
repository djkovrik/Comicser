package com.sedsoftware.comicser.utils;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import com.sedsoftware.comicser.base.BaseLceFragment;
import timber.log.Timber;

public class FragmentUtils {

  public static void addFragmentTo(@NonNull FragmentManager fragmentManager,
      @NonNull BaseLceFragment fragment, int frameId) {
    Timber.tag("Navigation").d("addFragmentTo called with " + fragment.getClass().getSimpleName());
    FragmentTransaction transaction = fragmentManager.beginTransaction();
    transaction.add(frameId, fragment);
    transaction.commit();
  }

  public static void replaceFragmentIn(@NonNull FragmentManager fragmentManager,
      @NonNull BaseLceFragment fragment, int frameId) {
    Timber.tag("Navigation").d("replaceFragmentIn called with " + fragment.getClass().getSimpleName());
    FragmentTransaction transaction = fragmentManager.beginTransaction();
    transaction.replace(frameId, fragment);
    transaction.commit();
  }
}
