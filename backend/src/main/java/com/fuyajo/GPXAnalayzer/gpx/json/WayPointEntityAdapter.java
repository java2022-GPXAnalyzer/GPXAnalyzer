package com.fuyajo.GPXAnalayzer.gpx.json;

import com.fuyajo.GPXAnalayzer.gpx.WayPointEntity;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.NoSuchElementException;

import io.jenetics.jpx.WayPoint;

public class WayPointEntityAdapter extends TypeAdapter<WayPointEntity>{
  @Override
  public void write(final JsonWriter out, final WayPointEntity wayPointEntity) throws IOException {
    WayPoint wayPoint = wayPointEntity.getWayPoint();

    out.beginObject();
    out.name("uuid").value(wayPointEntity.getUuid().toString());
    out.name("lat").value(wayPoint.getLatitude());
    out.name("lon").value(wayPoint.getLongitude());
    out.name("ele").value(wayPointEntity.getElevation());
    out.name("time").value(wayPointEntity.getTime().toString());
    out.name("name").value(wayPointEntity.getName());
    out.endObject();
  }
  
  @Override
  public WayPointEntity read(JsonReader in) throws IOException {
    return null;
  }
}