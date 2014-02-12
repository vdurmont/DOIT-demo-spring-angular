package com.github.vdurmont.doit.service;

import com.github.vdurmont.doit.AbstractTest;
import com.github.vdurmont.doit.dto.TodoDto;
import com.github.vdurmont.doit.exception.IllegalInputException;
import com.github.vdurmont.doit.model.Todo;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class TodoServiceTest extends AbstractTest {
	private TodoService todoService;

	private final AuthorizationService authorizationService = mock(AuthorizationService.class);
	private static final Long USER_ID = 42L;
	private static final Long PROJECT_ID = 43L;

	@Before
	public void setUp() {
		this.todoService = new TodoService(authorizationService, todoRepository);
	}

	@Test
	public void create_with_null_userId_fails() {
		// GIVEN
		TodoDto dto = generateTodoDto();

		// THEN
		this.expectedEx.expect(IllegalInputException.class);

		// WHEN
		this.todoService.create(null, PROJECT_ID, dto);
	}

	@Test
	public void create_with_null_projectId_fails() {
		// GIVEN
		TodoDto dto = generateTodoDto();

		// THEN
		this.expectedEx.expect(IllegalInputException.class);

		// WHEN
		this.todoService.create(USER_ID, null, dto);
	}

	@Test
	public void create_with_null_dto_fails() {
		// GIVEN

		// THEN
		this.expectedEx.expect(IllegalInputException.class);

		// WHEN
		this.todoService.create(USER_ID, PROJECT_ID, null);
	}

	@Test
	public void create_with_null_dto_text_fails() {
		// GIVEN
		TodoDto dto = generateTodoDto();
		dto.setText(null);

		// THEN
		this.expectedEx.expect(IllegalInputException.class);

		// WHEN
		this.todoService.create(USER_ID, PROJECT_ID, dto);
	}

	@Test
	public void create_with_empty_dto_text_fails() {
		// GIVEN
		TodoDto dto = generateTodoDto();
		dto.setText("");

		// THEN
		this.expectedEx.expect(IllegalInputException.class);

		// WHEN
		this.todoService.create(USER_ID, PROJECT_ID, dto);
	}

	@Test
	public void create_with_a_tick_defines_the_tick() {
		// GIVEN
		DateTime now = DateTime.now();
		setCurrentDate(now);
		TodoDto dto = generateTodoDto();

		// WHEN
		Todo todo = this.todoService.create(USER_ID, PROJECT_ID, dto);

		// THEN
		assertEquals(dto.getTicked(), todo.getTicked());
	}

	@Test
	public void create_with_no_tick_sets_the_tick_to_false() {
		// GIVEN
		DateTime now = DateTime.now();
		setCurrentDate(now);
		TodoDto dto = generateTodoDto();
		dto.setTicked(null);

		// WHEN
		Todo todo = this.todoService.create(USER_ID, PROJECT_ID, dto);

		// THEN
		assertFalse(todo.getTicked());
	}

	@Test
	public void create_creates_and_returns_the_todo() {
		// GIVEN
		DateTime now = DateTime.now();
		setCurrentDate(now);
		TodoDto dto = generateTodoDto();

		// WHEN
		Todo todo = this.todoService.create(USER_ID, PROJECT_ID, dto);

		// THEN
		assertNotNull(todo.getId());
		assertEquals(now, todo.getCreationDate());
		assertEquals(PROJECT_ID, todo.getProjectId());
		assertEquals(USER_ID, todo.getCreatorId());
		assertEquals(dto.getText(), todo.getText());
		assertEquals(dto.getTicked(), todo.getTicked());
	}

	@Test
	public void create_checks_the_authorizations() {
		// GIVEN
		TodoDto dto = generateTodoDto();

		// WHEN
		this.todoService.create(USER_ID, PROJECT_ID, dto);

		// THEN
		verify(this.authorizationService).assertCanAccessProject(USER_ID, PROJECT_ID);
	}

	@Test
	public void getAllByProject_with_null_projectId_fails() {
		// GIVEN

		// THEN
		this.expectedEx.expect(IllegalInputException.class);

		// WHEN
		this.todoService.getAllByProject(USER_ID, null);
	}

	@Test
	public void getAllByProject_returns_the_todo_of_the_project() {
		// GIVEN
		Todo todo1 = this.generateAndStoreTodo(PROJECT_ID);
		Todo todo2 = this.generateAndStoreTodo(PROJECT_ID);
		this.generateAndStoreTodo(55345454L);

		// WHEN
		List<Todo> todos = this.todoService.getAllByProject(USER_ID, PROJECT_ID);

		// THEN
		assertEquals(2, todos.size());
		assertEquals(todo2.getId(), todos.get(0).getId());
		assertEquals(todo1.getId(), todos.get(1).getId());
	}

	@Test
	public void getAllByProject_checks_the_authorizations() {
		// GIVEN

		// WHEN
		this.todoService.getAllByProject(USER_ID, PROJECT_ID);

		// THEN
		verify(this.authorizationService).assertCanAccessProject(USER_ID, PROJECT_ID);
	}

	@Test
	public void update_with_null_todoId_fails() {
		// GIVEN
		TodoDto dto = generateTodoDto();

		// THEN
		this.expectedEx.expect(IllegalInputException.class);

		// WHEN
		this.todoService.update(USER_ID, null, dto);
	}

	@Test
	public void update_with_null_dto_fails() {
		// GIVEN
		Todo todo = this.generateAndStoreTodo(PROJECT_ID);

		// THEN
		this.expectedEx.expect(IllegalInputException.class);

		// WHEN
		this.todoService.update(USER_ID, todo.getId(), null);
	}

	@Test
	public void update_with_null_dto_text_fails() {
		// GIVEN
		Todo todo = this.generateAndStoreTodo(PROJECT_ID);
		TodoDto dto = generateTodoDto();
		dto.setText(null);

		// THEN
		this.expectedEx.expect(IllegalInputException.class);

		// WHEN
		this.todoService.update(USER_ID, todo.getId(), dto);
	}

	@Test
	public void update_with_empty_dto_text_fails() {
		// GIVEN
		Todo todo = this.generateAndStoreTodo(PROJECT_ID);
		TodoDto dto = generateTodoDto();
		dto.setText("");

		// THEN
		this.expectedEx.expect(IllegalInputException.class);

		// WHEN
		this.todoService.update(USER_ID, todo.getId(), dto);
	}

	@Test
	public void update_updates_and_returns_the_todo() {
		// GIVEN
		Todo todo = this.generateAndStoreTodo(PROJECT_ID);
		TodoDto dto = generateTodoDto();
		dto.setTicked(!todo.getTicked());

		// WHEN
		Todo res = this.todoService.update(USER_ID, todo.getId(), dto);

		// THEN
		assertEquals(todo.getId(), res.getId());
		assertEquals(todo.getCreationDate(), res.getCreationDate());
		assertEquals(todo.getCreatorId(), res.getCreatorId());
		assertEquals(todo.getProjectId(), res.getProjectId());
		assertEquals(dto.getTicked(), res.getTicked());
		assertEquals(dto.getText(), res.getText());
	}

	@Test
	public void update_checks_the_authorizations() {
		// GIVEN
		Todo todo = this.generateAndStoreTodo(PROJECT_ID);
		TodoDto dto = generateTodoDto();

		// WHEN
		this.todoService.update(USER_ID, todo.getId(), dto);

		// THEN
		verify(this.authorizationService).assertCanAccessTodo(USER_ID, todo.getId());
	}

	@Test
	public void countByProject_with_null_projectId_fails() {
		// GIVEN

		// THEN
		this.expectedEx.expect(IllegalInputException.class);

		// WHEN
		this.todoService.countByProject(USER_ID, null);
	}

	@Test
	public void countByProject_counts_the_todos_of_the_project() {
		// GIVEN
		this.generateAndStoreTodo(PROJECT_ID);
		this.generateAndStoreTodo(PROJECT_ID);
		this.generateAndStoreTodo(544457L);

		// WHEN
		Long count = this.todoService.countByProject(USER_ID, null);

		// THEN
		assertEquals(new Long(2), count);
	}

	@Test
	public void countByProject_checks_the_authorizations() {
		// GIVEN

		// WHEN
		this.todoService.countByProject(USER_ID, PROJECT_ID);

		// THEN
		verify(this.authorizationService).assertCanAccessProject(USER_ID, PROJECT_ID);
	}

	private static TodoDto generateTodoDto() {
		TodoDto dto = new TodoDto();
		dto.setText(randomString());
		dto.setTicked(true);
		return dto;
	}

	private Todo generateAndStoreTodo(Long projectId) {
		Todo todo = new Todo();
		todo.setText(randomString());
		todo.setTicked(randomBoolean());
		todo.setProjectId(projectId);
		todo.setCreatorId(USER_ID);
		return this.todoRepository.save(todo);
	}
}
