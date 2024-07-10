package com.assignment.marks.service.impl;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.assignment.marks.dao.MarksRepository;
import com.assignment.marks.entity.StudentMarks;
import com.assignment.marks.service.MarksService;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;

@Service
public class MarksServiceImpl implements MarksService {

	private final MarksRepository marksRepository;

	@Autowired
	public MarksServiceImpl(MarksRepository marksRepository) {
		this.marksRepository = marksRepository;
	}

	
	@Override
	public String saveStudentMarks(MultipartFile file) throws FileNotFoundException, IOException {
		
		Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
		HeaderColumnNameTranslateMappingStrategy<StudentMarks> strategy = new HeaderColumnNameTranslateMappingStrategy<>();

		strategy.setType(StudentMarks.class);
		Map<String, String> mapping = new HashMap<>();
		mapping.put("id", "id");
		mapping.put("marks", "marks");
	
		strategy.setColumnMapping(mapping);

		List<StudentMarks> students = new CsvToBeanBuilder<StudentMarks>(reader).withType(StudentMarks.class)
				.withMappingStrategy(strategy).withIgnoreLeadingWhiteSpace(true).build().parse();

		marksRepository.saveAll(students);
		return "Students Marks Uploaded Successfully";
	}


}
