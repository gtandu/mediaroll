package fr.mediarollRest.mediarollRest.web.controller;

import static fr.mediarollRest.mediarollRest.constant.Paths.ACCOUNT_WITH_MAIL;
import static fr.mediarollRest.mediarollRest.constant.Paths.MEDIAS;
import static fr.mediarollRest.mediarollRest.constant.Paths.MEDIAS_WITH_ID;
import static fr.mediarollRest.mediarollRest.constant.Paths.MEDIA_ID;
import static fr.mediarollRest.mediarollRest.constant.Paths.PICTURES;
import static fr.mediarollRest.mediarollRest.constant.Paths.VIDEOS;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.util.List;
import java.util.Locale;

import org.apache.commons.io.IOUtils;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.config.AmazonConfigClient;

import fr.mediarollRest.mediarollRest.exception.AccountExistInSharedListOfMediaException;
import fr.mediarollRest.mediarollRest.exception.AccountNotExistInSharedListOfMediaException;
import fr.mediarollRest.mediarollRest.exception.AccountNotFoundException;
import fr.mediarollRest.mediarollRest.exception.MediaNotFoundException;
import fr.mediarollRest.mediarollRest.exception.SpaceAvailableNotEnoughException;
import fr.mediarollRest.mediarollRest.model.Account;
import fr.mediarollRest.mediarollRest.model.Media;
import fr.mediarollRest.mediarollRest.model.Picture;
import fr.mediarollRest.mediarollRest.model.Video;
import fr.mediarollRest.mediarollRest.service.implementation.AccountService;
import fr.mediarollRest.mediarollRest.service.implementation.AmazonClient;
import fr.mediarollRest.mediarollRest.service.implementation.MediaManagerService;
import fr.mediarollRest.mediarollRest.service.implementation.MediaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api(value = "medias", description = "Operations pertaining to medias in MediaRoll")
public class MediaController {

	@Autowired
	private AccountService accountService;

	@Autowired
	private MediaService mediaService;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private AmazonClient amazonClient;

	private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

