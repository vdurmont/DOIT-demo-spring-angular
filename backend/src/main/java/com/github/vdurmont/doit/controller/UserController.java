package com.github.vdurmont.doit.controller;

import com.github.vdurmont.doit.dto.UserDto;
import com.github.vdurmont.doit.mapper.UserMapper;
import com.github.vdurmont.doit.model.User;
import com.github.vdurmont.doit.service.LoginService;
import com.github.vdurmont.doit.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;

/**
 * CRUD operations on the users.
 */
@Controller
@RequestMapping("users")
@Transactional
public class UserController {
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Inject
	private LoginService loginService;
	@Inject
	private UserMapper userMapper;
	@Inject
	private UserService userService;

	/**
	 * Returns the current user.
	 */
	@RequestMapping(value = "me", method = RequestMethod.GET)
	@ResponseBody
	public UserDto getMe() {
		logger.debug("Got a GetMe request.");
		Long currentUserId = this.loginService.getCurrentUserId();
		User user = this.userService.get(currentUserId);
		return this.userMapper.generate(user);
	}
}
