package com.fuyajo.GPXAnalayzer.gpx;
import java.io.IOException;
import java.nio.file.Path;

import io.jenetics.jpx.GPX;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GpxFileHandler {
  
  private static final Logger LOGGER = LoggerFactory.getLogger(GpxFileHandler.class);
  public static final GpxFileHandler DEFAULT = new GpxFileHandler();
  private GPX gpx;

  public GpxFileHandler() {
    gpx = GPX.builder("fuyajo").addTrack(track -> track
      .addSegment(segment -> segment
      .addPoint(p -> p.lat(48.20100).lon(16.31651).ele(283))
      .addPoint(p -> p.lat(48.20112).lon(16.31639).ele(278))
      .addPoint(p -> p.lat(48.20126).lon(16.31601).ele(274))))
    .build();
  }

  public GpxFileHandler(GPX gpx) {
    this.gpx = gpx;
  }

  public void write(String filepath) throws IOException{
    LOGGER.info("Writing gpx to file: " + filepath);
    GPX.write(gpx, Path.of(filepath));
  }

  public GPX read(String filepath) throws IOException{
    LOGGER.info("Reading gpx from file: " + filepath);
    gpx = GPX.read(Path.of(filepath));
    return gpx;
  }
  
}
