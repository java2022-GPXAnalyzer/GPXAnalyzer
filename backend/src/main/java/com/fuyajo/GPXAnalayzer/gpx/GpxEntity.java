package com.fuyajo.GPXAnalayzer.gpx;

import java.util.UUID;

public class GpxEntity {
  private final UUID uuid;
  protected final String name;

  public GpxEntity(String name) {
    this.uuid = UUID.randomUUID();
    this.name = name;
  }

  public UUID getUUID() {
    return uuid;
  }
  
  public String getName() {
    return name;
  }
}
