package com.fuyajo.GPXAnalayzer.gpx.json;

import java.time.Instant;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.boot.test.context.SpringBootTest;

import com.fuyajo.GPXAnalayzer.gpx.GpxEntity;
import com.fuyajo.GPXAnalayzer.gpx.WayPointEntity;

import io.jenetics.jpx.GPX;
import io.jenetics.jpx.WayPoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootTest
class GpxGsonBuilderTest {
  private static final Logger LOGGER = LoggerFactory.getLogger(GpxGsonBuilderTest.class);

  @Test
  void TestWayPointToJson() {
    WayPoint.Builder wayPointBuilder = WayPoint.builder();
    WayPoint wayPoint = wayPointBuilder
      .lat(1.0)
      .lon(2.0)
      .ele(3.0)
      .time(Instant.ofEpochMilli(1655794784631L))
      .name("a point")
      .build();

    WayPointEntity wp1 = new WayPointEntity(wayPoint, UUID.randomUUID());
    String wpJson1 = GpxGsonBuilder.getNewBuilder().setPrettyPrinting().create().toJson(wp1);
    LOGGER.info(wpJson1);
    WayPointEntity wp2 = GpxGsonBuilder.getNewBuilder().create().fromJson(wpJson1, WayPointEntity.class);
    String wpJson2 = GpxGsonBuilder.getNewBuilder().setPrettyPrinting().create().toJson(wp2);
    LOGGER.info(wpJson2);
    LOGGER.info(Long.toString(wp1.getTime().toEpochMilli()));
    LOGGER.info(Long.toString(wp2.getTime().toEpochMilli()));
    Assertions.assertEquals(wpJson1, wpJson2);
  }

  @Test
  void TestGpxToJson() {
    GPX gpx = GPX.builder("fuyajo")
    .addTrack(track -> track
      .addSegment(segment -> segment
        .addPoint(p -> p.lat(48.20100).lon(16.31651).ele(283))
        .addPoint(p -> p.lat(48.20112).lon(16.31639).ele(278))
        .addPoint(p -> p.lat(48.20126).lon(16.31601).ele(274))))
    .build();
    GpxEntity gpxEntity1;

    try {
      gpxEntity1 = new GpxEntity(gpx);
      String gpxEntityJson1 = GpxGsonBuilder.getNewBuilder().setPrettyPrinting().create().toJson(gpxEntity1);
      LOGGER.info("gpxEntity1 toJson(): " + gpxEntityJson1);
      GpxEntity gpxEntity2 = GpxGsonBuilder.getNewBuilder().create().fromJson(gpxEntityJson1, GpxEntity.class);
      String gpxEntityJson2 = GpxGsonBuilder.getNewBuilder().setPrettyPrinting().create().toJson(gpxEntity2);
      LOGGER.info("gpxEntity2 toJson(): " + gpxEntityJson2);
    } catch (Exception e) {
      Assertions.fail("error: " + e);
    }

  }
}