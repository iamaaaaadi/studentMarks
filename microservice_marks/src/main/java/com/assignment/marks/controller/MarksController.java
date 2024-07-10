package com.assignment.marks.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.assignment.marks.service.MarksService;

@RestController
@RequestMapping("/api/v1/")
public class MarksController {
	
	private MarksService marksService;
	
	@Autowired
	public MarksController(MarksService marksService) {
		this.marksService = marksService;
	}
	
	
}
