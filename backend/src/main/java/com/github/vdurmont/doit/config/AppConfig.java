package com.github.vdurmont.doit.config;

import com.github.vdurmont.doit.controller.ExceptionResolver;
import com.github.vdurmont.doit.mapper.ProjectMapper;
import com.github.vdurmont.doit.mapper.TodoMapper;
import com.github.vdurmont.doit.mapper.UserMapper;
import com.github.vdurmont.doit.repository.ProjectRepository;
import com.github.vdurmont.doit.repository.TodoRepository;
import com.github.vdurmont.doit.repository.UserRepository;
import com.github.vdurmont.doit.service.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

import javax.inject.Inject;

/**
 * Main configuration class. Instanciate the services.
 */
@Configuration
@Import({DatabaseConfig.class, SecurityConfig.class})
public class AppConfig {
	@Inject
	private ProjectRepository projectRepository;
	@Inject
	private StandardPasswordEncoder standardPasswordEncoder;
	@Inject
	private TodoRepository todoRepository;
	@Inject
	private UserRepository userRepository;

	@Bean
	public AuthorizationService authorizationService() {
		return new AuthorizationService(projectRepository, todoRepository);
	}

	@Bean
	public ExceptionResolver exceptionResolver() {
		return new ExceptionResolver();
	}

	@Bean
	public PopulateService populateService() {
		return new PopulateService(true, projectService(), userService());
	}

	@Bean
	public ProjectMapper projectMapper() {
		return new ProjectMapper(todoService(), userMapper(), userService());
	}

	@Bean
	public ProjectService projectService() {
		return new ProjectService(authorizationService(), projectRepository);
	}

	@Bean
	public TodoMapper todoMapper() {
		return new TodoMapper(userMapper(), userService());
	}

	@Bean
	public TodoService todoService() {
		return new TodoService(authorizationService(), todoRepository);
	}

	@Bean
	public UserMapper userMapper() {
		return new UserMapper();
	}

	@Bean
	public UserService userService() {
		return new UserService(standardPasswordEncoder, userRepository);
	}
}
