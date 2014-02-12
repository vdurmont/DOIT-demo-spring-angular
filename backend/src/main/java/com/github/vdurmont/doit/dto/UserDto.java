package com.github.vdurmont.doit.dto;

import org.joda.time.DateTime;

public class UserDto extends UserSmallDto {
	private DateTime creationDate;
	private String password;

	public DateTime getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(DateTime creationDate) {
		this.creationDate = creationDate;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
