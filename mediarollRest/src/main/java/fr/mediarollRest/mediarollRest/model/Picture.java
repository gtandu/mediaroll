package fr.mediarollRest.mediarollRest.model;

import java.util.List;

import javax.persistence.Entity;

@Entity
public class Picture extends Media {

	public Picture() {
		super();
	}

	public Picture(String mediaName, String mediaPath, String uploadDate) {
		super(mediaName, mediaPath, uploadDate);
		// TODO Auto-generated constructor stub
	}

	public Picture(int id, String name, String description, String importDate, String filePath, boolean privateMedia,
			Account owner, List<Account> sharedPeople) {
		super(id, name, description, importDate, filePath, privateMedia, owner, sharedPeople);
		// TODO Auto-generated constructor stub
	}

}