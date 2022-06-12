package com.fuyajo.GPXAnalayzer.gpx;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;


import io.jenetics.jpx.GPX;
import io.jenetics.jpx.Track;
import io.jenetics.jpx.TrackSegment;
import io.jenetics.jpx.WayPoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GpxParser {
  
  private GPX gpx;
  private static final Logger logger = LoggerFactory.getLogger(GpxParser.class);

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
    logger.info("Writing gpx to file: " + filename);
    GPX.write(gpx, Path.of(filename));
  }

  public GpxDataEntity read(String filename) throws IOException{
    logger.info("Reading gpx from file: " + filename);
    gpx = GPX.read(Path.of(filename));

    var wrapper = new Object(){ GpxDataEntity data = new GpxDataEntity(filename, gpx.getCreator(), gpx.getMetadata().orElse(null)); };

    Consumer<WayPoint> analayzer = new Consumer<WayPoint>() {
      @Override
      public void accept(WayPoint t) {
        wrapper.data.add(new WayPointEntity(t.getName().orElse(null), t));
      }
    };

    gpx.tracks()
    .flatMap(Track::segments)
    .flatMap(TrackSegment::points)
    .forEach(analayzer);

    return wrapper.data;
  }

}
