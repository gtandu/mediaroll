package fr.mediarollRest.mediarollRest.web.resource;

import static fr.mediarollRest.mediarollRest.constant.Paths.ACCOUNT_WITH_MAIL;
import static fr.mediarollRest.mediarollRest.constant.Paths.ALBUMS;
import static fr.mediarollRest.mediarollRest.constant.Paths.ALBUM_WITH_ID;
import static fr.mediarollRest.mediarollRest.constant.Paths.COVER;
import static fr.mediarollRest.mediarollRest.constant.Paths.MEDIAS_WITH_ID;

import java.security.Principal;

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

import fr.mediarollRest.mediarollRest.exception.AlbumNotFoundException;
import fr.mediarollRest.mediarollRest.exception.AccountNotFoundException;
import fr.mediarollRest.mediarollRest.exception.MediaNotFoundException;
import fr.mediarollRest.mediarollRest.model.Account;
import fr.mediarollRest.mediarollRest.model.Album;
import fr.mediarollRest.mediarollRest.model.Media;
import fr.mediarollRest.mediarollRest.model.Picture;
import fr.mediarollRest.mediarollRest.service.implementation.AccountService;
import fr.mediarollRest.mediarollRest.service.implementation.AlbumService;
import fr.mediarollRest.mediarollRest.service.implementation.MediaService;

@RestController
public class AlbumResource {

	@Autowired
	private AlbumService albumService;

	@Autowired
	private MediaService mediaService;

	@Autowired
	private AccountService accountService;
	
	@GetMapping(value = ALBUM_WITH_ID)
	public ResponseEntity<Album> getAlbum(@PathVariable("albumId") Long albumId) {

		try {
			Album album = albumService.findAlbumById(albumId);
			return new ResponseEntity<>(album, HttpStatus.OK);
		} catch (AlbumNotFoundException e) {
			return new ResponseEntity<Album>(HttpStatus.NOT_FOUND);

		}
	}

	@PostMapping(value = ALBUMS)
	public ResponseEntity<Album> createAlbum(Principal user, @RequestBody Album album) {
		try {
			Account account = accountService.findByMail(user.getName());
			album.setOwner(account);
			Album albumSaved = albumService.saveAlbum(album);
			return new ResponseEntity<Album>(albumSaved, HttpStatus.OK);
		} catch (AccountNotFoundException e) {
			return new ResponseEntity<Album>(HttpStatus.NOT_FOUND);
		}

	}

	@PatchMapping(value = ALBUM_WITH_ID)
	public ResponseEntity<Album> updateAlbum(@PathVariable("albumId") Long albumId, @RequestBody Album album) {

		try {
			Album albumUpdated = albumService.updateAlbum(albumId, album);
			return new ResponseEntity<Album>(albumUpdated, HttpStatus.CREATED);
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
	public ResponseEntity<Album> addCoverToAlbum(@PathVariable("albumId") Long albumId,
			@PathVariable("mediaId") Long pictureId) {

		try {
			Picture picture = (Picture) mediaService.findById(pictureId);

			Album album = albumService.findAlbumById(albumId);
			album.setCover(picture);
			Album savedAlbum = albumService.saveAlbum(album);
			return new ResponseEntity<Album>(savedAlbum, HttpStatus.CREATED);

		} catch (MediaNotFoundException e) {
			return new ResponseEntity<Album>(HttpStatus.NOT_FOUND);
		} catch (AlbumNotFoundException e) {
			return new ResponseEntity<Album>(HttpStatus.NOT_FOUND);
		}
	}

	@PutMapping(value = ALBUM_WITH_ID + MEDIAS_WITH_ID)
	public ResponseEntity<Album> addMediaToAlbum(@PathVariable("albumId") Long albumId,
			@PathVariable("mediaId") Long mediaId) {

		try {
			Album album = albumService.findAlbumById(albumId);
			Media media = mediaService.findById(mediaId);

			if (album.getMedias().contains(media)) {
				return new ResponseEntity<>(HttpStatus.CONFLICT);
			} else {
				album.getMedias().add(media);
				Album albumSaved = albumService.saveAlbum(album);
				return new ResponseEntity<Album>(albumSaved, HttpStatus.CREATED);
			}

		} catch (AlbumNotFoundException e) {
			return new ResponseEntity<Album>(HttpStatus.NOT_FOUND);
		} catch (MediaNotFoundException e) {
			return new ResponseEntity<Album>(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping(value = ALBUM_WITH_ID + MEDIAS_WITH_ID)
	public ResponseEntity<Album> deleteMediaFromAlbum(@PathVariable("albumId") Long albumId,
			@PathVariable("mediaId") Long mediaId) {

		try {
			Album album = albumService.findAlbumById(albumId);
			Media media = mediaService.findById(mediaId);

			if (album.getMedias().contains(media)) {
				album.getMedias().remove(media);
				Album albumSaved = albumService.saveAlbum(album);
				return new ResponseEntity<Album>(albumSaved, HttpStatus.NO_CONTENT);

			} else {
				return new ResponseEntity<Album>(HttpStatus.NOT_FOUND);
			}
		} catch (AlbumNotFoundException e) {
			return new ResponseEntity<Album>(HttpStatus.NOT_FOUND);
		} catch (MediaNotFoundException e) {
			return new ResponseEntity<Album>(HttpStatus.NOT_FOUND);
		}
	}

	@PutMapping(value = ALBUM_WITH_ID + ACCOUNT_WITH_MAIL)
	public ResponseEntity<Album> addUserToSharedList(Principal principal, @PathVariable("albumId") Long albumId,
			@PathVariable("mail") String mail) {
		
		if(principal.getName().equals(mail)){
			return new ResponseEntity<Album>(HttpStatus.CONFLICT);
		}

		try {
			Album album = albumService.findAlbumById(albumId);
			Account account = accountService.findByMail(mail);
			
			if (album.getSharedPeople().contains(account)) {
				return new ResponseEntity<>(HttpStatus.CONFLICT);
			} else {
				album.getSharedPeople().add(account);
				Album albumSaved = albumService.saveAlbum(album);
				return new ResponseEntity<Album>(albumSaved, HttpStatus.CREATED);
			}
		} catch (AlbumNotFoundException e) {
			return new ResponseEntity<Album>(HttpStatus.NOT_FOUND);
		} catch (AccountNotFoundException e) {
			return new ResponseEntity<Album>(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping(value = ALBUM_WITH_ID + ACCOUNT_WITH_MAIL)
	public ResponseEntity<Album> deleteUserFromSharedList(Principal principal, @PathVariable("albumId") Long albumId,
			@PathVariable("mail") String mail) {
		
		try {
			Album album = albumService.findAlbumById(albumId);
			Account account = accountService.findByMail(mail);
			
			if (album.getSharedPeople().contains(account)) {
				album.getSharedPeople().remove(account);
				Album albumSaved = albumService.saveAlbum(album);
				return new ResponseEntity<Album>(albumSaved, HttpStatus.NO_CONTENT);
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			
		} catch (AlbumNotFoundException e) {
			return new ResponseEntity<Album>(HttpStatus.NOT_FOUND);
		} catch (AccountNotFoundException e) {
			return new ResponseEntity<Album>(HttpStatus.NOT_FOUND);
		}
	}
}
