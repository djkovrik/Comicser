package com.sedsoftware.comicser.base;

import android.os.Bundle;
import android.view.View;
import butterknife.ButterKnife;
import com.evernote.android.state.StateSaver;
import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.hannesdorfmann.mosby3.mvp.lce.MvpLceView;
import com.hannesdorfmann.mosby3.mvp.viewstate.lce.MvpLceViewStateActivity;

public abstract class BaseLceActivity<CV extends View, M, V extends MvpLceView<M>, P extends MvpPresenter<V>>
    extends MvpLceViewStateActivity<CV, M, V, P> {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
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
