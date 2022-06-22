package com.fuyajo.GPXAnalayzer.gpx;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.ArrayList;
import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fuyajo.GPXAnalayzer.gpx.json.GpxGsonBuilder;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import io.jenetics.jpx.GPX;
import io.jenetics.jpx.Metadata;
import io.jenetics.jpx.Route;
import io.jenetics.jpx.Track;
import io.jenetics.jpx.TrackSegment;
import io.jenetics.jpx.GPX.Version;

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
  private GPX gpx;

  private GpxInfo gpxInfo;
  private List<WayPointEntity> wayPoints = new ArrayList<>();
  private List<WayPointEntity> routePoints = new ArrayList<>();
  private List<WayPointEntity> trackPoints = new ArrayList<>();

  public GpxEntity(String filepath) throws IOException {
    this(GpxFileHandler.DEFAULT.read(filepath), filepath, null);
  }

  public GpxEntity(GPX gpx) throws IOException {
    this(gpx, null, null);
  }

  public GpxEntity(GPX gpx, UUID uuid) throws IOException {
    this(gpx, null, uuid);
  }

  public GpxEntity(GpxEntity gpxEntity) {
    super(gpxEntity.getUuid());
    this.filepath = gpxEntity.getFilepath();
    this.gpx = gpxEntity.getGpx();
    this.gpxInfo = gpxEntity.getGpxInfo();
    this.wayPoints = gpxEntity.getWayPoints();
    this.routePoints = gpxEntity.getRoutePoints();
    this.trackPoints = gpxEntity.getTrackPoints();
  }

  public GpxEntity(GPX gpx, String filepath, UUID uuid) throws IOException{
    super(uuid);
    this.filepath = filepath;
    this.gpx = gpx.toBuilder().build();
    gpx.getWayPoints().forEach(wayPoint -> {
      this.wayPoints.add(new WayPointEntity(wayPoint, this, this.wayPoints));
    });
    gpx.getRoutes().forEach(route -> {
      route.getPoints().forEach(point -> {
        this.routePoints.add(new WayPointEntity(point, this, this.routePoints));
      });
    });
    gpx.getTracks().forEach(track -> {
      track.getSegments().forEach(segment -> {
        segment.getPoints().forEach(point -> {
          this.trackPoints.add(new WayPointEntity(point, this, this.trackPoints));
        });
      });
    });
    gpxInfo = new GpxInfo(this);
  }
  
  public String getFilepath() {
    return filepath;
  }

  public GPX getGpx() {
    return gpx.toBuilder().build();
  }

  public void setGpx(GPX gpx) {
    this.gpx = gpx;
    this.syncDataWithGpx();
  }

  public void syncGpxWithData() {
    GPX.Builder gpxBuilder = GPX.builder(
      Version.of(this.gpxInfo.getVersion()),
      this.gpxInfo.getCreator()
    );
    // add metadata
    Metadata metadata = Metadata.builder().name(this.gpxInfo.getName()).build();
    gpxBuilder.metadata(metadata);
    // add waypoints
    for (WayPointEntity wayPoint : this.wayPoints) {
      gpxBuilder.addWayPoint(wayPoint.getWayPoint());
    }
    // add route points
    Route.Builder routeBuilder = Route.builder();
    for (WayPointEntity routePoint : this.routePoints) {
      routeBuilder.addPoint(routePoint.getWayPoint());
    }
    gpxBuilder.addRoute(routeBuilder.build());
    // add track points
    TrackSegment.Builder trackSegmentBuilder = TrackSegment.builder();
    for (WayPointEntity trackPoint : this.trackPoints) {
      trackSegmentBuilder.addPoint(trackPoint.getWayPoint());
    }
    Track.Builder trackBuilder = Track.builder();
    trackBuilder.addSegment(trackSegmentBuilder.build());
    gpxBuilder.addTrack(trackBuilder.build());

    // to gpx entity
    gpx = gpxBuilder.build();
  }

  public void syncDataWithGpx() {
    gpxInfo = new GpxInfo(this);
    gpx.getWayPoints().forEach(wayPoint -> {
      this.wayPoints.add(new WayPointEntity(wayPoint, this, this.wayPoints));
    });
    gpx.getRoutes().forEach(route -> {
      route.getPoints().forEach(point -> {
        this.routePoints.add(new WayPointEntity(point, this, this.routePoints));
      });
    });
    gpx.getTracks().forEach(track -> {
      track.getSegments().forEach(segment -> {
        segment.getPoints().forEach(point -> {
          this.trackPoints.add(new WayPointEntity(point, this, this.trackPoints));
        });
      });
    });
  }

  public GpxInfo getGpxInfo() {
    return new GpxInfo(gpxInfo);
  }

  public void setGpxInfo(GpxInfo gpxInfo) {
    this.gpxInfo = new GpxInfo(gpxInfo);
    this.syncGpxWithData();
  }

  public List<WayPointEntity> getWayPoints() {
    List<WayPointEntity> newWayPoints = new ArrayList<WayPointEntity>();
    this.wayPoints.forEach(wayPoint -> newWayPoints.add(new WayPointEntity(wayPoint)));
    return newWayPoints;
  }

  public void setWayPoints(List<WayPointEntity> wayPoints) {
    this.wayPoints = new ArrayList<>();
    wayPoints.forEach(wayPoint -> this.wayPoints.add(new WayPointEntity(wayPoint)));
    this.syncGpxWithData();
  }

  public List<WayPointEntity> getRoutePoints() {
    List<WayPointEntity> newRoutePoints = new ArrayList<WayPointEntity>();
    this.routePoints.forEach(routePoint -> newRoutePoints.add(new WayPointEntity(routePoint)));
    return newRoutePoints;
  }

  public void setRoutePoints(List<WayPointEntity> routePoints) {
    this.routePoints = new ArrayList<>();
    routePoints.forEach(routePoint -> this.routePoints.add(new WayPointEntity(routePoint)));
    this.syncGpxWithData();
  }

  public List<WayPointEntity> getTrackPoints() {
    List<WayPointEntity> newTrackPoints = new ArrayList<WayPointEntity>();
    this.trackPoints.forEach(trackPoint -> newTrackPoints.add(new WayPointEntity(trackPoint)));
    return newTrackPoints;
  }

  public void setTrackPoints(List<WayPointEntity> trackPoints) {
    this.trackPoints = new ArrayList<>();
    trackPoints.forEach(trackPoint -> this.trackPoints.add(new WayPointEntity(trackPoint)));
    this.syncGpxWithData();
  }

  public String getStartTime() {
    Instant time = Instant.ofEpochMilli(0);
    try {
      time = this.trackPoints.get(0).getTime();
    } catch (Exception e) {
      LOGGER.error("getStartTime() error: " + e);
    }
    return time.toString();
  }

  public String getEndTime() {
    Instant time = Instant.ofEpochMilli(0);
    try {
      time = this.trackPoints.get(trackPoints.size() - 1).getTime();
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

  public void updateGpx() {
    // TODO: update gpx
  }

  public void saveGpxFile() throws IOException {
    this.saveGpsFile(this.filepath);
  }

  public void saveGpsFile(String filepath) throws IOException {
    GpxFileHandler gpxFileHandler = new GpxFileHandler(this.gpx);
    gpxFileHandler.write(filepath);
  }

  public String toJson() {
    GsonBuilder gpxGsonBuilder = GpxGsonBuilder.getNewBuilder();
    return gpxGsonBuilder.create().toJson(this);
  }

  public static GpxEntity fromJson(String json) {
    GsonBuilder gpxGsonBuilder = GpxGsonBuilder.getNewBuilder();
    return gpxGsonBuilder.create().fromJson(json, GpxEntity.class);
  }

  public static GpxEntity fromJsonObject(JsonObject jsonObject) {
    GsonBuilder gpxGsonBuilder = GpxGsonBuilder.getNewBuilder();
    return gpxGsonBuilder.create().fromJson(jsonObject, GpxEntity.class);
  }

  @Override
  public String toString() {
    return "GpxEntity [filepath=" + filepath + ", gpx=" + gpx +
           ", wayPoints=" + wayPoints + ", routePoints=" + routePoints +
           ", trackPoints=" + trackPoints + "]";
  }  
}
