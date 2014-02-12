package com.github.vdurmont.doit.dto;

import org.joda.time.DateTime;

public class TodoDto {
	private Long id;
	private DateTime creationDate;
	private UserSmallDto creator;
	private String text;
	private Boolean ticked;

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

	public UserSmallDto getCreator() {
		return creator;
	}

	public void setCreator(UserSmallDto creator) {
		this.creator = creator;
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
