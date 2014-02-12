package com.github.vdurmont.doit.dto;

import java.util.List;

public class ProjectDto extends ProjectSmallDto {
	private UserSmallDto owner;
	private List<UserSmallDto> members;

	public UserSmallDto getOwner() {
		return owner;
	}

	public void setOwner(UserSmallDto owner) {
		this.owner = owner;
	}

	public List<UserSmallDto> getMembers() {
		return members;
	}

	public void setMembers(List<UserSmallDto> members) {
		this.members = members;
	}
}
