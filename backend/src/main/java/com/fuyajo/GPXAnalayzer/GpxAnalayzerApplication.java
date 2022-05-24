package com.fuyajo.GPXAnalayzer;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;

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
}
