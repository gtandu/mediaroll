package fr.mediarollRest.mediarollRest.web.resource;

import static fr.mediarollRest.mediarollRest.constant.Paths.ACCOUNT_WITH_MAIL;
import static fr.mediarollRest.mediarollRest.constant.Paths.ALBUMS;
import static fr.mediarollRest.mediarollRest.constant.Paths.ALBUM_WITH_ID;
import static fr.mediarollRest.mediarollRest.constant.Paths.COVER;
import static fr.mediarollRest.mediarollRest.constant.Paths.MEDIAS_WITH_ID;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.mediarollRest.mediarollRest.beans.AlbumResource;
import fr.mediarollRest.mediarollRest.exception.AccountNotFoundException;
import fr.mediarollRest.mediarollRest.exception.AlbumNotFoundException;
import fr.mediarollRest.mediarollRest.exception.MediaNotFoundException;
import fr.mediarollRest.mediarollRest.model.Account;
import fr.mediarollRest.mediarollRest.model.Album;
import fr.mediarollRest.mediarollRest.model.Media;
import fr.mediarollRest.mediarollRest.model.Picture;
import fr.mediarollRest.mediarollRest.service.IAccountService;
import fr.mediarollRest.mediarollRest.service.IAlbumService;
import fr.mediarollRest.mediarollRest.service.IMediaService;
import fr.mediarollRest.mediarollRest.web.resource.assembler.AlbumAssembler;

@RestController
public class AlbumController {

	@Autowired
	private IAlbumService albumService;

	@Autowired
	private IMediaService mediaService;

	@Autowired
	private IAccountService accountService;

	@Autowired
	private AlbumAssembler albumAssembler;

