package fr.mediarollRest.mediarollRest.web.resource.assembler;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Service;

import fr.mediarollRest.mediarollRest.model.Album;
import fr.mediarollRest.mediarollRest.resource.AlbumResource;
import fr.mediarollRest.mediarollRest.web.controller.AlbumController;

@Service
public class AlbumAssembler extends ResourceAssemblerSupport<Album, AlbumResource> {
	
	public AlbumAssembler() {
		super(Album.class,AlbumResource.class);
	}

	@Override
	public AlbumResource toResource(Album album) {
		AlbumResource albumResource = new AlbumResource(album);
	    Link selfLink = linkTo(
		        methodOn(AlbumController.class).getAlbum(album.getId()))
		        .withSelfRel();
	    albumResource.add(selfLink);
	    return albumResource;
	}

}
