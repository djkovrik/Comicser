package com.sedsoftware.comicser.features.issueslist;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.sedsoftware.comicser.R;
import com.sedsoftware.comicser.data.model.ComicIssueInfoList;
import com.sedsoftware.comicser.features.issueslist.IssuesAdapter.IssueViewHolder;
import java.util.ArrayList;
import java.util.List;

public class IssuesAdapter extends RecyclerView.Adapter<IssueViewHolder> {

  private List<ComicIssueInfoList> issues;

  public IssuesAdapter() {
    issues = new ArrayList<>();
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
    return issues.size();
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

  class IssueViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.issue_cover)
    ImageView issueCover;
    @BindView(R.id.issue_name)
    TextView issueName;
    @BindView(R.id.issue_number)
    TextView issueNumber;
    @BindView(R.id.issue_date)
    TextView issueDate;

    public IssueViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }

    void bindTo(ComicIssueInfoList issue) {

      String cover = issue.image().medium_url();
      String name = issue.volume().name();
      int number = issue.issue_number();
      String date = issue.store_date();

      loadCover(cover);
      setIssueName(name);
      setIssueNumber(number);
      setIssueDate(date);
    }

    private void loadCover(@Nullable String url) {
      if (url != null) {
        Glide.clear(issueCover);
        Glide.with(issueCover.getContext())
            .load(url)
            .fitCenter()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(issueCover);
      }
    }

    private void setIssueName(String name) {
      if (name != null) {
        issueName.setText(name);
      }
    }

    private void setIssueNumber(int number) {
      issueNumber.setText(String.valueOf(number));
    }

    private void setIssueDate(String date) {
      if (date != null) {
        issueDate.setText(date);
      }
    }
  }
}
