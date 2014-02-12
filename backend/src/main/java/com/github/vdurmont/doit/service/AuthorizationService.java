package com.github.vdurmont.doit.service;

import com.github.vdurmont.doit.exception.AuthorizationException;
import com.github.vdurmont.doit.exception.NoConnectedUserException;
import com.github.vdurmont.doit.exception.ResourceNotFoundException;
import com.github.vdurmont.doit.model.Project;
import com.github.vdurmont.doit.repository.ProjectRepository;
import com.github.vdurmont.doit.repository.TodoRepository;
import com.github.vdurmont.doit.tools.Preconditions;

public class AuthorizationService {
	private final ProjectRepository projectRepository;
	private final TodoRepository todoRepository;

	public AuthorizationService(ProjectRepository projectRepository, TodoRepository todoRepository) {
		this.projectRepository = projectRepository;
		this.todoRepository = todoRepository;
	}

	public void assertCanAccessProject(Long currentUserId, Long projectId) {
		Preconditions.checkNotNull(projectId, "projectId");
		Project project = this.projectRepository.findOne(projectId);
		this.assertCanAccessProject(currentUserId, project);
	}

	private void assertCanAccessProject(Long currentUserId, Project project) {
		this.assertIsConnected(currentUserId);
		if (project == null)
			throw new ResourceNotFoundException("The Project cannot be found.");
		if (!project.getOwnerId().equals(currentUserId) && !project.getMembersIds().contains(currentUserId))
			throw new AuthorizationException("You cannot access the Project#" + project.getId());
	}

	public void assertIsConnected(Long currentUserId) {
		if (currentUserId == null)
			throw new NoConnectedUserException();
	}

	public void assertCanAccessTodo(Long currentUserId, Long todoId) {
		this.assertIsConnected(currentUserId);
		Preconditions.checkNotNull(todoId, "todoId");
		Project project = this.todoRepository.findProject(todoId);
		this.assertCanAccessProject(currentUserId, project);
	}
}
