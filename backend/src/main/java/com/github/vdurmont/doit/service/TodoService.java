package com.github.vdurmont.doit.service;

import com.github.vdurmont.doit.dto.TodoDto;
import com.github.vdurmont.doit.exception.ResourceNotFoundException;
import com.github.vdurmont.doit.model.Todo;
import com.github.vdurmont.doit.repository.TodoRepository;
import com.github.vdurmont.doit.tools.Preconditions;
import org.springframework.data.domain.Sort;

import java.util.List;

public class TodoService {
	private final AuthorizationService authorizationService;
	private final TodoRepository todoRepository;

	public TodoService(AuthorizationService authorizationService, TodoRepository todoRepository) {
		this.authorizationService = authorizationService;
		this.todoRepository = todoRepository;
	}

	public List<Todo> getAllByProject(Long currentUserId, Long projectId) {
		this.authorizationService.assertCanAccessProject(currentUserId, projectId);
		Preconditions.checkNotNull(projectId, "projectId");
		Sort sort = new Sort(Sort.Direction.DESC, "creationDate");
		return this.todoRepository.findByProjectId(projectId, sort);
	}

	public Todo create(Long currentUserId, Long projectId, TodoDto dto) {
		this.authorizationService.assertCanAccessProject(currentUserId, projectId);
		Preconditions.checkNotNull(currentUserId, "userId");
		Preconditions.checkNotNull(projectId, "projectId");
		Preconditions.checkNotNull(dto, "todo");
		Preconditions.checkNotEmpty(dto.getText(), "text");
		Todo todo = new Todo();
		todo.setCreatorId(currentUserId);
		todo.setProjectId(projectId);
		todo.setText(dto.getText());
		todo.setTicked(dto.getTicked() == null ? false : dto.getTicked());
		return this.todoRepository.save(todo);
	}

	public Todo update(Long currentUserId, Long todoId, TodoDto dto) {
		this.authorizationService.assertCanAccessTodo(currentUserId, todoId);
		Preconditions.checkNotNull(dto, "todo");
		Preconditions.checkNotEmpty(dto.getText(), "text");
		Preconditions.checkNotNull(dto.getTicked(), "ticked");
		Todo todo = this.get(todoId);
		todo.setText(dto.getText());
		todo.setTicked(dto.getTicked());
		return this.todoRepository.save(todo);
	}

	private Todo get(Long todoId) {
		Preconditions.checkNotNull(todoId, "todoId");
		Todo todo = this.todoRepository.findOne(todoId);
		if (todo == null)
			throw new ResourceNotFoundException("The Todo#" + todoId + " cannot be found.");
		return todo;
	}

	public Long countByProject(Long currentUserId, Long projectId) {
		return null;
	}
}
