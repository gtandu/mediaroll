package fr.mediarollRest.mediarollRest.model;

import javax.persistence.Entity;

@Entity
public class Movie extends Media {

	private String duration;

	public Movie() {
		super();
	}
	
	public Movie(int id, String name, String description, String importDate, Account owner, String duration) {
		super(id, name, description, importDate, owner);
		this.duration=duration;
	}
	
	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}
	
}
