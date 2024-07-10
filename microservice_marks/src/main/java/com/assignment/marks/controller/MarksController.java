package com.assignment.marks.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.assignment.marks.exception.MarksApplicationException;
import com.assignment.marks.service.MarksService;
import com.assignment.marks.util.ConstantUtil;

@RestController
@RequestMapping("/api/v1/")
public class MarksController {

	private MarksService marksService;

	@Autowired
	public MarksController(MarksService marksService) {
		this.marksService = marksService;
	}

	// To Upload a Students Marks in CSV file
	@PostMapping("upload-marks")
	public ResponseEntity<String> uploadCSV(@RequestParam("file") MultipartFile file)
			throws IOException, MarksApplicationException {
		if (file.isEmpty()) {
			throw new MarksApplicationException(HttpStatus.BAD_REQUEST, ConstantUtil.EMPTY_CSV);
		}
		String response = marksService.saveStudentMarks(file);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

}
