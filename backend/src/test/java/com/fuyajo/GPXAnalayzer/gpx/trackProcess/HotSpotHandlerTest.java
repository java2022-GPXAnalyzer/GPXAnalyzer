package com.fuyajo.GPXAnalayzer.gpx.trackProcess;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import com.fuyajo.GPXAnalayzer.gpx.GpxEntity;
import com.fuyajo.GPXAnalayzer.gpx.WayPointEntity;

@SpringBootTest
class HotSpotHandlerTest {

  private static final Logger LOGGER = LoggerFactory.getLogger(HotSpotHandler.class);

	@Test
	void testGenerateHotSpot() {
    List<WayPointEntity> points = new ArrayList<WayPointEntity>();
    points.add(new WayPointEntity(1.0, 2.0));
    points.add(new WayPointEntity(2.0, 1.0));
    points.add(new WayPointEntity(1.0, 1.0));
    points.add(new WayPointEntity(2.0, 2.0));

    points.add(new WayPointEntity(4.0, 4.0));
    points.add(new WayPointEntity(4.0, 5.0));
    points.add(new WayPointEntity(5.0, 4.0));
    points.add(new WayPointEntity(5.0, 5.0));

    HotSpotHandler hotSpotHandler = new HotSpotHandler();
    List<WayPointEntity> hotSpots = hotSpotHandler.generateHotSpot(points);
    LOGGER.info(hotSpots.toString());
	}

  @Test
	void testGenerateHotSpot2() {
    try {
      GpxEntity gpxEntity = new GpxEntity("../GPXData/test_mock2.gpx");
      List<WayPointEntity> trackPoints = gpxEntity.getWayPoints();

      TrackSmoother trackSmoother = new TrackSmoother();
      trackPoints = trackSmoother.smoothTrack(trackPoints);
      trackPoints = trackSmoother.generateIsometricTrack(trackPoints, 5);

      HotSpotHandler hotSpotHandler = new HotSpotHandler();
      List<WayPointEntity> hotSpots = hotSpotHandler.generateHotSpot(trackPoints);
      LOGGER.info(hotSpots.toString());

      gpxEntity.setTrackPoints(trackPoints);
      gpxEntity.setWayPoints(hotSpots);
      gpxEntity.saveGpsFile("../GPXData/test_hotspot.gpx");
    } catch (Exception e) {
      Assertions.fail(e.getMessage());
    }
	}

}