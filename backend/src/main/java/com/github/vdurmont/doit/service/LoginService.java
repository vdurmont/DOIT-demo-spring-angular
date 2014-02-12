package com.github.vdurmont.doit.service;

import com.github.vdurmont.doit.model.User;
import com.github.vdurmont.doit.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class LoginService implements UserDetailsService {
	private static final Logger logger = LoggerFactory.getLogger(LoginService.class);

	private final UserRepository userRepository;

	public LoginService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
		logger.debug("Try to authenticate with email=" + email);
		User user = this.userRepository.findByEmail(email);
		if (user == null) {
			logger.debug("The user with email=" + email + " was not found.");
			throw new UsernameNotFoundException("The User with email '" + email + "' cannot be found.");
		}
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
		return new org.springframework.security.core.userdetails.User(email, user.getPassword(), authorities);
	}

	public Long getCurrentUserId() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof String) {
			logger.debug("There is no current connected User.");
			return null;
		} else {
			User user = this.userRepository.findByEmail(((UserDetails) principal).getUsername());
			logger.debug("The current connected User is #" + user.getId() + " " + user.getEmail() + ".");
			return user.getId();
		}
	}
}
