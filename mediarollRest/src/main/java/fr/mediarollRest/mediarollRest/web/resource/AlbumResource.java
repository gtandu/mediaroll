package fr.mediarollRest.mediarollRest.web.resource;

import static fr.mediarollRest.mediarollRest.constant.Paths.ALBUMS;
import static fr.mediarollRest.mediarollRest.constant.Paths.ALBUM_WITH_ID;
import static fr.mediarollRest.mediarollRest.constant.Paths.COVER;
import static fr.mediarollRest.mediarollRest.constant.Paths.MAIL;
import static fr.mediarollRest.mediarollRest.constant.Paths.MEDIAS;
import static fr.mediarollRest.mediarollRest.constant.Paths.MEDIAS_WITH_ID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.mediarollRest.mediarollRest.exception.AlbumNotFoundException;
import fr.mediarollRest.mediarollRest.exception.MailNotFoundException;
import fr.mediarollRest.mediarollRest.model.Account;
import fr.mediarollRest.mediarollRest.model.Album;
import fr.mediarollRest.mediarollRest.model.Media;
import fr.mediarollRest.mediarollRest.service.implementation.AccountService;
import fr.mediarollRest.mediarollRest.service.implementation.AlbumService;

@RestController
public class AlbumResource {

	@Autowired
	private AlbumService albumService;
	
	@Autowired
	private AccountService accountService;

	@PostMapping(value = ALBUMS+MAIL)
	public ResponseEntity<Album> createAlbum(@PathVariable("mail") String mail, @RequestBody Album album) {
		try {
			Account account = accountService.findByMail(mail);
			album.setOwner(account);
			Album albumSaved = albumService.saveAlbum(album);
			return new ResponseEntity<Album>(albumSaved, HttpStatus.OK);
		}
		catch (MailNotFoundException e) {
			return new ResponseEntity<Album>(HttpStatus.NOT_FOUND);
		}

	}

	@PutMapping(value = ALBUM_WITH_ID)
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
			if(albumService.deleteAlbum(albumId)) {
				return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
			}
			else
			{
				return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (AlbumNotFoundException e) {
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}
	}

	@PutMapping(value = ALBUM_WITH_ID + COVER)
	public ResponseEntity<Album> addCoverToAlbum(@PathVariable("albumId") Long albumId, @RequestBody Media media) {

		return null;
	}

	@DeleteMapping(value = ALBUM_WITH_ID + COVER)
	public ResponseEntity<Void> deleteCoverToAlbum(@PathVariable("albumId") Long albumId) {

		return null;
	}

	@PostMapping(value = ALBUM_WITH_ID + MEDIAS)
	public ResponseEntity<Album> addMediaToAlbum(@PathVariable("albumId") Long albumId, @RequestBody Media media) {

		return null;
	}

	@DeleteMapping(value = ALBUM_WITH_ID + MEDIAS_WITH_ID)
	public ResponseEntity<Void> deleteMediaFromAlbum(@PathVariable("albumId") Long albumId,
			@PathVariable("mediaId") Long mediaId) {

		return null;
	}

	@PostMapping(value = ALBUM_WITH_ID + MAIL)
	public ResponseEntity<Album> addUserToSharedList(@PathVariable("albumId") Long albumId,
			@PathVariable("mail") String mail) {
		return null;
	}

	@DeleteMapping(value = ALBUM_WITH_ID + MAIL)
	public ResponseEntity<Void> deleteUserFromSharedList(@PathVariable("albumId") Long albumId,
			@PathVariable("mail") String mail) {

		return null;
	}
}
