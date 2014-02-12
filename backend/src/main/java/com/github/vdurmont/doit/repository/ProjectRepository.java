package com.github.vdurmont.doit.repository;

import com.github.vdurmont.doit.model.Project;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
	public List<Project> findByOwnerId(Long ownerId, Sort sort);
}
