package com.github.vdurmont.doit.service;

import com.github.vdurmont.doit.AbstractTest;
import com.github.vdurmont.doit.dto.UserDto;
import com.github.vdurmont.doit.exception.IllegalInputException;
import com.github.vdurmont.doit.exception.NotAvailableException;
import com.github.vdurmont.doit.exception.ResourceNotFoundException;
import com.github.vdurmont.doit.model.User;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

import javax.inject.Inject;

import static org.junit.Assert.*;

public class UserServiceTest extends AbstractTest {
	@Inject
	private StandardPasswordEncoder standardPasswordEncoder;

	private UserService userService;

	@Before
	public void setUp() {
		this.userService = new UserService(standardPasswordEncoder, userRepository);
	}

	@Test
	public void create_with_null_dto_fails() {
		// GIVEN

		// THEN
		this.expectedEx.expect(IllegalInputException.class);

		// WHEN
		this.userService.create(null);
	}

	@Test
	public void create_with_null_email_fails() {
		// GIVEN
		UserDto dto = generateUserDto();
		dto.setEmail(null);

		// THEN
		this.expectedEx.expect(IllegalInputException.class);

		// WHEN
		this.userService.create(dto);
	}

	@Test
	public void create_with_empty_email_fails() {
		// GIVEN
		UserDto dto = generateUserDto();
		dto.setEmail("");

		// THEN
		this.expectedEx.expect(IllegalInputException.class);

		// WHEN
		this.userService.create(dto);
	}

	@Test
	public void create_with_invalid_email_fails() {
		// GIVEN
		UserDto dto = generateUserDto();
		dto.setEmail(randomString());

		// THEN
		this.expectedEx.expect(IllegalInputException.class);

		// WHEN
		this.userService.create(dto);
	}

	@Test
	public void create_with_null_password_fails() {
		// GIVEN
		UserDto dto = generateUserDto();
		dto.setPassword(null);

		// THEN
		this.expectedEx.expect(IllegalInputException.class);

		// WHEN
		this.userService.create(dto);
	}

	@Test
	public void create_with_empty_password_fails() {
		// GIVEN
		UserDto dto = generateUserDto();
		dto.setPassword("");

		// THEN
		this.expectedEx.expect(IllegalInputException.class);

		// WHEN
		this.userService.create(dto);
	}

	@Test
	public void create_with_used_email_fails() {
		// GIVEN
		UserDto dto = generateUserDto();

		User user = new User();
		user.setEmail(dto.getEmail());
		user.setPassword(dto.getPassword());
		this.userRepository.save(user);

		// THEN
		this.expectedEx.expect(NotAvailableException.class);

		// WHEN
		this.userService.create(dto);
	}

	@Test
	public void create_creates_the_user() {
		// GIVEN
		DateTime now = DateTime.now();
		setCurrentDate(now);
		UserDto dto = generateUserDto();

		// WHEN
		User user = this.userService.create(dto);

		// THEN
		assertNotNull(user.getId());
		assertEquals(now, user.getCreationDate());
		assertEquals(dto.getEmail(), user.getEmail());
		assertTrue(this.standardPasswordEncoder.matches(dto.getPassword(), user.getPassword()));
	}

	@Test
	public void getByEmail_with_null_email_fails() {
		// GIVEN

		// THEN
		this.expectedEx.expect(IllegalInputException.class);

		// WHEN
		this.userService.getByEmail(null);
	}

	@Test
	public void getByEmail_with_empty_email_fails() {
		// GIVEN

		// THEN
		this.expectedEx.expect(IllegalInputException.class);

		// WHEN
		this.userService.getByEmail("");
	}

	@Test
	public void getByEmail_with_unknown_email_fails() {
		// GIVEN

		// THEN
		this.expectedEx.expect(ResourceNotFoundException.class);

		// WHEN
		this.userService.getByEmail("unknown@github.com");
	}

	@Test
	public void getByEmail_returns_the_user() {
		// GIVEN
		UserDto dto = generateUserDto();
		User user = new User();
		user.setEmail(dto.getEmail());
		user.setPassword(dto.getPassword());
		user = this.userRepository.save(user);

		// WHEN
		User actual = this.userService.getByEmail(user.getEmail());

		// THEN
		assertEquals(user.getId(), actual.getId());
		assertEquals(user.getCreationDate(), actual.getCreationDate());
		assertEquals(user.getEmail(), actual.getEmail());
		assertEquals(user.getPassword(), actual.getPassword());
	}

	@Test
	public void get_with_null_userId_fails() {
		// GIVEN

		// THEN
		this.expectedEx.expect(IllegalInputException.class);

		// WHEN
		this.userService.get(null);
	}

	@Test
	public void get_with_unknown_userId_fails() {
		// GIVEN

		// THEN
		this.expectedEx.expect(ResourceNotFoundException.class);

		// WHEN
		this.userService.get(4298785L);
	}

	@Test
	public void get_returns_the_user() {
		// GIVEN
		UserDto dto = generateUserDto();
		User user = new User();
		user.setEmail(dto.getEmail());
		user.setPassword(dto.getPassword());
		user = this.userRepository.save(user);

		// WHEN
		User actual = this.userService.get(user.getId());

		// THEN
		assertEquals(user.getId(), actual.getId());
		assertEquals(user.getCreationDate(), actual.getCreationDate());
		assertEquals(user.getEmail(), actual.getEmail());
		assertEquals(user.getPassword(), actual.getPassword());
	}

	private static UserDto generateUserDto() {
		UserDto dto = new UserDto();
		dto.setEmail(randomString() + "@github.com");
		dto.setPassword(randomString());
		return dto;
	}
}
