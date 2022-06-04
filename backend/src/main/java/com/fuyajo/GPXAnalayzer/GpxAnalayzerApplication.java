package com.fuyajo.GPXAnalayzer;

import java.io.IOException;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;

import com.fuyajo.GPXAnalayzer.parser.GpxParser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.fuyajo.GPXAnalayzer.database.GPXPoint;

@SpringBootApplication
@RestController
@RequestMapping(produces=MediaType.APPLICATION_JSON_VALUE)
public class GpxAnalayzerApplication {

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

	@GetMapping("/api/test")
	public String apiTest() {
		return "API Test Success";
	}

	@GetMapping("/api/test/read")
	public String apiTestRead() {
		try{
			GpxParser gpx = new GpxParser();
			List<GPXPoint> output = gpx.read("../GPXData/test.gpx");

			GsonBuilder gsonBuilder = new GsonBuilder();
			gsonBuilder.registerTypeAdapter(GPXPoint.class, TypeAdapters.GPXPOINT_TYPEADAPTER);
			Gson gson = gsonBuilder.create();

			// final Gson gson = gsonBuilder.create();
			// String result = gson.toJson(output);
			String result = TypeAdapters
												.builder
												.addTypeAdapter(TypeAdapters.GPXPOINT_TYPEADAPTER)
												.toJson(output);
			return "API Test Read Success\nResult: \n" + result;
		} catch (IOException e) {
			return "file not found: " + e.getMessage();
		}
	}
}

