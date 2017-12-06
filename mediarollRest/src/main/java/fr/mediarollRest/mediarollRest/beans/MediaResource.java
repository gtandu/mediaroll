package fr.mediarollRest.mediarollRest.beans;

import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

import fr.mediarollRest.mediarollRest.model.Media;

public class MediaResource extends ResourceSupport {
	
	@JsonUnwrapped
	private Media media;
	
	public MediaResource(Media media) {
		this.media = media;
	}
	
	public Media getMedia() {
		return this.media;
	}

}
