package com.fuyajo.GPXAnalayzer.gpx;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class GpxCollector {

  private List<GpxEntity> gpxEntities = new ArrayList<>();
  
  public GpxCollector() {}

  public void addByFilepath(String filepath) throws IOException {
    for (GpxEntity gpx : gpxEntities) {
      if (gpx.getFilepath().equals(filepath)) {
        throw new IOException("Gpx File Path Already Exists, uuid: " + gpx.getUuid());
      }
    }
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

  public List<String> getUuids() {
    List<String> uuids = new ArrayList<>();
    for (GpxEntity gpx : gpxEntities) {
      uuids.add(gpx.getUuid().toString());
    }
    return uuids;
  }

  public GpxEntity getLast() {
    return gpxEntities.get(gpxEntities.size() - 1);
  }
}
