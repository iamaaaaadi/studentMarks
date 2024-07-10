 package com.assignment.marks.exception;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class MarksApplicationExceptionHandler {
   
	//To handle Custom Exception
	@ExceptionHandler(value = { MarksApplicationException.class })
	public ResponseEntity<Object> handleStudentApplicationException(
			MarksApplicationException marksApplicationException) {
		return ResponseEntity.status(marksApplicationException.getHttpStatus())
				.body(marksApplicationException.getMessage());
	} 

	//To handle File Not Found Exception
	@ExceptionHandler(value = { FileNotFoundException.class })
	public ResponseEntity<Object> handleRuntimeException(FileNotFoundException fileNotFoundException) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File not found");

	}

	//To handle IO Exception
	@ExceptionHandler(value = { IOException.class })
	public ResponseEntity<Object> handleRuntimeException(IOException fileNotFoundException) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something Went Wrong");

	}
	
}
