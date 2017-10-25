package fr.mediarollRest.mediarollRest.model;

import javax.persistence.Entity;

@Entity
public class Picture extends Media {

	public Picture() {
		super();
	}
	
	public Picture(int id, String name, String description, String importDate, User owner) {
		super(id, name, description, importDate, owner);
	}

}