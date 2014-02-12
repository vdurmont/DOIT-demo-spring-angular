package com.github.vdurmont.doit.dto;

public class ProjectStatsDto {
	private Long numTasks;
	private Long numMembers;

	public Long getNumTasks() {
		return numTasks;
	}

	public void setNumTasks(Long numTasks) {
		this.numTasks = numTasks;
	}

	public Long getNumMembers() {
		return numMembers;
	}

	public void setNumMembers(Long numMembers) {
		this.numMembers = numMembers;
	}
}
