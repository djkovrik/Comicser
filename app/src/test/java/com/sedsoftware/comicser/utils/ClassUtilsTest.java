package com.sedsoftware.comicser.utils;


import static org.junit.Assert.assertEquals;

import com.sedsoftware.comicser.data.model.ComicCharacterInfoShort;
import org.junit.Test;

public class ClassUtilsTest {

  @Test
  public void testGetFieldsReturnsCorrectString_forAutoValueClass() {

    // Arrange
    String detectedAVFields = ClassUtils.getMethodsList(ComicCharacterInfoShort.class);
    String actualFields = "api_detail_url,id,name";

    // Assert
    assertEquals("getMethodsList method returned incorrect list!",
        actualFields,
        detectedAVFields);
  }
}