package com.sedsoftware.comicser.utils;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import com.sedsoftware.comicser.base.BaseLceFragment;

public class FragmentUtils {

  public static void addFragmentTo(@NonNull FragmentManager fragmentManager,
      @NonNull BaseLceFragment fragment, int frameId) {
    FragmentTransaction transaction = fragmentManager.beginTransaction();
    transaction.add(frameId, fragment);
    transaction.commit();
  }

  public static void replaceFragmentIn(@NonNull FragmentManager fragmentManager,
      @NonNull Fragment fragment, int frameId, String tag) {
    FragmentTransaction transaction = fragmentManager.beginTransaction();
    transaction.replace(frameId, fragment, tag);
    transaction.commit();
  }
}
