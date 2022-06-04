package com.fuyajo.GPXAnalayzer;

import java.io.IOException;

import com.fuyajo.GPXAnalayzer.database.GPXPoint;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public final class TypeAdapters {
  
  public static final TypeAdapter<GPXPoint> GPXPOINT_TYPEADAPTER = new GPXPointTypeAdapter();

  private static final GsonBuilder gsonBuilder = new GsonBuilder();

  static public class builder{
    private static TypeAdapters typeAdapters = new TypeAdapters();

    public static Gson addTypeAdapter(TypeAdapter typeAdapter, boolean isPrettyPrinting) {
			gsonBuilder.registerTypeAdapter(GPXPoint.class, typeAdapter);
      if(isPrettyPrinting) gsonBuilder.setPrettyPrinting();
      return gsonBuilder.create();
    }
  }

  private static class GPXPointTypeAdapter extends TypeAdapter<GPXPoint>{
    @Override
    public void write(final JsonWriter out, final GPXPoint value) throws IOException {
      out.beginObject();
      out.name("UUID").value(value.getId().toString());
      out.name("Name").value(value.getName());
      out.name("Point").beginObject();
      out.name("Latitude").value(value.getPoint().getLatitude());
      out.name("Longitude").value(value.getPoint().getLongitude());
      out.name("Time").value(value.getPoint().getTime().orElse(null).toString());
      out.endObject();
      out.endObject();
    }
    
    @Override
    public GPXPoint read(JsonReader in) throws IOException {
      return null;
    }
  }
}
