package com.github.vdurmont.doit.model;

import org.joda.time.DateTime;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@MappedSuperclass
public class Entity {
	@Id
	@GeneratedValue
	private Long id;
	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	private Date creationDate;

	public Entity() {
		this.setCreationDate(DateTime.now());
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public DateTime getCreationDate() {
		if (creationDate == null)
			return null;
		return new DateTime(creationDate);
	}

	public void setCreationDate(DateTime creationDate) {
		if (creationDate == null)
			this.creationDate = null;
		else
			this.creationDate = creationDate.toDate();
	}

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer("Entity{");
		sb.append("id=").append(id);
		sb.append(", creationDate=").append(creationDate);
		sb.append('}');
		return sb.toString();
	}
}
