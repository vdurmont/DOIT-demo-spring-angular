package com.github.vdurmont.doit.mapper;

import com.github.vdurmont.doit.dto.UserDto;
import com.github.vdurmont.doit.dto.UserSmallDto;
import com.github.vdurmont.doit.model.User;

public class UserMapper {
	public UserSmallDto generateSmall(User user) {
		return this.fillUserSmall(new UserSmallDto(), user);

	}

	public UserDto generate(User user) {
		UserDto dto = this.fillUserSmall(new UserDto(), user);
		dto.setCreationDate(user.getCreationDate());
		return dto;
	}

	private <T extends UserSmallDto> T fillUserSmall(T dto, User user) {
		dto.setId(user.getId());
		dto.setEmail(user.getEmail());
		return dto;
	}
}
