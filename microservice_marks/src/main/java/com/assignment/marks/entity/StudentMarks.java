package com.assignment.marks.entity;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity

public class StudentMarks implements Serializable {
 
	/**
	 * 
	 */
	private static final long serialVersionUID = 8648125412892483351L;
	
	@Id
	private Long id;
	private Long marks;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getMarks() {
		return marks;
	}
	public void setMarks(Long marks) {
		this.marks = marks;
	} 
	
}
