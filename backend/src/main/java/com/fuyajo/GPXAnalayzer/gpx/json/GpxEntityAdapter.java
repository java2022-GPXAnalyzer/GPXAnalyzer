package com.fuyajo.GPXAnalayzer.gpx.json;

import com.fuyajo.GPXAnalayzer.gpx.GpxEntity;
import com.fuyajo.GPXAnalayzer.gpx.GpxInfo;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.NoSuchElementException;

import io.jenetics.jpx.GPX;

public class GpxEntityAdapter extends TypeAdapter<GpxEntity>{
  @Override
  public void write(final JsonWriter out, final GpxEntity gpxEntity) throws IOException {
    // GPX gpx = gpxEntity.getGpx();

    GpxInfo gpxInfo = gpxEntity.getGpxInfo();

    out.beginObject();
    out.name("myGpxInfo");
    // ...?

    // out.name("uuid").value(gpxEntity.getUuid().toString());
    // out.name("name");
    // try {
    //   out.value(gpx.getMetadata().get().getName().get());
    // } catch (NoSuchElementException e) {
    //   out.value("");
    // }
    out.endObject();
  }
  
  @Override
  public GpxEntity read(JsonReader in) throws IOException {
    return null;
  }
}