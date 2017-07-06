package com.sedsoftware.comicser.features.issueslist;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.sedsoftware.comicser.R;
import com.sedsoftware.comicser.data.model.ComicIssueInfoList;
import com.sedsoftware.comicser.features.issueslist.IssuesAdapter.IssueViewHolder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

class IssuesAdapter extends RecyclerView.Adapter<IssueViewHolder> {

  private List<ComicIssueInfoList> issues;

  IssuesAdapter() {
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

  class IssueViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.issue_cover)
    ImageView issueCover;
    @BindView(R.id.issue_name)
    TextView issueName;
    @BindView(R.id.issue_cover_progressbar)
    ProgressBar progressBar;

    IssueViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }

    void bindTo(ComicIssueInfoList issue) {

      String cover = issue.image().small_url();
      String issueNameText = issue.name();
      String volumeNameText = issue.volume().name();
      int number = issue.issue_number();

      loadCover(cover);
      setIssueName(issueNameText, volumeNameText, number);
    }

    private void loadCover(@Nullable String url) {
      if (url != null) {
        Glide.clear(issueCover);
        Glide.with(issueCover.getContext())
            .load(url)
            .fitCenter()
            .crossFade()
            .listener(new RequestListener<String, GlideDrawable>() {
              @Override
              public boolean onException(Exception e, String model, Target<GlideDrawable> target,
                  boolean isFirstResource) {
                progressBar.setVisibility(View.GONE);
                return false;
              }

              @Override
              public boolean onResourceReady(GlideDrawable resource, String model,
                  Target<GlideDrawable> target,
                  boolean isFromMemoryCache, boolean isFirstResource) {
                progressBar.setVisibility(View.GONE);
                return false;
              }
            })
            .error(R.drawable.placeholder_error)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(issueCover);
      }
    }

    private void setIssueName(String issue, String volume, int number) {

      String name;

      if (issue != null) {
        name = String.format(Locale.US, "%s #%d - %s", volume, number, issue);
      } else {
        name = String.format(Locale.US, "%s #%d", volume, number);
      }

      issueName.setText(name);
    }
  }
}
