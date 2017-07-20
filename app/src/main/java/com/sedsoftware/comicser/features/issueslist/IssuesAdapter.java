package com.sedsoftware.comicser.features.issueslist;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.sedsoftware.comicser.R;
import com.sedsoftware.comicser.data.model.ComicIssueInfoList;
import com.sedsoftware.comicser.features.issueslist.IssuesAdapter.IssueViewHolder;
import com.sedsoftware.comicser.utils.ImageUtils;
import com.sedsoftware.comicser.utils.IssueTextUtils;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("WeakerAccess")
class IssuesAdapter extends RecyclerView.Adapter<IssueViewHolder> {

  private List<ComicIssueInfoList> issues;
  final OnIssueClickListener listener;

  IssuesAdapter(OnIssueClickListener listener) {
    issues = new ArrayList<>();
    this.listener = listener;
  }

  @Override
  public IssueViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.fragment_issues_list_item, parent, false);

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

  public List<ComicIssueInfoList> getIssues() {
    return issues;
  }

  public void setIssues(List<ComicIssueInfoList> issues) {
    this.issues = issues;
  }

  class IssueViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private long currentIssueId;

    @BindView(R.id.issue_cover)
    ImageView issueCover;
    @BindView(R.id.issue_name)
    TextView issueName;
    @BindView(R.id.issue_cover_progressbar)
    ProgressBar progressBar;

    IssueViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);

      itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
      listener.issueClicked(currentIssueId);
    }

    void bindTo(ComicIssueInfoList issue) {

      currentIssueId = issue.id();

      String coverUrl = issue.image().small_url();
      String issueNameText = issue.name();
      String volumeNameText = issue.volume().name();
      int number = issue.issue_number();

      String name = IssueTextUtils.getFormattedIssueName(issueNameText, volumeNameText, number);
      issueName.setText(name);

      if (coverUrl != null) {
        ImageUtils.loadImageWithProgress(issueCover, coverUrl, progressBar);
      }
    }
  }

  interface OnIssueClickListener {

    void issueClicked(long issueId);
  }
}
