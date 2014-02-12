package com.github.vdurmont.doit.service;

import com.github.vdurmont.doit.dto.ProjectSmallDto;
import com.github.vdurmont.doit.exception.ResourceNotFoundException;
import com.github.vdurmont.doit.model.Project;
import com.github.vdurmont.doit.repository.ProjectRepository;
import com.github.vdurmont.doit.tools.Preconditions;
import org.springframework.data.domain.Sort;

import java.util.List;

public class ProjectService {
	private final AuthorizationService authorizationService;
	private final ProjectRepository projectRepository;

	public ProjectService(AuthorizationService authorizationService, ProjectRepository projectRepository) {
		this.authorizationService = authorizationService;
		this.projectRepository = projectRepository;
	}

	public List<Project> getAllByOwner(Long currentUserId) {
		this.authorizationService.assertIsConnected(currentUserId);
		Preconditions.checkNotNull(currentUserId, "userId");
		Sort sort = new Sort(Sort.Direction.DESC, "creationDate");
		return this.projectRepository.findByOwnerId(currentUserId, sort);
	}

	public Project create(Long currentUserId, ProjectSmallDto dto) {
		this.authorizationService.assertIsConnected(currentUserId);
		Preconditions.checkNotNull(currentUserId, "userId");
		Preconditions.checkNotNull(dto, "project");
		Preconditions.checkNotEmpty(dto.getName(), "name");
		Project project = new Project();
		project.setName(dto.getName());
		project.setOwnerId(currentUserId);
		return this.projectRepository.save(project);
	}

	public Project get(Long currentUserId, Long projectId) {
		this.authorizationService.assertCanAccessProject(currentUserId, projectId);
		Preconditions.checkNotNull(projectId, "projectId");
		Project project = this.projectRepository.findOne(projectId);
		if (project == null)
			throw new ResourceNotFoundException("The Project#" + projectId + " cannot be found.");
		return project;
	}
}
