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
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.fuyajo.GPXAnalayzer.gpx.GpxEntity;
import com.fuyajo.GPXAnalayzer.gpx.json.GpxGsonBuilder;
import com.google.gson.Gson;
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
}