package com.github.vdurmont.doit.model;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

@javax.persistence.Entity
public class Todo extends Entity {
	@NotNull
	private Long projectId;
	@NotNull
	private Long creatorId;
	@NotEmpty
	private String text;
	@NotNull
	private Boolean ticked;

	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	public Long getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(Long creatorId) {
		this.creatorId = creatorId;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Boolean getTicked() {
		return ticked;
	}

	public void setTicked(Boolean ticked) {
		this.ticked = ticked;
	}
}
