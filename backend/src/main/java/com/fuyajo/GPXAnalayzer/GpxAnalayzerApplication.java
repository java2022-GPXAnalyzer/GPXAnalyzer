package com.fuyajo.GPXAnalayzer;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.util.Pair;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.fuyajo.GPXAnalayzer.gpx.GpxEntity;
import com.fuyajo.GPXAnalayzer.gpx.WayPointEntity;
import com.fuyajo.GPXAnalayzer.gpx.json.GpxGsonBuilder;
import com.fuyajo.GPXAnalayzer.gpx.json.SpeedColorAdapter;
import com.fuyajo.GPXAnalayzer.gpx.speedColor.SpeedColorHandler;
import com.fuyajo.GPXAnalayzer.gpx.trackProcess.HotSpotHandler;
import com.fuyajo.GPXAnalayzer.gpx.trackProcess.TrackSmoother;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.fuyajo.GPXAnalayzer.gpx.GpxCollector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
@RestController
@RequestMapping(produces=MediaType.APPLICATION_JSON_VALUE)
public class GpxAnalayzerApplication {

  private static final Logger LOGGER = LoggerFactory.getLogger(GpxAnalayzerApplication.class);

  private GpxCollector gpxCollector = new GpxCollector();

	public static void main(String[] args) {
		SpringApplication.run(GpxAnalayzerApplication.class, args);
	}

	@GetMapping("/")
	public ResponseEntity<?> index() {
		return ResponseEntity.ok("GpxAnalayzer");
	}

  @GetMapping("/gpxApi/idList")
  public ResponseEntity<?> getIdList() {
    Gson gson = new Gson();
    List<String> idList = gpxCollector.getUuids().stream()
      .map(uuid -> uuid.toString())
      .collect(Collectors.toList()); 
    return ResponseEntity.ok(gson.toJson(idList));
  }

