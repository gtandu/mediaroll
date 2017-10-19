package fr.mediarollRest.mediarollRest.security;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.springframework.security.core.GrantedAuthority;

import fr.mediarollRest.mediarollRest.model.User;

@Entity
public class Authority implements GrantedAuthority {


	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private User user;

	private String role;

	public Authority() {
	}

	public Authority(String role) {
		super();
		this.role = role;
	}

	public Authority(User account, String role) {
		super();
		this.user = account;
		this.role = role;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public User getAccount() {
		return user;
	}

	public void setAccount(User account) {
		this.user = account;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	@Override
	public String getAuthority() {
		return this.role;
	}

}
