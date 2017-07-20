package com.sedsoftware.comicser.features.volumedetails;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.sedsoftware.comicser.R;
import com.sedsoftware.comicser.data.model.ComicIssueInfoShort;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@SuppressWarnings("WeakerAccess")
class VolumeDetailsIssueAdapter extends
    RecyclerView.Adapter<VolumeDetailsIssueAdapter.IssueViewHolder> {

  private List<ComicIssueInfoShort> issues;
  final IssuesAdapterCallbacks listener;

  VolumeDetailsIssueAdapter(IssuesAdapterCallbacks listener) {
    issues = new ArrayList<>();
    this.listener = listener;
  }

  @Override
  public IssueViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.fragment_volume_details_issue_item, parent, false);

    return new IssueViewHolder(view);
  }

  @Override
  public void onBindViewHolder(IssueViewHolder holder, int position) {
    holder.bindTo(issues.get(position));
  }

  @Override
  public int getItemCount() {
    return issues == null ? 0 : issues.size();
  }

  @Override
  public long getItemId(int position) {
    return issues.get(position).id();
  }

  public List<ComicIssueInfoShort> getIssues() {
    return issues;
  }

  public void setIssues(List<ComicIssueInfoShort> issues) {
    this.issues = issues;
  }

  class IssueViewHolder extends RecyclerView.ViewHolder {

    private long currentIssueId;

    @BindView(R.id.issue_number)
    TextView issueNumber;
    @BindString(R.string.volume_details_issue_number)
    String issueNumberFormat;
    @BindView(R.id.issue_name)
    TextView issueName;
    @BindView(R.id.issue_bookmarked_icon)
    ImageView bookmarkIcon;


    IssueViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);

      itemView.setOnClickListener(v -> listener.issueClicked(currentIssueId));
    }

    void bindTo(ComicIssueInfoShort issue) {

      currentIssueId = issue.id();
      int number = issue.issue_number();
      issueNumber.setText(String.format(Locale.US, issueNumberFormat, number));

      String issueNameText = issue.name();

      if (issueNameText != null) {
        issueName.setText(issueNameText);
      } else {
        issueName.setVisibility(View.GONE);
      }

      if (listener.isIssueTracked(currentIssueId)) {
        bookmarkIcon.setImageResource(R.drawable.ic_bookmark_black_24dp);
      } else {
        bookmarkIcon.setImageResource(R.drawable.ic_bookmark_border_black_24dp);
      }
    }
  }

  interface IssuesAdapterCallbacks {

    void issueClicked(long issueId);

    boolean isIssueTracked(long issueId);
  }
}