package fr.mediarollRest.mediarollRest.model;

import java.util.List;

import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

public class Album extends Media {

	@OneToOne
	private Picture cover;
	
	@OneToMany
	private List<Media> medias;

	public Album(int id, String name, String description, String importDate, User owner) {
		super(id, name, description, importDate, owner);
	}
	
	public Album(int id, String name, String description, String importDate, User owner, Picture cover, List<Media> medias) {
		super(id, name, description, importDate, owner);
		this.cover=cover;
		this.medias=medias;
	}
	
	public Picture getCover() {
		return cover;
	}

	public void setCover(Picture cover) {
		this.cover = cover;
	}

	public List<Media> getMedias() {
		return medias;
	}

	public void setMedias(List<Media> medias) {
		this.medias = medias;
	}
}
