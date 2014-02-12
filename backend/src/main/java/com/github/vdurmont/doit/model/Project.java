package com.github.vdurmont.doit.model;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.ElementCollection;
import javax.persistence.FetchType;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@javax.persistence.Entity
public class Project extends Entity {
	@NotEmpty
	private String name;
	@NotNull
	private Long ownerId;
	@NotNull
	@ElementCollection(fetch = FetchType.EAGER)
	private List<Long> membersIds;

	public Project() {
		this.setMembersIds(new ArrayList<Long>());
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public List<Long> getMembersIds() {
		return membersIds;
	}

	public void setMembersIds(List<Long> membersIds) {
		this.membersIds = membersIds;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("Project{");
		sb.append("id=").append(getId());
		sb.append(", creationDate=").append(getCreationDate());
		sb.append(", name='").append(name).append('\'');
		sb.append(", ownerId=").append(ownerId);
		sb.append(", membersIds=").append(membersIds);
		sb.append('}');
		return sb.toString();
	}
}
