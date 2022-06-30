package com.fuyajo.GPXAnalayzer.gpx;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import io.jenetics.jpx.GPX;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootTest
class GpxEntityTest {
  private static final Logger LOGGER = LoggerFactory.getLogger(GpxEntityTest.class);

  @Test
// contextLoads
  void test() {
    GPX gpx = GPX.builder("fuyajo")
    .addTrack(track -> track
      .addSegment(segment -> segment
        .addPoint(p -> p.lat(48.20100).lon(16.31651).ele(283))
        .addPoint(p -> p.lat(48.20112).lon(16.31639).ele(278))
        .addPoint(p -> p.lat(48.20126).lon(16.31601).ele(274))))
    .build();

    try {
      GpxEntity gpxEntity = new GpxEntity(gpx);
      LOGGER.info("gpxEntity toJson(): " + gpxEntity.toJson());
    } catch (Exception e) {
      Assertions.fail("test() error: " + e);
    }
  }
}
