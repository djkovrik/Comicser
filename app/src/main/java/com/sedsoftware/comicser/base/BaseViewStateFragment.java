package com.sedsoftware.comicser.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.evernote.android.state.StateSaver;
import com.hannesdorfmann.fragmentargs.FragmentArgs;
import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;
import com.hannesdorfmann.mosby3.mvp.viewstate.MvpViewStateFragment;
import com.hannesdorfmann.mosby3.mvp.viewstate.ViewState;

public abstract class BaseViewStateFragment<V extends MvpView, P extends MvpPresenter<V>, VS extends ViewState<V>>
    extends MvpViewStateFragment<V, P, VS> {

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
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
  }

  protected void injectDependencies() {

  }

}
