package com.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Future End points that does not need authentication can Go here
 * 
 **/
@RestController
@RequestMapping("/api/public")
public class PublicController {

	@GetMapping
	public String getMessage() {
		return "Hello from public API controller";
	}
}
