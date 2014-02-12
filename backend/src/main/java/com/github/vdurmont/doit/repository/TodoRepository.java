package com.github.vdurmont.doit.repository;

import com.github.vdurmont.doit.model.Project;
import com.github.vdurmont.doit.model.Todo;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Long> {
	public List<Todo> findByProjectId(Long projectId, Sort sort);

	@Query("SELECT p FROM Project p, Todo t WHERE t.projectId = p.id AND t.id = ?1")
	public Project findProject(Long todoId);
}
