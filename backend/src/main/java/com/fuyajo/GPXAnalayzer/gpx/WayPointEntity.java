package com.fuyajo.GPXAnalayzer.gpx;

import java.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.annotations.Expose;

import io.jenetics.jpx.WayPoint;

public class WayPointEntity extends AbstractEntity {

  private static final Logger LOGGER = LoggerFactory.getLogger(WayPointEntity.class);

  private final WayPoint wayPoint;
  @Expose
  private final String test = "test";

  public WayPointEntity(WayPoint wayPoint) {
    this.wayPoint = wayPoint;
  }

  public WayPoint getWayPoint() {
    return wayPoint;
  }

  public Instant getTime() {
    Instant time = Instant.ofEpochMilli(0);
    try {
      time = getWayPoint().getTime().orElse(time);
    } catch (Exception e) {
      LOGGER.error("getStartTime() error: " + e);
    }
    return time;
  }

  public String getName() {
    return wayPoint.getName().orElse("");
  }

  public double getElevation() {
    try {
      return wayPoint.getElevation().orElse(null).doubleValue();
    } catch (Exception e) {
      LOGGER.error("getElevation() error: " + e);
      return 0.0f;
    }
  }

  @Override
  public String toString() {
    return "WayPointEntity [id=" + getUuid() + ", point=" + wayPoint + "]";
  }
}