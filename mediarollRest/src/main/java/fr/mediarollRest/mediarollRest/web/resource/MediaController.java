package fr.mediarollRest.mediarollRest.web.resource;

import static fr.mediarollRest.mediarollRest.constant.Paths.MEDIAS;
import static fr.mediarollRest.mediarollRest.constant.Paths.MEDIAS_WITH_ID;
import static fr.mediarollRest.mediarollRest.constant.Paths.MEDIA_ID;

import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.util.List;
import java.util.Locale;

import org.apache.tika.io.IOUtils;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import fr.mediarollRest.mediarollRest.exception.AccountNotFoundException;
import fr.mediarollRest.mediarollRest.exception.MediaNotFoundException;
import fr.mediarollRest.mediarollRest.model.Account;
import fr.mediarollRest.mediarollRest.model.Media;
import fr.mediarollRest.mediarollRest.service.implementation.AccountService;
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
	private MediaManagerService mediaManagerService;

	@Autowired
	private MessageSource messageSource;

	private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

	@GetMapping(MEDIAS)
	public ResponseEntity<List<Media>> getAllMedias(Principal principal) {
		String mail = principal.getName();
		try {
			Account account = accountService.findByMail(mail);
			List<Media> mediaList = account.getMediaList();

			return new ResponseEntity<>(mediaList, HttpStatus.OK);
		} catch (AccountNotFoundException e) {
			logger.error(messageSource.getMessage("error.account.not.found", null, Locale.FRANCE), mail);
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

	}

	@GetMapping(MEDIAS_WITH_ID)
	public ResponseEntity<byte[]> getImageAsResponseEntity(@PathVariable("mediaId") Long mediaId) {
		HttpHeaders headers = new HttpHeaders();
		Media mediaFromDb = null;
		try {
			mediaFromDb = mediaService.findById(mediaId);
			InputStream in = mediaManagerService.getInputStreamFromMedia(mediaFromDb);
			byte[] media = IOUtils.toByteArray(in);
			headers.setCacheControl(CacheControl.noCache().getHeaderValue());
			return new ResponseEntity<>(media, headers, HttpStatus.OK);
		} catch (MediaNotFoundException e) {
			logger.error(messageSource.getMessage("error.media.not.found", null, Locale.FRANCE), mediaId);
			return new ResponseEntity<byte[]>(HttpStatus.NOT_FOUND);
		} catch (IOException e) {
			return new ResponseEntity<byte[]>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@ApiOperation(value = "Update media info")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully updated."),
			@ApiResponse(code = 404, message = "The media is not found. Check ID."), })
	@RequestMapping(value = MEDIAS + MEDIA_ID, method = RequestMethod.PUT)
	public ResponseEntity<Media> updateMediaInfo(@PathVariable("mediaId") Long mediaId, @RequestBody Media media) {

		try {
			Media mediaUpdated = mediaService.updateMediaInfo(mediaId, media);
			return new ResponseEntity<Media>(mediaUpdated, HttpStatus.OK);
		} catch (MediaNotFoundException e) {
			logger.error(messageSource.getMessage("error.media.not.found", null, Locale.FRANCE), mediaId);
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

	}

	@ApiOperation(value = "Upload a media")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Successfully upload."),
			@ApiResponse(code = 400, message = "The file is not a media."),
			@ApiResponse(code = 500, message = "Attempt to save file in file system failed."), })
	@RequestMapping(value = MEDIAS, method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Media> uploadMedia(@RequestParam("file") MultipartFile media, Principal principal)
			throws IOException {

		Media mediaToSave = null;
		Account account = null;
		String mail = principal.getName();

		if (mediaManagerService.isMedia(media)) {

			try {
				account = accountService.findByMail(mail);
				mediaToSave = mediaManagerService.saveMediaInFileSystem(media);
				mediaToSave.setOwner(account);
				account.getMediaList().add(mediaToSave);

				Media mediaSaved = mediaService.saveMedia(mediaToSave);

				return new ResponseEntity<Media>(mediaSaved, HttpStatus.CREATED);
			} catch (AccountNotFoundException e) {
				logger.error(messageSource.getMessage("error.account.not.found", null, Locale.FRANCE), mail);
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			} catch (IOException e) {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			} catch (FileUploadException e) {
				logger.error(messageSource.getMessage("error.upload.media", null, Locale.FRANCE), media.getOriginalFilename());
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
		}

		else {
			logger.error(messageSource.getMessage("error.upload.media.type", null, Locale.FRANCE), media.getOriginalFilename());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

	}

	@ApiOperation(value = "Delete a media")
	@ApiResponses(value = { @ApiResponse(code = 204, message = "Media successfully deleted"),
			@ApiResponse(code = 404, message = "Media not found in db. Please to check media ID"),
			@ApiResponse(code = 500, message = "An error occured during the process to delete Media"), })
	@RequestMapping(value = MEDIAS + MEDIA_ID, method = RequestMethod.DELETE)
	public ResponseEntity<Void> deleteMedia(@PathVariable("mediaId") Long mediaId) {
		try {
			Media mediaInDb = mediaService.findById(mediaId);
			boolean isDeleteFromFileSystem = mediaManagerService.deleteMediaInFileSystem(mediaInDb.getFilePath());

			if (isDeleteFromFileSystem) {
				boolean isDeleteFromDb = mediaService.deleteMediaById(mediaId);

				if (isDeleteFromDb) {
					return new ResponseEntity<>(HttpStatus.NO_CONTENT);
				} else {
					return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
				}
			} else {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}

		} catch (MediaNotFoundException e) {
			logger.error(messageSource.getMessage("error.media.not.found", null, Locale.FRANCE), mediaId);
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}

	}
}