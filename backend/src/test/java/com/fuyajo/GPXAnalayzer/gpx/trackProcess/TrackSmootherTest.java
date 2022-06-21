package com.fuyajo.GPXAnalayzer.gpx.trackProcess;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import com.fuyajo.GPXAnalayzer.gpx.GpxEntity;
import com.fuyajo.GPXAnalayzer.gpx.WayPointEntity;
import com.fuyajo.GPXAnalayzer.gpx.json.GpxGsonBuilder;
import com.google.gson.Gson;

import io.jenetics.jpx.WayPoint;

@SpringBootTest
public class TrackSmootherTest {

  private static final Logger LOGGER = LoggerFactory.getLogger(TrackSmootherTest.class);

  @Test
  void testProcess() {
    try {
      GpxEntity gpxEntity = new GpxEntity("../GPXData/test.gpx");
      List<WayPointEntity> trackPoints = gpxEntity.getTrackPoints();

      TrackSmoother trackSmoother = new TrackSmoother();
      trackPoints = trackSmoother.smoothTrack(trackPoints);

      GpxEntity oriGpxEntity = new GpxEntity(gpxEntity);
      gpxEntity.setTrackPoints(trackPoints);
      gpxEntity.saveGpsFile("../GPXData/test_kalman.gpx");
      LOGGER.info(gpxEntity.getTrackPoints().get(5).getLatitude() + " " + oriGpxEntity.getTrackPoints().get(5).getLatitude());
      Assertions.assertNotEquals(gpxEntity.getTrackPoints().get(5).getLatitude(), oriGpxEntity.getTrackPoints().get(5).getLatitude());
    } catch (Exception e) {
      Assertions.fail(e.getMessage());
    }
  }
}
