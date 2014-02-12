package com.github.vdurmont.doit.service;

import com.github.vdurmont.doit.dto.ProjectSmallDto;
import com.github.vdurmont.doit.dto.UserDto;
import com.github.vdurmont.doit.model.Project;
import com.github.vdurmont.doit.model.User;

/**
 * This service is used to populate the database with some fake data for the demonstrations.
 */
public class PopulateService {
	private final ProjectService projectService;
	private final UserService userService;

	public PopulateService(boolean isActive, ProjectService projectService, UserService userService) {
		this.projectService = projectService;
		this.userService = userService;
		if (isActive)
			populate();
	}

	private void populate() {
		User jobs = this.createUser("steve.jobs@vdurmont.github.com");
		User woz = this.createUser("steve.wozniak@vdurmont.github.com");
		User cook = this.createUser("tim.cook@vdurmont.github.com");

		this.createProject(jobs.getId(), "Apple");
	}

	private Project createProject(Long ownerId, String name) {
		ProjectSmallDto dto = new ProjectSmallDto();
		dto.setName(name);
		return this.projectService.create(ownerId, dto);
	}

	private User createUser(String email) {
		UserDto dto = new UserDto();
		dto.setEmail(email);
		dto.setPassword("password");
		return this.userService.create(dto);
	}
}
