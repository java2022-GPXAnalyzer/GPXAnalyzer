package com.fuyajo.GPXAnalayzer;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.Instant;

import com.fuyajo.GPXAnalayzer.gpx.WayPointEntity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import io.jenetics.jpx.WayPoint;

public final class TypeAdapters {

  public enum enumTypeAdapter{
    GPXPOINT, POINT, INSTANT_TIME
  }

  public static TypeAdapter getTypeAdapter(enumTypeAdapter type){
    switch(type){
      case GPXPOINT:
        return new GPXPointTypeAdapter();
      case POINT:
        return new PointTypeAdapter();
      case INSTANT_TIME:
        return new InstantTimeTypeAdapter();
      default:
        return null;
    }
  }

  private static Type getTypeClass(enumTypeAdapter type){
    switch(type){
      case GPXPOINT:
        return WayPointEntity.class;
      case POINT:
        return WayPoint.class;
      case INSTANT_TIME:
        return Instant.class;
      default:
        return null;
    }
  }

  private static final GsonBuilder gsonBuilder = new GsonBuilder();

  public static class Builder{

    public Builder addTypeAdapter(enumTypeAdapter typeAdapter){
      gsonBuilder.registerTypeAdapter(getTypeClass(typeAdapter), getTypeAdapter(typeAdapter));
      return this;
    }

    public Builder setPrettyPrinting(){
      gsonBuilder.setPrettyPrinting();
      return this;
    }
    
    public Gson build(){
      return gsonBuilder.create();
    }
  }

  private static class GPXPointTypeAdapter extends TypeAdapter<WayPointEntity>{
    @Override
    public void write(final JsonWriter out, final WayPointEntity value) throws IOException {
      out.beginObject();
        out.name("uuid").value(value.getUUID().toString());
        out.name("name").value(value.getName());
        out.name("point").beginObject();
          out.name("latitude").value(value.getPoint().getLatitude());
          out.name("longitude").value(value.getPoint().getLongitude());
          out.name("time").value(value.getPoint().getTime().orElse(null).toString());
        out.endObject();
      out.endObject();
    }
    
    @Override
    public WayPointEntity read(JsonReader in) throws IOException {
      return null;
    }
  }

  private static class PointTypeAdapter extends TypeAdapter<WayPoint>{
    @Override
    public void write(final JsonWriter out, final WayPoint value) throws IOException {
      out.beginObject();
      out.name("latitude").value(value.getLatitude());
      out.name("longitude").value(value.getLongitude());
      out.name("time").value(value.getTime().orElse(null).toString());
      out.endObject();
    }
    
    @Override
    public WayPoint read(JsonReader in) throws IOException {
      return null;
    }
  }

  private static class InstantTimeTypeAdapter extends TypeAdapter<Instant>{
    @Override
    public void write(final JsonWriter out, final Instant value) throws IOException {
      out.value(value.toString());
    }
    
    @Override
    public Instant read(JsonReader in) throws IOException {
      return null;
    }
  }
}
