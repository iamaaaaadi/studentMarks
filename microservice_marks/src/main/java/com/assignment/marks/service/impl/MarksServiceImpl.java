package com.assignment.marks.service.impl;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.assignment.marks.dao.MarksRepository;
import com.assignment.marks.entity.StudentMarks;
import com.assignment.marks.request.MarksCsvDto;
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

	//To save Student Marks into the DB. 
	
	@Override
	public String saveStudentMarks(MultipartFile file) throws FileNotFoundException, IOException {

		Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
		HeaderColumnNameTranslateMappingStrategy<MarksCsvDto> strategy = new HeaderColumnNameTranslateMappingStrategy<>();
		strategy.setType(MarksCsvDto.class);
		
		//Mapping the data
		Map<String, String> mapping = new HashMap<>();
		mapping.put("id", "id");
		mapping.put("marks", "marks");
		strategy.setColumnMapping(mapping);

		List<MarksCsvDto> marksCsvDtos = new CsvToBeanBuilder<MarksCsvDto>(reader).withType(MarksCsvDto.class)
				.withMappingStrategy(strategy).withIgnoreLeadingWhiteSpace(true).build().parse();
       
		//Creating list of students marks to save into DB. 
		List<StudentMarks> studentMarks = marksCsvDtos.stream().map(dto -> {
			StudentMarks studentMark = new StudentMarks();
			studentMark.setId(Long.parseLong(dto.getId()));
			studentMark.setMarks(Long.parseLong(dto.getMarks()));

			return studentMark;
		}).collect(Collectors.toList());

		marksRepository.saveAll(studentMarks);
		return "Students Marks Uploaded Successfully";

	}

}