  @GetMapping("/gpxApi/gpx/{gpxId}")
  public ResponseEntity<?> getGpx(@PathVariable("gpxId") String gpxId) {
    try {
      GpxEntity gpx = gpxCollector.getGpxEntity(gpxId);
      return ResponseEntity.ok(gpx.toJson());
    } catch (NoSuchElementException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @GetMapping("/gpxApi/gpx/{gpxId}/gpxInfo")
  public ResponseEntity<?> getGpxInfo(@PathVariable("gpxId") String gpxId) {
    try {
      GpxEntity gpx = gpxCollector.getGpxEntity(gpxId);
      return ResponseEntity.ok(gpx.getGpxInfo().toJson());
    } catch (NoSuchElementException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @GetMapping("/gpxApi/gpx/{gpxId}/trackPoints")
  public ResponseEntity<?> getGpxTrackPoints(@PathVariable("gpxId") String gpxId) {
    try {
      GpxEntity gpx = gpxCollector.getGpxEntity(gpxId);
      return ResponseEntity.ok(
        GpxGsonBuilder.getNewBuilder().create().toJson(gpx.getTrackPoints())
      );
    } catch (NoSuchElementException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @GetMapping("/gpxApi/gpx/{gpxId}/wayPoints")
  public ResponseEntity<?> getGpxWayPoints(@PathVariable("gpxId") String gpxId) {
    try {
      GpxEntity gpx = gpxCollector.getGpxEntity(gpxId);
      return ResponseEntity.ok(
        GpxGsonBuilder.getNewBuilder().create().toJson(gpx.getWayPoints())
      );
    } catch (NoSuchElementException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @GetMapping("/gpxApi/gpx/{gpxId}/trackPoint/speedColorInfo")
  public ResponseEntity<?> getTrackSpeedColorInfo(
      @PathVariable("gpxId") String gpxId,
      @RequestParam(required = false) Integer pointGap)
  {
    try {
      GpxEntity gpx = gpxCollector.getGpxEntity(gpxId);
      SpeedColorHandler speedColorHandler = new SpeedColorHandler();
      List<Pair<Double, Double> > speedColorList;
      if(pointGap == null) {
        speedColorList = speedColorHandler.generateSpeedColorList(gpx.getTrackPoints());
      } else {
        speedColorList = speedColorHandler.generateSpeedColorList(gpx.getTrackPoints(), pointGap);
      }

      LOGGER.info(speedColorList.toString());
      Type speedColorType = TypeToken.getParameterized(Pair.class, Double.class, Double.class).getType();
      Type speedColorListType = TypeToken.getParameterized(List.class, speedColorType).getType();
      LOGGER.info(speedColorListType.toString());
      Gson gson = (new GsonBuilder())
        .registerTypeAdapter(speedColorListType, new SpeedColorAdapter())
        .create();
      return ResponseEntity.ok(gson.toJson(speedColorList));
    } catch (NoSuchElementException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @GetMapping("/gpxApi/hotspots/")
  public ResponseEntity<?> getHotSpots() {
    try{
      LOGGER.info("getHotSpots() start");
      List<GpxEntity> gpxEntities = gpxCollector.getGpxEntities();
      List<WayPointEntity> points = new ArrayList<WayPointEntity>();

      LOGGER.info("Start smooth gpx tracks");
      TrackSmoother trackSmoother = new TrackSmoother();
      for(GpxEntity gpxEntity : gpxEntities) {
        List<WayPointEntity> trackPoints = gpxEntity.getTrackPoints();
        trackPoints = trackSmoother.smoothTrack(trackPoints);
        trackPoints = trackSmoother.generateIsometricTrack(trackPoints, 50);
        trackPoints.forEach(
          trackPoint -> points.add(trackPoint)
        );
      }

      LOGGER.info("Start generate hot spots");
      HotSpotHandler hotSpotHandler = new HotSpotHandler();
      List<WayPointEntity> hotSpots = hotSpotHandler.generateHotSpot(points);

      Gson gpxGson = GpxGsonBuilder.getNewBuilder().create();
      LOGGER.info("getHotSpots() end");
      return ResponseEntity.ok(gpxGson.toJson(hotSpots));
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @PostMapping("/gpxApi/uploadGpx")
  public ResponseEntity<?> uploadGpxList(@RequestBody String gpxFilePathsJson) {
    try {
      Gson gson = new Gson();
      Type stringListType = TypeToken.getParameterized(List.class, String.class).getType();
      List<String> gpxFilePaths = gson.fromJson(gpxFilePathsJson, stringListType);
      List<UUID> uuids = new ArrayList<UUID>();
      for (String gpxFilePath : gpxFilePaths) {
        gpxCollector.addByFilepath(gpxFilePath);
        uuids.add(gpxCollector.getLast().getUuid());
      }
      return ResponseEntity.ok(gson.toJson(uuids));
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    } catch (IOException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @PutMapping("/gpxApi/updateGpx/{gpxId}")
  public ResponseEntity<?> updateGpx(@PathVariable("gpxId") String gpxId, @RequestBody String gpxJson) {
    try {
      Gson gson = GpxGsonBuilder.getNewBuilder().create();
      GpxEntity gpxEntity = gson.fromJson(gpxJson, GpxEntity.class);
      gpxCollector.update(gpxEntity);
      return ResponseEntity.ok(gson.toJson(gpxId));
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    } catch (IOException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @PutMapping("/gpxApi/updateGpx")
  public ResponseEntity<?> updateGpxList(@RequestBody String GpxListJson) {
    try {
      Gson gson = GpxGsonBuilder.getNewBuilder().create();
      JsonArray GpxJsonListObject = gson.fromJson(GpxListJson, JsonArray.class);
      for (JsonElement gpxJson : GpxJsonListObject) {
        GpxEntity gpx = GpxEntity.fromJson(gpxJson.toString());
        gpxCollector.update(gpx);
      }
      return ResponseEntity.ok().build();
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    } catch (IOException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @PutMapping("/gpxApi/smootherGpx/")
  public ResponseEntity<?> smootherAllGpx() {
    try{
      LOGGER.info("getHotSpots() start");
      List<GpxEntity> gpxEntities = gpxCollector.getGpxEntities();

      LOGGER.info("Start smooth gpx tracks");
      TrackSmoother trackSmoother = new TrackSmoother();
      for(int i = 0; i < gpxEntities.size(); i++){
        List<WayPointEntity> trackPoints = gpxEntities.get(i).getTrackPoints();
        trackPoints = trackSmoother.smoothTrack(trackPoints);
        // trackPoints = trackSmoother.generateIsometricTrack(trackPoints, 50);
        GpxEntity gpxEntity = gpxEntities.get(i);
        gpxEntity.setTrackPoints(trackPoints);
        LOGGER.info("len: " + gpxEntity.getTrackPoints().size());
        gpxEntities.set(i, gpxEntity);
      }
      LOGGER.info("len: " + gpxEntities.get(0).getTrackPoints().size());
      gpxCollector.setGpxEntities(gpxEntities);

      Gson gpxGson = GpxGsonBuilder.getNewBuilder().create();
      return ResponseEntity.ok(gpxGson.toJson(gpxCollector.getGpxEntities()));
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }
}