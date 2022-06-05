package com.fuyajo.GPXAnalayzer.database;

import java.util.UUID;

public class GPXData {
  private final UUID uuid;
  protected final String name;

  public GPXData(String name) {
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
