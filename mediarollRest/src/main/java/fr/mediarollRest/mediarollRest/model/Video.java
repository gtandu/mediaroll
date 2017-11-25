package fr.mediarollRest.mediarollRest.model;

import javax.persistence.Entity;

@Entity
public class Video extends Media {

	private String duration;

	public Video() {
		super();
		this.duration = "";
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

}
