package com.fuyajo.GPXAnalayzer.gpx.json;

import com.fuyajo.GPXAnalayzer.gpx.WayPointEntity;
import com.google.gson.GsonBuilder;

public class GpxGsonBuilder {
  
  // TODO: decide if we need a instance of GsonBuilder
  // public static GpxGsonBuilder INSTANCE = new GpxGsonBuilder();

  private GpxGsonBuilder() {}

  public static GsonBuilder getNewBuilder() {
    GsonBuilder gsonBuilder = new GsonBuilder();
    gsonBuilder
      .registerTypeAdapter(WayPointEntity.class, new WayPointEntityAdapter())
      .excludeFieldsWithoutExposeAnnotation();
    return gsonBuilder;
  }
}
