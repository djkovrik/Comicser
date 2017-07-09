package com.sedsoftware.comicser.utils;

import android.text.Html;
import android.text.Spanned;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

public class HtmlUtils {

  public static Spanned parseHtmlText(String htmlText) {

    Spanned result;

    String cleanedText = Jsoup.clean(htmlText, Whitelist.basic().addTags("p"));

    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
      result = Html.fromHtml(cleanedText, Html.FROM_HTML_MODE_LEGACY);
    } else {
      result = Html.fromHtml(cleanedText);
    }
    return result;
  }
}
