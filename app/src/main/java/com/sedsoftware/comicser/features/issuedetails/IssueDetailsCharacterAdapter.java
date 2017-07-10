package com.sedsoftware.comicser.features.issuedetails;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.sedsoftware.comicser.R;
import com.sedsoftware.comicser.data.model.ComicCharacterInfoShort;
import java.util.List;

class IssueDetailsCharacterAdapter extends BaseAdapter {

  private List<ComicCharacterInfoShort> characters;

  IssueDetailsCharacterAdapter(List<ComicCharacterInfoShort> characters) {
    this.characters = characters;
  }

  public void replaceCharacters(List<ComicCharacterInfoShort> characters) {
    this.characters = characters;
    notifyDataSetChanged();
  }

  @Override

  public View getView(int position, View convertView, ViewGroup parent) {

    CharacterViewHolder holder;

    if (convertView != null) {
      holder = (CharacterViewHolder) convertView.getTag();
    } else {
      convertView = LayoutInflater.from(parent.getContext())
          .inflate(R.layout.fragment_issue_details_character_item, parent, false);
      holder = new CharacterViewHolder(convertView);
      convertView.setTag(holder);
    }

    holder.bindTo(characters.get(position));

    return convertView;
  }

  @Override
  public int getCount() {
    return characters.size();
  }

  @Override
  public ComicCharacterInfoShort getItem(int position) {
    return characters.get(position);
  }

  @Override
  public long getItemId(int position) {
    return characters.get(position).id();
  }


  class CharacterViewHolder {

    @BindView(R.id.issue_details_character_name)
    TextView characterName;

    long characterId;

    CharacterViewHolder(View view) {
      ButterKnife.bind(this, view);
    }

    void bindTo(ComicCharacterInfoShort character) {
      String name = character.name();
      characterId = character.id();

      if (name != null) {
        characterName.setText(name);
      }
    }
  }
}
