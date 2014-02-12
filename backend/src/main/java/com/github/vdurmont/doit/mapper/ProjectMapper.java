package com.github.vdurmont.doit.mapper;

import com.github.vdurmont.doit.dto.ProjectDto;
import com.github.vdurmont.doit.dto.ProjectSmallDto;
import com.github.vdurmont.doit.dto.ProjectStatsDto;
import com.github.vdurmont.doit.dto.UserSmallDto;
import com.github.vdurmont.doit.model.Project;
import com.github.vdurmont.doit.model.User;
import com.github.vdurmont.doit.service.TodoService;
import com.github.vdurmont.doit.service.UserService;

import java.util.ArrayList;
import java.util.List;

public class ProjectMapper {
	private final TodoService todoService;
	private final UserMapper userMapper;
	private final UserService userService;

	public ProjectMapper(TodoService todoService, UserMapper userMapper, UserService userService) {
		this.todoService = todoService;
		this.userMapper = userMapper;
		this.userService = userService;
	}

	public List<ProjectSmallDto> generateListSmall(Long currentUserId, List<Project> projects) {
		List<ProjectSmallDto> dtos = new ArrayList<>();
		for (Project project : projects)
			dtos.add(this.generateSmall(currentUserId, project));
		return dtos;
	}

	public ProjectSmallDto generateSmall(Long currentUserId, Project project) {
		return this.fillSmall(currentUserId, project, new ProjectSmallDto());
	}

	private <T extends ProjectSmallDto> T fillSmall(Long currentUserId, Project project, T dto) {
		dto.setId(project.getId());
		dto.setCreationDate(project.getCreationDate());
		dto.setName(project.getName());

		ProjectStatsDto stats = new ProjectStatsDto();
		stats.setNumMembers(Long.valueOf(project.getMembersIds().size()));
		stats.setNumTasks(this.todoService.countByProject(currentUserId, project.getId()));
		dto.setStats(stats);

		return dto;

	}

	public ProjectDto generate(Long currentUserId, Project project) {
		ProjectDto dto = this.fillSmall(currentUserId, project, new ProjectDto());
		User owner = this.userService.get(project.getOwnerId());
		UserSmallDto ownerDto = this.userMapper.generateSmall(owner);
		dto.setOwner(ownerDto);

		List<UserSmallDto> membersDtos = new ArrayList<>();
		for (Long memberId : project.getMembersIds()) {
			User member = this.userService.get(memberId);
			UserSmallDto memberDto = this.userMapper.generateSmall(member);
			membersDtos.add(memberDto);
		}
		dto.setMembers(membersDtos);
		return dto;
	}
}
