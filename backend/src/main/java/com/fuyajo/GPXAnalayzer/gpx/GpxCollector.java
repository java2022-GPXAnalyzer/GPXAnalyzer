package com.fuyajo.GPXAnalayzer.gpx;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.ArrayList;

public class GpxCollector {

  private List<GpxEntity> gpxEntities = new ArrayList<>();
  
  public GpxCollector() {}

  public void addByFilepath(String filepath) throws IOException, IllegalArgumentException {
    for (GpxEntity gpx : gpxEntities) {
      if (gpx.getFilepath().equals(filepath)) {
        throw new IllegalArgumentException("Gpx File Path Already Exists, uuid: " + gpx.getUuid());
      }
    }
    GpxEntity gpx = new GpxEntity(filepath);
    gpxEntities.add(gpx);
  }

  public void update(GpxEntity gpx) throws IOException, IllegalArgumentException {
    this.update(gpx, gpx.getUuid());
  }

  public void update(GpxEntity gpx, UUID uuid) throws IOException, IllegalArgumentException {
    for (GpxEntity gpxEntity : gpxEntities) {
      if (gpxEntity.getUuid().equals(uuid)) {
        gpxEntities.remove(gpxEntity);
        gpxEntities.add(gpx);
        return;
      }
    }
    throw new IllegalArgumentException("Gpx Entity Not Found, uuid: " + uuid);
  }

  public GpxEntity getGpxEntity(String uuid) throws NoSuchElementException {
    for (GpxEntity gpx : gpxEntities) {
      if (gpx.getUuid().toString().equals(uuid)) {
        return gpx;
      }
    }
    throw new NoSuchElementException("Gpx Entity Not Found");
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
