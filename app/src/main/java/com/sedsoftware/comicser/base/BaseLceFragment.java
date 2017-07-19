package com.sedsoftware.comicser.base;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.evernote.android.state.StateSaver;
import com.hannesdorfmann.fragmentargs.FragmentArgs;
import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.hannesdorfmann.mosby3.mvp.lce.MvpLceView;
import com.hannesdorfmann.mosby3.mvp.viewstate.lce.MvpLceViewStateFragment;
import com.sedsoftware.comicser.R;

public abstract class BaseLceFragment<CV extends View, M, V extends MvpLceView<M>, P extends MvpPresenter<V>>
    extends MvpLceViewStateFragment<CV, M, V, P> {

  private static final long TOOLBAR_ANIMATION_DURATION = 200;
  private static final long TOOLBAR_ANIMATION_DELAY = 200;

  private Toolbar toolbar;
  private Unbinder unbinder;

  @LayoutRes
  protected abstract int getLayoutRes();

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    FragmentArgs.inject(this);
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    StateSaver.restoreInstanceState(this, savedInstanceState);
    return inflater.inflate(getLayoutRes(), container, false);
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    StateSaver.saveInstanceState(this, outState);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    injectDependencies();
    super.onViewCreated(view, savedInstanceState);
    unbinder = ButterKnife.bind(this, view);
    toolbar = ButterKnife.findById(getActivity(), R.id.toolbar);
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
  }

  protected void hideToolbar() {
    int toolbarSize = toolbar.getHeight();
    toolbar.setTranslationY(-toolbarSize);
  }

  protected void startToolbarAnimation() {
    toolbar.animate()
        .translationY(0)
        .setDuration(TOOLBAR_ANIMATION_DURATION)
        .setStartDelay(TOOLBAR_ANIMATION_DELAY)
        .setListener(new AnimatorListenerAdapter() {
          @Override
          public void onAnimationEnd(Animator animation) {
            onToolbarAnimationEnd();
          }
        });
  }

  protected void onToolbarAnimationEnd() {

  }

  protected void injectDependencies() {

  }
}
