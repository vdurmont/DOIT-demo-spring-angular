package com.github.vdurmont.doit.model;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.Table;

@javax.persistence.Entity
@Table(name = "DoitUser") // Because "User" is often a reserved name
public class User extends Entity {
	@NotEmpty
	@Email
	@Column(unique = true)
	private String email;
	@NotEmpty
	private String password;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer("User{");
		sb.append("id=").append(getId());
		sb.append(", creationDate=").append(getCreationDate());
		sb.append(", email='").append(email).append('\'');
		if (password == null)
			sb.append(", password=null");
		else
			sb.append(", password='••••••••••'");
		sb.append('}');
		return sb.toString();
	}
}
