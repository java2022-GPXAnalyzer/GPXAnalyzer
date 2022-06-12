package com.fuyajo.GPXAnalayzer;

import java.io.IOException;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;

import com.fuyajo.GPXAnalayzer.gpx.GpxDataEntity;
import com.fuyajo.GPXAnalayzer.gpx.GpxParser;

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
			GpxDataEntity output = gpx.read("../GPXData/test.gpx");
			String result = new TypeAdapters.Builder()
												.addTypeAdapter(TypeAdapters.enumTypeAdapter.INSTANT_TIME)
												.addTypeAdapter(TypeAdapters.enumTypeAdapter.POINT)
												.setPrettyPrinting()
												.build()
												.toJson(output);
			return "API Test Read Success\nResult: \n" + result;
		} catch (IOException e) {
			return "file not found: " + e.getMessage();
		}
	}
}