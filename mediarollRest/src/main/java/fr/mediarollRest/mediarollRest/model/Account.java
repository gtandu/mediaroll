package fr.mediarollRest.mediarollRest.model;

import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import fr.mediarollRest.mediarollRest.security.Authority;

@SuppressWarnings("serial")
@Entity
public class Account implements UserDetails {
	
	@Id
	private String mail;
	private String password;
	private String firstname;
	private String lastname;
	
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "account")
	private Collection<Authority> authorities;
	
	public Account() {
		super();
	}
	
	public Account(String mail, String password, String firstName, String lastName) {
		super();
		this.mail = mail;
		this.password = password;
		this.firstname = firstName;
		this.lastname = lastName;
	}
	
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getFirstName() {
		return firstname;
	}
	public void setFirstName(String firstName) {
		this.firstname = firstName;
	}
	public String getLastName() {
		return lastname;
	}
	public void setLastName(String lastName) {
		this.lastname = lastName;
	}
	
	@Override
	@JsonIgnore
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}
	@Override
	public String getUsername() {
		return mail;
	}
	@Override
	@JsonIgnore
	public boolean isAccountNonExpired() {
		return true;
	}
	@Override
	@JsonIgnore
	public boolean isAccountNonLocked() {
		return true;
	}
	@Override
	@JsonIgnore
	public boolean isCredentialsNonExpired() {
		return true;
	}
	@Override
	public boolean isEnabled() {
		return false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((mail == null) ? 0 : mail.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Account other = (Account) obj;
		if (mail == null) {
			if (other.mail != null)
				return false;
		} else if (!mail.equals(other.mail))
			return false;
		return true;
	}
	
	
	
}
