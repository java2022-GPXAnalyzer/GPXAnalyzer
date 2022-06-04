package com.fuyajo.GPXAnalayzer.parser;
import java.io.IOException;
import java.nio.file.Path;

import io.jenetics.jpx.*;

public class GpxParser {
  
  private GPX gpx;

  public GpxParser() {
    gpx = GPX.builder("fuyajo")
    .addTrack(track -> track
        .addSegment(segment -> segment
        .addPoint(p -> p.lat(48.20100).lon(16.31651).ele(283))
        .addPoint(p -> p.lat(48.20112).lon(16.31639).ele(278))
        .addPoint(p -> p.lat(48.20126).lon(16.31601).ele(274))))
    .build();
  }

  public void write(String filename) throws IOException{
    System.out.println(filename);
    GPX.write(gpx, Path.of(filename));
  }

  public void read(String filename) throws IOException{
    System.out.println(filename);
    gpx = GPX.read(Path.of(filename));
  }
}
