package com.assignment.marks.request;

import com.opencsv.bean.CsvBindByPosition;

public class MarksCsvDto {

	@CsvBindByPosition(position = 0) 
	String id;
	@CsvBindByPosition(position = 1)
	String marks;

	public String getMarks() {
		return marks;
	}

	public void setMarks(String marks) {
		this.marks = marks;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
