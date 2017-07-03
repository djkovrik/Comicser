package com.sedsoftware.comicser.base;

import android.os.Bundle;
import butterknife.ButterKnife;
import com.evernote.android.state.StateSaver;
import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;
import com.hannesdorfmann.mosby3.mvp.viewstate.MvpViewStateActivity;
import com.hannesdorfmann.mosby3.mvp.viewstate.ViewState;

public abstract class BaseViewStateActivity<V extends MvpView, P extends MvpPresenter<V>, VS extends ViewState<V>>
    extends MvpViewStateActivity<V, P, VS> {

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
