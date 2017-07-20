package com.sedsoftware.comicser.utils;

import android.text.Html;
import android.text.Spanned;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

public class HtmlUtils {

  @SuppressWarnings("deprecation")
  public static Spanned parseHtmlText(String htmlText) {

    Spanned result;

    // Remove cover creators table because formatting is not supported by Spanned
    String textWithoutTable = htmlText.replaceAll("<h4.*?/table>", "");
    // Remove other tags
    String cleanedText = Jsoup.clean(textWithoutTable, Whitelist.basic());

    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
      result = Html.fromHtml(cleanedText, Html.FROM_HTML_MODE_LEGACY);
    } else {
      result = Html.fromHtml(cleanedText);
    }
    return result;
  }
}
