package com.exemple.entity;

public class Project {
	private String code;
	private String description;
	private String startDate;

	public Project() {
		super();
	}

	public Project(String code, String description) {
		super();
		this.code = code;
		this.description = description;
	}

	public Project(String code, String description, String startDate) {
		this.code = code;
		this.description = description;
		this.startDate = startDate;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
}