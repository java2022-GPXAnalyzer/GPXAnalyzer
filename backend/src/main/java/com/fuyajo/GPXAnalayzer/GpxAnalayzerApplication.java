package com.fuyajo.GPXAnalayzer;

import java.io.IOException;
import java.util.NoSuchElementException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.fuyajo.GPXAnalayzer.gpx.GpxEntity;
import com.fuyajo.GPXAnalayzer.gpx.json.GpxGsonBuilder;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
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

  @GetMapping("/gpxApi/idList/")
  public ResponseEntity<?> getIdList() {
    Gson gson = new Gson();
    return ResponseEntity.ok(gson.toJson(gpxCollector.getUuids()));
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

  @PostMapping("/gpxApi/uploadGpx")
  public ResponseEntity<?> uploadGpx(@RequestBody String gpxFilePath) {
    try {
      gpxCollector.addByFilepath(gpxFilePath);
      return ResponseEntity.ok("OK, " + "uploaded gpx file uuid: " + gpxCollector.getLast().getUuid());
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
      return ResponseEntity.ok("OK, updated gpx file uuid: " + gpxId);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    } catch (IOException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @PutMapping("/gpxApi/updateGpx")
  public ResponseEntity<?> updateAllGpx(@RequestBody String allGpxJson) {
    try {
      Gson gson = GpxGsonBuilder.getNewBuilder().create();
      JsonArray allGpxJsonObject = gson.fromJson(allGpxJson, JsonArray.class);
      for (JsonElement gpxJson : allGpxJsonObject) {
        GpxEntity gpx = GpxEntity.fromJson(gpxJson.toString());
        gpxCollector.update(gpx);
      }
      return ResponseEntity.ok("OK, updated gpx files");
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    } catch (IOException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }
}