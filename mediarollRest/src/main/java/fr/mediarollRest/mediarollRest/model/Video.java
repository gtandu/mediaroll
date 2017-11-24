package fr.mediarollRest.mediarollRest.model;

import java.util.List;

import javax.persistence.Entity;

@Entity
public class Video extends Media {

	private String duration;

	public Video() {
		super();
		this.duration = "";
	}

	public Video(String mediaName, String mediaPath, String uploadDate) {
		super(mediaName, mediaPath, uploadDate);
	}

	public Video(Long id, String name, String description, String importDate, String filePath, boolean privateMedia,
			Account owner, List<Account> sharedPeople) {
		super(id, name, description, importDate, filePath, privateMedia, owner, sharedPeople);
		// TODO Auto-generated constructor stub
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

}
