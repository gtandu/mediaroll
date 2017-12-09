package fr.mediarollRest.mediarollRest.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import fr.mediarollRest.mediarollRest.security.Authority;

@SuppressWarnings("serial")
@Entity
public class Account implements UserDetails {

	// 1000 MB -> 1GB
	public static final double STORAGE_SPACE_SIZE = 1000.0;
	@Id
	private String mail;
	private String password;
	private String firstname;
	private String lastname;
	private double storageSpace;

	@OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
	private List<Media> mediaList;

	@OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
	private List<Album> albumList;

	@OneToMany(mappedBy = "owner", cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	private List<Media> sharedMedias;

	@OneToMany(mappedBy = "owner", cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	private List<Album> sharedAlbums;

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
		this.albumList = new ArrayList<>();
		this.mediaList = new ArrayList<>();
		this.storageSpace = STORAGE_SPACE_SIZE;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	@JsonIgnore
	public String getPassword() {
		return this.password;
	}

	@JsonProperty
	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	@JsonIgnore
	public List<Media> getMediaList() {
		return mediaList;
	}

	public void setMediaList(List<Media> mediaList) {
		this.mediaList = mediaList;
	}

	@JsonIgnore
	public List<Album> getAlbumList() {
		return albumList;
	}

	public void setAlbumList(List<Album> albumList) {
		this.albumList = albumList;
	}

	@JsonIgnore
	public List<Media> getSharedMedias() {
		return sharedMedias;
	}

	public void setSharedMedias(List<Media> sharedMedias) {
		this.sharedMedias = sharedMedias;
	}

	@JsonIgnore
	public List<Album> getSharedAlbums() {
		return sharedAlbums;
	}

	public void setSharedAlbums(List<Album> sharedAlbums) {
		this.sharedAlbums = sharedAlbums;
	}
	
	public double getStorageSpace() {
		return storageSpace;
	}

	public void setStorageSpace(double storageSpace) {
		this.storageSpace = storageSpace;
	}

	@Override
	@JsonIgnore
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	@JsonIgnore
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
	@JsonIgnore
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
