package com.fuyajo.GPXAnalayzer.gpx;

import java.time.Instant;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.jenetics.jpx.WayPoint;

public class WayPointEntity extends AbstractEntity {

  private static final Logger LOGGER = LoggerFactory.getLogger(WayPointEntity.class);

  private WayPoint wayPoint;

  public WayPointEntity(WayPoint wayPoint) {
    this.wayPoint = wayPoint.toBuilder().build();
  }

  public WayPointEntity(WayPoint wayPoint, UUID uuid) {
    super(uuid);
    this.wayPoint = wayPoint.toBuilder().build();
  }

  public WayPointEntity(WayPointEntity wayPointEntity) {
    super(wayPointEntity.getUuid());
    this.wayPoint = wayPointEntity.getWayPoint();
  }

  public static Double getDistanceWithMeter(WayPointEntity wayPointEntity1, WayPointEntity wayPointEntity2) {
    Double lat1 = wayPointEntity1.getWayPoint().getLatitude().doubleValue();
    Double lat2 = wayPointEntity2.getWayPoint().getLatitude().doubleValue();
    Double lon1 = wayPointEntity1.getWayPoint().getLongitude().doubleValue();
    Double lon2 = wayPointEntity2.getWayPoint().getLongitude().doubleValue();

    // TODO: increse distance accuracy in WGS84
    return Math.sqrt(Math.pow((lat1 - lat2) * 100000, 2) + Math.pow((lon1 - lon2) * 100000, 2));
  }

  public static long getMillisecondDifferent(WayPointEntity wayPointEntity1, WayPointEntity wayPointEntity2) {
    return Math.abs(wayPointEntity1.getTime().toEpochMilli() - wayPointEntity2.getTime().toEpochMilli());
  }

  public WayPoint getWayPoint() {
    return wayPoint.toBuilder().build();
  }

  public void setWayPoint(WayPoint wayPoint) {
    this.wayPoint = wayPoint;
  }

  public Double getLatitude() {
    return wayPoint.getLatitude().doubleValue();
  }

  public Double getLongitude() {
    return wayPoint.getLongitude().doubleValue();
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
      return getWayPoint().getElevation().orElse(null).doubleValue();
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
