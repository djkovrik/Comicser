package com.sedsoftware.comicser.features.characterslist;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.sedsoftware.comicser.R;
import com.sedsoftware.comicser.data.model.ComicCharacterInfoList;
import com.sedsoftware.comicser.data.model.ComicImages;
import com.sedsoftware.comicser.data.model.ComicPublisherInfo;
import com.sedsoftware.comicser.features.characterslist.CharactersAdapter.CharacterViewHolder;
import com.sedsoftware.comicser.utils.ImageUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CharactersAdapter extends RecyclerView.Adapter<CharacterViewHolder>{

  private List<ComicCharacterInfoList> characters;
  private final OnVolumeClickListener listener;

  public CharactersAdapter(OnVolumeClickListener listener) {
    characters = new ArrayList<>(0);
    this.listener = listener;
  }

  @Override
  public CharacterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.fragment_characters_list_item, parent, false);

    return new CharacterViewHolder(view);
  }

  @Override
  public void onBindViewHolder(CharacterViewHolder holder, int position) {
    holder.bindTo(characters.get(position));
  }

  @Override
  public int getItemCount() {
    return characters == null ? 0 : characters.size();
  }

  List<ComicCharacterInfoList> getCharacters() {
    return characters;
  }

  void setCharacters(List<ComicCharacterInfoList> volumes) {
    this.characters = volumes;
  }

  class CharacterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private long currentCharacterId;

    @BindView(R.id.character_image_layout)
    FrameLayout imageLayout;
    @BindView(R.id.character_poster)
    ImageView characterPoster;
    @BindView(R.id.character_poster_progressbar)
    ProgressBar progressBar;
    @BindView(R.id.character_name)
    TextView characterName;
    @BindView(R.id.character_publisher)
    TextView characterPublisher;
    @BindString(R.string.volumes_publisher_text)
    String publisherFormat;

    CharacterViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);

      itemView.setOnClickListener(this);
    }

    void bindTo(ComicCharacterInfoList character) {

      currentCharacterId = character.id();

      ComicImages image = character.image();
      if (image != null) {
        String url = image.small_url();
        ImageUtils.loadImageWithProgress(characterPoster, url, progressBar);
      } else {
        imageLayout.setVisibility(View.GONE);
      }

      characterName.setText(character.name());

      ComicPublisherInfo publisher = character.publisher();

      if (publisher != null) {
        String publisherName = String.format(Locale.US, publisherFormat, publisher.name());
        characterPublisher.setText(publisherName);
      } else {
        characterPublisher.setVisibility(View.GONE);
      }
    }

    @Override
    public void onClick(View v) {
      listener.volumeClicked(currentCharacterId);
    }
  }

  interface OnVolumeClickListener {

    void volumeClicked(long characterId);
  }
}
