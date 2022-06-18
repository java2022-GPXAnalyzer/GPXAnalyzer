package com.fuyajo.GPXAnalayzer.gpx;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fuyajo.GPXAnalayzer.gpx.json.GpxGsonBuilder;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

import io.jenetics.jpx.GPX;

// 包含一個 GPX 檔案的資訊
/*
 *  GPX: {
 *    metadata [0..1]: {}
 * 
 *    waypoints [0..*]: [wpt]
 * 
 *    routes [0..*]: [
 *      rte [0..*]: {[rtept]}
 *    ]
 * 
 *    tracks: [
 *      trk [0..*]: {
 *        trkseg [0..*]: {[trkpt]}
 *      }
 *    ] 
 *  }
 */

// TODO: make this class more fit to the GPX format
public class GpxEntity extends AbstractEntity {

  private static final Logger LOGGER = LoggerFactory.getLogger(GpxEntity.class);
  private final String filepath;
  private final GPX gpx;

  @Expose
  private GpxInfo gpxInfo;
  @Expose
  private List<WayPointEntity> wayPoints = new ArrayList<>();
  private List<WayPointEntity> routePoints = new ArrayList<>();
  @Expose
  private List<WayPointEntity> trackPoints = new ArrayList<>();

  public GpxEntity(String filepath) throws IOException {
    this(GpxFileHandler.DEFAULT.read(filepath), filepath);
  }
  
  public GpxEntity(GPX gpx) throws IOException {
    this(gpx, null);
  }

  public GpxEntity(GPX gpx, String filepath) throws IOException{
    super();
    this.filepath = filepath;
    this.gpx = gpx;
    gpxInfo = new GpxInfo(this);
    gpx.getWayPoints().forEach(wayPoint -> {
      wayPoints.add(new WayPointEntity(wayPoint));
    });
    gpx.getRoutes().forEach(route -> {
      route.getPoints().forEach(point -> {
        routePoints.add(new WayPointEntity(point));
      });
    });
    gpx.getTracks().forEach(track -> {
      track.getSegments().forEach(segment -> {
        segment.getPoints().forEach(point -> {
          trackPoints.add(new WayPointEntity(point));
        });
      });
    });
  }
  
  public String getFilepath() {
    return filepath;
  }

  public GPX getGpx() {
    return gpx;
  }

  public GpxInfo getGpxInfo() {
    return gpxInfo;
  }

  public List<WayPointEntity> getWayPoints() {
    return wayPoints;
  }

  public List<WayPointEntity> getRoutePoints() {
    return routePoints;
  }

  public List<WayPointEntity> getTrackPoints() {
    return trackPoints;
  }

  public String getStartTime() {
    Instant time = Instant.ofEpochMilli(0);
    try {
      time = trackPoints.get(0).getTime();
    } catch (Exception e) {
      LOGGER.error("getStartTime() error: " + e);
    }
    return time.toString();
  }

  public String getEndTime() {
    Instant time = Instant.ofEpochMilli(0);
    try {
      time = trackPoints.get(trackPoints.size() - 1).getTime();
    } catch (Exception e) {
      LOGGER.error("getEndTime() error: " + e);
    }
    return time.toString();
  }

  public String getName() {
    try {
      return gpx.getMetadata().get().getName().get();
    } catch (Exception e) {
      LOGGER.error("getName() error: " + e);
      return "";
    }
  }

  public String toJson() {
    GsonBuilder gpxGsonBuilder = GpxGsonBuilder.getNewBuilder();
    gpxGsonBuilder.setPrettyPrinting();
    return gpxGsonBuilder.create().toJson(this);
  }

  @Override
  public String toString() {
    return "GpxEntity [filepath=" + filepath + ", gpx=" + gpx +
           ", wayPoints=" + wayPoints + ", routePoints=" + routePoints +
           ", trackPoints=" + trackPoints + "]";
  }  
}
