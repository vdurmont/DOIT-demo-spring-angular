package com.github.vdurmont.doit.repository;

import com.github.vdurmont.doit.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
	public User findByEmail(String email);
}
