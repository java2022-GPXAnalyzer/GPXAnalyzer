package com.fuyajo.GPXAnalayzer.gpx.speedColor;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.util.Pair;

import com.fuyajo.GPXAnalayzer.gpx.GpxEntity;
import com.fuyajo.GPXAnalayzer.gpx.WayPointEntity;

import io.jenetics.jpx.GPX;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootTest
class SpeedColorHandlerTest {

  private static final Logger LOGGER = LoggerFactory.getLogger(SpeedColorHandlerTest.class);

  // TODO: test generateSpeedColor() with mock data
  // ./mvnw -Dtest=SpeedColorHandlerTest#generateSpeedColor test
  @Test
  void generateSpeedColor() {
    GPX gpx = GPX.builder("fuyajo")
    .addTrack(track -> track
      .addSegment(segment -> segment
        .addPoint(p -> p.lat(48.20100).lon(16.31651).ele(283))
        .addPoint(p -> p.lat(48.20112).lon(16.31639).ele(278))
        .addPoint(p -> p.lat(48.20126).lon(16.31601).ele(274))))
    .build();
    try {
      GpxEntity gpxEntity1 = new GpxEntity(gpx);
      List<WayPointEntity> trackPoint = gpxEntity1.getTrackPoints();

      SpeedColorHandler speedColorHandler = new SpeedColorHandler();
      List<Pair<Double, Double> > speedColorList = speedColorHandler.generateSpeedColorList(trackPoint);
      LOGGER.info("speedColorList: " + speedColorList);
    } catch (Exception e) {
      Assertions.fail(e.getMessage());
    }
  }
}