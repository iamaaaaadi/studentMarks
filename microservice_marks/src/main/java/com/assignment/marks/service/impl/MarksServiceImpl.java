package com.assignment.marks.service.impl;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.assignment.marks.dao.MarksRepository;
import com.assignment.marks.entity.StudentMarks;
import com.assignment.marks.request.MarksCsvDto;
import com.assignment.marks.request.StudentDto;
import com.assignment.marks.service.MarksService;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;

@Service
public class MarksServiceImpl implements MarksService {

	private final MarksRepository marksRepository;
	
    @Value("${api.url}")
    private String apiUrl;

	@Autowired
	public MarksServiceImpl(MarksRepository marksRepository) {
		this.marksRepository = marksRepository;
	}
	
	@Autowired
	private RestTemplate restTemplate;

	// To save Student Marks into the DB.

	@Override
	public String saveStudentMarks(MultipartFile file) throws FileNotFoundException, IOException {

		Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
		HeaderColumnNameTranslateMappingStrategy<MarksCsvDto> strategy = new HeaderColumnNameTranslateMappingStrategy<>();
		strategy.setType(MarksCsvDto.class);

		// Mapping the data
		Map<String, String> mapping = new HashMap<>();
		mapping.put("id", "id");
		mapping.put("marks", "marks");
		strategy.setColumnMapping(mapping);

		List<MarksCsvDto> marksCsvDtos = new CsvToBeanBuilder<MarksCsvDto>(reader).withType(MarksCsvDto.class)
				.withMappingStrategy(strategy).withIgnoreLeadingWhiteSpace(true).build().parse();

		// Creating list of students marks to save into DB.
		List<StudentMarks> studentMarks = marksCsvDtos.stream().map(dto -> {
			StudentMarks studentMark = new StudentMarks();
			studentMark.setId(Long.parseLong(dto.getId()));
			studentMark.setMarks(Long.parseLong(dto.getMarks()));

			return studentMark;
		}).collect(Collectors.toList());

		marksRepository.saveAll(studentMarks);
		return "Students Marks Uploaded Successfully";

	}
	
	//Combining Student Info and Marks
	//returns a CSV

	@Override
	public byte[] generateCombinedDataCsv() throws IOException, RestClientException {

		List<StudentMarks> studentMarks = marksRepository.findAll();

		// Fetching data from another API using RestTemplate
		String apiUrl = "http://localhost:8080/api/v1/";
		List<Map<String, Object>> externalData = restTemplate.getForObject(apiUrl, List.class);

		// Mapping external data to match with local data
		List<StudentDto> combinedData = externalData.stream().map(data -> {
			StudentDto dto = new StudentDto();
			dto.setId(String.valueOf(data.get("id")));
			dto.setName(String.valueOf(data.get("name")));
			dto.setPhoneNumber(String.valueOf(data.get("phoneNumber")));
			dto.setDepartment(String.valueOf(data.get("department")));

			// Fetching Marks from DB
			StudentMarks localMarks = studentMarks.stream()
					.filter(mark -> mark.getId().equals(Long.parseLong(dto.getId()))).findFirst().orElse(null);
			if (localMarks != null) {
				dto.setMarks(String.valueOf(localMarks.getMarks()));
			} else {
				dto.setMarks("N/A"); // Handle case where marks are not found
			}

			return dto;
		}).collect(Collectors.toList());


		// Writing combined data to CSV
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		OutputStreamWriter writer = new OutputStreamWriter(outputStream);
		CSVWriter csvWriter = new CSVWriter(writer);

		// Writing headers
		String[] headers = { "ID", "Name", "Phone", "Department", "Marks" };
		csvWriter.writeNext(headers);

		// Writing data rows
		combinedData.forEach(dto -> csvWriter
				.writeNext(new String[] { dto.getId(), dto.getName(), dto.getPhoneNumber(), dto.getDepartment(), dto.getMarks() }));

		csvWriter.close();
		writer.close();

		return outputStream.toByteArray();

	}

}
