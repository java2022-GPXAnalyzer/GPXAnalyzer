package com.fuyajo.GPXAnalayzer.database;

import java.util.UUID;
import io.jenetics.jpx.WayPoint;

public class GPXPoint {

  private final UUID id;
  private final String name;
  private final WayPoint point;

  public GPXPoint(WayPoint point, String name) {
    this.id = UUID.randomUUID();
    this.name = name;
    this.point = point;
  }

  public UUID getId() {
    return id;
  }
  
  public String getName() {
    return name;
  }

  public WayPoint getPoint() {
    return point;
  }

  @Override
  public String toString() {
    return "GPXPoint [id=" + id + ", point=" + point + "]";
  }
}
