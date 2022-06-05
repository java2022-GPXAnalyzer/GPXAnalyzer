package com.fuyajo.GPXAnalayzer.database;

import java.util.List;
import java.time.Instant;

import io.jenetics.jpx.Metadata;

import java.util.ArrayList;

public class GPXMap extends GPXData {

  private List<GPXPoint> points;
  private final String creator;
  private final String author;
  private final String bounds;
  private final String copyright;
  private final String description;
  private final String keywords;
  private final Instant time;
  private String filename;

  public GPXMap(String filename, String creator, Metadata result) {
    super(result != null ? result.getName().orElse("null") : "null");
    this.filename = filename;
    this.creator = creator;
    this.points = new ArrayList<>();
    if(result != null) {
      this.author = result.getAuthor().isEmpty() ? null : result.getAuthor().get().toString() ;
      this.bounds = result.getBounds().isEmpty() ? null : result.getBounds().get().toString();
      this.copyright = result.getCopyright().isEmpty() ? null : result.getCopyright().get().toString();
      this.description = result.getDescription().isEmpty() ? null : result.getDescription().get().toString();
      this.keywords = result.getKeywords().isEmpty() ? null : result.getKeywords().get().toString();
      this.time = result.getTime().isEmpty() ? null : result.getTime().get();
    } 
    else {
      this.author = null;
      this.bounds = null;
      this.copyright = null;
      this.description = null;
      this.keywords = null;
      this.time = null;
    }
  }

  public List<GPXPoint> getPoints() {
    return points;
  }

  public void add(GPXPoint point) {
    points.add(point);
  }

  @Override
  public String toString() {
    return "GPXMap [id=" + getUUID() + ", points=" + points + "]";
  }  
}