	@GetMapping(value = ALBUM_WITH_ID)
	public ResponseEntity<AlbumResource> getAlbum(@PathVariable("albumId") Long albumId) {

		try {
			Album album = albumService.findAlbumById(albumId);
			AlbumResource albumResource = albumAssembler.toResource(album);
			return new ResponseEntity<>(albumResource, HttpStatus.OK);
		} catch (AlbumNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);

		}
	}

	@GetMapping(ALBUMS)
	public ResponseEntity<List<AlbumResource>> getAllAlbums(Principal principal) {

		
		ArrayList<AlbumResource> albumsResourceList = new ArrayList<>();
		try {
			Account account = accountService.findByMail(principal.getName());
			List<Album> albumsList = albumService.findAlbumByOwner(account);
			for (Album album : albumsList) {
				AlbumResource albumResource = albumAssembler.toResource(album);
				albumsResourceList.add(albumResource);
			}
			return new ResponseEntity<>(albumsResourceList, HttpStatus.OK);
		} catch (AccountNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping(value = ALBUMS)
	public ResponseEntity<AlbumResource> createAlbum(Principal user, @RequestBody Album album) {
		try {
			Account account = accountService.findByMail(user.getName());
			album.setOwner(account);
			Album albumSaved = albumService.saveAlbum(album);
			AlbumResource albumResource = albumAssembler.toResource(albumSaved);
			return new ResponseEntity<AlbumResource>(albumResource, HttpStatus.OK);
		} catch (AccountNotFoundException e) {
			return new ResponseEntity<AlbumResource>(HttpStatus.NOT_FOUND);
		}

	}

	@PatchMapping(value = ALBUM_WITH_ID)
	public ResponseEntity<AlbumResource> updateAlbum(@PathVariable("albumId") Long albumId, @RequestBody Album album) {

		try {
			Album albumUpdated = albumService.updateAlbum(albumId, album);
			AlbumResource albumResource = albumAssembler.toResource(albumUpdated);
			return new ResponseEntity<AlbumResource>(albumResource, HttpStatus.CREATED);
		} catch (AlbumNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping(value = ALBUM_WITH_ID)
	public ResponseEntity<Void> deleteAlbum(@PathVariable("albumId") Long albumId) {

		try {
			if (albumService.deleteAlbum(albumId)) {
				return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
			} else {
				return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (AlbumNotFoundException e) {
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}
	}

	@PutMapping(value = ALBUM_WITH_ID + COVER + MEDIAS_WITH_ID)
	public ResponseEntity<AlbumResource> addCoverToAlbum(@PathVariable("albumId") Long albumId,
			@PathVariable("mediaId") Long pictureId) {

		try {
			Picture picture = (Picture) mediaService.findById(pictureId);
			Album album = albumService.findAlbumById(albumId);
			album.setCover(picture);
			Album savedAlbum = albumService.saveAlbum(album);
			AlbumResource albumResource = albumAssembler.toResource(savedAlbum);
			
			return new ResponseEntity<AlbumResource>(albumResource, HttpStatus.CREATED);

		} catch (MediaNotFoundException e) {
			return new ResponseEntity<AlbumResource>(HttpStatus.NOT_FOUND);
		} catch (AlbumNotFoundException e) {
			return new ResponseEntity<AlbumResource>(HttpStatus.NOT_FOUND);
		}
	}

	@PutMapping(value = ALBUM_WITH_ID + MEDIAS_WITH_ID)
	public ResponseEntity<AlbumResource> addMediaToAlbum(@PathVariable("albumId") Long albumId,
			@PathVariable("mediaId") Long mediaId) {

		try {
			Album album = albumService.findAlbumById(albumId);
			Media media = mediaService.findById(mediaId);

			if (album.getMedias().contains(media)) {
				return new ResponseEntity<>(HttpStatus.CONFLICT);
			} else {
				album.getMedias().add(media);
				Album albumSaved = albumService.saveAlbum(album);
				AlbumResource albumResource = albumAssembler.toResource(albumSaved);
				return new ResponseEntity<AlbumResource>(albumResource, HttpStatus.CREATED);
			}

		} catch (AlbumNotFoundException e) {
			return new ResponseEntity<AlbumResource>(HttpStatus.NOT_FOUND);
		} catch (MediaNotFoundException e) {
			return new ResponseEntity<AlbumResource>(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping(value = ALBUM_WITH_ID + MEDIAS_WITH_ID)
	public ResponseEntity<AlbumResource> deleteMediaFromAlbum(@PathVariable("albumId") Long albumId,
			@PathVariable("mediaId") Long mediaId) {

		try {
			Album album = albumService.findAlbumById(albumId);
			Media media = mediaService.findById(mediaId);

			if (album.getMedias().contains(media)) {
				album.getMedias().remove(media);
				Album albumSaved = albumService.saveAlbum(album);
				AlbumResource albumResource = albumAssembler.toResource(albumSaved);
				return new ResponseEntity<AlbumResource>(albumResource, HttpStatus.NO_CONTENT);

			} else {
				return new ResponseEntity<AlbumResource>(HttpStatus.NOT_FOUND);
			}
		} catch (AlbumNotFoundException e) {
			return new ResponseEntity<AlbumResource>(HttpStatus.NOT_FOUND);
		} catch (MediaNotFoundException e) {
			return new ResponseEntity<AlbumResource>(HttpStatus.NOT_FOUND);
		}
	}

	@PutMapping(value = ALBUM_WITH_ID + ACCOUNT_WITH_MAIL)
	public ResponseEntity<AlbumResource> addUserToSharedList(Principal principal, @PathVariable("albumId") Long albumId,
			@PathVariable("mail") String mail) {

		if (principal.getName().equals(mail)) {
			return new ResponseEntity<AlbumResource>(HttpStatus.CONFLICT);
		}

		try {
			Album album = albumService.findAlbumById(albumId);
			Account account = accountService.findByMail(mail);

			if (album.getSharedPeople().contains(account)) {
				return new ResponseEntity<>(HttpStatus.CONFLICT);
			} else {
				album.getSharedPeople().add(account);
				Album albumSaved = albumService.saveAlbum(album);
				AlbumResource albumResource = albumAssembler.toResource(albumSaved);
				return new ResponseEntity<AlbumResource>(albumResource, HttpStatus.CREATED);
			}
		} catch (AlbumNotFoundException e) {
			return new ResponseEntity<AlbumResource>(HttpStatus.NOT_FOUND);
		} catch (AccountNotFoundException e) {
			return new ResponseEntity<AlbumResource>(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping(value = ALBUM_WITH_ID + ACCOUNT_WITH_MAIL)
	public ResponseEntity<AlbumResource> deleteUserFromSharedList(Principal principal, @PathVariable("albumId") Long albumId,
			@PathVariable("mail") String mail) {

		try {
			Album album = albumService.findAlbumById(albumId);
			Account account = accountService.findByMail(mail);

			if (album.getSharedPeople().contains(account)) {
				album.getSharedPeople().remove(account);
				Album albumSaved = albumService.saveAlbum(album);
				AlbumResource albumResource = albumAssembler.toResource(albumSaved);
				return new ResponseEntity<AlbumResource>(albumResource, HttpStatus.NO_CONTENT);
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}

		} catch (AlbumNotFoundException e) {
			return new ResponseEntity<AlbumResource>(HttpStatus.NOT_FOUND);
		} catch (AccountNotFoundException e) {
			return new ResponseEntity<AlbumResource>(HttpStatus.NOT_FOUND);
		}
	}
}
