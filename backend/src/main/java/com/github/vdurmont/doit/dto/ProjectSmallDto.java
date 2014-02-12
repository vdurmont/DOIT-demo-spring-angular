package com.github.vdurmont.doit.dto;

import org.joda.time.DateTime;

public class ProjectSmallDto {
	private Long id;
	private DateTime creationDate;
	private String name;
	private ProjectStatsDto stats;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public DateTime getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(DateTime creationDate) {
		this.creationDate = creationDate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ProjectStatsDto getStats() {
		return stats;
	}

	public void setStats(ProjectStatsDto stats) {
		this.stats = stats;
	}
}
