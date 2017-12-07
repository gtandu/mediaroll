package fr.mediarollRest.mediarollRest.resource;

import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

import fr.mediarollRest.mediarollRest.model.Album;

public class AlbumResource extends ResourceSupport {
	
	@JsonUnwrapped
	private Album album;

	public AlbumResource(Album album) {
		this.album = album;
	}

	public Album getAlbum() {
		return album;
	}	

}
