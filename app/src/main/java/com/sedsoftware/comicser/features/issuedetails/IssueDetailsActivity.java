package com.sedsoftware.comicser.features.issuedetails;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import com.evernote.android.state.State;
import com.sedsoftware.comicser.R;
import com.sedsoftware.comicser.base.BaseActivity;
import com.sedsoftware.comicser.utils.FragmentUtils;

public class IssueDetailsActivity extends BaseActivity {

  public static String EXTRA_ISSUE_ID_ARG = "current_issue_id";

  @State
  long chosenIssueId;

  public static Intent prepareIntent(Context context, long issueId) {
    Intent intent = new Intent(context, IssueDetailsActivity.class);
    intent.putExtra(EXTRA_ISSUE_ID_ARG, issueId);
    return intent;
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_issue_details);

    Bundle extras = getIntent().getExtras();
    chosenIssueId = getIdFromExtras(extras);

    IssueDetailsFragment fragment =
        (IssueDetailsFragment) getSupportFragmentManager()
            .findFragmentById(R.id.issue_details_container);

    if (fragment == null) {
      fragment = new IssueDetailsFragmentBuilder(chosenIssueId).build();
      FragmentUtils.addFragmentTo(getSupportFragmentManager(), fragment,
          R.id.issue_details_container);
    }
  }

  private long getIdFromExtras(Bundle extras) {
    if (extras != null && extras.containsKey(EXTRA_ISSUE_ID_ARG)) {
      return extras.getLong(EXTRA_ISSUE_ID_ARG);
    }
    return 1L;
  }
}
