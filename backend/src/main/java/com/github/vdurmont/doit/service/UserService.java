package com.github.vdurmont.doit.service;

import com.github.vdurmont.doit.dto.UserDto;
import com.github.vdurmont.doit.exception.NotAvailableException;
import com.github.vdurmont.doit.exception.ResourceNotFoundException;
import com.github.vdurmont.doit.model.User;
import com.github.vdurmont.doit.repository.UserRepository;
import com.github.vdurmont.doit.tools.Preconditions;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

public class UserService {
	private final StandardPasswordEncoder standardPasswordEncoder;
	private final UserRepository userRepository;

	public UserService(StandardPasswordEncoder standardPasswordEncoder, UserRepository userRepository) {
		this.standardPasswordEncoder = standardPasswordEncoder;
		this.userRepository = userRepository;
	}

	public User getByEmail(String email) {
		Preconditions.checkNotEmpty(email, "email");
		User user = this.userRepository.findByEmail(email);
		if (user == null)
			throw new ResourceNotFoundException("The User with email '" + email + "' cannot be found.");
		return user;
	}

	public User create(UserDto dto) {
		Preconditions.checkNotNull(dto, "user");
		Preconditions.checkEmail(dto.getEmail(), "email");
		Preconditions.checkNotEmpty(dto.getPassword(), "password");
		try {
			this.getByEmail(dto.getEmail());
			throw new NotAvailableException("The email '" + dto.getEmail() + "' is not available.");
		} catch (ResourceNotFoundException e) {
			User user = new User();
			user.setEmail(dto.getEmail());
			String encoded = this.standardPasswordEncoder.encode(dto.getPassword());
			user.setPassword(encoded);
			return this.userRepository.save(user);
		}
	}

	public User get(Long userId) {
		Preconditions.checkNotNull(userId, "userId");
		User user = this.userRepository.findOne(userId);
		if (user == null)
			throw new ResourceNotFoundException("The User#" + userId + " cannot be found.");
		return user;
	}
}
