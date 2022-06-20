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

  @PostMapping("gpxApi/uploadGpx")
  public ResponseEntity<?> uploadGpx(@RequestBody String gpxFilePath) {
    try {
      gpxCollector.addByFilepath(gpxFilePath);
      return ResponseEntity.ok("OK, " + "uploaded gpx file uuid: " + gpxCollector.getLast().getUuid());
    } catch (IOException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

}