package com.sedsoftware.comicser.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import butterknife.ButterKnife;
import com.evernote.android.state.StateSaver;
import com.hannesdorfmann.mosby3.mvp.MvpActivity;
import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;

@SuppressWarnings({"WeakerAccess", "EmptyMethod"})
public abstract class BaseMvpActivity<V extends MvpView, P extends MvpPresenter<V>> extends MvpActivity<V, P> {
  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    injectDependencies();
    super.onCreate(savedInstanceState);
    StateSaver.restoreInstanceState(this, savedInstanceState);
  }

  @Override
  public void onContentChanged() {
    super.onContentChanged();
    ButterKnife.bind(this);
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    StateSaver.saveInstanceState(this, outState);
  }

  protected void injectDependencies() {

  }
}
