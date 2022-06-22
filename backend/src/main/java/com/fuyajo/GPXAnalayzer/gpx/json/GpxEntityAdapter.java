package com.fuyajo.GPXAnalayzer.gpx.json;

import com.fuyajo.GPXAnalayzer.gpx.GpxEntity;
import com.fuyajo.GPXAnalayzer.gpx.GpxInfo;
import com.fuyajo.GPXAnalayzer.gpx.WayPointEntity;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.jenetics.jpx.GPX;
import io.jenetics.jpx.Metadata;
import io.jenetics.jpx.Route;
import io.jenetics.jpx.Track;
import io.jenetics.jpx.TrackSegment;
import io.jenetics.jpx.GPX.Version;

public class GpxEntityAdapter extends TypeAdapter<GpxEntity>{

  @Override
  public void write(final JsonWriter out, final GpxEntity gpxEntity) throws IOException {
    Gson gpxGson = GpxGsonBuilder.getNewBuilder().setPrettyPrinting().create();
    out.beginObject();
    out.name("gpxInfo").jsonValue(gpxGson.toJson(gpxEntity.getGpxInfo()));
    out.name("wayPoints").jsonValue(gpxGson.toJson(gpxEntity.getWayPoints()));
    out.name("routePoints").jsonValue(gpxGson.toJson(gpxEntity.getRoutePoints()));
    out.name("trackPoints").jsonValue(gpxGson.toJson(gpxEntity.getTrackPoints()));
    out.name("filepath").value(gpxEntity.getFilepath());
    out.endObject();
  }
  
  @Override
  public GpxEntity read(JsonReader in) throws IOException {
    Gson gpxGson = GpxGsonBuilder.getNewBuilder().setPrettyPrinting().create();
    UUID uuid = UUID.randomUUID();
    Version version = Version.of("1.1");
    String creator = "";
    String name = "";
    String filepath = null;
    List<WayPointEntity> wayPoints = new ArrayList<>();
    List<WayPointEntity> routePoints = new ArrayList<>();
    List<WayPointEntity> trackPoints = new ArrayList<>();

    in.beginObject();
    while (in.hasNext()) {
      String nextName = in.nextName();
      if (nextName.equals("gpxInfo")) {
        GpxInfo gpxInfo = gpxGson.fromJson(in, GpxInfo.class);
        uuid = UUID.fromString(gpxInfo.getUuid());
        version = Version.of(gpxInfo.getVersion());
        creator = gpxInfo.getCreator();
        name = gpxInfo.getName();
      } else if (nextName.equals("wayPoints")) {
        in.beginArray();
        while (in.hasNext()) {
          wayPoints.add(gpxGson.fromJson(in, WayPointEntity.class));
        }
        in.endArray();
      } else if (nextName.equals("routePoints")) {
        in.beginArray();
        while (in.hasNext()) {
          routePoints.add(gpxGson.fromJson(in, WayPointEntity.class));
        }
        in.endArray();
      } else if (nextName.equals("trackPoints")) {
        in.beginArray();
        while (in.hasNext()) {
          trackPoints.add(gpxGson.fromJson(in, WayPointEntity.class));
        }
        in.endArray();
      } else if (nextName.equals("filepath")) {
        filepath = in.nextString();
      } else {
        in.skipValue();
      }
    }
    in.endObject();
    
    GPX.Builder gpxBuilder = GPX.builder(version, creator);
    // add metadata
    Metadata metadata = Metadata.builder().name(name).build();
    gpxBuilder.metadata(metadata);
    // add waypoints
    for (WayPointEntity wayPoint : wayPoints) {
      gpxBuilder.addWayPoint(wayPoint.getWayPoint());
    }
    // add route points
    Route.Builder routeBuilder = Route.builder();
    for (WayPointEntity routePoint : routePoints) {
      routeBuilder.addPoint(routePoint.getWayPoint());
    }
    gpxBuilder.addRoute(routeBuilder.build());
    // add track points
    TrackSegment.Builder trackSegmentBuilder = TrackSegment.builder();
    for (WayPointEntity trackPoint : trackPoints) {
      trackSegmentBuilder.addPoint(trackPoint.getWayPoint());
    }
    Track.Builder trackBuilder = Track.builder();
    trackBuilder.addSegment(trackSegmentBuilder.build());
    gpxBuilder.addTrack(trackBuilder.build());

    // to gpx entity
    GPX gpx = gpxBuilder.build();
    GpxEntity gpxEntity = new GpxEntity(gpx, filepath, uuid);

    return gpxEntity;
  }
}