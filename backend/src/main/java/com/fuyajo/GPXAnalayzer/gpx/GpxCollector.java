package com.fuyajo.GPXAnalayzer.gpx;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.ArrayList;

public class GpxCollector {

  private List<GpxEntity> gpxEntities = new ArrayList<>();
  
  public void addByFilepath(String filepath) throws IOException, IllegalArgumentException {
    for (GpxEntity gpx : gpxEntities) {
      if (gpx.getFilepath().equals(filepath)) {
        throw new IllegalArgumentException("Gpx File Path Already Exists, uuid: " + gpx.getUuid());
      }
    }
    GpxEntity gpx = new GpxEntity(filepath);
    gpxEntities.add(gpx);
  }

  public void update(GpxEntity newGpxEntity) throws IOException, IllegalArgumentException {
    this.update(newGpxEntity, newGpxEntity.getUuid());
  }

  // TODO: don't remove gpx, update them instead
  public void update(GpxEntity newGpxEntity, UUID uuid) throws IOException, IllegalArgumentException {
    for (GpxEntity gpxEntity : gpxEntities) {
      if (gpxEntity.getUuid().equals(uuid)) {
        gpxEntities.remove(gpxEntity);
        gpxEntities.add(newGpxEntity);
        newGpxEntity.saveGpxFile();
        return;
      }
    }
    throw new IllegalArgumentException("Gpx Entity Not Found, uuid: " + uuid);
  }

  public GpxEntity getGpxEntity(String uuid) throws NoSuchElementException {
    for (GpxEntity gpxEntity : gpxEntities) {
      if (gpxEntity.getUuid().toString().equals(uuid)) {
        return new GpxEntity(gpxEntity);
      }
    }
    throw new NoSuchElementException("Gpx Entity Not Found");
  }

  public List<UUID> getUuids() {
    List<UUID> uuids = new ArrayList<>();
    for (GpxEntity gpx : gpxEntities) {
      uuids.add(gpx.getUuid());
    }
    return uuids;
  }

  public GpxEntity getLast() {
    return new GpxEntity(gpxEntities.get(gpxEntities.size() - 1));
  }

  public List<GpxEntity> getGpxEntities() {
    List<GpxEntity> newGpxEntities = new ArrayList<>();
    this.gpxEntities.forEach(gpxEntity -> newGpxEntities.add(new GpxEntity(gpxEntity)));
    return newGpxEntities;
  }
}
