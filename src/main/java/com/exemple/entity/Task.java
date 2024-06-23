package com.exemple.entity;

public class Task {
	private String code;
	private String description;
	private String startDate;
	private String endDate;
	private Project project;

	public Task() {
		super();
	}

	public Task(String code, String description, String startDate, String endDate, Project associedProject) {
		super();
		this.code = code;
		this.description = description;
		this.startDate = startDate;
		this.endDate = endDate;
		this.project = associedProject;
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

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

}
