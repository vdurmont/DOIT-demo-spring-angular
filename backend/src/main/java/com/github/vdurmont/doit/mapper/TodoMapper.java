package com.github.vdurmont.doit.mapper;

import com.github.vdurmont.doit.dto.TodoDto;
import com.github.vdurmont.doit.model.Todo;
import com.github.vdurmont.doit.model.User;
import com.github.vdurmont.doit.service.UserService;

import java.util.ArrayList;
import java.util.List;

public class TodoMapper {
	private final UserMapper userMapper;
	private final UserService userService;

	public TodoMapper(UserMapper userMapper, UserService userService) {
		this.userMapper = userMapper;
		this.userService = userService;
	}

	public TodoDto generate(Todo todo) {
		TodoDto dto = new TodoDto();
		dto.setId(todo.getId());
		dto.setCreationDate(todo.getCreationDate());
		User creator = this.userService.get(todo.getCreatorId());
		dto.setCreator(this.userMapper.generateSmall(creator));
		dto.setText(todo.getText());
		return dto;
	}

	public List<TodoDto> generateList(List<Todo> todos) {
		List<TodoDto> dtos = new ArrayList<>();
		for (Todo todo : todos)
			dtos.add(this.generate(todo));
		return dtos;
	}
}
