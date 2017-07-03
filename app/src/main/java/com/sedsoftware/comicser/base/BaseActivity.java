package com.sedsoftware.comicser.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import butterknife.ButterKnife;
import com.evernote.android.state.StateSaver;

public class BaseActivity extends AppCompatActivity {

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
