package com.github.vdurmont.doit.controller;

import com.github.vdurmont.doit.dto.ProjectDto;
import com.github.vdurmont.doit.dto.ProjectSmallDto;
import com.github.vdurmont.doit.dto.TodoDto;
import com.github.vdurmont.doit.mapper.ProjectMapper;
import com.github.vdurmont.doit.mapper.TodoMapper;
import com.github.vdurmont.doit.model.Project;
import com.github.vdurmont.doit.model.Todo;
import com.github.vdurmont.doit.service.LoginService;
import com.github.vdurmont.doit.service.ProjectService;
import com.github.vdurmont.doit.service.TodoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import java.util.List;

/**
 * CRUD operations on the projects.
 */
@Controller
@RequestMapping("projects")
@Transactional
public class ProjectController {
	private static final Logger logger = LoggerFactory.getLogger(ProjectController.class);

	@Inject
	private LoginService loginService;
	@Inject
	private ProjectMapper projectMapper;
	@Inject
	private ProjectService projectService;
	@Inject
	private TodoMapper todoMapper;
	@Inject
	private TodoService todoService;

	@RequestMapping(value = "", method = RequestMethod.GET)
	@ResponseBody
	public List<ProjectSmallDto> getAll() {
		logger.debug("Got a GetAll request");
		Long currentUserId = this.loginService.getCurrentUserId();
		List<Project> projects = this.projectService.getAllByOwner(currentUserId);
		return this.projectMapper.generateListSmall(currentUserId, projects);
	}

	@RequestMapping(value = "{projectId}", method = RequestMethod.GET)
	@ResponseBody
	public ProjectDto get(@PathVariable Long projectId) {
		logger.debug("Got a get request for the Project#" + projectId);
		Long currentUserId = this.loginService.getCurrentUserId();
		Project project = this.projectService.get(currentUserId, projectId);
		return this.projectMapper.generate(currentUserId, project);
	}

	@RequestMapping(value = "{projectId}/todos", method = RequestMethod.GET)
	@ResponseBody
	public List<TodoDto> getAllByProject(@PathVariable Long projectId) {
		logger.debug("Got a GET request for Todos of the Project#" + projectId);
		Long currentUserId = this.loginService.getCurrentUserId();
		List<Todo> todos = this.todoService.getAllByProject(currentUserId, projectId);
		return this.todoMapper.generateList(todos);
	}
}
