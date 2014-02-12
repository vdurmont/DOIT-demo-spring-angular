package com.github.vdurmont.doit.controller;

import com.github.vdurmont.doit.dto.TodoDto;
import com.github.vdurmont.doit.mapper.TodoMapper;
import com.github.vdurmont.doit.model.Todo;
import com.github.vdurmont.doit.service.LoginService;
import com.github.vdurmont.doit.service.TodoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;

/**
 * CRUD operations on the projects.
 */
@Controller
@RequestMapping(value = "todos")
@Transactional
public class TodoController {
	private static final Logger logger = LoggerFactory.getLogger(TodoController.class);

	@Inject
	private LoginService loginService;
	@Inject
	private TodoMapper todoMapper;
	@Inject
	private TodoService todoService;

	@RequestMapping(value = "{todoId}", method = RequestMethod.PUT)
	@ResponseBody
	public TodoDto get(@PathVariable Long todoId, @RequestBody TodoDto input) {
		logger.debug("Got a PUT request for the Todo#" + todoId + " with input=" + input);
		Long currentUserId = this.loginService.getCurrentUserId();
		Todo todo = this.todoService.update(currentUserId, todoId, input);
		return this.todoMapper.generate(todo);
	}
}
