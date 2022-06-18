package com.fuyajo.GPXAnalayzer.gpx;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class GpxCollector {

  private List<GpxEntity> gpxEntities = new ArrayList<>();
  
  public GpxCollector() {}

  public void addByFilepath(String filepath) throws IOException {
    GpxEntity gpx = new GpxEntity(filepath);
    gpxEntities.add(gpx);
  }

  public GpxEntity getGpxEntity(String uuid) {
    for (GpxEntity gpx : gpxEntities) {
      if (gpx.getUuid().toString().equals(uuid)) {
        return gpx;
      }
    }
    return null;
  }

  public GpxEntity getLast() {
    return gpxEntities.get(gpxEntities.size() - 1);
  }
}
