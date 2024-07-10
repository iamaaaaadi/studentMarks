package com.assignment.marks.service;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface MarksService {
	String saveStudentMarks(MultipartFile file) throws FileNotFoundException, IOException; 
}
