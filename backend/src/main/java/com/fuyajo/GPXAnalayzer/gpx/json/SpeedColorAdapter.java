package com.fuyajo.GPXAnalayzer.gpx.json;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.List;

import org.springframework.data.util.Pair;

public class SpeedColorAdapter extends TypeAdapter<List<Pair<Double, Double> > >{

  @Override
  public void write(final JsonWriter out, final List<Pair<Double, Double> > speedColorList) throws IOException {
    out.beginObject();
    out.beginArray();
    for(Pair<Double, Double> speedColor : speedColorList) {
      System.out.println(speedColor);
      if(!Double.isNaN(speedColor.getFirst())){
        out.value(speedColor.getFirst());
      } else {
        out.value("NaN");
      }
    }
    out.endArray();
    out.beginArray();
    for(Pair<Double, Double> speedColor : speedColorList) {
      if(!Double.isNaN(speedColor.getSecond())){
        out.value(speedColor.getSecond());
      } else {
        out.value("NaN");
      }
    }
    out.endArray();
    out.endObject();
  }
  
  @Override
  public List<Pair<Double, Double> > read(JsonReader in) throws IOException {
    // TODO: complete speed color list reader
    return null;
  }
}