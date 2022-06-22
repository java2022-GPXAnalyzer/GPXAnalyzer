package com.fuyajo.GPXAnalayzer.gpx.json;

import com.fuyajo.GPXAnalayzer.gpx.WayPointEntity;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.time.Instant;
import java.util.UUID;

import io.jenetics.jpx.WayPoint;

public class WayPointEntityAdapter extends TypeAdapter<WayPointEntity>{
  @Override
  public void write(final JsonWriter out, final WayPointEntity wayPointEntity) throws IOException {
    out.beginObject();
    out.name("uuid").value(wayPointEntity.getUuid().toString());
    out.name("lat").value(wayPointEntity.getLatitude());
    out.name("lon").value(wayPointEntity.getLongitude());
    out.name("ele").value(wayPointEntity.getElevation());
    out.name("time").value(wayPointEntity.getTime().toString());
    out.name("name").value(wayPointEntity.getName());
    out.endObject();
  }
  
  @Override
  public WayPointEntity read(JsonReader in) throws IOException {
    UUID uuid = UUID.randomUUID();
    double lat = 0.0f;
    double lon = 0.0f;
    double ele = 0.0f;
    Instant time = Instant.ofEpochMilli(0);
    String name = "";
    in.beginObject();
    while (in.hasNext()) {
      String nextName = in.nextName();
      if (nextName.equals("uuid")) {
        uuid = UUID.fromString(in.nextString());
      } else if (nextName.equals("lat")) {
        lat = in.nextDouble();
      } else if (nextName.equals("lon")) {
        lon = in.nextDouble();
      } else if (nextName.equals("ele")) {
        ele = in.nextDouble();
      } else if (nextName.equals("time")) {
        time = Instant.parse(in.nextString());
      } else if (nextName.equals("name")) {
        name = in.nextString();
      } else {
        in.skipValue();
      }
    }
    in.endObject();
    WayPoint wayPoint = WayPoint.of(lat, lon, ele, time.toEpochMilli()).toBuilder().name(name).build();
    return new WayPointEntity(wayPoint, uuid);
  }
}