	@ApiOperation(value = "Get media info")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully retrieved media"),
			@ApiResponse(code = 404, message = "The media is not found.") })
	@GetMapping(MEDIAS_WITH_ID)
	public ResponseEntity<Media> getMediaById(Principal principal, @PathVariable("mediaId") String mediaId) {

		try {
			Media media = mediaService.findById(mediaId);
			buildLink(principal, media);
			return new ResponseEntity<Media>(media, HttpStatus.OK);
		} catch (MediaNotFoundException e) {
			logger.error(messageSource.getMessage("error.media.not.found", null, Locale.FRANCE), mediaId);
			return new ResponseEntity<Media>(HttpStatus.NOT_FOUND);
		}
	}

	@ApiOperation(value = "Get all medias")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully retrieved list"),
			@ApiResponse(code = 404, message = "The account/media is not found.") })
	@GetMapping(MEDIAS)
	public ResponseEntity<List<Media>> getAllMedias(Principal principal) {
		String mail = principal.getName();
		try {
			Account account = accountService.findByMail(mail);
			List<Media> mediaList = account.getMediaList();
			for (Media media : mediaList) {
				buildLink(principal, media);
			}
			return new ResponseEntity<>(mediaList, HttpStatus.OK);
		} catch (AccountNotFoundException e) {
			logger.error(messageSource.getMessage("error.account.not.found", null, Locale.FRANCE), mail);
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

	}

	@ApiOperation(value = "Update media info")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully updated."),
			@ApiResponse(code = 404, message = "The media is not found. Check ID.") })
	@PutMapping(value = MEDIAS + MEDIA_ID)
	public ResponseEntity<Media> updateMediaInfo(Principal principal, @PathVariable("mediaId") String mediaId,
			@RequestBody Media media) {

		try {
			Media mediaUpdated = mediaService.updateMediaInfo(mediaId, media);
			buildLink(principal, media);
			return new ResponseEntity<Media>(mediaUpdated, HttpStatus.OK);
		} catch (MediaNotFoundException e) {
			logger.error(messageSource.getMessage("error.media.not.found", null, Locale.FRANCE), mediaId);
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

	}

	@ApiOperation(value = "Get media stream")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully retrieved media's stream."),
			@ApiResponse(code = 404, message = "The media is not found. Check ID."),
			@ApiResponse(code = 500, message = "An error occured during the process") })
	@GetMapping(value = MEDIAS_WITH_ID + "/response")
	public ResponseEntity<byte[]> getMediaAsResponseEntity(@PathVariable("mediaId") String mediaId) {
		HttpHeaders headers = new HttpHeaders();

		byte[] media;
		try {
			Media mediaFromDb = mediaService.findById(mediaId);
			InputStream in = amazonClient.getInputStreamFromMedia(mediaFromDb.getKeyS3());
			media = IOUtils.toByteArray(in);
			headers.setCacheControl(CacheControl.noCache().getHeaderValue());
			ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(media, headers, HttpStatus.OK);
			return responseEntity;
		} catch (IOException e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (MediaNotFoundException e) {
			logger.error(messageSource.getMessage("error.media.not.found", null, Locale.FRANCE), mediaId);
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

	}

	@ApiOperation(value = "Upload a media")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Successfully upload."),
			@ApiResponse(code = 400, message = "The file is not a media."),
			@ApiResponse(code = 404, message = "Account is not found in Db."),
			@ApiResponse(code = 500, message = "Attempt to save file in file system failed."),
			@ApiResponse(code = 507, message = "Insufficient storage space in system") })
	@PostMapping(value = MEDIAS)
	@ResponseBody
	public ResponseEntity<Media> uploadMedias(@RequestParam("file") MultipartFile media, Principal principal)
			throws IOException {

		Media mediaToSave = null;
		Account account = null;
		String mail = principal.getName();

		if (mediaService.isMedia(media)) {

			try {
				account = accountService.findByMail(mail);
				mediaToSave = amazonClient.uploadFile(account, media);
				mediaToSave.setOwner(account);
				account.getMediaList().add(mediaToSave);
				Media mediaSaved = mediaService.saveMedia(mediaToSave);
				buildLink(principal, mediaSaved);
				return new ResponseEntity<Media>(mediaSaved, HttpStatus.CREATED);

			} catch (AccountNotFoundException e) {
				logger.error(messageSource.getMessage("error.account.not.found", null, Locale.FRANCE), mail);
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			} catch (IOException e) {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			} catch (FileUploadException e) {
				logger.error(messageSource.getMessage("error.upload.media", null, Locale.FRANCE),
						media.getOriginalFilename());
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			} catch (SpaceAvailableNotEnoughException e) {
				logger.error(messageSource.getMessage("error.storage.space", null, Locale.FRANCE), account.getMail(),
						account.getStorageSpace());
				return new ResponseEntity<>(HttpStatus.INSUFFICIENT_STORAGE);
			}
		}

		else {
			logger.error(messageSource.getMessage("error.upload.media.type", null, Locale.FRANCE),
					media.getOriginalFilename());
			return new ResponseEntity<>(HttpStatus.UNSUPPORTED_MEDIA_TYPE);
		}

	}

	@ApiOperation(value = "Delete a media")
	@ApiResponses(value = { @ApiResponse(code = 204, message = "Media successfully deleted"),
			@ApiResponse(code = 404, message = "Account/Media not found in db. Please check media ID"),
			@ApiResponse(code = 500, message = "An error occured during the process to delete Media"), })
	@DeleteMapping(value = MEDIAS_WITH_ID)
	public ResponseEntity<Void> deleteMedia(Principal principal, @PathVariable("mediaId") String mediaId) {
		try {
			Media mediaInDb = mediaService.findById(mediaId);
			Account account = accountService.findByMail(principal.getName());
			boolean isDeleteFromFileSystem = amazonClient.deleteFileFromS3Bucket(account, mediaInDb.getKeyS3());
			mediaService.deleteMediaById(mediaId);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);

		} catch (MediaNotFoundException e) {
			logger.error(messageSource.getMessage("error.media.not.found", null, Locale.FRANCE), mediaId);
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		} catch (AccountNotFoundException e) {
			logger.error(messageSource.getMessage("error.account.not.found", null, Locale.FRANCE), principal.getName());
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}

	}

	@ApiOperation(value = "Get all pictures from user")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully retrieved user's pictures.") })
	@GetMapping(value = PICTURES)
	public ResponseEntity<List<Picture>> getAllPictures(Principal principal) {

		List<Picture> picturesList;
		try {
			picturesList = mediaService.getAllPictures(principal.getName());
			for (Picture picture : picturesList) {
				buildLink(principal, picture);
			}
			return new ResponseEntity<List<Picture>>(picturesList, HttpStatus.OK);
		} catch (AccountNotFoundException e) {
			logger.error(messageSource.getMessage("error.account.not.found", null, Locale.FRANCE), principal.getName());
		}
		return null;

	}

	@ApiOperation(value = "Get all videos from user")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully retrieved user's videos.") })
	@GetMapping(value = VIDEOS)
	public ResponseEntity<List<Video>> getAllVideos(Principal principal) {

		List<Video> videosList;
		try {
			videosList = mediaService.getAllVideos(principal.getName());
			for (Video video : videosList) {
				buildLink(principal, video);
			}
			return new ResponseEntity<List<Video>>(videosList, HttpStatus.OK);
		} catch (AccountNotFoundException e) {
			logger.error(messageSource.getMessage("error.account.not.found", null, Locale.FRANCE), principal.getName());

		}
		return null;

	}

	@PostMapping(MEDIAS_WITH_ID + ACCOUNT_WITH_MAIL)
	public ResponseEntity<Media> addUserToSharedList(@PathVariable("mediaId") String mediaId,
			@PathVariable("mail") String mail, Principal principal) {
		try {
			Media media = mediaService.addUserToSharedList(mail, mediaId);
			buildLink(principal, media);
			return new ResponseEntity<>(media, HttpStatus.OK);
		} catch (AccountExistInSharedListOfMediaException e) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		} catch (AccountNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (MediaNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping(MEDIAS_WITH_ID + ACCOUNT_WITH_MAIL)
	public ResponseEntity<Media> removeUserToSharedList(@PathVariable("mediaId") String mediaId,
			@PathVariable("mail") String mail, Principal principal) {
		try {
			Media media = mediaService.removeUserFromSharedList(mail, mediaId);
			buildLink(principal, media);
			return new ResponseEntity<>(media, HttpStatus.NO_CONTENT);
		} catch (AccountNotExistInSharedListOfMediaException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (AccountNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (MediaNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	private void buildLink(Principal principal, Media media) {
		media.add(linkTo(methodOn(MediaController.class).getMediaById(principal, media.getId())).withSelfRel());
		media.add(linkTo(methodOn(MediaController.class).getAllMedias(principal)).withRel("media lists"));
	}
}
