package com.fuyajo.GPXAnalayzer;

import java.io.IOException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;

import com.fuyajo.GPXAnalayzer.gpx.GpxEntity;
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
	public String index() {
		return "GpxAnalayzer";
	}

	@GetMapping("/hello")
	public String hello(@RequestParam(value="name", defaultValue="World") String name) {
		return "Hello " + name;
	}

  @GetMapping("/gpxApi/gpx/{gpxId}")
  public String getGpxJson(@PathVariable("gpxId") String gpxId) {
    GpxEntity gpx = gpxCollector.getGpxEntity(gpxId);
    if (gpx == null) {
      return "";
    }
    return gpx.toJson();
  }

  @PostMapping("gpxApi/uploadGpx")
  public String uploadGpx(@RequestBody String gpxFilePath) {
    try {
      gpxCollector.addByFilepath(gpxFilePath);
      return "API Test Upload Gpx Success\nGpx File Path: " + gpxFilePath
        + "\nGpx Entity UUID: " + gpxCollector.getLast().getUuid();
    } catch (IOException e) {
      return "API Test Upload Gpx Failed\nError: \n" + e;
    }
  }

}