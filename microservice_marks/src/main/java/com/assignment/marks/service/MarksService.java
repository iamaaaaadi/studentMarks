package com.assignment.marks.service;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.assignment.marks.exception.MarksApplicationException;

public interface MarksService {
	String saveStudentMarks(MultipartFile file) throws FileNotFoundException, IOException;
	byte[] generateCombinedDataCsv() throws MarksApplicationException, IOException;
}
