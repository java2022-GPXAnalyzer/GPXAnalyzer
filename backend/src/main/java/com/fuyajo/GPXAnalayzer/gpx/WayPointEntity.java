package com.fuyajo.GPXAnalayzer.gpx;

import io.jenetics.jpx.WayPoint;

public class WayPointEntity extends GpxEntity {

  private final WayPoint point;

  public WayPointEntity(String name, WayPoint point) {
    super(name);
    this.point = point;
  }

  public WayPoint getPoint() {
    return point;
  }

  @Override
  public String toString() {
    return "GPXPoint [id=" + getUUID() + ", point=" + point + "]";
  }
}
