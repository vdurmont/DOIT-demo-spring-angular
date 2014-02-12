package com.github.vdurmont.doit.service;

import com.github.vdurmont.doit.AbstractTest;
import com.github.vdurmont.doit.dto.ProjectSmallDto;
import com.github.vdurmont.doit.exception.IllegalInputException;
import com.github.vdurmont.doit.model.Project;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ProjectServiceTest extends AbstractTest {
	private ProjectService projectService;

	private final AuthorizationService authorizationService = mock(AuthorizationService.class);
	private static final Long USER_ID = 42L;

	@Before
	public void setUp() {
		this.projectService = new ProjectService(authorizationService, projectRepository);
	}

	@Test
	public void create_with_null_dto_fails() {
		// GIVEN

		// THEN
		this.expectedEx.expect(IllegalInputException.class);

		// WHEN
		this.projectService.create(USER_ID, null);
	}

	@Test
	public void create_with_null_userId_fails() {
		// GIVEN
		ProjectSmallDto dto = generateProjectDto();

		// THEN
		this.expectedEx.expect(IllegalInputException.class);

		// WHEN
		this.projectService.create(null, dto);
	}

	@Test
	public void create_with_null_name_fails() {
		// GIVEN
		ProjectSmallDto dto = generateProjectDto();
		dto.setName(null);

		// THEN
		this.expectedEx.expect(IllegalInputException.class);

		// WHEN
		this.projectService.create(USER_ID, dto);
	}

	@Test
	public void create_with_empty_name_fails() {
		// GIVEN
		ProjectSmallDto dto = generateProjectDto();
		dto.setName("");

		// THEN
		this.expectedEx.expect(IllegalInputException.class);

		// WHEN
		this.projectService.create(USER_ID, dto);
	}

	@Test
	public void create_creates_the_project() {
		// GIVEN
		DateTime now = DateTime.now();
		setCurrentDate(now);
		ProjectSmallDto dto = generateProjectDto();

		// WHEN
		Project project = this.projectService.create(USER_ID, dto);

		// THEN
		assertNotNull(project.getId());
		assertEquals(now, project.getCreationDate());
		assertEquals(dto.getName(), project.getName());
		assertEquals(USER_ID, project.getOwnerId());
		assertEquals(0, project.getMembersIds().size());
	}

	@Test
	public void create_checks_the_authorizations() {
		// GIVEN
		ProjectSmallDto dto = generateProjectDto();

		// WHEN
		this.projectService.create(USER_ID, dto);

		// THEN
		verify(this.authorizationService).assertIsConnected(USER_ID);
	}

	@Test
	public void getAllByOwner_with_null_userId_fails() {
		// GIVEN

		// THEN
		this.expectedEx.expect(IllegalInputException.class);

		// WHEN
		this.projectService.getAllByOwner(null);
	}

	@Test
	public void getAllByOwner_returns_the_projects_of_the_user_order_by_createdDate_DESC() {
		// GIVEN
		Project p1 = this.generateAndStoreProject(USER_ID);
		Project p2 = this.generateAndStoreProject(USER_ID);
		this.generateAndStoreProject(USER_ID + 1);

		// WHEN
		List<Project> projects = this.projectService.getAllByOwner(USER_ID);

		// THEN
		assertEquals(2, projects.size());
		assertEquals(p2.getId(), projects.get(0).getId());
		assertEquals(p1.getId(), projects.get(1).getId());
	}

	@Test
	public void getAllByOwner_checks_the_authorizations() {
		// GIVEN

		// WHEN
		this.projectService.getAllByOwner(USER_ID);

		// THEN
		verify(this.authorizationService).assertIsConnected(USER_ID);
	}

	@Test
	public void get_with_null_projectId_fails() {
		// GIVEN

		// THEN
		this.expectedEx.expect(IllegalInputException.class);

		// WHEN
		this.projectService.get(USER_ID, null);
	}

	@Test
	public void get_returns_the_project() {
		// GIVEN
		Project project = this.generateAndStoreProject(USER_ID);

		// WHEN
		Project res = this.projectService.get(USER_ID, project.getId());

		// THEN
		assertEquals(project.getId(), res.getId());
		assertEquals(project.getCreationDate(), res.getCreationDate());
		assertEquals(project.getName(), res.getName());
		assertEquals(project.getOwnerId(), res.getOwnerId());
		assertEquals(project.getMembersIds().size(), res.getMembersIds().size());
	}

	@Test
	public void get_checks_the_authorizations() {
		// GIVEN
		Long projectId = this.generateAndStoreProject(USER_ID).getId();

		// WHEN
		this.projectService.get(USER_ID, projectId);

		// THEN
		verify(this.authorizationService).assertCanAccessProject(USER_ID, projectId);
	}

	private Project generateAndStoreProject(long userId) {
		Project project = new Project();
		project.setName(randomString());
		project.setOwnerId(userId);
		return this.projectRepository.save(project);
	}

	private static ProjectSmallDto generateProjectDto() {
		ProjectSmallDto dto = new ProjectSmallDto();
		dto.setName("my project");
		return dto;
	}
}
