package com.fuyajo.GPXAnalayzer.gpx;

import com.google.gson.annotations.Expose;

public class GpxInfo {

  @Expose
  private final String uuid;
  @Expose
  private final String startTime;
  @Expose
  private final String endTime;
  @Expose
  private final String creator;
  @Expose
  private final String version;
  @Expose
  private final String name;

  public GpxInfo(GpxEntity gpxEntity){
    this.uuid = gpxEntity.getUuid().toString();
    this.startTime = gpxEntity.getStartTime();
    this.endTime = gpxEntity.getEndTime();
    this.creator = gpxEntity.getGpx().getCreator();
    this.version = gpxEntity.getGpx().getVersion();
    this.name = gpxEntity.getName();
  } 

  public String getUuid() {
    return uuid;
  }

  public String getStartTime() {
    return startTime;
  }

  public String getEndTime() {
    return endTime;
  }

  public String getCreator() {
    return creator;
  }

  public String getVersion() {
    return version;
  }

  public String getName() {
    return name;
  }

  @Override
  public String toString() {
    return "GpxInfo [uuid=" + uuid + ", startTime=" + startTime + ", endTime=" + endTime + ", creator=" + creator
        + ", version=" + version + ", name=" + name + "]";
  }
}
