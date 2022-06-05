package com.fuyajo.GPXAnalayzer.database;

import io.jenetics.jpx.WayPoint;

public class GPXPoint extends GPXData {

  private final WayPoint point;

  public GPXPoint(String name, WayPoint point) {
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
