package com.github.vdurmont.doit.service;

import com.google.common.collect.Lists;
import com.github.vdurmont.doit.AbstractTest;
import com.github.vdurmont.doit.exception.AuthorizationException;
import com.github.vdurmont.doit.exception.IllegalInputException;
import com.github.vdurmont.doit.exception.NoConnectedUserException;
import com.github.vdurmont.doit.exception.ResourceNotFoundException;
import com.github.vdurmont.doit.model.Project;
import com.github.vdurmont.doit.repository.ProjectRepository;
import com.github.vdurmont.doit.repository.TodoRepository;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AuthorizationServiceTest extends AbstractTest {
	private AuthorizationService authorizationService;

	private final ProjectRepository mockProjectRepository = mock(ProjectRepository.class);
	private final TodoRepository mockTodoRepository = mock(TodoRepository.class);

	private static final Long USER_ID = 42L;
	private static final Long PROJECT_ID = 43L;
	private static final Long TODO_ID = 44L;

	@Before
	public void setUp() {
		this.authorizationService = new AuthorizationService(mockProjectRepository, mockTodoRepository);
	}

	@Test
	public void assertCanAccessProject_if_null_userId_fails() {
		// GIVEN

		// THEN
		this.expectedEx.expect(NoConnectedUserException.class);

		// WHEN
		this.authorizationService.assertCanAccessProject(null, PROJECT_ID);
	}

	@Test
	public void assertCanAccessProject_if_null_projectId_fails() {
		// GIVEN

		// THEN
		this.expectedEx.expect(IllegalInputException.class);

		// WHEN
		this.authorizationService.assertCanAccessProject(USER_ID, null);
	}

	@Test
	public void assertCanAccessProject_if_unknown_projectId_fails() {
		// GIVEN

		// THEN
		this.expectedEx.expect(ResourceNotFoundException.class);

		// WHEN
		this.authorizationService.assertCanAccessProject(USER_ID, 846544687L);
	}

	@Test
	public void assertCanAccessProject_if_owner_is_ok() {
		// GIVEN
		Project project = new Project();
		project.setOwnerId(USER_ID);
		when(this.mockProjectRepository.findOne(PROJECT_ID)).thenReturn(project);

		// WHEN
		this.authorizationService.assertCanAccessProject(USER_ID, PROJECT_ID);

		// THEN
		// Nothing happens
	}

	@Test
	public void assertCanAccessProject_if_member_is_ok() {
		// GIVEN
		Project project = new Project();
		project.setOwnerId(498464L);
		project.setMembersIds(Lists.newArrayList(USER_ID));
		when(this.mockProjectRepository.findOne(PROJECT_ID)).thenReturn(project);

		// WHEN
		this.authorizationService.assertCanAccessProject(USER_ID, PROJECT_ID);

		// THEN
		// Nothing happens
	}

	@Test
	public void assertCanAccessProject_if_not_owner_nor_member_fails() {
		// GIVEN
		Project project = new Project();
		project.setOwnerId(498464L);
		when(this.mockProjectRepository.findOne(PROJECT_ID)).thenReturn(project);

		// THEN
		this.expectedEx.expect(AuthorizationException.class);

		// WHEN
		this.authorizationService.assertCanAccessProject(USER_ID, PROJECT_ID);
	}

	@Test
	public void assertIsConnected_if_null_userId_fails() {
		// GIVEN

		// THEN
		this.expectedEx.expect(NoConnectedUserException.class);

		// WHEN
		this.authorizationService.assertIsConnected(null);
	}

	@Test
	public void assertIsConnected_if_not_null_userId_is_ok() {
		// GIVEN

		// WHEN
		this.authorizationService.assertIsConnected(USER_ID);

		// THEN
		// Nothing happens
	}

	@Test
	public void assertCanAccessTodo_if_null_userId_fails() {
		// GIVEN

		// THEN
		this.expectedEx.expect(NoConnectedUserException.class);

		// WHEN
		this.authorizationService.assertCanAccessTodo(null, TODO_ID);
	}

	@Test
	public void assertCanAccessTodo_if_null_todoId_fails() {
		// GIVEN

		// THEN
		this.expectedEx.expect(IllegalInputException.class);

		// WHEN
		this.authorizationService.assertCanAccessTodo(USER_ID, null);
	}

	@Test
	public void assertCanAccessTodo_if_unknown_todoId_fails() {
		// GIVEN

		// THEN
		this.expectedEx.expect(ResourceNotFoundException.class);

		// WHEN
		this.authorizationService.assertCanAccessTodo(USER_ID, TODO_ID);
	}

	@Test
	public void assertCanAccessTodo_if_not_owner_nor_member_of_the_project_fails() {
		// GIVEN
		Project project = new Project();
		project.setOwnerId(5546L);
		when(this.mockTodoRepository.findProject(TODO_ID)).thenReturn(project);

		// THEN
		this.expectedEx.expect(AuthorizationException.class);

		// WHEN
		this.authorizationService.assertCanAccessTodo(USER_ID, TODO_ID);
	}

	@Test
	public void assertCanAccessTodo_if_owner_of_the_project_is_ok() {
		// GIVEN
		Project project = new Project();
		project.setOwnerId(USER_ID);
		when(this.mockTodoRepository.findProject(TODO_ID)).thenReturn(project);

		// WHEN
		this.authorizationService.assertCanAccessTodo(USER_ID, TODO_ID);

		// THEN
		// Nothing happens
	}

	@Test
	public void assertCanAccessTodo_if_member_of_the_project_is_ok() {
		// GIVEN
		Project project = new Project();
		project.setOwnerId(849464L);
		project.setMembersIds(Lists.newArrayList(USER_ID));
		when(this.mockTodoRepository.findProject(TODO_ID)).thenReturn(project);

		// WHEN
		this.authorizationService.assertCanAccessTodo(USER_ID, TODO_ID);

		// THEN
		// Nothing happens
	}
}
