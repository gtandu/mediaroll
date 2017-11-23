package fr.mediarollRest.mediarollRest.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class Album {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@ManyToOne
	private Account owner;

	@OneToOne
	private Picture cover;

	@OneToMany
	private List<Media> medias;

	public Album() {
		super();
	}

	public Album(Picture cover, List<Media> medias) {
		super();
		this.cover = cover;
		this.medias = medias;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Account getOwner() {
		return owner;
	}

	public void setOwner(Account owner) {
		this.owner = owner;
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
