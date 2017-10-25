package fr.mediarollRest.mediarollRest.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
@Inheritance
public abstract class Media {

	@Id
	private int id;
	private String name;
	private String description;
	private String importDate;
	private boolean privateMedia;
	
	@OneToOne
	private User owner;
	
	@OneToMany
	private List<User> SharedPeople;

	public Media(int id, String name, String description, String importDate, User owner) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.importDate = importDate;
		this.privateMedia = true;
		this.owner = owner;
		this.SharedPeople = new ArrayList<User>();		
	}
	
	public Media() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImportDate() {
		return importDate;
	}

	public void setImportDate(String importDate) {
		this.importDate = importDate;
	}

	public boolean isPrivateMedia() {
		return privateMedia;
	}

	public void setPrivateMedia(boolean privateMedia) {
		this.privateMedia = privateMedia;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public List<User> getSharedPeople() {
		return SharedPeople;
	}

	public void setSharedPeople(List<User> sharedPeople) {
		SharedPeople = sharedPeople;
	}
}
