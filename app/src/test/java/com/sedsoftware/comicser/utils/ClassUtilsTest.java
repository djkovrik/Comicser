package com.sedsoftware.comicser.utils;


import static org.junit.Assert.assertEquals;

import com.sedsoftware.comicser.data.model.ComicVolumeInfoList;
import org.junit.Test;

public class ClassUtilsTest {

  @Test
  public void testGetFieldsReturnsCorrectString_forAutoValueClass() {

    // Arrange
    String detectedAVFields = ClassUtils.getMethodsList(ComicVolumeInfoList.class);
    String actualFields = "count_of_issues,id,image,name,publisher,start_year";

    // Assert
    assertEquals("getMethodsList method returned incorrect list!",
        actualFields,
        detectedAVFields);
  }
}